package net.catsbilisim.canliyayin.Api;

import java.util.List;

public class UploadMedya {

    private String kullaniciAdi;
    private String Sifre;
    private String Uid;
    private List<Medyas> medyalar;

    public UploadMedya setMedyalar(List<Medyas> medyalar) {
        this.medyalar = medyalar;
        return this;
    }

    public UploadMedya setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
        return this;
    }

    public UploadMedya setSifre(String sifre) {
        Sifre = sifre;
        return this;
    }

    public UploadMedya setUid(String uid) {
        Uid = uid;
        return this;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public String getSifre() {
        return Sifre;
    }

    public String getUid() {
        return Uid;
    }

    public List<Medyas> getMedyalar() {
        return medyalar;
    }

    public static class Medyas{

        private String YayinName;
        private String Link;
        private String Type;

        public Medyas setYayinName(String yayinName) {
            YayinName = yayinName;
            return this;
        }

        public Medyas setLink(String link) {
            Link = link;
            return this;
        }

        public Medyas setType(String type) {
            Type = type;
            return this;
        }

        public String getYayinName() {
            return YayinName;
        }

        public String getLink() {
            return Link;
        }

        public String getType() {
            return Type;
        }
    }
}