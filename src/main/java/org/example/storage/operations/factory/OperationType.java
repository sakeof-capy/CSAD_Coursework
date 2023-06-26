package org.example.storage.operations.factory;

import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.storage.operations.GetGoodQuantityOperation;

import java.util.function.Function;

/**
 * OperationType - enumeration of storage operation types.
 * Each OperationType value holds a 'creator' of the operation
 * corresponding to the value.
 * To extend the OperationType enumeration, it is required
 * to indicate directly which concrete operation corresponds
 * to the value by passing its 'creator' as a param.
 */
public enum OperationType {
    GET_GOOD_QUANTITY(GetGoodQuantityOperation::new),
    ;

    OperationType(Function<Storage, StorageOperation> operation) {
        this.operationConstructor = operation;
    }

    public Function<Storage, StorageOperation> getOperationConstructor() {
        return operationConstructor;
    }

    private final Function<Storage, StorageOperation> operationConstructor;
}