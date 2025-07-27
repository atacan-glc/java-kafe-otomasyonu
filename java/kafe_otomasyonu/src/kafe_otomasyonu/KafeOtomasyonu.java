package kafe_otomasyonu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



import java.sql.*;
import java.util.*;

public class KafeOtomasyonu {
    public Map<Integer, Map<String, Integer>> masalar;
    public Map<String, Double> tatliMenu;
    public Map<String, Double> icecekMenu;
    public Map<String, Double> anaYemekMenu;
    private boolean isMenuDisplayed;

    public Kullanici aktifKullanici;

    public KafeOtomasyonu() {
        this.masalar = new HashMap<>();
        this.tatliMenu = new HashMap<>();
        this.icecekMenu = new HashMap<>();
        this.anaYemekMenu = new HashMap<>();
        this.isMenuDisplayed = false;

        // Veritabanından masaları ve siparişleri yükle
        masalariYukle();
        siparisleriYukle();
    }

    private void masalariYukle() {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM Masa";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int masaNo = rs.getInt("masaNo");
                String durumu = rs.getString("durumu");
                masalar.put(masaNo, new HashMap<>());
            }
        } catch (SQLException e) {
            System.out.println("Masalar yüklenemedi: " + e.getMessage());
        }
    }

    private void siparisleriYukle() {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM Siparis";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int masaNo = rs.getInt("masaNo");
                String urun = rs.getString("urun");
                int adet = rs.getInt("adet");
                masalar.get(masaNo).put(urun, adet);
            }
        } catch (SQLException e) {
            System.out.println("Siparişler yüklenemedi: " + e.getMessage());
        }
    }

    public void kayit(String isim, String sifre, String rol) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO Kullanici (isim, sifre, rol) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isim);
            pstmt.setString(2, sifre);
            pstmt.setString(3, rol);
            pstmt.executeUpdate();
            System.out.println("Kayıt başarılı!");
        } catch (SQLException e) {
            System.out.println("Kayıt başarısız: " + e.getMessage());
        }
    }

    public boolean giris(String isim, String sifre) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM Kullanici WHERE isim = ? AND sifre = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isim);
            pstmt.setString(2, sifre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String rol = rs.getString("rol");
                if ("admin".equals(rol)) {
                    aktifKullanici = new Admin(isim, rol);
                } else {
                    aktifKullanici = new Musteri(isim, rol);
                }
                aktifKullanici.girisYap();
                aktifKullanici.yetki();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Giriş başarısız: " + e.getMessage());
        }
        return false;
    }

    public void urunleriYukle() {
        tatliMenu.clear();
        icecekMenu.clear();
        anaYemekMenu.clear();
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM Urun";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String isim = rs.getString("isim");
                double fiyat = rs.getDouble("fiyat");
                String kategori = rs.getString("kategori");
                switch (kategori) {
                    case "tatli":
                        tatliMenu.put(isim, fiyat);
                        break;
                    case "icecek":
                        icecekMenu.put(isim, fiyat);
                        break;
                    case "anaYemek":
                        anaYemekMenu.put(isim, fiyat);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ürünler yüklenemedi: " + e.getMessage());
        }
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

            // Ürün silindikten sonra menüyü yeniden yükle
            urunleriYukle();
        } catch (SQLException e) {
            System.out.println("Ürün silinemedi: " + e.getMessage());
        }
    }

    public void siparisVer(int masaNo, String urun, int adet) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO Siparis (masaNo, urun, adet) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, masaNo);
            pstmt.setString(2, urun);
            pstmt.setInt(3, adet);
            pstmt.executeUpdate();

            masalar.get(masaNo).put(urun, adet);
            String updateMasaSql = "UPDATE Masa SET durumu = 'dolu' WHERE masaNo = ?";
            PreparedStatement pstmtMasa = conn.prepareStatement(updateMasaSql);
            pstmtMasa.setInt(1, masaNo);
            pstmtMasa.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Sipariş verilemedi: " + e.getMessage());
        }
    }

    public String hesapAl(int masaNo) {
        Map<String, Integer> siparisler = masalar.get(masaNo);
        StringBuilder sb = new StringBuilder();
        if (siparisler.isEmpty()) {
            sb.append("Masa ").append(masaNo).append(" boş!\n");
        } else {
            double toplamTutar = hesapHesapla(siparisler);
            sb.append("Masa ").append(masaNo).append(" Hesap:\n");
            sb.append("--------------------------------------------------\n");
            sb.append("| Ürün        | Adet | Toplam (TL) |\n");
            sb.append("--------------------------------------------------\n");
            for (Map.Entry<String, Integer> entry : siparisler.entrySet()) {
                String urun = entry.getKey();
                int adet = entry.getValue();
                double urunTutari = 0;
                if (tatliMenu.containsKey(urun)) {
                    urunTutari = tatliMenu.get(urun);
                } else if (icecekMenu.containsKey(urun)) {
                    urunTutari = icecekMenu.get(urun);
                } else if (anaYemekMenu.containsKey(urun)) {
                    urunTutari = anaYemekMenu.get(urun);
                }
                sb.append(String.format("| %-12s| %-5d| %-12.2f|\n", urun, adet, urunTutari * adet));
            }
            sb.append("--------------------------------------------------\n");
            sb.append("Toplam Tutar: ").append(toplamTutar).append(" TL\n");
        }
        return sb.toString();
    }

    public void masaKapat(int masaNo) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String deleteSiparisSql = "DELETE FROM Siparis WHERE masaNo = ?";
            PreparedStatement pstmtSiparis = conn.prepareStatement(deleteSiparisSql);
            pstmtSiparis.setInt(1, masaNo);
            pstmtSiparis.executeUpdate();

            String updateMasaSql = "UPDATE Masa SET durumu = 'bos' WHERE masaNo = ?";
            PreparedStatement pstmtMasa = conn.prepareStatement(updateMasaSql);
            pstmtMasa.setInt(1, masaNo);
            pstmtMasa.executeUpdate();

            masalar.put(masaNo, new HashMap<>()); // Masa siparişlerini sıfırla
        } catch (SQLException e) {
            System.out.println("Masa kapatılırken hata oluştu: " + e.getMessage());
        }
    }

    public double hesapHesapla(Map<String, Integer> siparisler) {
        double toplamTutar = 0;
        for (Map.Entry<String, Integer> entry : siparisler.entrySet()) {
            String urun = entry.getKey();
            int adet = entry.getValue();
            double urunTutari = 0;
            if (tatliMenu.containsKey(urun)) {
                urunTutari = tatliMenu.get(urun);
            } else if (icecekMenu.containsKey(urun)) {
                urunTutari = icecekMenu.get(urun);
            } else if (anaYemekMenu.containsKey(urun)) {
                urunTutari = anaYemekMenu.get(urun);
            }
            toplamTutar += urunTutari * adet;
        }
        return toplamTutar;
    }
}
