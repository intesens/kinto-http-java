package com.intesens.kinto_http;

import org.json.JSONObject;

import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 16/09/16.
 */
public class Bucket {

    private KintoClient kintoClient;

    private String name;

    public Bucket(KintoClient kintoClient, String name) {
        this.kintoClient = kintoClient;
        this.name = name;
    }

    public JSONObject listCollections() throws KintoException, KintoHTTPException {
        GetRequest request = kintoClient.request(ENDPOINTS.COLLECTIONS)
                .routeParam("bucket", name);
        return kintoClient.execute(request);
    }

    public Collection collection(String name) {
        return new Collection(this.kintoClient, this, name);
    }

    public JSONObject getData() throws KintoHTTPException, KintoException {
        GetRequest request = kintoClient
                .request(ENDPOINTS.BUCKET)
                .routeParam("bucket", name);
        return kintoClient.execute(request);
    }
}
