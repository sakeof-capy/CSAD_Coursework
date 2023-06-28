package org.example.storage.operations.factory;

import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.storage.operations.category.CreateCategoryOperation;
import org.example.storage.operations.category.DeleteCategoryOperation;
import org.example.storage.operations.category.ReadCategoryOperation;
import org.example.storage.operations.category.UpdateCategoryOperation;
import org.example.storage.operations.product.CreateProductOperation;
import org.example.storage.operations.product.DeleteProductOperation;
import org.example.storage.operations.product.ReadProductOperation;
import org.example.storage.operations.product.UpdateProductOperation;
import org.example.storage.operations.user.CreateUserOperation;
import org.example.storage.operations.user.ReadUserOperation;

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
    CREATE_PRODUCT(CreateProductOperation::new),
    READ_PRODUCT(ReadProductOperation::new),
    UPDATE_PRODUCT(UpdateProductOperation::new),
    DELETE_PRODUCT(DeleteProductOperation::new),

    CREATE_CATEGORY(CreateCategoryOperation::new),
    READ_CATEGORY(ReadCategoryOperation::new),
    UPDATE_CATEGORY(UpdateCategoryOperation::new),
    DELETE_CATEGORY(DeleteCategoryOperation::new),

    CREATE_USER(CreateUserOperation::new),
    READ_USER(ReadUserOperation::new)
    ;

    OperationType(Function<Storage, StorageOperation> operation) {
        this.operationConstructor = operation;
    }

    public Function<Storage, StorageOperation> getOperationConstructor() {
        return operationConstructor;
    }

    private final Function<Storage, StorageOperation> operationConstructor;
}