package org.example.storage.operations;

import org.example.storage.Storage;
import org.example.utilities.dynobjects.DynamicObject;

import java.util.List;
import java.util.Optional;

public class GetGoodQuantityOperation implements StorageOperation {
    public GetGoodQuantityOperation(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<List<DynamicObject>> execute(DynamicObject params) {
        //Some execution logic...
        return Optional.empty();
    }

    private final Storage storage;
}
