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
}

