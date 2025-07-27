package kafe_otomasyonu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin extends Kullanici {
    public Admin(String isim, String rol) {
        super(isim, rol);
    }

    @Override
    public void yetki() {
        System.out.println("Admin yetkilerine sahip.");
    }

    public void urunEkle(String isim, double fiyat, String kategori) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO Urun (isim, fiyat, kategori) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isim);
            pstmt.setDouble(2, fiyat);
            pstmt.setString(3, kategori);
            pstmt.executeUpdate();
            System.out.println("Ürün başarıyla eklendi.");
        } catch (SQLException e) {
            System.out.println("Ürün eklenemedi: " + e.getMessage());
        }
    }

    public void urunSil(String isim) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "DELETE FROM Urun WHERE isim = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isim);
            pstmt.executeUpdate();
            System.out.println("Ürün başarıyla silindi.");
        } catch (SQLException e) {
            System.out.println("Ürün silinemedi: " + e.getMessage());
        }
    }
}
