package org.example.factories;

public interface OneParamFactory<Product, Param1> {
    Product create(Param1 param1);
}
