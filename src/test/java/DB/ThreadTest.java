package DB;

import org.example.exceptions.storage.StorageException;
import org.example.storage.DBConnector;
import org.example.storage.operations.product.CreateProductOperation;
import org.example.storage.operations.product.DeleteProductOperation;
import org.example.storage.operations.product.ReadProductOperation;
import org.example.utilities.ThreadUtils;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ThreadTest {
    @Test
    void threadPool() {
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int numberOfThreads = 100;
        DBConnector finalConnector = connector;
        for(int i = 0; i < numberOfThreads; ++i) {

            threadPool.submit(() -> {
                createProduct(finalConnector);
            });
        }

        ThreadUtils.shutDownThreadPool(threadPool);

        assertEquals(readProduct(finalConnector).size(), 1);

        DynamicObject someObject = new StandardDynamicObject();
        someObject.put("product_name", "Product_1");
        DeleteProductOperation deleteProductOperation = new DeleteProductOperation(finalConnector);
        try {
            deleteProductOperation.execute(someObject);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    void createProduct(DBConnector connector){
        DynamicObject someObject = new StandardDynamicObject();
        someObject.put("product_name", "Product_1");
        someObject.put("category_name", "Fruits");
        someObject.put("product_description", "i=1");
        someObject.put("in_stock", String.valueOf(100));
        someObject.put("price", String.valueOf(50));
        someObject.put("producer", "Home_1");
        CreateProductOperation createProductOperation = new CreateProductOperation(connector);
        try {
            createProductOperation.execute(someObject);
        } catch (StorageException e) {
            //fail(e.getMessage());

        }
    }

    private static List<DynamicObject> readProduct(DBConnector creator) {
        ReadProductOperation readProductOperation = new ReadProductOperation(creator);
        DynamicObject object = new StandardDynamicObject();
        object.put("product_name", "Product_1");
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
