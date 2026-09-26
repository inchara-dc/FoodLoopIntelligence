package db;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/food_loop";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found. Add mysql-connector-j.jar to classpath.");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(Connection c, Statement s, ResultSet r) {
        try { if (r != null) r.close(); } catch (Exception ignored) {}
        try { if (s != null) s.close(); } catch (Exception ignored) {}
        try { if (c != null) c.close(); } catch (Exception ignored) {}
    }
}
