package kafe_otomasyonu;

public class Musteri extends Kullanici {
    public Musteri(String isim, String rol) {
        super(isim, rol);
    }

    @Override
    public void yetki() {
        System.out.println("Müşteri yetkilerine sahip.");
    }
}
