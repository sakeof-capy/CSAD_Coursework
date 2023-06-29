package org.example.server.contexts.executors;

import com.sun.net.httpserver.HttpExchange;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;
import org.example.utilities.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class DebugExecutor implements StorageOperationExecutor {
    @Override
    public void executeOperation(StorageOperation operation, DynamicObject params, HttpExchange exchange) {
        final var operationName = operation.getClass().getTypeName();
        System.out.println("--------------------------------------------------------");
        System.out.println("Operation: " + operationName);
        System.out.println("Params {");
        var map = params.getMap();
        for(var entry : map.entrySet()) {
            System.out.println("\t\"" + entry.getKey() + '"' + " : " + '"' + entry.getValue() + '"' + ',');
        }
        System.out.println("}");
        System.out.println("--------------------------------------------------------");
        HttpUtils.sendResponseOperationResult(exchange, 200, DEFAULT_RESPONSE);
    }

    private static final List<DynamicObject> DEFAULT_RESPONSE = new ArrayList<>() {{
        add(new StandardDynamicObject() {{
            put("product_name", "DefaultGood1");
            put("category_name", "DefaultCategory");
            put("product_description",
                    "Some long default description. It is so default that no longer can be short enough.");
            put("in_stock", "1324");
            put("price", "228.32");
            put("producer", "Gogi Vasyl");
        }});
        add(new StandardDynamicObject() {{
            put("product_name", "DefaultGood2");
            put("category_name", "DefaultCategory");
            put("product_description",
                    "Some long default description. It is so default that no longer can be short enough.");
            put("in_stock", "1324");
            put("price", "228.32");
            put("producer", "Gogi Vasyl");
        }});
        add(new StandardDynamicObject() {{
            put("product_name", "DefaultGood3");
            put("category_name", "DefaultCategory");
            put("product_description",
                    "Some long default description. It is so default that no longer can be short enough.");
            put("in_stock", "1324");
            put("price", "228.32");
            put("producer", "Gogi Vasyl");
        }});
        add(new StandardDynamicObject() {{
            put("product_name", "DefaultGood4");
            put("category_name", "DefaultCategory");
            put("product_description",
                    "Some long default description. It is so default that no longer can be short enough.");
            put("in_stock", "1324");
            put("price", "228.32");
            put("producer", "Gogi Vasyl");
        }});
        add(new StandardDynamicObject() {{
            put("product_name", "DefaultGood5");
            put("category_name", "DefaultCategory");
            put("product_description",
                    "Some long default description. It is so default that no longer can be short enough.");
            put("in_stock", "1324");
            put("price", "228.32");
            put("producer", "Gogi Vasyl");
        }});
    }};
}
