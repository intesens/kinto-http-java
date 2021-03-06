package com.intesens.kinto_http;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import java.util.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 20/09/16.
 */
public class BucketTest {

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
    public void listCollections() throws Exception {
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
                assertThat(getRequest.getUrl(), is(remote + "/buckets/bucketName/collections"));
                // AND headers are corrects
                assertThat(getRequest.getHeaders(), is(expectedHeaders));
                // AND the get method is used
                assertThat(getRequest.getHttpMethod(), is(HttpMethod.GET));
                return new JSONObject("{data:[{id: \"col1\"},{id:\"col2\"}]}");
            }
        })
                .when(kintoClient)
                .execute(any(GetRequest.class));
        // WHEN calling listBuckets
        Set<Collection> collections = kintoClient.bucket("bucketName").listCollections();
        // THEN check if the answer is correctly called by checking the result

        assertThat(collections.size(), is(2));
    }

    @Test
    public void collection() throws Exception {
        // GIVEN a fake url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = new KintoClient(remote);
        // AND a collection name
        String collectionName = "collectionName";
        // AND a bucket name
        String bucketId = "bucketId";
        // WHEN calling bucket.collection
        Collection collection = kintoClient.bucket(bucketId).collection(collectionName);
        // THEN collection has a name
        assertThat(collection.getId(), is(collectionName));
        // AND the collection has a bucket (with a name)
        assertThat(collection.getBucket().getId(), is(bucketId));
    }

    @Test
    public void getData() throws Exception {
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
                assertThat(getRequest.getUrl(), is(remote + "/buckets/bucketName"));
                // AND headers are corrects
                assertThat(getRequest.getHeaders(), is(expectedHeaders));
                // AND the get method is used
                assertThat(getRequest.getHttpMethod(), is(HttpMethod.GET));
                return new JSONObject("{}");
            }
        })
                .when(kintoClient)
                .execute(any(GetRequest.class));
        // WHEN calling bucket getData
        JSONObject jsonObject = kintoClient.bucket("bucketName").getData();
        // THEN check if the answer is correctly called by checking the result
        assertThat(jsonObject.toString(), is("{}"));
    }

    @Test
    public void getName() throws Exception {
        // GIVEN a fake remote kinto url
        String remote = "https://fake.kinto.url";
        // AND a kintoClient
        KintoClient kintoClient = spy(new KintoClient(remote));
        // WHEN calling bucket
        Bucket bucket = kintoClient.bucket("bucketName");
        // THEN check if the answer is correctly called by checking the result
        assertThat(bucket.getId(), is("bucketName"));
    }

}
