package org.example.storage.operations.product;

import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeleteProductOperation implements StorageOperation {

    private Storage creator = null;

    public DeleteProductOperation(Storage creator){
        this.creator = creator;
    }

    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String productName = params.get("product_name").get();
            creator.executeUpdate(String.format("delete from product where product_name = '%s'", productName));
        } catch (SQLException e) {
            throw new NotFoundException("There is no such product!!!");
        }
        return Optional.empty();
    }
}
