package kafe_otomasyonu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/kafe_otomasyonu";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Veritabanı bağlantısı kurulamadı.", e);
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Bağlantı başarılı!");
            } else {
                System.out.println("Bağlantı başarısız!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
