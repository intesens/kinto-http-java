package com.intesens.kinto_http;

import org.json.JSONObject;

/**
 * Created by amalle on 16/09/16.
 */
public class Collection {

    private KintoClient kintoClient;

    private Bucket bucket;

    private String name;

    public Collection(KintoClient client, Bucket bucket, String name) {
        this.kintoClient = client;
        this.bucket = bucket;
        this.name = name;
    }

    public JSONObject getData() {
        // TODO
        return null;
    }

    public JSONObject listRecords() {
        //TODO
        return null;
    }
}
