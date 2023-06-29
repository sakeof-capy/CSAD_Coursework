package DB;

import org.example.exceptions.storage.StorageException;
import org.example.storage.DBConnector;
import org.example.storage.operations.product.CreateProductOperation;
import org.example.storage.operations.product.DeleteProductOperation;
import org.example.storage.operations.product.ReadProductOperation;
import org.example.storage.operations.product.UpdateProductOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestDB {

    @Test
    void testDB() throws Exception{
        DBConnector creator = null;
        try {
            creator = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }
        assertEquals(readProduct(creator).size(), 5);

        for (int i = 0; i < 10; ++i){
            DynamicObject someObject = new StandardDynamicObject();
            someObject.put("product_name", "Product_" + i);
            String cat;
            if (i%2 == 0){
                cat = "Fruits";
            } else {
                cat = "Vegetables";
            }
            someObject.put("category_name", cat);
            someObject.put("product_description", "i=" + i);
            int j = i*100 + 1;
            someObject.put("in_stock", String.valueOf(j));
            double p = (i+1)*5.5;
            someObject.put("price", String.valueOf(p));
            someObject.put("producer", "Home_" + i);
            CreateProductOperation createProductOperation = new CreateProductOperation(creator);
            try {
                createProductOperation.execute(someObject);
            } catch (StorageException e) {
                fail(e.getMessage());

            }
        }
        System.out.println("Created");
        assertEquals(readProduct(creator).size(), 15);

        for (int i = 0; i < 10; ++i){
            DynamicObject someObject = new StandardDynamicObject();
            someObject.put("product_name", "Product_" + i);
            String cat;
            if (i%2 == 0){
                cat = "Fruits";
            } else {
                cat = "Vegetables";
            }
            someObject.put("category_name", cat);
            someObject.put("product_description", "i=" + i + i);
            int j = i*100 + 1;
            someObject.put("in_stock", String.valueOf(j));
            double p = (i+1)*5.5;
            someObject.put("price", String.valueOf(p));
            someObject.put("producer", "Home_" + i);
            UpdateProductOperation updateProductOperation = new UpdateProductOperation(creator);
            try {
                updateProductOperation.execute(someObject);
            } catch (StorageException e) {
                fail(e.getMessage());
            }
        }
        System.out.println("Updated");
        assertEquals(readProduct(creator).size(), 15);

        for (int i = 0; i < 10; ++i){
            DynamicObject someObject = new StandardDynamicObject();
            someObject.put("product_name", "Product_" + i);
            DeleteProductOperation deleteProductOperation = new DeleteProductOperation(creator);
            try {
                deleteProductOperation.execute(someObject);
            } catch (StorageException e) {
                fail(e.getMessage());
            }
        }
        System.out.println("Deleted");

        assertEquals(readProduct(creator).size(), 5);


        creator.close();
    }

    private static List<DynamicObject> readProduct(DBConnector creator) {
        ReadProductOperation readProductOperation = new ReadProductOperation(creator);
        DynamicObject object = new StandardDynamicObject();
        Optional<List<DynamicObject>> optional = Optional.empty();
        try {
            optional = readProductOperation.execute(object);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
        List<DynamicObject> products = optional.get();

        /*for (DynamicObject product : products){
            String productName = product.get("product_name").get();
            String categoryName = product.get("category_name").get();
            String productDescription = product.get("product_description").get();
            int inStock = Integer.parseInt(product.get("in_stock").get());
            double price = Double.parseDouble(product.get("price").get());
            String producer = product.get("producer").get();

            assertFalse(product.getMap().isEmpty());

            System.out.println("{\n" +
                    "product_name: " + productName+ ";\n" +
                    "category_name: " + categoryName + ";\n" +
                    "product_description: " + productDescription + ";\n" +
                    "in_stock: " + inStock + ";\n" +
                    "price: " + price + ";\n" +
                    "producer: " + producer + ";\n" +
                    "}");
        }*/
        return products;
    }
}
