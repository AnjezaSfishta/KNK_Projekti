package repositories;
import java.sql.*;

public class DatabaseHandler {
    private static final String databaseName = "admin";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/" + databaseName;
    // Variables used for connecting to the database and creating statements
    // Change these values to your mySQL values
    private static DatabaseHandler handler = null;
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler() {
        createConnection();
        setupBookTable();
        setupAddMemberTable();
        setupIssuedBooksTable();
    }

    void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/admin", "root", "12345");
            if (conn != null) {
                System.out.println("Database connection successful.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void setupBookTable() {
        String table_name = "addbook";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("Table " + table_name + " already exists.");
            }
            else {
                stmt.execute("Create Table " + table_name + "(\r\n"
                        + "Bookid varchar(200) not null,\n"
                        + "title varchar(200) not null,\n"
                        + "author varchar(200) not null,\n"
                        + "publisher varchar(200) not null,\n"
                        + "quantity int not null,\n"
                        + "isAvail boolean default true,\n"
                        + "primary key(Bookid) );\n"
                );
                System.out.println("Table " + table_name + " created.");

            }
        }catch(SQLException e) {
            System.err.println(e.getMessage() + "---setup");
        }
    }
    void setupAddMemberTable() {
        String table_name = "addMember";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("database responded");
            } else {
                stmt.execute("CREATE TABLE " + table_name + " (\n"
                        + "Memberid VARCHAR(200) NOT NULL,\n"
                        + "name VARCHAR(200) NOT NULL,\n"
                        + "email VARCHAR(200) NOT NULL,\n"
                        + "phone VARCHAR(200) NOT NULL,\n"
                        + "gender ENUM('female', 'male') NOT NULL,\n"
                        + "PRIMARY KEY (Memberid)\n"
                        + ");");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "---setupAddMemberTable");
        }
    }

    void setupUserAccountTable() {
        String table_name = "userAccount";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null, table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("database responded");
            } else {
                stmt.execute("CREATE TABLE " + table_name + " (\n"
                        + "idUserAccount INT UNSIGNED NOT NULL AUTO_INCREMENT,\n"
                        + "firstName VARCHAR(200) NOT NULL,\n"
                        + "lastName VARCHAR(200) NOT NULL,\n"
                        + "userName VARCHAR(200) NOT NULL,\n"
                        + "password VARCHAR(200) NOT NULL,\n"
                        + "PRIMARY KEY (idUserAccount)\n"
                        + ");");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "---setupUserAccountTable");
        }
    }

    public void setupIssuedBooksTable() {
        String table_name = "issuedBooks";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("database responded");
            } else {
                stmt.execute("CREATE TABLE " + table_name + " (\n"
                        + "bookID VARCHAR(200) NOT NULL,\n"
                        + "memberID VARCHAR(200) NOT NULL,\n"
                        + "issueTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                        + "renew_count INTEGER DEFAULT 0,\n"
                        + "PRIMARY KEY (bookID, memberID),\n"
                        + "FOREIGN KEY (bookID) REFERENCES addBook(Bookid),\n"
                        + "FOREIGN KEY (memberID) REFERENCES addMember(Memberid)\n"
                        + ");");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "---setupIssuedBooksTable");
        }
    }


}

