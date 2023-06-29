package org.example.server.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.factories.OneParamFactory;
import org.example.server.contexts.dispatching.EndpointDispatcher;
import org.example.server.contexts.executors.StorageOperationExecutor;
import org.example.storage.operations.StorageOperation;
import org.example.storage.operations.factory.OperationType;
import org.example.utilities.dynobjects.StandardDynamicObject;
import org.example.utilities.http.HttpUtils;


import static org.example.utilities.http.HttpUtils.extractMergedParamsFromBodyAndQuery;

public class StorageContext implements HttpHandler {
    public StorageContext(OneParamFactory<StorageOperation, OperationType> operationFactory,
                          StorageOperationExecutor operationExecutor) {
        this.operationFactory = operationFactory;
        this.operationDispatcher = new EndpointDispatcher<>();
        this.operationExecutor = operationExecutor;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            final var requestMethod = exchange.getRequestMethod();
            final var uri = exchange.getRequestURI();
            final var operationType = operationDispatcher.dispatch(requestMethod, uri)
                    .orElseThrow(() -> new IllegalArgumentException("Non-handled endpoint: " + requestMethod + ": " + uri));
            final var operation = operationFactory.create(operationType);
            final var params = extractMergedParamsFromBodyAndQuery(exchange)
                    .orElse(new StandardDynamicObject());
            operationExecutor.executeOperation(operation, params, exchange);
        } catch (IllegalArgumentException e) {
            System.out.println("Endpoint not found.");
            HttpUtils.sendResponse(exchange, 404);
        } catch (CreationException e) {
            e.printStackTrace();
        }
    }

    public void mapEndpointToOperation(String requestMethod, String uri, OperationType operationType) throws HolderException {
        System.out.println("Endpoint added: " + requestMethod + ' ' + uri);
        operationDispatcher.addEndpoint(requestMethod, uri, operationType);
    }

    private final EndpointDispatcher<OperationType> operationDispatcher;
    private final OneParamFactory<StorageOperation, OperationType> operationFactory;
    private final StorageOperationExecutor operationExecutor;
}
