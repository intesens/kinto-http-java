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
     * @param kintoClient the {@link KintoClient} to request this bucket with
     * @param name the name of the bucket
     */
    public Bucket(KintoClient kintoClient, String name) {
        this.kintoClient = kintoClient;
        this.name = name;
    }

    /**
     * Retrieves the list of collections in the current bucket.
     * TODO: make this method return a {@code List<Collection>} instead
     * @return a {@link JSONObject} containing the response data
     * @throws KintoException in case kinto answers with an error
     * @throws ClientException in case of transport errors
     */
    public JSONObject listCollections() throws KintoException, ClientException {
        GetRequest request = kintoClient.request(ENDPOINTS.COLLECTIONS)
                .routeParam("bucket", name);
        return kintoClient.execute(request);
    }

    /**
     * Retrieve a collection object to perorm operations on it
     * @param name the name of the wanted collection
     * @return a {@link Collection} object
     */
    public Collection collection(String name) {
        return new Collection(this.kintoClient, this, name);
    }

    /**
     * Retrive bucket data
     * @return the raw data from kinto
     * @throws ClientException in case of transport errors
     * @throws KintoException in case kinto answers with an error
     */
    public JSONObject getData() throws ClientException, KintoException {
        GetRequest request = kintoClient
                .request(ENDPOINTS.BUCKET)
                .routeParam("bucket", name);
        return kintoClient.execute(request);
    }

    public String getName() {
        return name;
    }
}
