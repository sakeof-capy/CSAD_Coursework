package org.example.utilities.dynobjects;

import java.util.Optional;

public interface DynamicObject {
    void put(String keyName, String value) throws IllegalArgumentException;
    void remove(String keyName);
    Optional<String> get(String keyName);
}
