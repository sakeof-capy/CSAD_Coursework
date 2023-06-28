package Creator;

import org.example.exceptions.storage.StorageException;
import org.example.storage.DBConnector;
import org.example.storage.operations.product.ReadProductOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;

import java.util.List;
import java.util.Optional;

public class TestDB {
    public static void main(String[] args) {
        DBConnector creator = null;
        try {
            creator = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }

        ReadProductOperation readProductOperation = new ReadProductOperation(creator);
        DynamicObject object = new StandardDynamicObject();
        //object.put("product_name", "Apple");
        //object.put("category_name", "Fruits");
        object.put("category_name", "Vegetables");
        Optional<List<DynamicObject>> optional;
        try {
            optional = readProductOperation.execute(object);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        List<DynamicObject> products = optional.get();
        for (DynamicObject product : products){
            String productName = product.get("product_name").get();
            String categoryName = product.get("category_name").get();
            String productDescription = product.get("product_description").get();
            int inStock = Integer.parseInt(product.get("in_stock").get());
            double price = Double.parseDouble(product.get("price").get());
            String producer = product.get("producer").get();
            System.out.println("{\n" +
                    "product_name: " + productName+ ";\n" +
                    "category_name: " + categoryName + ";\n" +
                    "product_description: " + productDescription + ";\n" +
                    "in_stock: " + inStock + ";\n" +
                    "price: " + price + ";\n" +
                    "producer: " + producer + ";\n" +
                    "}");
        }
        System.out.println("Finish!!");
    }
}
