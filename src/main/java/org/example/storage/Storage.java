package org.example.storage;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface Storage extends AutoCloseable {
    void executeUpdate(String sql) throws SQLException;
    /* Determine which result type to return: Optional<...>?*/ ResultSet executeQuery(String sql) throws SQLException;
    /*
        There might also be some additional methods that
        would allow to use prepared statements or something...
    */
}
