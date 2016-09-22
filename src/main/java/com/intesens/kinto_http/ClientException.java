package com.intesens.kinto_http;

/**
 * Created by amalle on 16/09/16.
 * Http client exception class
 */
public class ClientException extends Exception {

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
