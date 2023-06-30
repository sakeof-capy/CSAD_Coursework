package org.example.storage.operations;

import org.example.exceptions.storage.NotFoundException;
import org.example.utilities.dynobjects.DynamicObject;

import java.util.List;

public class SumCounter {
    public double count(List<DynamicObject> list) {
        double sum = 0;
        for (DynamicObject object : list) {
            String product = object.get("product_name").orElse("");
            if (product.equals("")) {
                return sum;
            } else {
                int inStock = Integer.parseInt(object.get("in_stock").get());
                double price = Double.parseDouble(object.get("price").get());
                sum += inStock * price;
            }
        }
        return sum;
    }
}
