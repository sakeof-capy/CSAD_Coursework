package DB;

import org.example.exceptions.storage.StorageException;
import org.example.storage.DBConnector;
import org.example.storage.operations.category.CreateCategoryOperation;
import org.example.storage.operations.category.DeleteCategoryOperation;
import org.example.storage.operations.category.ReadCategoryOperation;
import org.example.storage.operations.category.UpdateCategoryOperation;
import org.example.storage.operations.product.CreateProductOperation;
import org.example.storage.operations.product.ReadProductOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryTest {
    @Test
    @Order(1)
    void createCategory() throws Exception{
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someCategory = new StandardDynamicObject();
        someCategory.put("category_name", "testcategory");
        someCategory.put("category_description", "This category was created for testing code");
        CreateCategoryOperation createCategoryOperation = new CreateCategoryOperation(connector);
        createCategoryOperation.execute(someCategory);
        connector.close();
    }
    @Test
    @Order(2)
    void readCategory() throws Exception{
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someCategory = new StandardDynamicObject();
        ReadCategoryOperation readCategoryOperation = new ReadCategoryOperation(connector);
        Optional<List<DynamicObject>> optional = readCategoryOperation.execute(someCategory);

        List<DynamicObject> list = optional.get();

        assertEquals(list.size(), 3);

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        DynamicObject user = list.get(2);
            String categoryName = user.get("category_name").get();
            String categoryDescription = user.get("category_description").get();
            assertEquals("testcategory", categoryName);
            assertEquals("This category was created for testing code", categoryDescription);


        connector.executeUpdate("delete from users where username = 'testuser'");

        connector.close();
    }

    @Test
    @Order(3)
    void updateCategory() throws Exception{
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someCategory = new StandardDynamicObject();
        someCategory.put("category_name", "testcategory");
        someCategory.put("category_description", "This category was changed by testing code!!!");
        UpdateCategoryOperation updateCategoryOperation = new UpdateCategoryOperation(connector);
        updateCategoryOperation.execute(someCategory);
        connector.close();
    }
    @Test
    @Order(4)
    void readCategory2() throws Exception{
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someCategory = new StandardDynamicObject();
        ReadCategoryOperation readCategoryOperation = new ReadCategoryOperation(connector);
        Optional<List<DynamicObject>> optional = readCategoryOperation.execute(someCategory);

        List<DynamicObject> list = optional.get();

        assertEquals(list.size(), 3);

        DynamicObject user = list.get(2);
        String categoryName = user.get("category_name").get();
        String categoryDescription = user.get("category_description").get();
        assertEquals("testcategory", categoryName);
        assertEquals("This category was changed by testing code!!!", categoryDescription);


        connector.executeUpdate("delete from users where username = 'testuser'");

        connector.close();
    }

    @Test
    @Order(5)
    void createProduct() throws Exception{
        DBConnector creator = null;
        try {
            creator = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }
        assertEquals(readAllProduct(creator).size(), 5);

        for (int i = 0; i < 10; ++i){
            DynamicObject someObject = new StandardDynamicObject();
            someObject.put("product_name", "Product_" + i);
            String cat = "testcategory";
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

        assertEquals(readAllProduct(creator).size(), 15);
        assertEquals(readSpecProduct(creator).size(), 10);
        creator.close();
    }
    @Test
    @Order(6)
    void deleteCategory() throws Exception{
        DBConnector creator = null;
        try {
            creator = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someCategory = new StandardDynamicObject();
        someCategory.put("category_name", "testcategory");
        DeleteCategoryOperation deleteCategoryOperation = new DeleteCategoryOperation(creator);
        deleteCategoryOperation.execute(someCategory);

        assertEquals(readAllProduct(creator).size(), 5);
        assertEquals(readSpecProduct(creator).size(), 0);
        creator.close();
    }

    @Test
    @Order(7)
    void readCategory3() throws Exception {
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someCategory = new StandardDynamicObject();
        ReadCategoryOperation readCategoryOperation = new ReadCategoryOperation(connector);
        Optional<List<DynamicObject>> optional = readCategoryOperation.execute(someCategory);

        List<DynamicObject> list = optional.get();

        assertEquals(list.size(), 2);
        connector.close();
    }

    private static List<DynamicObject> readAllProduct(DBConnector creator) {
        ReadProductOperation readProductOperation = new ReadProductOperation(creator);
        DynamicObject object = new StandardDynamicObject();
        Optional<List<DynamicObject>> optional = Optional.empty();
        try {
            optional = readProductOperation.execute(object);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
        List<DynamicObject> products = optional.get();

        return products;
    }

    private static List<DynamicObject> readSpecProduct(DBConnector creator) {
        ReadProductOperation readProductOperation = new ReadProductOperation(creator);
        DynamicObject object = new StandardDynamicObject();
        object.put("category_name", "testcategory");
        Optional<List<DynamicObject>> optional = Optional.empty();
        try {
            optional = readProductOperation.execute(object);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
        List<DynamicObject> products = optional.get();

        return products;
    }
}
