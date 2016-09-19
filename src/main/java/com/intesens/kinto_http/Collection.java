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
     * @param client
     * @param bucket the bucket parent of the collection
     * @param name the name of the collection
     */
    public Collection(KintoClient client, Bucket bucket, String name) {
        this.kintoClient = client;
        this.bucket = bucket;
        this.name = name;
    }

    /**
     * Retrieve the collection data.
     * @return
     * @throws KintoException
     * @throws KintoHTTPException
     */
    public JSONObject getData() throws KintoException, KintoHTTPException {
        GetRequest request = kintoClient.request(ENDPOINTS.COLLECTION)
                .routeParam("bucket", bucket.getName())
                .routeParam("collection", name);
        return kintoClient.execute(request);
    }

    /**
     * Retrive the records list of the current collection
     * @return
     * @throws KintoException
     * @throws KintoHTTPException
     */
    public JSONObject listRecords() throws KintoException, KintoHTTPException {
        GetRequest request = kintoClient.request(ENDPOINTS.RECORDS)
                .routeParam("bucket", bucket.getName())
                .routeParam("collection", name);
        return kintoClient.execute(request);
    }

    /**
     * Retrive a record from the current collection
     * @param id the record id to retrieve
     * @return
     * @throws KintoException
     * @throws KintoHTTPException
     */
    public JSONObject getRecord(String id) throws KintoException, KintoHTTPException {
        GetRequest request = kintoClient.request(ENDPOINTS.RECORD)
                .routeParam("bucket", bucket.getName())
                .routeParam("collection", name)
                .routeParam("record", id);
        return kintoClient.execute(request);
    }
}
