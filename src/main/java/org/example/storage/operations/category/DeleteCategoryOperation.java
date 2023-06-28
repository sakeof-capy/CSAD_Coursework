package org.example.storage.operations.category;

import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeleteCategoryOperation implements StorageOperation {

    private Storage creator = null;
    public DeleteCategoryOperation(Storage creator){
        this.creator = creator;
    }

    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String categoryName = params.get("category_name").get();
            creator.executeUpdate(String.format("delete from category where category_name = '%s'", categoryName));
        } catch (SQLException e) {
            throw new NotFoundException("There is no such category!!!");
        }
        return Optional.empty();
    }
}
