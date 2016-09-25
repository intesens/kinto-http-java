package com.intesens.kinto_http;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.ObjectMapper;

/**
 * Test class illustrating deserialization of record objects. This example uses Jackson as a JSON mapper.
 * Created by jmarty on 25/09/16.
 */
public class ObjectMapperTest {

    private KintoClient kintoClient;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(6706);

    @Before
    public void setUp() throws Exception {
        com.fasterxml.jackson.databind.ObjectMapper jacksonMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        ObjectMapper mapper = new ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException("cannot deserialize", e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return jacksonMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("cannot serialize", e);
                }
            }
        };
        this.kintoClient = new KintoClient("http://0.0.0.0:6706", mapper);
    }

    @Test
    public void testDeserializeRecord() throws Exception {
        // GIVEN a mock server that answers a json object
        String bucket = "mybucket";
        String collection = "mycollection";
        String record = "myrecord";
        String requestPath = ENDPOINTS.RECORD.getPath()
                .replace("{bucket}", bucket)
                .replace("{collection}", collection)
                .replace("{record}", record);
        wireMockRule.stubFor(get(urlPathEqualTo(requestPath))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=UTF-8")
                        .withBody("{\"data\": {\"foo\": \"bar\", \"echo\": \"charlie\"}}")));

        // WHEN the client queries the record as a DomainObject class
        DomainObject actual = this.kintoClient.bucket(bucket).collection(collection).getRecord(record, DomainObject.class);

        // THEN I get the expected DomainObject object
        DomainObject expectedDomainObject = new DomainObject();
        expectedDomainObject.getProperties().put("foo", "bar");
        expectedDomainObject.getProperties().put("echo", "charlie");
        assertThat(actual, is(expectedDomainObject));
    }

    @Test
    public void testDezerializeRecords() throws Exception {
        // GIVEN a mock server that answers a json array
        String bucket = "mybucket";
        String collection = "mycollection";
        String requestPath = ENDPOINTS.RECORDS.getPath()
                .replace("{bucket}", bucket)
                .replace("{collection}", collection);
        wireMockRule.stubFor(get(urlPathEqualTo(requestPath))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=UTF-8")
                        .withBody("{\"data\": [{\"foo\": \"bar\"}, {\"foo\": \"baz\"}]}")));

        // WHEN the client queries the record as a DomainObject class
        RecordWrapper actual = this.kintoClient.bucket(bucket).collection(collection)
                .listRecords(RecordWrapper.class);

        // THEN I get the expected DomainObject object
        OtherDomainObject barObject = new OtherDomainObject("bar");
        OtherDomainObject bazObject = new OtherDomainObject("baz");
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getValues(), is(not(nullValue())));
        assertThat(actual.getValues(), hasItem(barObject));
        assertThat(actual.getValues(), hasItem(bazObject));
    }

    static class DomainObject {

        @JsonProperty("data")
        private Map<String, String> properties;

        public DomainObject() {
            this.properties = new HashMap<>();
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DomainObject domainObject = (DomainObject) o;

            return properties.equals(domainObject.properties);

        }

        @Override
        public int hashCode() {
            return properties.hashCode();
        }
    }

    /**
     * It is necessary to wrap kinto 'collection' responses in an object as we cannot directly dezerialize the "data"
     * property from the response as a collection of custom objects.
     */
    static class RecordWrapper {
        @JsonProperty("data")
        private List<OtherDomainObject> values;

        public RecordWrapper() {
        }

        public List<OtherDomainObject> getValues() {
            return values;
        }

        public void setValues(List<OtherDomainObject> values) {
            this.values = values;
        }
    }

    static class OtherDomainObject {

        private String foo;

        public OtherDomainObject() {
        }

        public OtherDomainObject(String foo) {
            this.foo = foo;
        }

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OtherDomainObject that = (OtherDomainObject) o;

            return foo != null ? foo.equals(that.foo) : that.foo == null;

        }

        @Override
        public int hashCode() {
            return foo != null ? foo.hashCode() : 0;
        }
    }
}
