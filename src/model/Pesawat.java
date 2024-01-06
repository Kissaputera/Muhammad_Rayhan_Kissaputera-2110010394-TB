package model;

public class Pesawat {
    private int idPesawat;
    private String kdPesawat;
    private String nama;
    private String asal;
    private String tujuan;
    private String waktu;
    private double harga;

    public Pesawat() {
    }

    public Pesawat(int idPesawat, String kdPesawat, String nama, String asal, String tujuan, String waktu, double harga) {
        this.idPesawat = idPesawat;
        this.kdPesawat = kdPesawat;
        this.nama = nama;
        this.asal = asal;
        this.tujuan = tujuan;
        this.waktu = waktu;
        this.harga = harga;
    }

    public int getIdPesawat() {
        return idPesawat;
    }

    public void setIdPesawat(int idPesawat) {
        this.idPesawat = idPesawat;
    }

    public String getKdPesawat() {
        return kdPesawat;
    }

    public void setKdPesawat(String kdPesawat) {
        this.kdPesawat = kdPesawat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAsal() {
        return asal;
    }

    public void setAsal(String asal) {
        this.asal = asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
