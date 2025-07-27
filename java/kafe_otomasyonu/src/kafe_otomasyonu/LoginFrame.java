package kafe_otomasyonu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private KafeOtomasyonu kafeOtomasyonu;

    public LoginFrame(KafeOtomasyonu kafeOtomasyonu) {
        this.kafeOtomasyonu = kafeOtomasyonu;
        setTitle("Kafe Otomasyonu - Giriş");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Kullanıcı Adı:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Şifre:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(new LoginActionListener());
        panel.add(loginButton);

        registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(new RegisterActionListener());
        panel.add(registerButton);

        add(panel);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (kafeOtomasyonu.giris(username, password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Giriş başarılı!");
                dispose();
                // Ana ekranı aç
                new MainFrame(kafeOtomasyonu).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Giriş başarısız!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            kafeOtomasyonu.kayit(username, password, "musteri");
            JOptionPane.showMessageDialog(LoginFrame.this, "Kayıt başarılı!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KafeOtomasyonu kafeOtomasyonu = new KafeOtomasyonu();
            new LoginFrame(kafeOtomasyonu).setVisible(true);
        });
    }
}
