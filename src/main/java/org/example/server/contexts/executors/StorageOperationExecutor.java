package org.example.server.contexts.executors;

import com.sun.net.httpserver.HttpExchange;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;

public interface StorageOperationExecutor {
    void executeOperation(StorageOperation operation, DynamicObject params, HttpExchange exchange);
}
