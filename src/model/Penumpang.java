package model;
import java.sql.Blob;

public class Penumpang {
    private int idPenumpang;
    private String nama;
    private String passport;
    private String jenkel;
    private String telepon;
    private Blob foto;

    public Penumpang() {
    }

    public Penumpang(int idPenumpang, String nama, String passport, String jenkel, String telepon, Blob foto) {
        this.idPenumpang = idPenumpang;
        this.nama = nama;
        this.passport = passport;
        this.jenkel = jenkel;
        this.telepon = telepon;
        this.foto = foto;
    }

    public int getIdPenumpang() {
        return idPenumpang;
    }

    public void setIdPenumpang(int idPenumpang) {
        this.idPenumpang = idPenumpang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getJenkel() {
        return jenkel;
    }

    public void setJenkel(String jenkel) {
        this.jenkel = jenkel;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }
    
}

