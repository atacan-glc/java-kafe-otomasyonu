package kafe_otomasyonu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveProductDialog extends JDialog {
    private JTextField productNameField;
    private KafeOtomasyonu kafeOtomasyonu;

    public RemoveProductDialog(Frame owner, KafeOtomasyonu kafeOtomasyonu) {
        super(owner, "Ürün Sil", true);
        this.kafeOtomasyonu = kafeOtomasyonu;
        setSize(300, 150);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        panel.add(new JLabel("Ürün Adı:"));
        productNameField = new JTextField();
        panel.add(productNameField);

        JButton removeButton = new JButton("Sil");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProduct();
            }
        });
        panel.add(removeButton);

        add(panel);
    }

    private void removeProduct() {
        String productName = productNameField.getText();

        Admin admin = (Admin) kafeOtomasyonu.aktifKullanici;
        admin.urunSil(productName);

        JOptionPane.showMessageDialog(this, "Ürün başarıyla silindi!");
        dispose();
    }
}
