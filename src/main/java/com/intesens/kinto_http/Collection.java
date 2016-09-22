package com.intesens.kinto_http;

import org.json.JSONObject;

import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 16/09/16.
 */
public class Collection {

    private KintoClient kintoClient;

    private Bucket bucket;

    private String name;

    /**
     * @param client the {@link KintoClient} used for subsequent requests
     * @param bucket the bucket parent of the collection
     * @param name the name of the collection
     */
    public Collection(KintoClient client, Bucket bucket, String name) {
        this.kintoClient = client;
        this.bucket = bucket;
        this.name = name;
    }

    /**
     * @return the current collection name
     */
    public String getName() {
        return name;
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
                .routeParam("bucket", bucket.getName())
                .routeParam("collection", name);
        return kintoClient.execute(request);
    }

    private GetRequest getListRecordsRequest() {
        return kintoClient.request(ENDPOINTS.RECORDS)
                .routeParam("bucket", bucket.getName())
                .routeParam("collection", name);
    }

    /**
     * Retrieve the records list of the current collection
     * @return the records as a raw {@link JSONObject}
     * @throws KintoException in case of error response from Kinto
     * @throws ClientException in case of transport error
     */
    public JSONObject listRecords() throws KintoException, ClientException {
        return kintoClient.execute(getListRecordsRequest());
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
                .routeParam("bucket", bucket.getName())
                .routeParam("collection", name)
                .routeParam("record", id);
        return kintoClient.execute(request);
    }
}
