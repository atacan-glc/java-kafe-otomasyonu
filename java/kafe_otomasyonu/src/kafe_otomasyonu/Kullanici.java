package kafe_otomasyonu;

public abstract class Kullanici {
    protected String isim;
    protected String rol;

    public Kullanici(String isim, String rol) {
        this.isim = isim;
        this.rol = rol;
    }

    public void girisYap() {
        System.out.println(isim + " giriş yaptı.");
    }

    public abstract void yetki();
}
