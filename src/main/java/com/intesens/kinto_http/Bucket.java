package com.intesens.kinto_http;

import org.json.JSONObject;

import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 16/09/16.
 */
public class Bucket {

    private KintoClient kintoClient;

    /** The bucket name */
    private String name;

    /**
     * @param kintoClient
     * @param name the name of the bucket
     */
    public Bucket(KintoClient kintoClient, String name) {
        this.kintoClient = kintoClient;
        this.name = name;
    }

    /**
     * Retrieves the list of collections in the current bucket.
     * @return
     * @throws KintoException
     * @throws KintoHTTPException
     */
    public JSONObject listCollections() throws KintoException, KintoHTTPException {
        GetRequest request = kintoClient.request(ENDPOINTS.COLLECTIONS)
                .routeParam("bucket", name);
        return kintoClient.execute(request);
    }

    /**
     * Retrieve a collection object to perorm operations on it
     * @param name the name of the wanted collection
     * @return
     */
    public Collection collection(String name) {
        return new Collection(this.kintoClient, this, name);
    }

    /**
     * Retrive bucket data
     * @return
     * @throws KintoHTTPException
     * @throws KintoException
     */
    public JSONObject getData() throws KintoHTTPException, KintoException {
        GetRequest request = kintoClient
                .request(ENDPOINTS.BUCKET)
                .routeParam("bucket", name);
        return kintoClient.execute(request);
    }

    public String getName() {
        return name;
    }
}
