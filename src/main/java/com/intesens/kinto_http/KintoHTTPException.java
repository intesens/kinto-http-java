package com.intesens.kinto_http;

/**
 * Created by amalle on 16/09/16.
 * Http Exception
 */
public class KintoHTTPException extends Exception {

    public KintoHTTPException(String message) {
        super(message);
    }

    public KintoHTTPException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
