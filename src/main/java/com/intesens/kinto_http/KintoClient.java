package com.intesens.kinto_http;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

/**
 * Created by amalle on 16/09/16.
 */
public class KintoClient {

    /**
     * The remote server base URL.
     */
    private String remote;

    /**
     * @param remote The remote URL
     *               Must contain the version
     * @throws IllegalArgumentException if remote is null or an empty String
     */
    public KintoClient(String remote) {

        setRemote(remote);
    }

    /**
     * @param remote The remote URL
     *               Must contain the version
     * @throws IllegalArgumentException if remote is null or an empty String
     */
    public void setRemote(String remote) {
        if(remote == null || remote.length() <= 0) {
            throw new IllegalArgumentException("Illegal remote argument : \"" + remote + "\"");
        }
        // TODO check version
        this.remote = remote;
    }

    public JSONObject listBuckets() throws KintoHTTPException, KintoException {
        return execute(request(ENDPOINTS.BUCKETS));
    }

    public Bucket bucket(String name) {
        return new Bucket(this, name);
    }

    /**
     * Prepare a get request for the requested endpoint
     * Handle :
     * - Auth (TODO)
     * - Accept and Content-Type Headers
     * @param endpoint
     * @return
     */
    GetRequest request(ENDPOINTS endpoint) {
        return Unirest.get(endpoint.getPath())
                .routeParam("root", remote)
                //.basicAuth("username", "password") TODO
                .header("Accept",       "application/json")
                .header("Content-Type", "application/json")
                ;
    }

    /**
     * Execute the given request
     * @param request
     * @return the response body as a JSONObject
     * @throws KintoException if there is a kinto error
     * @throws KintoHTTPException if there is a http transport error
     */
    JSONObject execute(GetRequest request) throws KintoException, KintoHTTPException {
        try {
            // Call the remote with a request
            HttpResponse<JsonNode> response = request.asJson();
            // Handle kinto errors
            if(response.getStatus() != 200) {
                throw new KintoException(response.getStatusText());
            }
            return response.getBody().getObject();
        } catch (UnirestException e) {
            throw new KintoHTTPException("Error during bucket.getData", e);
        }
    }
}
