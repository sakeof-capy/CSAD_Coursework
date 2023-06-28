package org.example.storage.operations.category;

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

public class ReadCategoryOperation implements StorageOperation {
    private Storage creator = null;

    public ReadCategoryOperation(Storage creator){
        this.creator = creator;
    }
    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            List<DynamicObject> resultList = new ArrayList<>();
            ResultSet resultSet = creator.executeQuery("select * from category");
            while(resultSet.next()){
                DynamicObject object = new StandardDynamicObject();
                object.put("category_name", resultSet.getString("category_name"));
                object.put("category_description", resultSet.getString("category_description"));
                resultList.add(object);
            }
            resultSet.close();
            return Optional.of(resultList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
