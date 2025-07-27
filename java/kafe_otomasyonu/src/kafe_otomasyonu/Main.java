package kafe_otomasyonu;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KafeOtomasyonu kafeOtomasyonu = new KafeOtomasyonu();
            new LoginFrame(kafeOtomasyonu).setVisible(true);
        });
    }
}
