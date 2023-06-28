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

public class CreateProductOperation implements StorageOperation {
    private Storage creator = null;

    public CreateProductOperation(Storage creator){
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

            creator.executeUpdate(String.format("insert into product(product_name, category_name, product_description, in_stock, price, producer) values('%s', '%s', '%s', '%s', '%s', '%s');", productName, categoryName, productDescription, inStock, price, producer));

            return Optional.empty();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new DataConflictException("Product with name " + params.get("product_name").get() + " already exists!!!");
        }
    }
}
