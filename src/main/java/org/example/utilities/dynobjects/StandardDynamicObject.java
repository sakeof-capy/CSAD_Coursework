package org.example.utilities.dynobjects;

import org.example.exceptions.HolderException;
import org.example.utilities.holders.Holder;
import org.example.utilities.holders.StandardHolder;

import java.util.Optional;

public class StandardDynamicObject implements DynamicObject {
    public StandardDynamicObject() {
        this.holder = new StandardHolder<>();
    }

    @Override
    public void put(String keyName, String value) throws IllegalArgumentException {
        try {
            holder.hold(keyName, value);
        } catch (HolderException e) {
            throw new IllegalArgumentException("Key '" + keyName + "' already exists in the object.");
        }
    }

    @Override
    public void remove(String keyName) {
        try {
            holder.release(keyName);
        } catch (HolderException e) {
            throw new IllegalArgumentException("Key '" + keyName + "' does not exist in the object.");
        }
    }

    @Override
    public Optional<String> get(String keyName) {
        return holder.getHoldable(keyName);
    }

    private final Holder<String, String> holder;
}