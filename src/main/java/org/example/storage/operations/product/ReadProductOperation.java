package org.example.storage.operations.product;

import org.example.exceptions.storage.InvalidParamSetException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReadProductOperation implements StorageOperation {
    private final Storage creator;

    public ReadProductOperation(Storage creator){
        this.creator = creator;
    }
    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            final var filter = filterFrom(params);
            List<DynamicObject> resultList = new ArrayList<>();
            ResultSet resultSet = creator.executeQuery("select * from product" + filter);
            while(resultSet.next()){
                DynamicObject object = new StandardDynamicObject();
                object.put("product_name", resultSet.getString("product_name"));
                object.put("category_name", resultSet.getString("category_name"));
                object.put("product_description", resultSet.getString("product_description"));
                object.put("in_stock", String.valueOf(resultSet.getInt("in_stock")));
                object.put("price", String.valueOf(resultSet.getDouble("price")));
                object.put("producer", resultSet.getString("producer"));
                resultList.add(object);
            }
            resultSet.close();
            return Optional.of(resultList);
        } catch (SQLException e) {
            throw new InvalidParamSetException(e.getMessage());
        }
    }

    private String filterFrom(DynamicObject params) {
        if(params.getMap().isEmpty())
            return "";
        final var pairs = params.getMap()
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .toList();
        final var joined = String.join(" AND ", pairs);
        return " WHERE " + joined;
    }
}
