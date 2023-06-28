package org.example.server.contexts.executors;

import com.sun.net.httpserver.HttpExchange;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.http.HttpUtils;

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
        HttpUtils.sendResponse(exchange, 200, "Debugging operation " + operationName);
    }
}
