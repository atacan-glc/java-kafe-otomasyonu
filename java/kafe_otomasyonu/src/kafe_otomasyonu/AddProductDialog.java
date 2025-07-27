package kafe_otomasyonu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductDialog extends JDialog {
    private JTextField productNameField;
    private JTextField productPriceField;
    private JComboBox<String> categoryComboBox;
    private KafeOtomasyonu kafeOtomasyonu;

    public AddProductDialog(Frame owner, KafeOtomasyonu kafeOtomasyonu) {
        super(owner, "Ürün Ekle", true);
        this.kafeOtomasyonu = kafeOtomasyonu;
        setSize(300, 200);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Ürün Adı:"));
        productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("Fiyat:"));
        productPriceField = new JTextField();
        panel.add(productPriceField);

        panel.add(new JLabel("Kategori:"));
        categoryComboBox = new JComboBox<>(new String[]{"tatli", "icecek", "anaYemek"});
        panel.add(categoryComboBox);

        JButton addButton = new JButton("Ekle");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        panel.add(addButton);

        add(panel);
    }

    private void addProduct() {
        String productName = productNameField.getText();
        double productPrice = Double.parseDouble(productPriceField.getText());
        String category = (String) categoryComboBox.getSelectedItem();

        Admin admin = (Admin) kafeOtomasyonu.aktifKullanici;
        admin.urunEkle(productName, productPrice, category);

        JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!");
        dispose();
    }
}
