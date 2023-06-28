package org.example.storage.operations.category;

import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UpdateCategoryOperation implements StorageOperation {

    private Storage creator = null;

    public UpdateCategoryOperation(Storage creator){
        this.creator = creator;
    }

    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String categoryName = params.get("category_name").get();
            String categoryDescription = params.get("category_description").get();

            creator.executeUpdate(String.format("update category set category_description = '%s' where category_name = '%s';", categoryDescription, categoryName));

            return Optional.empty();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new NotFoundException("No such category found!!!");
        }
    }
}
