package kafe_otomasyonu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private KafeOtomasyonu kafeOtomasyonu;
    private JTextArea displayArea;

    public MainFrame(KafeOtomasyonu kafeOtomasyonu) {
        this.kafeOtomasyonu = kafeOtomasyonu;
        setTitle("Kafe Otomasyonu - Ana Ekran");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1));

        JButton viewMenuButton = new JButton("Menüyü Görüntüle");
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuGoster();
            }
        });
        panel.add(viewMenuButton);

        JButton showTablesButton = new JButton("Masa Durumu Göster");
        showTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                masaDurumunuGoster();
            }
        });
        panel.add(showTablesButton);

        JButton orderButton = new JButton("Sipariş Ver");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siparisVer();
            }
        });
        panel.add(orderButton);

        JButton billButton = new JButton("Hesap Al");
        billButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hesapAl();
            }
        });
        panel.add(billButton);

        JButton closeTableButton = new JButton("Masa Kapat");
        closeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                masaKapat();
            }
        });
        panel.add(closeTableButton);

        // Admin kullanıcılar için "Ürün Ekle" butonu
        if (kafeOtomasyonu.aktifKullanici instanceof Admin) {
            JButton addProductButton = new JButton("Ürün Ekle");
            addProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AddProductDialog(MainFrame.this, kafeOtomasyonu).setVisible(true);
                }
            });
            panel.add(addProductButton);

            // Admin kullanıcılar için "Ürün Sil" butonu
            JButton removeProductButton = new JButton("Ürün Sil");
            removeProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new RemoveProductDialog(MainFrame.this, kafeOtomasyonu).setVisible(true);
                }
            });
            panel.add(removeProductButton);
        }

        // Çıkış Yap butonu
        JButton logoutButton = new JButton("Çıkış Yap");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        panel.add(logoutButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.WEST);
    }

    private void menuGoster() {
        kafeOtomasyonu.urunleriYukle(); // Menü verilerini yeniden yükle
        StringBuilder sb = new StringBuilder();
        sb.append("Menüler:\n\nTatlılar:\n");
        sb.append("--------------------------------------------------\n");
        sb.append("| No  | Ürün        | Fiyat (TL) |\n");
        sb.append("--------------------------------------------------\n");
        int i = 1;
        for (Map.Entry<String, Double> entry : kafeOtomasyonu.tatliMenu.entrySet()) {
            String urun = entry.getKey();
            double fiyat = entry.getValue();
            sb.append(String.format("| %-3s | %-12s| %-13s|\n", i, urun, fiyat));
            i++;
        }
        sb.append("--------------------------------------------------\n\nİçecekler:\n");
        sb.append("--------------------------------------------------\n");
        sb.append("| No  | Ürün        | Fiyat (TL) |\n");
        sb.append("--------------------------------------------------\n");
        i = 1;
        for (Map.Entry<String, Double> entry : kafeOtomasyonu.icecekMenu.entrySet()) {
            String urun = entry.getKey();
            double fiyat = entry.getValue();
            sb.append(String.format("| %-3s | %-12s| %-13s|\n", i + kafeOtomasyonu.tatliMenu.size(), urun, fiyat));
            i++;
        }
        sb.append("--------------------------------------------------\n\nAna Yemekler:\n");
        sb.append("--------------------------------------------------\n");
        sb.append("| No  | Ürün        | Fiyat (TL) |\n");
        sb.append("--------------------------------------------------\n");
        i = 1;
        for (Map.Entry<String, Double> entry : kafeOtomasyonu.anaYemekMenu.entrySet()) {
            String urun = entry.getKey();
            double fiyat = entry.getValue();
            sb.append(String.format("| %-3s | %-12s| %-13s|\n", i + kafeOtomasyonu.tatliMenu.size() + kafeOtomasyonu.icecekMenu.size(), urun, fiyat));
            i++;
        }
        sb.append("--------------------------------------------------\n");

        displayArea.setText(sb.toString());
    }

    private void masaDurumunuGoster() {
        StringBuilder sb = new StringBuilder();
        sb.append("Masalar:\n");
        for (int masaNo : kafeOtomasyonu.masalar.keySet()) {
            sb.append("Masa ").append(masaNo).append(": ").append(kafeOtomasyonu.masalar.get(masaNo).isEmpty() ? "Boş" : "Dolu").append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void siparisVer() {
        String masaNoStr = JOptionPane.showInputDialog("Hangi masa için sipariş vermek istiyorsunuz? (1-10)");
        int masaNo = Integer.parseInt(masaNoStr);

        do {
            String siparis = JOptionPane.showInputDialog("Sipariş vermek istediğiniz ürünü ve adedini girin (örn: Pasta 2):");
            String[] secilen = siparis.split(" ");
            if (secilen.length != 2) {
                JOptionPane.showMessageDialog(this, "Geçersiz giriş!", "Hata", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            try {
                String urun = secilen[0];
                int adet = Integer.parseInt(secilen[1]);

                if (kafeOtomasyonu.tatliMenu.containsKey(urun) || kafeOtomasyonu.icecekMenu.containsKey(urun) || kafeOtomasyonu.anaYemekMenu.containsKey(urun)) {
                    kafeOtomasyonu.siparisVer(masaNo, urun, adet);
                    JOptionPane.showMessageDialog(this, "Siparişiniz alındı: " + adet + " adet " + urun);
                } else {
                    JOptionPane.showMessageDialog(this, "Geçersiz ürün!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Geçersiz giriş!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } while (JOptionPane.showConfirmDialog(this, "Başka bir sipariş vermek istiyor musunuz?", "Sipariş", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
    }

    private void hesapAl() {
        String masaNoStr = JOptionPane.showInputDialog("Hangi masa için hesap almak istiyorsunuz? (1-10)");
        int masaNo = Integer.parseInt(masaNoStr);
        String hesapBilgileri = kafeOtomasyonu.hesapAl(masaNo);
        displayArea.setText(hesapBilgileri);
    }

    private void masaKapat() {
        String masaNoStr = JOptionPane.showInputDialog("Hangi masa için kapatmak istiyorsunuz? (1-10)");
        int masaNo = Integer.parseInt(masaNoStr);
        String hesapBilgileri = kafeOtomasyonu.hesapAl(masaNo);
        displayArea.setText(hesapBilgileri);
        kafeOtomasyonu.masaKapat(masaNo);  // Masayı kapat ve veritabanından siparişleri sil
    }

    private void logout() {
        int response = JOptionPane.showConfirmDialog(this, "Çıkış yapmak istediğinizden emin misiniz?", "Çıkış Yap", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            dispose(); // Ana ekranı kapat
            new LoginFrame(kafeOtomasyonu).setVisible(true); // Giriş ekranını aç
        }
    }
}
