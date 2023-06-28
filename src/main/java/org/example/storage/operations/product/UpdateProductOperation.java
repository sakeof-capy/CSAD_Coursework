package org.example.storage.operations.product;

import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UpdateProductOperation implements StorageOperation {

    private Storage creator = null;

    public UpdateProductOperation(Storage creator){
        this.creator = creator;
    }


    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String productName = params.get("product_name").get();
            String categoryName = params.get("category_name").get();
            String productDescription = params.get("product_description").get();
            int inStock = Integer.parseInt(params.get("in_stock").get());
            double price = Double.parseDouble(params.get("price").get());
            String producer = params.get("producer").get();

            if (inStock < 0 || price < -0.000001){
                throw new DataConflictException("Wrong inputs!!!");
            }

            creator.executeUpdate(String.format("update product set category_name = '%s', product_description = '%s', in_stock = '%s', price = '%s', producer = '%s' where product_name = '%s';", categoryName, productDescription, inStock, price, producer, productName));

            return Optional.empty();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new DataConflictException("No such product found!!!");
        }
    }
}
