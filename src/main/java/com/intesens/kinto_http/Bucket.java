package com.intesens.kinto_http;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 16/09/16.
 */
public class Bucket {

    private KintoClient kintoClient;

    /** The bucket id */
    private String id;

    /**
     * @param kintoClient the {@link KintoClient} to request this bucket with
     * @param name the id of the bucket
     */
    public Bucket(KintoClient kintoClient, String name) {
        this.kintoClient = kintoClient;
        this.id = name;
    }

    /**
     * Retrieves the list of collections in the current bucket.
     * @return a {@link Set<Collection>} of the collections
     * @throws KintoException in case kinto answers with an error
     * @throws ClientException in case of transport errors
     */
    public Set<Collection> listCollections() throws KintoException, ClientException {
        Set<Collection> collections = new HashSet<>();
        GetRequest request = kintoClient.request(ENDPOINTS.COLLECTIONS)
                .routeParam("bucket", id);
        JSONObject response = kintoClient.execute(request);
        JSONArray data = response.getJSONArray("data");
        if(data != null) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonCol = data.getJSONObject(i);
                collections.add(collection(jsonCol.getString("id")));
            }
        }
        return collections;
    }

    /**
     * Retrieve a collection object to perorm operations on it
     * @param id the id of the wanted collection
     * @return a {@link Collection} object
     */
    public Collection collection(String id) {
        return new Collection(this.kintoClient, this, id);
    }

    /**
     * Retrieve raw bucket data
     * @return the raw data from kinto
     * @throws ClientException in case of transport errors
     * @throws KintoException in case kinto answers with an error
     */
    public JSONObject getData() throws ClientException, KintoException {
        GetRequest request = kintoClient
                .request(ENDPOINTS.BUCKET)
                .routeParam("bucket", id);
        return kintoClient.execute(request);
    }

    public String getId() {
        return id;
    }
}
