package org.example;

import org.example.server.StandardHttpServerFactory;
import org.example.storage.DBConnector;
import org.example.storage.Storage;
import org.example.utilities.http.HttpUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        final var pathToPassword = "src/main/resources/password.txt";
        FileReader reader = null;
        try {
            reader = new FileReader(pathToPassword);
        } catch (FileNotFoundException e) {
            System.err.println("Create file: " + pathToPassword + " and store your postgres password here.");
            System.exit(-1);
        }
        var buffReader = new BufferedReader(reader);
        var password = buffReader.readLine();
        try {
            var storage = new DBConnector("client-server", "postgres",password);
            closeDbWhenShutdown(storage);
            var serverFactory = new StandardHttpServerFactory();
            var server = serverFactory.create(null, new InetSocketAddress(HttpUtils.PORT));
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeDbWhenShutdown(Storage storage) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                storage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}