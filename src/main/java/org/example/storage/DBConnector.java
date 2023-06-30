package org.example.storage;

import org.example.exceptions.storage.StorageException;

import java.sql.*;

public class DBConnector implements Storage {
    private Connection conn;

    public DBConnector(String dbname, String user, String pass) throws StorageException {
        conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+ dbname, user, pass);
            if (conn != null){
                System.out.println("Connected to DB.");
            } else {
                System.out.println("Failed to connect to db.");
            }
        } catch (Exception e) {
            throw new StorageException(e.getMessage());
        }

    }

    public synchronized void executeUpdate(String sql) throws SQLException {
        Statement statement = null;
        statement = conn.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    @Override
    public synchronized ResultSet executeQuery(String sql) throws SQLException {

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
    }

    @Override
    public void close() throws Exception {
        conn.close();
        System.out.println("Disconnected from DB.");
    }
}
