package org.example.storage.operations.user;

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

public class ReadUserOperation implements StorageOperation {

    private Storage creator = null;

    public ReadUserOperation(Storage creator){
        this.creator = creator;
    }
    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String username = params.get("username").orElse("");
            String addition = "";
            if (!username.equals("")){
                addition = String.format(" where username = '%s'", username);
            }
            List<DynamicObject> resultList = new ArrayList<>();
            ResultSet resultSet = creator.executeQuery("select * from users" + addition);
            while(resultSet.next()){
                DynamicObject object = new StandardDynamicObject();
                object.put("username", resultSet.getString("username"));
                object.put("password", resultSet.getString("password"));
                object.put("role", resultSet.getString("role"));
                resultList.add(object);
            }
            resultSet.close();
            return Optional.of(resultList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
