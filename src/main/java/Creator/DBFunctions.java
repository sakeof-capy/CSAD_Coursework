package Creator;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBFunctions {
    public Connection connect_to_db(String dbname, String user, String pass){
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, pass);
            if (conn != null){
                System.out.println("Connected");
            } else {
                System.out.println("Failed");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

    public void create_UserRole(Connection conn){
        Statement statement;
        try{
            String query = "CREATE TYPE UserRole AS ENUM ('admin', 'unspecified', 'normal');";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created user");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void create_user(Connection conn){
        Statement statement;
        try{
            String query = "create table users (username varchar(50) not null, password text not null, role varchar(50) not null, primary key(username));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created user");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void create_session(Connection conn){
        Statement statement;
        try{
            String query = "create table session (session_id serial8 not null, username varchar(50) not null, token uuid not null, primary key(session_id));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created session");
            create_fki_username(conn);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void create_category(Connection conn){
        Statement statement;
        try {
            String query = "create table category (category_name varchar(50) not null, category_description varchar(100), primary key(category_name))";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created category");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void create_product (Connection conn){
        Statement statement;
        try{
            String query = "create table product (product_name varchar(50) not null, category_name varchar(50) not null, product_description varchar(100), in_stock integer default 0, price decimal(13,2) not null, producer varchar(50) not null, primary key(product_name));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created product");
            create_fki_category_name(conn);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void create_fki_category_name(Connection conn) {
        Statement statement;
        try{
            String query = "ALTER TABLE IF EXISTS public.product\n" +
                    "    ADD FOREIGN KEY (category_name)\n" +
                    "    REFERENCES public.category (category_name) MATCH FULL\n" +
                    "    ON UPDATE CASCADE\n" +
                    "    ON DELETE CASCADE;\n" +
                    "CREATE INDEX IF NOT EXISTS fki_category_name\n" +
                    "    ON public.product(category_name);";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created fki_category_name");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void create_fki_username (Connection conn) {
        Statement statement;
        try{
            String query = "ALTER TABLE IF EXISTS public.session\n" +
                    "    ADD FOREIGN KEY (username)\n" +
                    "    REFERENCES public.users (username) MATCH SIMPLE\n" +
                    "    ON UPDATE CASCADE\n" +
                    "    ON DELETE CASCADE;\n" +
                    "CREATE INDEX IF NOT EXISTS fki_username\n" +
                    "    ON public.session(username);";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Created fki_username");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void add_user (Connection conn, String username, String pass, String role){
        Statement statement;
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPass = passwordEncryptor.encryptPassword(pass);
        try{
            String query = String.format("insert into users(username, password, role) values('%s', '%s', '%s');", username, encryptedPass, role);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Added" + username);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void add_category (Connection conn, String name, String description) {
        Statement statement;
        try{
            String query = String.format("insert into category(category_name, category_description) values('%s', '%s');", name, description);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Added" + name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void add_product (Connection conn, String product_name, String category_name, String description, int in_stock, double price, String producer){
        Statement statement;
        try{
            String query = String.format("insert into product(product_name, category_name, product_description, in_stock, price, producer) values('%s', '%s', '%s', '%s', '%s', '%s');", product_name, category_name, description, in_stock, price, producer);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Added" + product_name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
