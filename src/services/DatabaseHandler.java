package services;

import java.sql.*;

public class DatabaseHandler {
    private static final String databaseName = "admin";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/" + databaseName;
    // Variables used for connecting to the database and creating statements
    // Change these values to your mySQL values
    private static DatabaseHandler handler = null;
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssuedBooksTable();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
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
        String table_name = "BOOK";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("Table " + table_name + " already exists.");
            }
            else {
                stmt.execute("Create Table " + table_name + "(\r\n"
                        + "bookId varchar(200) not null,\n"
                        + "title varchar(200) not null,\n"
                        + "author varchar(200) not null,\n"
                        + "publisher varchar(200) not null,\n"
                        //+ "quantity int not null,\n"
                        + "isAvail boolean default true,\n"
                        + "primary key(bookId) );\n"
                );
                System.out.println("Table " + table_name + " created.");

            }
        }catch(SQLException e) {
            System.err.println(e.getMessage() + "---setup");
        }
    }

    public boolean deleteBook(models.Book book) {
        try {
            String deleteStatement = "DELETE FROM BOOK WHERE bookId  = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, book.getBookID());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Nuk mundemi me fshi nje liber qe vetem e ka marr dikush, dhe nuk e ka kthy ende!");
        }
        return false;
    }

    void setupMemberTable() {
        String table_name = "MEMBER";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("Table " + table_name + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + table_name + " (\n"
                        + "memberId VARCHAR(200) NOT NULL,\n"
                        + "name VARCHAR(200) NOT NULL,\n"
                        + "phone VARCHAR(200) NOT NULL,\n"
                        + "email VARCHAR(200) NOT NULL,\n"
                        //+ "gender ENUM('female', 'male') NOT NULL,\n"
                        + "PRIMARY KEY (memberid)\n"
                        + ");");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "---setupAddMemberTable");
        }
    }

    public boolean deleteMember(models.Member member) {
        try {
            String deleteStatement = "DELETE FROM MEMBER where memberId = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, member.getMemberID());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Nuk mundemi me fshi nje liber qe vetem e ka marr dikush, dhe nuk e ka kthy ende!");
        }
        return false;
    }


//    void setupUserAccountTable() {
//        String table_name = "userAccount";
//        try {
//            stmt = conn.createStatement();
//
//            DatabaseMetaData dmb = conn.getMetaData();
//            ResultSet tables = dmb.getTables(null, null, table_name.toUpperCase(), null);
//
//            if(tables.next()) {
//                System.out.println("Table " + table_name + " already exists.");
//            } else {
//                stmt.execute("CREATE TABLE " + table_name + " (\n"
//                        + "idUserAccount INT UNSIGNED NOT NULL AUTO_INCREMENT,\n"
//                        + "firstName VARCHAR(200) NOT NULL,\n"
//                        + "lastName VARCHAR(200) NOT NULL,\n"
//                        + "userName VARCHAR(200) NOT NULL,\n"
//                        + "password VARCHAR(200) NOT NULL,\n"
//                        + "PRIMARY KEY (idUserAccount)\n"
//                        + ");");
//            }
//        } catch(SQLException e) {
//            System.err.println(e.getMessage() + "---setupUserAccountTable");
//        }
//    }

    public boolean updateMember(models.Member member) {
        String update = "UPDATE MEMBER SET name =? , email = ? , phone = ? WHERE memberId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getMemberID());


            int res = stmt.executeUpdate();

            return (res > 0);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean updateBook(models.Book book) {
        String update = "UPDATE BOOK SET title =? , author = ? , publisher = ?  WHERE bookId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getBookID());


            int res = stmt.executeUpdate();

            return (res > 0);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void setupIssuedBooksTable() {
        String table_name = "ISSUE";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dmb = conn.getMetaData();
            ResultSet tables = dmb.getTables(null, null,  table_name.toUpperCase(), null);

            if(tables.next()) {
                System.out.println("Table " + table_name + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + table_name + " (\n"
                        + "bookId VARCHAR(200) NOT NULL,\n"
                        + "memberId VARCHAR(200) NOT NULL,\n"
                        + "issueTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                        + "renew_count INTEGER DEFAULT 0,\n"
                        + "PRIMARY KEY (bookId, memberId),\n"
                        + "FOREIGN KEY (bookId) REFERENCES BOOK(bookId),\n"
                        + "FOREIGN KEY (memberId) REFERENCES MEMBER(memberId)\n"
                        + ");");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "---setupIssuedBooksTable");
        }
    }



    //__________________________________________________________________________________
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }


    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }
}

