package org.example;

import org.example.exceptions.CreationException;
import org.example.server.StandardHttpServerFactory;
import org.example.utilities.http.HttpUtils;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            var serverFactory = new StandardHttpServerFactory();
            var server = serverFactory.create(null, new InetSocketAddress(HttpUtils.PORT));
            server.start();
        } catch (CreationException e) {
            throw new RuntimeException(e);
        }
    }
}