package org.example.factories;

public interface ThreeParamFactory<Product, Param1, Param2> {
    Product create(Param1 param1, Param2 param2);
}
