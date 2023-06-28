package org.example.storage.operations.category;

import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CreateCategoryOperation implements StorageOperation {

    private Storage creator = null;

    public CreateCategoryOperation(Storage creator){
        this.creator = creator;
    }

    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String categoryName = params.get("category_name").get();
            String categoryDescription = params.get("category_description").get();

            creator.executeUpdate(String.format("insert into category(category_name, category_description) values('%s', '%s');", categoryName, categoryDescription));

            return Optional.empty();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new DataConflictException("Product with name " + params.get("product_name").get() + " already exists!!!");
        }
    }
}
