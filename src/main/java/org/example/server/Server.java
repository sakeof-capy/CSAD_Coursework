package org.example.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpHandler;

public interface Server {
    void start();
    void stop();
    void addContext(String url, HttpHandler handler, Authenticator authenticator);
    default void addContext(String url, HttpHandler handler) {
        addContext(url, handler, null);
    }
}
