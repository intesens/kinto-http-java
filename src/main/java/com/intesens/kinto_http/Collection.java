package com.intesens.kinto_http;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 16/09/16.
 */
public class Collection {

    private KintoClient kintoClient;

    private Bucket bucket;

    private String id;

    /**
     * @param client the {@link KintoClient} used for subsequent requests
     * @param bucket the bucket parent of the collection
     * @param id the id of the collection
     */
    public Collection(KintoClient client, Bucket bucket, String id) {
        this.kintoClient = client;
        this.bucket = bucket;
        this.id = id;
    }

    /**
     * @return the current collection id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the current collection parent bucket
     */
    public Bucket getBucket() {
        return bucket;
    }

    /**
     * Retrieve the collection data.
     * @return the raw data as a {@link JSONObject}
     * @throws KintoException in case of error response from Kinto
     * @throws ClientException in case of transport error
     */
    public JSONObject getData() throws KintoException, ClientException {
        GetRequest request = kintoClient.request(ENDPOINTS.COLLECTION)
                .routeParam("bucket", bucket.getId())
                .routeParam("collection", id);
        return kintoClient.execute(request);
    }

    private GetRequest getListRecordsRequest() {
        return kintoClient.request(ENDPOINTS.RECORDS)
                .routeParam("bucket", bucket.getId())
                .routeParam("collection", id);
    }

    /**
     * Retrieve the records list of the current collection
     * @return the records as a set of {@link JSONObject}
     * @throws KintoException in case of error response from Kinto
     * @throws ClientException in case of transport error
     */
    public Set<JSONObject> listRecords() throws KintoException, ClientException {
        Set<JSONObject> records = new HashSet<>();
        JSONObject response = kintoClient.execute(getListRecordsRequest());
        JSONArray data = response.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            records.add(data.getJSONObject(i));
        }
        return records;
    }

    /**
     * Retrieve the records list of the current collection
     * @param clazz the class of the retrieved Object
     * @param <T> the type the records should be unmarshalled to
     * @return a clazz object
     * @throws KintoException in case of error response from Kinto
     * @throws ClientException in case of transport error
     */
    public <T> T listRecords(Class<? extends T> clazz) throws KintoException, ClientException {
        return kintoClient.execute(getListRecordsRequest(), clazz);
    }

    /**
     * Retrieve a record from the current collection
     * @param id the record id to retrieve
     * @return the record as a raw JSONObject
     * @throws KintoException in case of error response from Kinto
     * @throws ClientException in case of transport error
     */
    public JSONObject getRecord(String id) throws KintoException, ClientException {
        GetRequest request = kintoClient.request(ENDPOINTS.RECORD)
                .routeParam("bucket", bucket.getId())
                .routeParam("collection", this.id)
                .routeParam("record", id);
        return kintoClient.execute(request);
    }

    /**
     * Retrieve a record from the current collection as an object
     * @param id the record id to retrieve
     * @return the record as a T object
     * @throws KintoException in case of error response from Kinto
     * @throws ClientException in case of transport error
     */
    public <T> T getRecord(String id, Class<? extends T> clazz) throws KintoException, ClientException {
        GetRequest request = kintoClient.request(ENDPOINTS.RECORD)
                .routeParam("bucket", bucket.getId())
                .routeParam("collection", this.id)
                .routeParam("record", id);
        return kintoClient.execute(request, clazz);
    }
}
