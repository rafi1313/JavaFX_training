package service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
//    final static String driverMySQl = "com.mysql.jdbc.Driver";
    final static String url = "jdbc:mysql://localhost/task_manager";
    final static String uid = "root";
    final static String pwd = "SqlAccount!23";
    private Connection con = null;

    public DBConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,uid,pwd);
//            con.setAutoCommit(false);
        }
         catch (ClassNotFoundException e) {
            System.out.println("Nie znaleziono sterownika bazy danych");
            System.out.println(e);
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Nie połaczyłem się z bazą " + url);
            System.out.println(e);
            System.exit(1);
        }
    }

    public Connection getCon() {
        return con;
    }
}
