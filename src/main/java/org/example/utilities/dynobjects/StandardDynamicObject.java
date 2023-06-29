package org.example.utilities.dynobjects;

import org.example.exceptions.HolderException;
import org.example.utilities.holders.Holder;
import org.example.utilities.holders.TreeHolder;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class StandardDynamicObject implements DynamicObject {
    public StandardDynamicObject() {
        this.holder = new TreeHolder<>();
    }

    public StandardDynamicObject(TreeMap<String, String> treeMap) {
        this.holder = new TreeHolder<>(treeMap);
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

    @Override
    public Map<String, String> getMap() {
        return holder.getMap();
    }

    private final Holder<String, String> holder;
}
