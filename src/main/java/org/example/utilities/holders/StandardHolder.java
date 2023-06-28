package org.example.utilities.holders;

import org.example.exceptions.HolderException;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class StandardHolder<Key extends Comparable<Key>, Holdable>
        implements Holder<Key, Holdable> {
    public StandardHolder() {
        keyToHoldable = new TreeMap<>();
    }

    @Override
    public void hold(Key key, Holdable holdable) throws HolderException {
        if(holds(key))
            throw new HolderException("Holdable related to the key " + key.toString() + " already held.");
        keyToHoldable.put(key, holdable);
    }

    @Override
    public void release(Key key) throws HolderException {
        if(!holds(key))
            throw new HolderException("Holdable related to the key " + key.toString() + " already held.");
        keyToHoldable.remove(key);
    }

    @Override
    public Optional<Holdable> getHoldable(Key key) {
        if(!holds(key))
            return Optional.empty();
        return Optional.of(keyToHoldable.get(key));
    }

    @Override
    public boolean holds(Key key) {
        return keyToHoldable.containsKey(key);
    }

    @Override
    public Map<Key, Holdable> getMap() {
        return keyToHoldable;
    }

    private final Map<Key, Holdable> keyToHoldable;
}
