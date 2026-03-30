package Control;
import java.sql.*;

public class DBConnection {
    private static final String URL  = "jdbc:postgresql://localhost:5432/staff_management";
    private static final String USER = "postgres";
    private static final String PASS = "140407";
    private static Connection conn;

    public static Connection get() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return conn;
    }

    public static void close() {
        try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}