package DB;

import org.example.exceptions.storage.StorageException;
import org.example.storage.DBConnector;
import org.example.storage.operations.user.CreateUserOperation;
import org.example.storage.operations.user.ReadUserOperation;
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
public class UserTest {

    @Test
    @Order(1)
    void userCreationTest() throws Exception{
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someUser = new StandardDynamicObject();
        someUser.put("username", "testuser");
        someUser.put("password", "12tEstpaSS0006");
        someUser.put("role", "normal");
        CreateUserOperation createUserOperation = new CreateUserOperation(connector);
        createUserOperation.execute(someUser);
        connector.close();
    }
    @Test
    @Order(2)
    void userReadTest() throws Exception{
        DBConnector connector = null;
        try {
            connector = new DBConnector("client-server", "postgres", "root");
        } catch (StorageException e) {
            fail(e.getMessage());
        }

        DynamicObject someUser = new StandardDynamicObject();
        someUser.put("username", "testuser");
        ReadUserOperation readUserOperation = new ReadUserOperation(connector);
        Optional<List<DynamicObject>> optional = readUserOperation.execute(someUser);

        List<DynamicObject> list = optional.get();

        assertEquals(list.size(), 1);

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        for (DynamicObject user : list){
            String name = user.get("username").get();
            String password = user.get("password").get();
            String role = user.get("role").get();
            assertEquals("testuser", name);
            assertNotEquals("12tEstpaSS0006", password);
            assertTrue(passwordEncryptor.checkPassword("12tEstpaSS0006", password));
            assertEquals("normal", role);
        }

        connector.executeUpdate("delete from users where username = 'testuser'");

        connector.close();
    }
}
