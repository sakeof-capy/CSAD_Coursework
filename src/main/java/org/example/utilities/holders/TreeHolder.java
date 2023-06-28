package org.example.utilities.holders;

import org.example.exceptions.HolderException;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class TreeHolder<Key extends Comparable<Key>, Holdable>
        implements Holder<Key, Holdable> {
    public TreeHolder() {
        this(new TreeMap<>());
    }

    public TreeHolder(TreeMap<Key, Holdable> treeMap) {
        this.holder = new MapHolder<>(treeMap);
    }

    @Override
    public void hold(Key key, Holdable holdable) throws HolderException {
        holder.hold(key, holdable);
    }

    @Override
    public void release(Key key) throws HolderException {
        holder.release(key);
    }

    @Override
    public Optional<Holdable> getHoldable(Key key) {
        return holder.getHoldable(key);
    }

    @Override
    public boolean holds(Key key) {
        return holder.holds(key);
    }

    @Override
    public Map<Key, Holdable> getMap() {
        return holder.getMap();
    }

    private final Holder<Key, Holdable> holder;
}
