package http;

import io.restassured.RestAssured;
import org.example.exceptions.CreationException;
import org.example.exceptions.storage.StorageException;
import org.example.server.Server;
import org.example.server.StandardHttpServer;
import org.example.server.StandardHttpServerFactory;
import org.example.storage.DBConnector;
import org.example.storage.Storage;
import org.example.utilities.http.HttpUtils;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

public class HTTPTest {

    private static Server server;


    @BeforeAll
    public static void init() throws IOException {

        RestAssured.port = HttpUtils.PORT;
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
        try {
            var storage = new DBConnector("client-server", "postgres",password);
            closeDbWhenShutdown(storage);
            var serverFactory = new StandardHttpServerFactory();
            server = serverFactory.create(storage, new InetSocketAddress(HttpUtils.PORT));
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void closeDbWhenShutdown(Storage storage) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                storage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
    @Test
    void getProduct() {
        given()
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body(not(emptyOrNullString()));
    }
    @Test
    void putPostDeleteProduct(){

        given()
                .body("""
                        {
                            "category_name": "Fruits",
                            "in_stock": "100",
                            "price": "50.0",
                            "producer": "Ukraine",
                            "product_description": "They are orange!!:)",
                            "product_name": "Orange"
                        }
                        """)
                .when()
                .put("/api/product")
                .then()
                .statusCode(204);
        given()
                .body("""
                        {
                            "category_name": "Fruits",
                            "in_stock": "5100",
                            "price": "555.0",
                            "producer": "Ukraine",
                            "product_description": "They are orange!!:)",
                            "product_name": "Orange"
                        }
                        """)
                .when()
                .post("/api/product/?product_name=Orange")
                .then()
                .statusCode(204);
        given()
                .when()
                .delete("/api/product/?product_name=Orange")
                .then()
                .statusCode(204);
    }

    @Test
    void fakeCreationProduct(){
        given()
                .body("""
                        {
                            "category_name": "Fruits",
                            "in_stock": "100",
                            "price": "50.0",
                            "producer": "Ukraine",
                            "product_description": "They are orange!!:)",
                            "product_name": "Apple"
                        }
                        """)
                .when()
                .put("/api/product")
                .then()
                .statusCode(409);
    }

    @Test
    void getCategory() {
        given()
                .when()
                .get("/api/categories")
                .then()
                .statusCode(200)
                .body(not(emptyOrNullString()));
    }
    @Test
    void putPostDeleteCategory(){

        given()
                .body("""
                        {
                            "category_name": "Cat",
                            "category_description": "Just new category for test!!:)"
                        }
                        """)
                .when()
                .put("/api/category")
                .then()
                .statusCode(204);
        given()
                .body("""
                        {
                            "category_name": "Cat",
                            "category_description": "Just changed category for test!!:)"
                        }
                        """)
                .when()
                .post("/api/category/?category_name=Cat")
                .then()
                .statusCode(204);
        given()
                .when()
                .delete("/api/category/?category_name=Cat")
                .then()
                .statusCode(204);
    }

    @Test
    void fakeCreationCategory(){
        given()
                .body("""
                        {
                            "category_name": "Fruits",
                            "category_description": "Just changed category for test!!:)"
                        }
                        """)
                .when()
                .put("/api/category")
                .then()
                .statusCode(409);
    }

}
