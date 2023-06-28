package DB;

import org.example.exceptions.storage.StorageException;
import org.example.storage.DBConnector;
import org.example.storage.operations.category.CreateCategoryOperation;
import org.example.storage.operations.category.ReadCategoryOperation;
import org.example.storage.operations.category.UpdateCategoryOperation;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateCategoryTest {
    @Test
    @Order(1)
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

        DynamicObject user = list.get(2);
        String categoryName = user.get("category_name").get();
        String categoryDescription = user.get("category_description").get();
        assertEquals("testcategory", categoryName);
        assertEquals("This category was changed by testing code!!!", categoryDescription);


        connector.executeUpdate("delete from users where username = 'testuser'");

        connector.close();
    }
}
