package org.example.factories;

import org.example.exceptions.CreationException;

public interface OneParamFactory<Product, Param1> {
    Product create(Param1 param1) throws CreationException;
}
