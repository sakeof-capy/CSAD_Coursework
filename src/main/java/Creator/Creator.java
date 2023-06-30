package Creator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

public class Creator {
    public static void main(String[] args) throws IOException {
        final var pathToPassword = "src/main/resources/password.txt";
        FileReader reader = null;
        try {
            reader = new FileReader(pathToPassword);
        } catch (FileNotFoundException e) {
            System.err.println("Create file: " + pathToPassword + " and store your postgres password here.");
            System.exit(-1);
        }
        var buffReader = new BufferedReader(reader);
        var password = buffReader.readLine();

        DBFunctions db = new DBFunctions();
        Connection conn = db.connect_to_db("client-server", "postgres", password);

        //db.create_UserRole(conn);
        db.create_category(conn);
        db.create_user(conn);
        db.create_product(conn);
        //db.create_session(conn);

        // add users
        db.add_user(conn, "admin", "admin", "admin");
        db.add_user(conn, "user1", "mypass", "normal");
        db.add_user(conn, "user2", "password", "normal");
        db.add_user(conn, "user3", "user", "normal");

        //add category
        db.add_category(conn, "Fruits", "Better to eat fresh");
        db.add_category(conn, "Vegetables", "");

        //add product
        db.add_product(conn, "Apple", "Fruits", "", 100, 50, "Ukraine");
        db.add_product(conn, "Pineapple", "Fruits", "", 25, 55, "Ukraine");
        db.add_product(conn, "Carrot", "Vegetables", "", 60, 30, "United Kingdom");
        db.add_product(conn, "Beet", "Vegetables", "", 20, 78, "Turkey");
        db.add_product(conn, "Potato", "Vegetables", "", 500, 65, "Poland");
    }
}
