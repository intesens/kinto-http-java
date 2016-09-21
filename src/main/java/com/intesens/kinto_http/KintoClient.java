package com.intesens.kinto_http;

    import java.io.IOException;
import java.util.*;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
    import com.mashape.unirest.http.ObjectMapper;
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
     * Simple constructor that allows to talk to a kinto store. Using this constructor does not allow automatic
     * response deserialization of custom types.
     * @param remote The remote URL
     *               Must contain the version
     * @throws IllegalArgumentException if remote is null or an empty String
     */
    public KintoClient(String remote) {
        setRemote(remote);
    }

    /**
     * Simple constructor that allows to talk to a kinto store and also allows to configure default headers. Using
     * this constructor does not allow automatic response deserialization of custom types.
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
     * {@link KintoClient} constructor that allows to specify a remote kinto installation as well as an
     * {@link ObjectMapper} that will allow unmarshalling of responses as objects (that must be properly annotated
     * with jackson annotations).
     * @param remote The remote URL
     *               Must contain the version
     * @param objectMapper {@link ObjectMapper} to use for deserialization
     * @throws IllegalArgumentException
     *          if remote is null or an empty String
     *          if headers is null
     */
    public KintoClient(String remote, ObjectMapper objectMapper) {
        this(remote);
        Unirest.setObjectMapper(objectMapper);
    }

    /**
     * {@link KintoClient} constructor that allows to specify a remote kinto installation, default headers and an
     * {@link ObjectMapper} that will allow unmarshalling of responses as objects (that must be properly annotated
     * with jackson annotations).
     * @param remote The remote URL
     *               Must contain the version
     * @param headers Custom http headers added to each requests
     * @param objectMapper Custom object mapper
     * @throws IllegalArgumentException
     *          if remote is null or an empty String
     *          if headers is null
     */
    public KintoClient(String remote, Map<String, String> headers, ObjectMapper objectMapper) {
        this(remote, headers);
        Unirest.setObjectMapper(objectMapper);
    }

    /**
     * setter for remote
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
     * getter for remote
     * @return the current remote
     */
    public String getRemote() {
        return remote;
    }

    /**
     * Custom http headers that will be added to each request
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
     * @return true if default header are provided
     */
    public boolean isHeaders() {
        return headers != null && !headers.isEmpty();
    }

    /**
     * Retrieves the list of buckets
     * @return a list of buckets
     * @throws ClientException
     * @throws KintoException
     */
    public JSONObject listBuckets() throws ClientException, KintoException {
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
     * @param request the {@link GetRequest} object to execute
     * @return the response body as a JSONObject
     * @throws KintoException if there is a kinto error
     * @throws ClientException if there is a http transport error
     */
    JSONObject execute(GetRequest request) throws KintoException, ClientException {
        try {
            // Call the remote with a request
            HttpResponse<JsonNode> response = request.asJson();
            // Handle kinto errors
            if(response.getStatus() != 200) {
                throw new KintoException(response.getStatusText());
            }
            return response.getBody().getObject();
        } catch (UnirestException e) {
            throw new ClientException("Error during \"" + request.getUrl() + "\"", e);
        }
    }

    /**
     * Execute the given request
     * @param request the {@link GetRequest} object to execute
     * @param clazz the response class
     * @return the response body as a clazz object
     * @throws KintoException if there is a kinto error
     * @throws ClientException if there is a http transport error
     */
    <T> T execute(GetRequest request, Class<? extends T> clazz) throws KintoException, ClientException {
        try {
            // Call the remote with a request
            HttpResponse<T> response = request.asObject(clazz);
            // Handle kinto errors
            if(response.getStatus() != 200) {
                throw new KintoException(response.getStatusText());
            }
            return response.getBody();
        } catch (UnirestException e) {
            throw new ClientException("Error during \"" + request.getUrl() + "\"", e);
        }
    }

    /**
     * Closes the asynchronous http client and its event loop.
     * Use this method to close all the threads and allow your application to exit.
     * @throws IOException
     */
    public void shutdown() throws IOException {
        Unirest.shutdown();
    }
}
