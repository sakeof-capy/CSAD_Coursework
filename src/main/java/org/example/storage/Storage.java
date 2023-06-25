package org.example.storage;

import org.example.exceptions.StorageException;

import java.util.Optional;

public interface Storage extends AutoCloseable {
    void executeUpdate(String sql) throws StorageException;
    /* Determine which result type to return: Optional<...>?*/ void executeQuery(String sql);
    /*
        There might also be some additional methods that
        would allow to use prepared statements or something...
    */
}
