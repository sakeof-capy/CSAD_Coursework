package org.example.storage.operations;

import org.example.utilities.dynobjects.DynamicObject;

import java.util.Optional;

/**
 * StorageOperation interface - base-unit for storage operation.
 * Designed to implement the Command Design Pattern.
 */
public interface StorageOperation {
    /**
     * Method of operation execution.
     * @param params - dynamic object of params. It is made
     *               dynamic for the sake of providing the
     *               operations with the same interface
     *               that would allow to pass different
     *               sets of params to different operations.
     * @return dynamic object for the same reason as with params.
     *         Optional.empty() is returned when the operation
     *         is supposed to be 'void'
     */
    Optional<DynamicObject> execute(DynamicObject params);
}
