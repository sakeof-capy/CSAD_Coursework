package org.example.storage.operations.user;

import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.StorageException;
import org.example.storage.Storage;
import org.example.storage.operations.StorageOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CreateUserOperation implements StorageOperation {

    private Storage creator = null;

    public CreateUserOperation(Storage creator){
        this.creator = creator;
    }

    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) throws StorageException {
        try {
            String username = params.get("username").get();
            String pass = params.get("password").get();
            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            String encryptedPass = passwordEncryptor.encryptPassword(pass);
            String role = params.get("role").get();

            creator.executeUpdate(String.format("insert into users(username, password, role) values('%s', '%s', '%s');", username, encryptedPass, role));

            return Optional.empty();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new DataConflictException("Product with name " + params.get("username").get() + " already exists!!!");
        }
    }
}
