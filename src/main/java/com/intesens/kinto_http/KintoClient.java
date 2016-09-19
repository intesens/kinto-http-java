package com.intesens.kinto_http;

    import java.io.IOException;
import java.util.*;

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
     * Custom headers added to each requests
     */
    private Map<String, String> headers;

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
     * @param headers Custom http headers added to each requests
     * @throws IllegalArgumentException
     *          if remote is null or an empty String
     *          if headers is null
     */
    public KintoClient(String remote, Map<String, String> headers) {
        this(remote);
        setHeaders(headers);
    }

    /**
     * @param remote The remote URL
     *               Must contain the version
     * @throws IllegalArgumentException if remote is null or an empty String
     */
    public void setRemote(String remote) {
        if (remote == null || remote.length() <= 0) {
            throw new IllegalArgumentException("Illegal remote argument : \"" + remote + "\"");
        }
        // TODO check version
        this.remote = remote;
    }

    /**
     * @return the current remote
     */
    public String getRemote() {
        return remote;
    }

    /**
     * Customs http headers that will be added to each requests
     * @param headers a map of http headers fields and values
     * @throws IllegalArgumentException if headers is null
     */
    public void setHeaders(Map<String, String> headers) {
        if (headers == null) {
            throw new IllegalArgumentException("Illegal headers argument : \"" + headers + "\"");
        }
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * @return true if headers is not null and not empty
     */
    public boolean isHeaders() {
        return headers != null && !headers.isEmpty();
    }

    /**
     * Retrieves the list of buckets
     * @return a list of buckets
     * @throws KintoHTTPException
     * @throws KintoException
     */
    public JSONObject listBuckets() throws KintoHTTPException, KintoException {
        return execute(request(ENDPOINTS.BUCKETS));
    }

    /**
     * Retrieve a bucket objet to perform operations on it
     * @param name the bucket name
     * @return
     */
    public Bucket bucket(String name) {
        return new Bucket(this, name);
    }

    /**
     * Prepare a get request for the requested endpoint
     * Add default http headers (Accept, Content-Type)
     * Then add custom headers {@link #setHeaders(Map)}
     * @param endpoint
     * @return
     */
    GetRequest request(ENDPOINTS endpoint) {
        String url = remote + endpoint.getPath();
        GetRequest request = Unirest.get(url)
                .header("Accept",       "application/json")
                .header("Content-Type", "application/json");
        if(isHeaders()) {
            request.headers(headers);
        }
        return request;
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

    /**
     * Clone the asynchronous http client and its event loop.
     * Use this method to close all the threads and allow your application to exit.
     * @throws IOException
     */
    public void shutdown() throws IOException {
        Unirest.shutdown();
    }
}
