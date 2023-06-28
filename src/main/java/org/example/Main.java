package org.example;

import org.example.exceptions.CreationException;
import org.example.server.StandardHttpServerFactory;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            var serverFactory = new StandardHttpServerFactory();
            var server = serverFactory.create(null, new InetSocketAddress(8000));
            server.start();
        } catch (CreationException e) {
            throw new RuntimeException(e);
        }
    }
}