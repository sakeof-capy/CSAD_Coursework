package org.example.server;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.factories.TwoParamFactory;
import org.example.server.contexts.StorageContext;
import org.example.server.contexts.executors.StandardExecutor;
import org.example.storage.Storage;
import org.example.storage.operations.factory.OperationType;
import org.example.storage.operations.factory.StorageOperationFactoryAttacher;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StandardHttpServerFactory implements TwoParamFactory<Server, Storage, InetSocketAddress> {
    @Override
    public Server create(Storage storage, InetSocketAddress port) throws CreationException {
        try {
            final var server = new StandardHttpServer(port, 10);
            final var operationFactory = StorageOperationFactoryAttacher.factoryOf(storage);
            final var operationExecutor = new StandardExecutor();

            final var storageContext = new StorageContext(operationFactory, operationExecutor);
            storageContext.mapEndpointToOperation("GET", "/api/products", OperationType.READ_PRODUCT);
            storageContext.mapEndpointToOperation("PUT", "/api/product", OperationType.CREATE_PRODUCT);
            storageContext.mapEndpointToOperation("POST", "/api/product/{product_name}", OperationType.UPDATE_PRODUCT);
            storageContext.mapEndpointToOperation("DELETE", "/api/product/{product_name}", OperationType.DELETE_PRODUCT);

            storageContext.mapEndpointToOperation("GET", "/api/categories", OperationType.READ_CATEGORY);
            storageContext.mapEndpointToOperation("PUT", "/api/category", OperationType.CREATE_CATEGORY);
            storageContext.mapEndpointToOperation("POST", "/api/category/{category_name}", OperationType.UPDATE_CATEGORY);
            storageContext.mapEndpointToOperation("DELETE", "/api/category/{category_name}", OperationType.DELETE_CATEGORY);

            server.addContext("/api", storageContext);
            return server;
        } catch (IOException | HolderException e) {
            throw new CreationException(e.getMessage());
        }
    }
}
