package org.example.server.contexts.executors;

import com.sun.net.httpserver.HttpExchange;
import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.http.HttpUtils;

public class StandardExecutor implements StorageOperationExecutor {
    @Override
    public void executeOperation(StorageOperation operation, DynamicObject params, HttpExchange exchange) {
        try {
            var res = operation.execute(params);
            if(res.isPresent())
                HttpUtils.sendResponseOperationResult(exchange, 200, res.get());
            else
                HttpUtils.sendResponse(exchange, 204);
        } catch (NotFoundException e) {
            HttpUtils.sendResponse(exchange, 404, e.getMessage());
        } catch (DataConflictException e) {
            HttpUtils.sendResponse(exchange, 409, e.getMessage());
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
}
