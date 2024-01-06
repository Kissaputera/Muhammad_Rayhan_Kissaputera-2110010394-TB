package model;

public class Tiket {
    private int idTiket;
    private int idPenumpang2;
    private int idPesawat2;
    private String kelas;
    private double biaya;
    private String tanggal;
    private String namaPenumpang;
    private String namaPesawat;
    private String kdPesawat;
    private String asal;
    private String tujuan;
    private double harga;
    private String waktu;

    public Tiket() {
    }

    public Tiket(int idTiket, int idPenumpang2, int idPesawat2, String kelas, double biaya, String tanggal, String namaPenumpang, String namaPesawat, String kdPesawat, String asal, String tujuan, double harga, String waktu) {
        this.idTiket = idTiket;
        this.idPenumpang2 = idPenumpang2;
        this.idPesawat2 = idPesawat2;
        this.kelas = kelas;
        this.biaya = biaya;
        this.tanggal = tanggal;
        this.namaPenumpang = namaPenumpang;
        this.namaPesawat = namaPesawat;
        this.kdPesawat = kdPesawat;
        this.asal = asal;
        this.tujuan = tujuan;
        this.harga = harga;
        this.waktu = waktu;
    }

    public int getIdTiket() {
        return idTiket;
    }

    public void setIdTiket(int idTiket) {
        this.idTiket = idTiket;
    }

    public int getIdPenumpang2() {
        return idPenumpang2;
    }

    public void setIdPenumpang2(int idPenumpang2) {
        this.idPenumpang2 = idPenumpang2;
    }

    public int getIdPesawat2() {
        return idPesawat2;
    }

    public void setIdPesawat2(int idPesawat2) {
        this.idPesawat2 = idPesawat2;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaPenumpang() {
        return namaPenumpang;
    }

    public void setNamaPenumpang(String namaPenumpang) {
        this.namaPenumpang = namaPenumpang;
    }

    public String getNamaPesawat() {
        return namaPesawat;
    }

    public void setNamaPesawat(String namaPesawat) {
        this.namaPesawat = namaPesawat;
    }

    public String getKdPesawat() {
        return kdPesawat;
    }

    public void setKdPesawat(String kdPesawat) {
        this.kdPesawat = kdPesawat;
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

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    
}
