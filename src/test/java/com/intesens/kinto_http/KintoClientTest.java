package com.intesens.kinto_http;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;

import java.util.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 19/09/16.
 */
public class KintoClientTest {

    private Map<String, List<String>> defaultHeaders;

    @Before
    public void setUp() throws Exception {
        // set default headers
        defaultHeaders = new HashMap<>();
        defaultHeaders.put("Accept", new ArrayList<>(Arrays.asList("application/json")));
        defaultHeaders.put("Content-Type", new ArrayList<>(Arrays.asList("application/json")));
    }

    @After
    public void tearDown() throws Exception {
        Unirest.shutdown();
    }

    @Test
    public void testConstructorWithCorrectRemote() {
        // GIVEN a remote location
        String expectedRemote = "http://remote.kinto.mock/v1";
        // WHEN building a kintoClient
        KintoClient kintoClient = new KintoClient(expectedRemote);
        // THEN remote is correctly set
        assertThat(kintoClient.getRemote(), is(expectedRemote));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyRemote() {
        // GIVEN an empty remote location
        String expectedRemote = "";
        // WHEN building a kintoClient
        KintoClient kintoClient = new KintoClient(expectedRemote);
        // THEN an IllegalArgumentException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRemote() {
        // GIVEN a null remote location
        String expectedRemote = null;
        // WHEN building a kintoClient
        KintoClient kintoClient = new KintoClient(expectedRemote);
        // THEN an IllegalArgumentException is thrown
    }

    // TODO testConstructorWithCorrectHeaders
    public void testConstructorWithCorrectHeaders() {
        // GIVEN correct remote
        // AND correct headers
        // WHEN building a kintoClient
        // THEN headers is correctly set
    }

    // TODO testConstructorWithNullHeaders
    public void testConstructorWithNullHeaders() {
        // GIVEN correct remote
        // AND null headers
        // WHEN building a kintoClient
        // THEN an IllegalArgumentException is thrown
    }

    // TODO testSetRemoteWithCorrectRemote
    public void testSetRemoteWithCorrectRemote() {
        // GIVEN correct remote
        // AND a kintoClient
        // WHEN setting the correct remote
        // THEN the remote is correctly set
    }

    // TODO testSetRemoteWithEmptyRemote
    public void testSetRemoteWithEmptyRemote() {
        // GIVEN an empty remote
        // AND a kintoClient (with a correct remote)
        // WHEN setting the empty remote
        // THEN an IllegalArgumentException is thrown
    }

    // TODO testSetRemoteWithNullRemote
    public void testSetRemoteWithNullRemote() {
        // GIVEN a null remote
        // AND a kintoClient (with a correct remote)
        // WHEN setting the remote
        // THEN an IllegalArgumentException is thrown
    }

    // TODO testSetHeadersWithCorrectHeaders
    public void testSetHeadersWithCorrectHeaders() {
        // GIVEN correct headers
        // AND a kintoClient (with a correct header)
        // WHEN setting the headers
        // THEN the correct headers are set
    }

    // TODO testSetHeadersWithNullHeaders
    public void testSetHeadersWithNullHeaders() {
        // GIVEN null headers
        // AND a kintoClient
        // WHEN setting the headers
        // THEN an IllegalArgumentException is thrown
    }

    @Test
    public void testListBuckets() throws KintoException, KintoHTTPException, UnirestException {
        // GIVEN a fake remote kinto url
        String remote = "https://fake.kinto.url";
        // AND expected headers
        Map<String, List<String>> expectedHeaders = new HashMap<>(defaultHeaders);
        // AND a kintoClient
        KintoClient kintoClient = spy(new KintoClient(remote));
        // AND a mocked kintoClient
        doAnswer(new Answer<JSONObject>() {
            public JSONObject answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetRequest getRequest = (GetRequest)args[0];
                // THEN the correct endpoint is called
                assertThat(getRequest.getUrl(), is(remote + "/buckets"));
                // AND headers are corrects
                assertThat(getRequest.getHeaders(), is(expectedHeaders));
                // AND the get method is used
                assertThat(getRequest.getHttpMethod(), is(HttpMethod.GET));
                return new JSONObject("{}");
            }
        })
        .when(kintoClient)
        .execute(any(GetRequest.class));
        // WHEN calling listBuckets
        JSONObject jsonObject = kintoClient.listBuckets();
        // THEN check if the answer is correctly called by checking the result
        assertThat(jsonObject.toString(), is("{}"));
    }

    @Test
    public void testBucket() {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote);
        // AND a bucket name
        String bucketName = "abucketname";
        // WHEN calling bucket
        Bucket bucket = kintoClient.bucket(bucketName);
        // THEN bucket have a name
        assertThat(bucket.getName(), is(bucketName));
    }

    @Test
    public void testRequestWithoutCustomHeaders() {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote);
        // AND an ENDPOINTS
        ENDPOINTS endpoint = ENDPOINTS.GROUPS;
        // WHEN calling request
        GetRequest request = kintoClient.request(endpoint);
        // THEN the root part is initialized
        assertThat(request.getUrl(), is(remote + "/buckets/{bucket}/groups"));
        // AND the get http method is used
        assertThat(request.getHttpMethod(), is(HttpMethod.GET));
        // AND the default headers are set
        assertThat(request.getHeaders(), is(defaultHeaders));
    }

    @Test
    public void testRequestWithCustomHeaders() {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND custom headers
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Authorization", "Basic supersecurestuff");
        customHeaders.put("Warning", "Be careful");
        customHeaders.put("Accept", "application/html");
        // AND expected headers
        Map<String, List<String>> expectedHeaders = new HashMap<>(defaultHeaders);
        customHeaders.forEach((k, v) -> expectedHeaders.merge(k, Arrays.asList(v), (a, b) -> a.addAll(b) ? a:a));
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote, customHeaders);
        // AND an ENDPOINTS
        ENDPOINTS endpoint = ENDPOINTS.GROUPS;
        // WHEN calling request
        GetRequest request = kintoClient.request(endpoint);
        // THEN the root part is initialized
        assertThat(request.getUrl(), is(remote + "/buckets/{bucket}/groups"));
        // AND the get http method is used
        assertThat(request.getHttpMethod(), is(HttpMethod.GET));
        // AND the default headers are set
        assertThat(request.getHeaders(), is(expectedHeaders));
    }

    @Test
    public void testExecuteCorrect() throws UnirestException, KintoException, KintoHTTPException {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote);
        // AND a simple JsonNode
        JsonNode jsonNode = new JsonNode("{}");
        // AND a httpResponse mock
        HttpResponse<JsonNode> response = mock(HttpResponse.class);
        doReturn(200).when(response).getStatus();
        doReturn(jsonNode).when(response).getBody();
        // AND GetRequest mock
        GetRequest request = mock(GetRequest.class);
        doReturn(response).when(request).asJson();
        // WHEN calling execute
        JSONObject jsonObject = kintoClient.execute(request);
        // THEN the result is correct;
        assertThat(jsonObject.toString(), is("{}"));
    }

    @Test(expected = KintoException.class)
    public void testExecuteKintoError() throws UnirestException, KintoException, KintoHTTPException {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote);
        // AND a httpResponse mock
        HttpResponse<JsonNode> response = mock(HttpResponse.class);
        doReturn(400).when(response).getStatus();
        doReturn("an error").when(response).getStatusText();
        // AND GetRequest mock
        GetRequest request = mock(GetRequest.class);
        doReturn(response).when(request).asJson();
        // WHEN calling execute
        kintoClient.execute(request);
        // THEN a KintoException is thrown
    }

    @Test(expected = KintoHTTPException.class)
    public void testExecuteHttpError() throws UnirestException, KintoException, KintoHTTPException {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote);
        // AND GetRequest mock
        GetRequest request = mock(GetRequest.class);
        doThrow(UnirestException.class).when(request).asJson();
        // WHEN calling execute
        kintoClient.execute(request);
        // THEN a KintoHTTPException is thrown
    }

    // TODO testShutdown
    public void testShutdown() {
        // GIVEN a kinto client
        // WHEN calling shutdown
        // THEN Unirest shutdown is called
    }
}