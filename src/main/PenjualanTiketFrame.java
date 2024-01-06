/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main;

import com.mysql.cj.protocol.Resultset;
import com.raven.event.EventTimePicker;
import db.Koneksi;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Penumpang;
import model.Pesawat;
import model.Tiket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import model.User;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author User
 */
public class PenjualanTiketFrame extends javax.swing.JFrame {
    
    Penumpang penumpang;
    Pesawat pesawat;
    Tiket tiket;
    User user;
    BufferedImage bImage;
    public Statement st;
    
    
    int status = 0;
    private final int TAMBAH = 0;
    private final int UBAH = 1;
    private final int IMG_WIDTH = 91;
    private final int IMG_HEIGHT = 113;
    

    /**
     * Creates new form PenjualanTiketFrame
     */
    public PenjualanTiketFrame(String nama) {
        initComponents();
        panel();
        pnlHome.setVisible(true);
        jspColorBg();
        tbhFont();
        lblWelcome.setText("Selamat Datang!"+" "+nama);
        refreshData("");
        timePicker1.addEventTimePicker(new EventTimePicker(){
            @Override
            public void timeSelected(String string){
                System.out.println(string);
            }
        });
        timePicker1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Time Selected "+timePicker1.getSelectedTime());
            }
        });
        
    }
    
    public void jspColorBg(){
       jScrollPane1.getViewport().setBackground(Color.white);
       jScrollPane2.getViewport().setBackground(Color.white);
       jScrollPane3.getViewport().setBackground(Color.white);
       jScrollPane4.getViewport().setBackground(Color.white);
    }
    
    public void tbhFont(){
        tbPenumpang.getTableHeader().setFont(new Font ("Dialog", Font.BOLD, 12));
        tbPesawat.getTableHeader().setFont(new Font ("Dialog", Font.BOLD, 12));
        tbTiket.getTableHeader().setFont(new Font ("Dialog", Font.BOLD, 12));
        tbUser.getTableHeader().setFont(new Font ("Dialog", Font.BOLD, 12));
    }
    
    public void reset(){
        //reset input data pesawat
        tfIdPesawat.setText("");       
        tfNamaPesawat.setText("");
        tfAsal.setText("");
        tfTujuan.setText("");
        tfHarga.setText("");
        tfWaktu.setText("");
        lblInputPesawat.setText("Input Data Pesawat");
        //reset input data penumpang
        tfIdPenumpang.setText("");
        tfNamaPenumpang.setText("");
        tfPassport.setText("");
        buttonGroup1.clearSelection();
        tfTelepon.setText("");
        lblFoto.setIcon(null);
        lblInputPenumpang.setText("Input Data Penumpang");
        //reset input data tiket
        lblInputTiket.setText("Input Data Transaksi Tiket");
        cbbPesawat.setSelectedIndex(0);
        cbbPenumpang.setSelectedIndex(0);
        cbbKelas.setSelectedIndex(0);
        tfTiketNamaPesawat.setText("");
        tfTiketAsal.setText("");
        tfTiketTujuan.setText("");
        tfTiketHarga.setText("");
        tfTiketWaktu.setText("");
        tfHarga.setText("");
        tfBiaya.setText("");
        //reset input data user
        tfNamaUser.setText("");
        tfUsername.setText("");
        tfPassword.setText("");
    }
        
//    public void generate(){
//        Random random = new Random();
//        int randomValue = random.nextInt(1000);
//        DateTimeFormatter kd = DateTimeFormatter.ofPattern("mmss");
//        LocalDateTime now = LocalDateTime.now();
//        String generatedCode = "PSWT" + randomValue + kd.format(now);
//        jLabel2.setText(generatedCode); 
//    }
    
    public void panel(){
        pnlHome.setVisible(false);
        pnlPenumpang.setVisible(false);
        pnlPesawat.setVisible(false);
        pnlTiket.setVisible(false);
        pnlUser.setVisible(false);
        pnlTambahPesawat.setVisible(false);
        pnlTambahPenumpang.setVisible(false);
        pnlTambahTiket.setVisible(false);
    }
    
     public void rbJenisKelaminSetSelected(String jenisKelamin) {
       if(jenisKelamin.equals("Laki-laki")){
           rbL.setSelected(true);
       } else if(jenisKelamin.equals("Perempuan")){
          rbP.setSelected(true);
       }          
    }
    
    public String rbJenisKelaminGetSelected() {
        if(rbL.isSelected())
            return "Laki-laki";
        else if(rbP.isSelected())
            return "Perempuan";
        else
            return "";
    }
    
    public BufferedImage getBufferedImage(Blob imageBlob) {
        InputStream binaryStream = null;
        BufferedImage b = null;
        try {
            binaryStream = imageBlob.getBinaryStream();
            b = ImageIO.read(binaryStream);
        } catch (SQLException | IOException ex) {
            System.err.println("Error getBufferedImage : "+ex);
        }
        return b;
    }
    
    public Blob getBlobImage(BufferedImage bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Blob blFile = null;
        try {
            ImageIO.write(bi, "png", baos);
            blFile = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
        } catch (SQLException | IOException ex) {
            Logger.getLogger(pnlTambahPenumpang.getName()).log(Level.SEVERE, null, ex);
        }
        return blFile;
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }
    
    public ArrayList<Pesawat> getPesawatList(String keyword){
    ArrayList<Pesawat> pesawatList = new ArrayList<Pesawat>();
    Koneksi koneksi = new Koneksi();
    Connection connection = koneksi.getConnection();
    
    PreparedStatement ps;
    ResultSet rs;
    String query = "SELECT * FROM pesawat ";
    String order = " ORDER BY id_pesawat";
    
    if(!keyword.equals(""))
        query = query+ "WHERE nm_pesawat like ? OR asal like ? OR tujuan like ? ";
       query = query+order;
       
    try {
           ps = connection.prepareStatement(query);
            if(!keyword.equals("")){
              ps.setString(1, "%"+tfCariPesawat.getText()+"%");
              ps.setString(2, "%"+tfCariPesawat.getText()+"%");
              ps.setString(3, "%"+tfCariPesawat.getText()+"%");
            }
           rs = ps.executeQuery();
            while(rs.next()) {
                pesawat = new Pesawat(
                rs.getInt("id_pesawat"),
                rs.getString("kd_pesawat"),        
                rs.getString("nm_pesawat"),
                rs.getString("asal"),
                rs.getString("tujuan"),
                rs.getString("waktu"),
                rs.getDouble("harga"));
                pesawatList.add(pesawat);
            }
        } catch (SQLException ex) {
            System.err.println("ERROR getPesawatList : "+ex);
        }
        return pesawatList;
   }

public void selectPesawat(String keyword){
       ArrayList<Pesawat> list = getPesawatList(keyword);
       DefaultTableModel model = (DefaultTableModel)tbPesawat.getModel();
       Object[] row = new Object[7];
   
       
       for (int i = 0; i < list.size(); i++) {
           row[0] = list.get(i).getIdPesawat();
           row[1] = list.get(i).getNama();
           row[2] = list.get(i).getWaktu();
           row[3] = list.get(i).getAsal();
           row[4] = list.get(i).getTujuan();
           row[5] = list.get(i).getHarga();
           row[6] = list.get(i).getKdPesawat();
           model.addRow(row);
       }
   }

public ArrayList<Penumpang> getPenumpangList(String keyword){
    ArrayList<Penumpang> penumpangList = new ArrayList<Penumpang>();
    Koneksi koneksi = new Koneksi();
    Connection connection = koneksi.getConnection();
    
    PreparedStatement ps;
    ResultSet rs;
    String query = "SELECT * FROM penumpang ";
    String order = " ORDER BY id";
    
    if(!keyword.equals(""))
        query = query+ "WHERE nm_penumpang like ? OR passport like ? OR jenkel like ? ";
       query = query+order;
       
    try {
           ps = connection.prepareStatement(query);
            if(!keyword.equals("")){
              ps.setString(1, "%"+tfCariPenumpang.getText()+"%");
              ps.setString(2, "%"+tfCariPenumpang.getText()+"%");
              ps.setString(3, "%"+tfCariPenumpang.getText()+"%");
            }
           rs = ps.executeQuery();
            while(rs.next()) {
                penumpang = new Penumpang(
                rs.getInt("id"),
                rs.getString("nm_penumpang"),
                rs.getString("passport"),
                rs.getString("jenkel"),
                rs.getString("telepon"),
                rs.getBlob("foto"));
                penumpangList.add(penumpang);
            }
        } catch (SQLException ex) {
            System.err.println("ERROR getPenumpangList : "+ex);
        }
        return penumpangList;
   }

public void selectPenumpang(String keyword){
       ArrayList<Penumpang> list = getPenumpangList(keyword);
       DefaultTableModel modelp = (DefaultTableModel)tbPenumpang.getModel();
       Object[] row = new Object[6];
   
       
       for (int i = 0; i < list.size(); i++) {
           row[0] = list.get(i).getIdPenumpang();
           row[1] = list.get(i).getNama();
           row[2] = list.get(i).getPassport();
           row[3] = list.get(i).getJenkel();
           row[4] = list.get(i).getTelepon();
           row[5] = list.get(i).getFoto();

           modelp.addRow(row);
       }
   }

public ArrayList<Tiket> getTiketList(String keyword){
    ArrayList<Tiket> tiketList = new ArrayList<Tiket>();
    Koneksi koneksi = new Koneksi();
    Connection connection = koneksi.getConnection();
    
    PreparedStatement ps;
    ResultSet rs;
    String query = "SELECT tiket.*, penumpang.nm_penumpang, pesawat.nm_pesawat, " 
            + "pesawat.kd_pesawat, pesawat.asal, pesawat.tujuan, pesawat.harga, pesawat.waktu " 
            + "FROM tiket INNER JOIN penumpang ON tiket.id = penumpang.id "
            + "INNER JOIN pesawat ON tiket.id_pesawat = pesawat.id_pesawat ";        
    String order = " ORDER BY tiket.id_tiket";
    if(!keyword.equals(""))
        query = query+ "WHERE tiket.id_tiket like ? OR nm_pesawat like ? OR nm_penumpang like ?";
    
    query = query+order;
    try {
           ps = connection.prepareStatement(query);
            if(!keyword.equals("")){
              ps.setString(1, "%"+tfCariTiket.getText()+"%");
              ps.setString(2, "%"+tfCariTiket.getText()+"%");
              ps.setString(3, "%"+tfCariTiket.getText()+"%");
            }
           rs = ps.executeQuery();
            while(rs.next()) {
                tiket = new Tiket(
                rs.getInt("id_tiket"),
                rs.getInt("id"),
                rs.getInt("id_pesawat"),
                rs.getString("kelas"),
                rs.getDouble("biaya"),
                rs.getString("tanggal"),
                rs.getString("penumpang.nm_penumpang"),
                rs.getString("pesawat.nm_pesawat"),
                rs.getString("pesawat.kd_pesawat"),
                rs.getString("pesawat.asal"),
                rs.getString("pesawat.tujuan"),
                rs.getDouble("pesawat.harga"),    
                rs.getString("pesawat.waktu"));        
                tiketList.add(tiket);
            }
        } catch (SQLException ex) {
            System.err.println("ERROR getTiketList : "+ex);
        }
        return tiketList;
   }

public void selectTiket(String keyword){
       ArrayList<Tiket> list = getTiketList(keyword);
       DefaultTableModel modelt = (DefaultTableModel)tbTiket.getModel();
       Object[] row = new Object[13];
   
       
       for (int i = 0; i < list.size(); i++) {
           row[0] = list.get(i).getIdTiket();
           row[1] = list.get(i).getTanggal();
           row[2] = list.get(i).getNamaPesawat();
           row[3] = list.get(i).getKdPesawat();
           row[4] = list.get(i).getNamaPenumpang();
           row[5] = list.get(i).getKelas();
           row[6] = list.get(i).getAsal();
           row[7] = list.get(i).getTujuan();
           row[8] = list.get(i).getWaktu();
           row[9] = list.get(i).getBiaya();
           row[10] = list.get(i).getIdPesawat2();
           row[11] = list.get(i).getIdPenumpang2();
           row[12] = list.get(i).getHarga();

           modelt.addRow(row);
       }
   }

public ArrayList<User> getUserList(String keyword){
    ArrayList<User> userList = new ArrayList<User>();
    Koneksi koneksi = new Koneksi();
    Connection connection = koneksi.getConnection();
    
    PreparedStatement ps;
    ResultSet rs;
    String query = "SELECT * FROM user ";
    String order = " ORDER BY id_user";
    
    if(!keyword.equals(""))
        query = query+ "WHERE nama like ? OR username like ? OR pass like ? ";
       query = query+order;
       
    try {
           ps = connection.prepareStatement(query);
            if(!keyword.equals("")){
              ps.setString(1, "%"+tfCariUser.getText()+"%");
              ps.setString(2, "%"+tfCariUser.getText()+"%");
              ps.setString(3, "%"+tfCariUser.getText()+"%");
            }
           rs = ps.executeQuery();
            while(rs.next()) {
                user = new User(
                rs.getInt("id_user"),
                rs.getString("nama"),
                rs.getString("username"),
                rs.getString("pass"));
                userList.add(user);
            }
        } catch (SQLException ex) {
            System.err.println("ERROR getUserList : "+ex);
        }
        return userList;
   }

public void selectUser(String keyword){
       ArrayList<User> list = getUserList(keyword);
       DefaultTableModel modelu = (DefaultTableModel)tbUser.getModel();
       Object[] row = new Object[4];
   
       
       for (int i = 0; i < list.size(); i++) {          
           row[0] = list.get(i).getNamaUser();
           row[1] = list.get(i).getUsername();
           row[2] = list.get(i).getPassword();
           row[3] = list.get(i).getIdUser();
          
           modelu.addRow(row);
       }
   }

public final void refreshData(String keyword){
        DefaultTableModel model = (DefaultTableModel)tbPesawat.getModel();
        DefaultTableModel modelp = (DefaultTableModel)tbPenumpang.getModel();
        DefaultTableModel modelt = (DefaultTableModel)tbTiket.getModel();
        DefaultTableModel modelu = (DefaultTableModel)tbUser.getModel();
        model.setRowCount(0);
        modelp.setRowCount(0);
        modelt.setRowCount(0);
        modelu.setRowCount(0);
        selectPesawat(keyword);
        selectPenumpang(keyword);
        selectTiket(keyword);
        selectUser(keyword);
    }   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timePicker1 = new com.raven.swing.TimePicker();
        buttonGroup1 = new javax.swing.ButtonGroup();
        fChooser = new javax.swing.JFileChooser();
        tfIdPesawat = new javax.swing.JTextField();
        tfIdTiket = new javax.swing.JTextField();
        tfTiketIdPesawat = new javax.swing.JTextField();
        tfTiketIdPenumpang = new javax.swing.JTextField();
        tfTiketHarga = new javax.swing.JTextField();
        tfIdPenumpang = new javax.swing.JTextField();
        dateChooser1 = new com.raven.datechooser.DateChooser();
        tfIdUser = new javax.swing.JTextField();
        pnlBg = new javax.swing.JPanel();
        pnlNav = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblNavTiket = new javax.swing.JLabel();
        lblNavHome = new javax.swing.JLabel();
        lblNavPenumpang = new javax.swing.JLabel();
        lblNavPesawat = new javax.swing.JLabel();
        lblNavUser = new javax.swing.JLabel();
        pnlPenumpang = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPenumpang = new javax.swing.JTable();
        tfCariPenumpang = new javax.swing.JTextField();
        btnCariPenumpang = new javax.swing.JButton();
        btnTambahPenumpang = new javax.swing.JButton();
        btnUbahPenumpang = new javax.swing.JButton();
        btnHapusPenumpang = new javax.swing.JButton();
        btnCetakPenumpang = new javax.swing.JButton();
        pnlPesawat = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnCariPesawat = new javax.swing.JButton();
        tfCariPesawat = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbPesawat = new javax.swing.JTable();
        btnTambahPesawat = new javax.swing.JButton();
        btnUbahPesawat = new javax.swing.JButton();
        btnHapusPesawat = new javax.swing.JButton();
        btnCetakPesawat = new javax.swing.JButton();
        pnlTiket = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbTiket = new javax.swing.JTable();
        tfCariTiket = new javax.swing.JTextField();
        btnCariTiket = new javax.swing.JButton();
        btnHapusTiket = new javax.swing.JButton();
        btnTambahTiket = new javax.swing.JButton();
        btnUbahTiket = new javax.swing.JButton();
        btnCetakTiket = new javax.swing.JButton();
        pnlTambahPesawat = new javax.swing.JPanel();
        lblInputPesawat = new javax.swing.JLabel();
        tfNamaPesawat = new javax.swing.JTextField();
        tfAsal = new javax.swing.JTextField();
        tfTujuan = new javax.swing.JTextField();
        tfWaktu = new javax.swing.JTextField();
        btnSimpanPesawat = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnPilihWaktu = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tfHarga = new javax.swing.JTextField();
        btnBatalPesawat = new javax.swing.JButton();
        pnlTambahPenumpang = new javax.swing.JPanel();
        lblInputPenumpang = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        tfNamaPenumpang = new javax.swing.JTextField();
        tfPassport = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        rbL = new javax.swing.JRadioButton();
        rbP = new javax.swing.JRadioButton();
        jLabel20 = new javax.swing.JLabel();
        tfTelepon = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        btnPilihFoto = new javax.swing.JButton();
        btnSimpanPenumpang = new javax.swing.JButton();
        btnBatalPenumpang = new javax.swing.JButton();
        pnlHome = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        pnlTambahTiket = new javax.swing.JPanel();
        lblInputTiket = new javax.swing.JLabel();
        cbbPesawat = new javax.swing.JComboBox<>();
        tfBiaya = new javax.swing.JTextField();
        cbbPenumpang = new javax.swing.JComboBox<>();
        tfTanggal = new javax.swing.JTextField();
        btnSimpanTiket = new javax.swing.JButton();
        tfTiketNamaPesawat = new javax.swing.JTextField();
        tfTiketAsal = new javax.swing.JTextField();
        tfTiketTujuan = new javax.swing.JTextField();
        tfTiketWaktu = new javax.swing.JTextField();
        cbbKelas = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btnPilihTanggal = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnBatalTiket = new javax.swing.JButton();
        pnlUser = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        btnSimpanUser = new javax.swing.JButton();
        tfNamaUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        tfPassword = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        btnHapusUser = new javax.swing.JButton();
        btnUbahUser = new javax.swing.JButton();
        tfCariUser = new javax.swing.JTextField();
        btnCariUser = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbUser = new javax.swing.JTable();

        timePicker1.setBackground(new java.awt.Color(255, 255, 153));
        timePicker1.setForeground(new java.awt.Color(0, 0, 0));
        timePicker1.setDisplayText(tfWaktu);

        tfIdPesawat.setEditable(false);
        tfIdPesawat.setBackground(new java.awt.Color(255, 255, 255));

        tfIdPenumpang.setEditable(false);
        tfIdPenumpang.setBackground(new java.awt.Color(255, 255, 255));

        dateChooser1.setForeground(new java.awt.Color(79, 196, 234));
        dateChooser1.setDateFormat("yyyy-MM-dd");
        dateChooser1.setTextRefernce(tfTanggal);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        pnlBg.setBackground(new java.awt.Color(153, 255, 255));
        pnlBg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlNav.setBackground(new java.awt.Color(44, 33, 163));
        pnlNav.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/airplane_65px.png"))); // NOI18N
        pnlNav.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 70, 70));

        lblNavTiket.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lblNavTiket.setForeground(new java.awt.Color(255, 255, 255));
        lblNavTiket.setText("Tiket");
        lblNavTiket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNavTiketMouseClicked(evt);
            }
        });
        pnlNav.add(lblNavTiket, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, -1, -1));

        lblNavHome.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lblNavHome.setForeground(new java.awt.Color(255, 255, 255));
        lblNavHome.setText("Home");
        lblNavHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNavHomeMouseClicked(evt);
            }
        });
        pnlNav.add(lblNavHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        lblNavPenumpang.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lblNavPenumpang.setForeground(new java.awt.Color(255, 255, 255));
        lblNavPenumpang.setText("Penumpang");
        lblNavPenumpang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNavPenumpangMouseClicked(evt);
            }
        });
        pnlNav.add(lblNavPenumpang, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, -1));

        lblNavPesawat.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lblNavPesawat.setForeground(new java.awt.Color(255, 255, 255));
        lblNavPesawat.setText("Pesawat");
        lblNavPesawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNavPesawatMouseClicked(evt);
            }
        });
        pnlNav.add(lblNavPesawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        lblNavUser.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lblNavUser.setForeground(new java.awt.Color(255, 255, 255));
        lblNavUser.setText("User");
        lblNavUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNavUserMouseClicked(evt);
            }
        });
        pnlNav.add(lblNavUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, -1, -1));

        pnlBg.add(pnlNav, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 70));

        pnlPenumpang.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel7.setText("Informasi Data Penumpang");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbPenumpang.setAutoCreateRowSorter(true);
        tbPenumpang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbPenumpang.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        tbPenumpang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Penumpang", "Passport", "Jenis Kelamin", "Telepon", "Foto"
            }
        ));
        tbPenumpang.setGridColor(new java.awt.Color(255, 255, 255));
        tbPenumpang.setRowHeight(26);
        jScrollPane1.setViewportView(tbPenumpang);
        if (tbPenumpang.getColumnModel().getColumnCount() > 0) {
            tbPenumpang.getColumnModel().getColumn(0).setMaxWidth(35);
        }

        tfCariPenumpang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfCariPenumpangKeyTyped(evt);
            }
        });

        btnCariPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N
        btnCariPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariPenumpangActionPerformed(evt);
            }
        });

        btnTambahPenumpang.setBackground(new java.awt.Color(204, 255, 102));
        btnTambahPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plus.png"))); // NOI18N
        btnTambahPenumpang.setText("Tambah");
        btnTambahPenumpang.setBorderPainted(false);
        btnTambahPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPenumpangActionPerformed(evt);
            }
        });

        btnUbahPenumpang.setBackground(new java.awt.Color(0, 255, 255));
        btnUbahPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUbahPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit.png"))); // NOI18N
        btnUbahPenumpang.setText("Ubah");
        btnUbahPenumpang.setBorderPainted(false);
        btnUbahPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahPenumpangActionPerformed(evt);
            }
        });

        btnHapusPenumpang.setBackground(new java.awt.Color(255, 102, 102));
        btnHapusPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapusPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete.png"))); // NOI18N
        btnHapusPenumpang.setText("Hapus");
        btnHapusPenumpang.setBorderPainted(false);
        btnHapusPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPenumpangActionPerformed(evt);
            }
        });

        btnCetakPenumpang.setBackground(new java.awt.Color(204, 204, 204));
        btnCetakPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCetakPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/printing.png"))); // NOI18N
        btnCetakPenumpang.setText("Cetak");
        btnCetakPenumpang.setBorderPainted(false);
        btnCetakPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPenumpangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPenumpangLayout = new javax.swing.GroupLayout(pnlPenumpang);
        pnlPenumpang.setLayout(pnlPenumpangLayout);
        pnlPenumpangLayout.setHorizontalGroup(
            pnlPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPenumpangLayout.createSequentialGroup()
                .addGroup(pnlPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPenumpangLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(pnlPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnHapusPenumpang)
                            .addGroup(pnlPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(pnlPenumpangLayout.createSequentialGroup()
                                    .addComponent(btnTambahPenumpang)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUbahPenumpang)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnCetakPenumpang)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCariPenumpang)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfCariPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlPenumpangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pnlPenumpangLayout.setVerticalGroup(
            pnlPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPenumpangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(26, 26, 26)
                .addGroup(pnlPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfCariPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariPenumpang)
                    .addComponent(btnTambahPenumpang)
                    .addComponent(btnUbahPenumpang)
                    .addComponent(btnCetakPenumpang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusPenumpang)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        pnlBg.add(pnlPenumpang, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlPesawat.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel8.setText("Informasi Data Pesawat");

        btnCariPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N
        btnCariPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariPesawatActionPerformed(evt);
            }
        });

        tfCariPesawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfCariPesawatKeyTyped(evt);
            }
        });

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbPesawat.setAutoCreateRowSorter(true);
        tbPesawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbPesawat.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        tbPesawat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Maskapai", "Waktu Penerbangan", "Asal", "Tujuan", "Harga", "Kd"
            }
        ));
        tbPesawat.setGridColor(new java.awt.Color(255, 255, 255));
        tbPesawat.setRowHeight(25);
        jScrollPane2.setViewportView(tbPesawat);
        if (tbPesawat.getColumnModel().getColumnCount() > 0) {
            tbPesawat.getColumnModel().getColumn(0).setMaxWidth(30);
            tbPesawat.getColumnModel().getColumn(6).setMinWidth(0);
            tbPesawat.getColumnModel().getColumn(6).setPreferredWidth(0);
            tbPesawat.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        btnTambahPesawat.setBackground(new java.awt.Color(204, 255, 102));
        btnTambahPesawat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plus.png"))); // NOI18N
        btnTambahPesawat.setText("Tambah");
        btnTambahPesawat.setBorderPainted(false);
        btnTambahPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPesawatActionPerformed(evt);
            }
        });

        btnUbahPesawat.setBackground(new java.awt.Color(0, 255, 255));
        btnUbahPesawat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUbahPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit.png"))); // NOI18N
        btnUbahPesawat.setText("Ubah");
        btnUbahPesawat.setBorderPainted(false);
        btnUbahPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahPesawatActionPerformed(evt);
            }
        });

        btnHapusPesawat.setBackground(new java.awt.Color(255, 102, 102));
        btnHapusPesawat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapusPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete.png"))); // NOI18N
        btnHapusPesawat.setText("Hapus");
        btnHapusPesawat.setBorderPainted(false);
        btnHapusPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPesawatActionPerformed(evt);
            }
        });

        btnCetakPesawat.setBackground(new java.awt.Color(204, 204, 204));
        btnCetakPesawat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCetakPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/printing.png"))); // NOI18N
        btnCetakPesawat.setText("Cetak");
        btnCetakPesawat.setBorderPainted(false);
        btnCetakPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPesawatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPesawatLayout = new javax.swing.GroupLayout(pnlPesawat);
        pnlPesawat.setLayout(pnlPesawatLayout);
        pnlPesawatLayout.setHorizontalGroup(
            pnlPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPesawatLayout.createSequentialGroup()
                .addGroup(pnlPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPesawatLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(pnlPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(pnlPesawatLayout.createSequentialGroup()
                                    .addComponent(btnTambahPesawat)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUbahPesawat)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnCetakPesawat)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCariPesawat)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(tfCariPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnHapusPesawat)))
                    .addGroup(pnlPesawatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)))
                .addGap(22, 22, 22))
        );
        pnlPesawatLayout.setVerticalGroup(
            pnlPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPesawatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(26, 26, 26)
                .addGroup(pnlPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfCariPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariPesawat)
                    .addComponent(btnTambahPesawat)
                    .addComponent(btnUbahPesawat)
                    .addComponent(btnCetakPesawat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHapusPesawat)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        pnlBg.add(pnlPesawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlTiket.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setText("Informasi Data Tiket");

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbTiket.setAutoCreateRowSorter(true);
        tbTiket.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbTiket.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        tbTiket.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Maskapai", "Kode Pesawat", "Nama Penumpang", "Kelas", "Asal", "Tujuan", "Waktu", "Biaya", "Id Pesawat", "Id Penumpang", "Harga"
            }
        ));
        tbTiket.setGridColor(new java.awt.Color(255, 255, 255));
        tbTiket.setRowHeight(26);
        tbTiket.setShowGrid(true);
        jScrollPane3.setViewportView(tbTiket);
        if (tbTiket.getColumnModel().getColumnCount() > 0) {
            tbTiket.getColumnModel().getColumn(0).setMaxWidth(30);
            tbTiket.getColumnModel().getColumn(1).setMaxWidth(70);
            tbTiket.getColumnModel().getColumn(2).setMaxWidth(70);
            tbTiket.getColumnModel().getColumn(3).setMinWidth(0);
            tbTiket.getColumnModel().getColumn(3).setPreferredWidth(0);
            tbTiket.getColumnModel().getColumn(3).setMaxWidth(0);
            tbTiket.getColumnModel().getColumn(5).setMaxWidth(60);
            tbTiket.getColumnModel().getColumn(6).setMaxWidth(70);
            tbTiket.getColumnModel().getColumn(7).setMaxWidth(70);
            tbTiket.getColumnModel().getColumn(8).setMaxWidth(60);
            tbTiket.getColumnModel().getColumn(10).setMinWidth(0);
            tbTiket.getColumnModel().getColumn(10).setPreferredWidth(0);
            tbTiket.getColumnModel().getColumn(10).setMaxWidth(0);
            tbTiket.getColumnModel().getColumn(11).setMinWidth(0);
            tbTiket.getColumnModel().getColumn(11).setPreferredWidth(0);
            tbTiket.getColumnModel().getColumn(11).setMaxWidth(0);
            tbTiket.getColumnModel().getColumn(12).setMinWidth(0);
            tbTiket.getColumnModel().getColumn(12).setPreferredWidth(0);
            tbTiket.getColumnModel().getColumn(12).setMaxWidth(0);
        }

        tfCariTiket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfCariTiketKeyTyped(evt);
            }
        });

        btnCariTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N
        btnCariTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariTiketActionPerformed(evt);
            }
        });

        btnHapusTiket.setBackground(new java.awt.Color(255, 102, 102));
        btnHapusTiket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapusTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete.png"))); // NOI18N
        btnHapusTiket.setText("Hapus");
        btnHapusTiket.setBorderPainted(false);
        btnHapusTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusTiketActionPerformed(evt);
            }
        });

        btnTambahTiket.setBackground(new java.awt.Color(204, 255, 102));
        btnTambahTiket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plus.png"))); // NOI18N
        btnTambahTiket.setText("Tambah");
        btnTambahTiket.setBorderPainted(false);
        btnTambahTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahTiketActionPerformed(evt);
            }
        });

        btnUbahTiket.setBackground(new java.awt.Color(0, 255, 255));
        btnUbahTiket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUbahTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit.png"))); // NOI18N
        btnUbahTiket.setText("Ubah");
        btnUbahTiket.setBorderPainted(false);
        btnUbahTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahTiketActionPerformed(evt);
            }
        });

        btnCetakTiket.setBackground(new java.awt.Color(204, 204, 204));
        btnCetakTiket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCetakTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/printing.png"))); // NOI18N
        btnCetakTiket.setText("Cetak");
        btnCetakTiket.setBorderPainted(false);
        btnCetakTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTiketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTiketLayout = new javax.swing.GroupLayout(pnlTiket);
        pnlTiket.setLayout(pnlTiketLayout);
        pnlTiketLayout.setHorizontalGroup(
            pnlTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTiketLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                    .addGroup(pnlTiketLayout.createSequentialGroup()
                        .addGroup(pnlTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTiketLayout.createSequentialGroup()
                                .addComponent(btnTambahTiket)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUbahTiket)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCetakTiket)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCariTiket))
                            .addComponent(btnHapusTiket)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCariTiket, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlTiketLayout.setVerticalGroup(
            pnlTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTiketLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(pnlTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahTiket)
                    .addComponent(btnUbahTiket)
                    .addComponent(btnCariTiket)
                    .addComponent(tfCariTiket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCetakTiket))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusTiket)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        pnlBg.add(pnlTiket, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlTambahPesawat.setBackground(new java.awt.Color(255, 255, 255));

        lblInputPesawat.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblInputPesawat.setText("Input Data Pesawat");

        btnSimpanPesawat.setBackground(new java.awt.Color(255, 204, 51));
        btnSimpanPesawat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/diskette.png"))); // NOI18N
        btnSimpanPesawat.setText("Simpan");
        btnSimpanPesawat.setBorderPainted(false);
        btnSimpanPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanPesawatActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Maskapai");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("Asal");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setText("Tujuan");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setText("Waktu");

        btnPilihWaktu.setText("Pilih");
        btnPilihWaktu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihWaktuActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Harga");

        tfHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfHargaKeyTyped(evt);
            }
        });

        btnBatalPesawat.setBackground(new java.awt.Color(255, 102, 102));
        btnBatalPesawat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBatalPesawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/back.png"))); // NOI18N
        btnBatalPesawat.setText("Batal");
        btnBatalPesawat.setBorderPainted(false);
        btnBatalPesawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalPesawatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTambahPesawatLayout = new javax.swing.GroupLayout(pnlTambahPesawat);
        pnlTambahPesawat.setLayout(pnlTambahPesawatLayout);
        pnlTambahPesawatLayout.setHorizontalGroup(
            pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTambahPesawatLayout.createSequentialGroup()
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTambahPesawatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInputPesawat))
                    .addGroup(pnlTambahPesawatLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(pnlTambahPesawatLayout.createSequentialGroup()
                                    .addComponent(btnSimpanPesawat)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnBatalPesawat))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlTambahPesawatLayout.createSequentialGroup()
                                    .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel10))
                                    .addGap(90, 90, 90)
                                    .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnlTambahPesawatLayout.createSequentialGroup()
                                            .addComponent(tfWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnPilihWaktu))
                                        .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfAsal)
                                        .addComponent(tfNamaPesawat)
                                        .addComponent(tfTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        pnlTambahPesawatLayout.setVerticalGroup(
            pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTambahPesawatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInputPesawat)
                .addGap(18, 18, 18)
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNamaPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfAsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tfWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihWaktu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(36, 36, 36)
                .addGroup(pnlTambahPesawatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanPesawat)
                    .addComponent(btnBatalPesawat))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        pnlBg.add(pnlTambahPesawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlTambahPenumpang.setBackground(new java.awt.Color(255, 255, 255));

        lblInputPenumpang.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblInputPenumpang.setText("Input Data Penumpang");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setText("Nama Penumpang");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setText("Passport");

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setText("Jenis Kelamin");

        rbL.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbL);
        rbL.setText("Laki-laki");

        rbP.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbP);
        rbP.setText("Perempuan");

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel20.setText("Telepon");

        jLabel21.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel21.setText("Foto");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );

        btnPilihFoto.setText("Pilih");
        btnPilihFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihFotoActionPerformed(evt);
            }
        });

        btnSimpanPenumpang.setBackground(new java.awt.Color(255, 204, 51));
        btnSimpanPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/diskette.png"))); // NOI18N
        btnSimpanPenumpang.setText("Simpan");
        btnSimpanPenumpang.setBorderPainted(false);
        btnSimpanPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanPenumpangActionPerformed(evt);
            }
        });

        btnBatalPenumpang.setBackground(new java.awt.Color(255, 102, 102));
        btnBatalPenumpang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBatalPenumpang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/back.png"))); // NOI18N
        btnBatalPenumpang.setText("Batal");
        btnBatalPenumpang.setBorderPainted(false);
        btnBatalPenumpang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalPenumpangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTambahPenumpangLayout = new javax.swing.GroupLayout(pnlTambahPenumpang);
        pnlTambahPenumpang.setLayout(pnlTambahPenumpangLayout);
        pnlTambahPenumpangLayout.setHorizontalGroup(
            pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSimpanPenumpang)
                            .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel21))
                                .addGap(53, 53, 53)
                                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnPilihFoto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnBatalPenumpang)
                                        .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                                                .addComponent(rbL)
                                                .addGap(18, 18, 18)
                                                .addComponent(rbP))
                                            .addComponent(tfNamaPenumpang)
                                            .addComponent(tfTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfPassport, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInputPenumpang)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        pnlTambahPenumpangLayout.setVerticalGroup(
            pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInputPenumpang)
                .addGap(18, 18, 18)
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNamaPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPassport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbP)
                    .addComponent(rbL)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(pnlTambahPenumpangLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPilihFoto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(pnlTambahPenumpangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanPenumpang)
                    .addComponent(btnBatalPenumpang))
                .addGap(22, 22, 22))
        );

        pnlBg.add(pnlTambahPenumpang, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlHome.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel6.setText("Home");

        lblWelcome.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lblWelcome.setText("Hi! Username");

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHomeLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel6))
                    .addGroup(pnlHomeLayout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addComponent(lblWelcome)))
                .addContainerGap(382, Short.MAX_VALUE))
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel6)
                .addGap(59, 59, 59)
                .addComponent(lblWelcome)
                .addContainerGap(223, Short.MAX_VALUE))
        );

        pnlBg.add(pnlHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlTambahTiket.setBackground(new java.awt.Color(255, 255, 255));

        lblInputTiket.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblInputTiket.setText("Input Data Transaksi Tiket");

        cbbPesawat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Kode Pesawat -" }));
        cbbPesawat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbPesawatItemStateChanged(evt);
            }
        });

        tfBiaya.setEditable(false);
        tfBiaya.setBackground(new java.awt.Color(255, 255, 255));

        cbbPenumpang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Nama Penumpang -" }));
        cbbPenumpang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbPenumpangItemStateChanged(evt);
            }
        });

        btnSimpanTiket.setBackground(new java.awt.Color(255, 204, 51));
        btnSimpanTiket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/diskette.png"))); // NOI18N
        btnSimpanTiket.setText("Simpan");
        btnSimpanTiket.setBorderPainted(false);
        btnSimpanTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanTiketActionPerformed(evt);
            }
        });

        tfTiketNamaPesawat.setEditable(false);

        tfTiketAsal.setEditable(false);

        tfTiketTujuan.setEditable(false);

        tfTiketWaktu.setEditable(false);

        cbbKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Kelas -", "Ekonomi", "Bisnis" }));
        cbbKelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbKelasItemStateChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Nama Penumpang");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Kode Pesawat");

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setText("Maskapai");

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setText("Asal");

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel23.setText("Tujuan");

        btnPilihTanggal.setText("Pilih");
        btnPilihTanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihTanggalActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel25.setText("Waktu");

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel26.setText("Kelas");

        jLabel27.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel27.setText("Biaya");

        jLabel28.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel28.setText("Tanggal");

        btnBatalTiket.setBackground(new java.awt.Color(255, 102, 102));
        btnBatalTiket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBatalTiket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/back.png"))); // NOI18N
        btnBatalTiket.setText("Batal");
        btnBatalTiket.setBorderPainted(false);
        btnBatalTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalTiketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTambahTiketLayout = new javax.swing.GroupLayout(pnlTambahTiket);
        pnlTambahTiket.setLayout(pnlTambahTiketLayout);
        pnlTambahTiketLayout.setHorizontalGroup(
            pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                                .addGap(224, 224, 224)
                                .addComponent(cbbPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel14)
                                    .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                                        .addComponent(btnSimpanTiket)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnBatalTiket))
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel28))))
                        .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                            .addGap(224, 224, 224)
                            .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cbbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfTiketWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfTiketTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfTiketAsal, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbbPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                                        .addComponent(tfTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnPilihTanggal))
                                    .addComponent(tfTiketNamaPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInputTiket)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        pnlTambahTiketLayout.setVerticalGroup(
            pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTambahTiketLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInputTiket)
                .addGap(18, 18, 18)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbbPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTiketNamaPesawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTiketAsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTiketTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTiketWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPilihTanggal)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(37, 37, 37)
                .addGroup(pnlTambahTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanTiket)
                    .addComponent(btnBatalTiket))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pnlBg.add(pnlTambahTiket, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        pnlUser.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel16.setText("Data User");

        btnSimpanUser.setBackground(new java.awt.Color(255, 204, 51));
        btnSimpanUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/diskette.png"))); // NOI18N
        btnSimpanUser.setText("Simpan");
        btnSimpanUser.setBorderPainted(false);
        btnSimpanUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanUserActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Nama");

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel22.setText("Username");

        jLabel24.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel24.setText("Password");

        btnHapusUser.setBackground(new java.awt.Color(255, 102, 102));
        btnHapusUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapusUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete.png"))); // NOI18N
        btnHapusUser.setText("Hapus");
        btnHapusUser.setBorderPainted(false);
        btnHapusUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusUserActionPerformed(evt);
            }
        });

        btnUbahUser.setBackground(new java.awt.Color(0, 255, 255));
        btnUbahUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUbahUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit.png"))); // NOI18N
        btnUbahUser.setText("Ubah");
        btnUbahUser.setBorderPainted(false);
        btnUbahUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahUserActionPerformed(evt);
            }
        });

        tfCariUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfCariUserKeyTyped(evt);
            }
        });

        btnCariUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N
        btnCariUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariUserActionPerformed(evt);
            }
        });

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbUser.setAutoCreateRowSorter(true);
        tbUser.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        tbUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama", "Username", "Password", "Id User"
            }
        ));
        tbUser.setGridColor(new java.awt.Color(255, 255, 255));
        tbUser.setRowHeight(25);
        jScrollPane4.setViewportView(tbUser);
        if (tbUser.getColumnModel().getColumnCount() > 0) {
            tbUser.getColumnModel().getColumn(3).setMinWidth(0);
            tbUser.getColumnModel().getColumn(3).setPreferredWidth(0);
            tbUser.getColumnModel().getColumn(3).setMaxWidth(0);
        }

        javax.swing.GroupLayout pnlUserLayout = new javax.swing.GroupLayout(pnlUser);
        pnlUser.setLayout(pnlUserLayout);
        pnlUserLayout.setHorizontalGroup(
            pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUserLayout.createSequentialGroup()
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlUserLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16))
                    .addGroup(pnlUserLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlUserLayout.createSequentialGroup()
                                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel22))
                                .addGap(74, 74, 74)
                                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tfUsername, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfNamaUser, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnHapusUser)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlUserLayout.createSequentialGroup()
                                    .addComponent(btnUbahUser)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnSimpanUser)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCariUser)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(tfCariUser, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlUserLayout.setVerticalGroup(
            pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNamaUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfCariUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSimpanUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUbahUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCariUser)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pnlBg.add(pnlUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 730, 380));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlBg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlBg, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblNavHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNavHomeMouseClicked
        panel();
        pnlHome.setVisible(true);
    }//GEN-LAST:event_lblNavHomeMouseClicked

    private void lblNavPenumpangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNavPenumpangMouseClicked
        panel();       
        pnlPenumpang.setVisible(true);
        reset();
    }//GEN-LAST:event_lblNavPenumpangMouseClicked

    private void lblNavPesawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNavPesawatMouseClicked
        panel();
        pnlPesawat.setVisible(true);
        reset();
    }//GEN-LAST:event_lblNavPesawatMouseClicked

    private void lblNavTiketMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNavTiketMouseClicked
        panel();       
        pnlTiket.setVisible(true);
        reset();
    }//GEN-LAST:event_lblNavTiketMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        refreshData("");
    }//GEN-LAST:event_formWindowActivated

    private void btnCariPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPesawatActionPerformed
        refreshData(tfCariPesawat.getText());
    }//GEN-LAST:event_btnCariPesawatActionPerformed

    private void btnTambahPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPesawatActionPerformed
        status = TAMBAH;       
        panel();         
        pnlTambahPesawat.setVisible(true);
        reset();
    }//GEN-LAST:event_btnTambahPesawatActionPerformed

    private void btnSimpanPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanPesawatActionPerformed
        try {
            //generate random code untuk kd_pesawat
            Random random = new Random();
            int randomValue = random.nextInt(999);
            DateTimeFormatter kd = DateTimeFormatter.ofPattern("mmss");
            LocalDateTime now = LocalDateTime.now();
            String generatedCode = "PSWT" + randomValue + kd.format(now);
            
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            PreparedStatement ps;
            if(status==TAMBAH) {
                if(tfNamaPesawat.getText().equals("")||
                        tfAsal.getText().equals("")||
                        tfTujuan.getText().equals("")||
                        tfHarga.getText().equals("")||
                        tfWaktu.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Lengkapi Data!");
                } else {            
                String executeQuery = "insert into pesawat "
                        + "(kd_pesawat, nm_pesawat, asal, tujuan, harga, waktu) values (?, ?, ?, ?, ?, ?)";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, generatedCode);
                ps.setString(2, tfNamaPesawat.getText());
                ps.setString(3, tfAsal.getText());
                ps.setString(4, tfTujuan.getText());
                ps.setString(5, tfHarga.getText());
                ps.setString(6, tfWaktu.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Tambah!");
                panel();
                pnlPesawat.setVisible(true);    
                 }
            } else {
                String executeQuery = "update pesawat set "
                        + "nm_pesawat=?, asal=?, tujuan=?, harga=?, waktu=? where id_pesawat=?";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, tfNamaPesawat.getText());
                ps.setString(2, tfAsal.getText());
                ps.setString(3, tfTujuan.getText());
                ps.setString(4, tfHarga.getText());
                ps.setString(5, tfWaktu.getText());
                ps.setString(6, tfIdPesawat.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Ubah!");
                panel();
                pnlPesawat.setVisible(true);    
                status = TAMBAH;
            }                      
        } catch (SQLException ex) {
            System.err.println(ex);
        }       
    }//GEN-LAST:event_btnSimpanPesawatActionPerformed

    private void btnUbahPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahPesawatActionPerformed
        status = UBAH;
        int i = tbPesawat.getSelectedRow();
        if(i>=0) {
           TableModel model = tbPesawat.getModel();
           tfIdPesawat.setText(model.getValueAt(i, 0).toString());
           tfNamaPesawat.setText(model.getValueAt(i, 1).toString());
           tfWaktu.setText(model.getValueAt(i, 2).toString());
           tfAsal.setText(model.getValueAt(i, 3).toString());
           tfTujuan.setText(model.getValueAt(i, 4).toString());
           tfHarga.setText(model.getValueAt(i, 5).toString());
           
           lblInputPesawat.setText("Ubah Data Pesawat");
           panel();
           pnlTambahPesawat.setVisible(true);
       } else {
           JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Diubah!");
       }
    }//GEN-LAST:event_btnUbahPesawatActionPerformed

    private void btnHapusPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPesawatActionPerformed
        int i = tbPesawat.getSelectedRow();
        if(i>=0) {
        int pilihan = JOptionPane.showConfirmDialog(
            null,
            "Yakin Mau Hapus?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        if(pilihan==0) {           
                try {
                    TableModel model = tbPesawat.getModel();
                    Koneksi koneksi = new Koneksi();
                    Connection con = koneksi.getConnection();
                    String executeQuery = "delete from pesawat where id_pesawat =?";
                    PreparedStatement ps = con.prepareStatement(executeQuery);
                    ps.setString(1, model.getValueAt(i,0).toString());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
            } 
        } else {
                JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Dihapus");
            }
        refreshData("");
    }//GEN-LAST:event_btnHapusPesawatActionPerformed

    private void btnPilihWaktuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihWaktuActionPerformed
        timePicker1.showPopup(this, 100, 100);
    }//GEN-LAST:event_btnPilihWaktuActionPerformed

    private void btnPilihFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihFotoActionPerformed
        FileFilter filter = new FileNameExtensionFilter("Image Files",
                                                 "jpg", "png", "gif", "jpeg");
        fChooser.setFileFilter(filter);
        BufferedImage img = null;
        try {
            int result = fChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fChooser.getSelectedFile();
                img = ImageIO.read(file);
                int type = img.getType() == 0? BufferedImage.TYPE_INT_ARGB : img.getType();
                bImage = resizeImage(img, type);
                lblFoto.setIcon(new ImageIcon(bImage));
            }
        } catch (IOException e) {
            System.err.println("Error bPilih : "+e);
        }
    }//GEN-LAST:event_btnPilihFotoActionPerformed

    private void btnSimpanPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanPenumpangActionPerformed
        try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            PreparedStatement ps;
            if(status==TAMBAH) {
                if(tfNamaPenumpang.getText().equals("")||
                        tfPassport.getText().equals("")||
                        rbJenisKelaminGetSelected().equals("")||
                        tfTelepon.getText().equals("")||
                        lblFoto.getIcon()== null){
                    
                    JOptionPane.showMessageDialog(null, "Lengkapi Data!");
                } else {     
                String executeQuery = "insert into penumpang "
                        + "(nm_penumpang, passport, jenkel, telepon, foto) values (?, ?, ?, ?, ?)";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, tfNamaPenumpang.getText());
                ps.setString(2, tfPassport.getText());
                ps.setString(3, rbJenisKelaminGetSelected());
                ps.setString(4, tfTelepon.getText());
                ps.setBlob(5, getBlobImage(bImage));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Tambah!");
                panel();
                pnlPenumpang.setVisible(true);
                }
            } else {
                String executeQuery = "update penumpang set "
                        + "nm_penumpang=?, passport=?, jenkel=?, telepon=?, foto=? where id=?";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, tfNamaPenumpang.getText());
                ps.setString(2, tfPassport.getText());
                ps.setString(3, rbJenisKelaminGetSelected());
                ps.setString(4, tfTelepon.getText());
                ps.setBlob(5,getBlobImage(bImage));
                ps.setString(6,tfIdPenumpang.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Ubah!");
                panel();
                pnlPenumpang.setVisible(true);
                //status = TAMBAH;
            }                      
        } catch (SQLException ex) {
            System.err.println(ex);
        }           
    }//GEN-LAST:event_btnSimpanPenumpangActionPerformed

    private void btnCariPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPenumpangActionPerformed
        refreshData(tfCariPenumpang.getText());
    }//GEN-LAST:event_btnCariPenumpangActionPerformed

    private void btnTambahPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPenumpangActionPerformed
        status = TAMBAH;       
        panel();         
        pnlTambahPenumpang.setVisible(true);
        reset();
    }//GEN-LAST:event_btnTambahPenumpangActionPerformed

    private void btnUbahPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahPenumpangActionPerformed
        status = UBAH;
        int i = tbPenumpang.getSelectedRow();
        if(i>=0) {
           TableModel model = tbPenumpang.getModel();
           tfIdPenumpang.setText(model.getValueAt(i, 0).toString());
           tfNamaPenumpang.setText(model.getValueAt(i, 1).toString());
           tfPassport.setText(model.getValueAt(i, 2).toString());
           rbJenisKelaminSetSelected(model.getValueAt(i, 3).toString());
           tfTelepon.setText(model.getValueAt(i, 4).toString());
           Blob blob = (Blob) model.getValueAt(i, 5);
           penumpang.setFoto(blob);
           bImage = getBufferedImage(penumpang.getFoto());
           lblFoto.setIcon(new ImageIcon(bImage));
           lblInputPenumpang.setText("Ubah Data Penumpang");
           panel();
           pnlTambahPenumpang.setVisible(true);
       } else {
           JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Diubah!");
       }
    }//GEN-LAST:event_btnUbahPenumpangActionPerformed

    private void btnHapusPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPenumpangActionPerformed
        int i = tbPenumpang.getSelectedRow();
        if(i>=0) {
        int pilihan = JOptionPane.showConfirmDialog(
            null,
            "Yakin Mau Hapus?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        if(pilihan==0) {
            
                try {
                    TableModel model = tbPenumpang.getModel();
                    Koneksi koneksi = new Koneksi();
                    Connection con = koneksi.getConnection();
                    String executeQuery = "delete from penumpang where id =?";
                    PreparedStatement ps = con.prepareStatement(executeQuery);
                    ps.setString(1, model.getValueAt(i,0).toString());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
            } 
        } else {
                JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Dihapus");
            }
        refreshData("");
    }//GEN-LAST:event_btnHapusPenumpangActionPerformed

    private void btnHapusTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusTiketActionPerformed
        int i = tbTiket.getSelectedRow();
        if(i>=0) {
        int pilihan = JOptionPane.showConfirmDialog(
            null,
            "Yakin Mau Hapus?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
        if(pilihan==0) {
            
                try {
                    TableModel model = tbTiket.getModel();
                    Koneksi koneksi = new Koneksi();
                    Connection con = koneksi.getConnection();
                    String executeQuery = "delete from tiket where id_tiket =?";
                    PreparedStatement ps = con.prepareStatement(executeQuery);
                    ps.setString(1, model.getValueAt(i,0).toString());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
            } 
        } else {
                JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Dihapus");
            }
        refreshData("");
    }//GEN-LAST:event_btnHapusTiketActionPerformed

    private void tfCariPenumpangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCariPenumpangKeyTyped
        refreshData("");
    }//GEN-LAST:event_tfCariPenumpangKeyTyped

    private void tfCariPesawatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCariPesawatKeyTyped
        refreshData("");
    }//GEN-LAST:event_tfCariPesawatKeyTyped

    private void btnTambahTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahTiketActionPerformed
        status = TAMBAH;     
        panel();
        pnlTambahTiket.setVisible(true);
        reset();
        cbbPesawat.removeAllItems();
        cbbPesawat.addItem("- Pilih Kode Pesawat -");
        cbbPenumpang.removeAllItems();
        cbbPenumpang.addItem("- Pilih Nama Penumpang -");
        try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from pesawat");
            while(rs.next()){
                cbbPesawat.addItem(rs.getString("kd_pesawat"));
            }
            con.close();
        } catch(SQLException e){
            System.err.println("error"+e);
        }
         try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from penumpang");
            while(rs.next()){
                cbbPenumpang.addItem(rs.getString("nm_penumpang"));
            }
            con.close();
        } catch(SQLException e){
            System.err.println("error"+e);
        }
    }//GEN-LAST:event_btnTambahTiketActionPerformed

    private void cbbPenumpangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbPenumpangItemStateChanged
        String penumpang = (String) cbbPenumpang.getSelectedItem();
        
        try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            String sql = "select * from penumpang where nm_penumpang =?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, penumpang);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String id_penumpang = rs.getString("id");
                tfTiketIdPenumpang.setText(id_penumpang);
            }
        } catch(SQLException e){
            System.err.println("error"+e);
        }
    }//GEN-LAST:event_cbbPenumpangItemStateChanged

    private void cbbPesawatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbPesawatItemStateChanged
        String pesawat = (String) cbbPesawat.getSelectedItem();
        
        try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            String sql = "select * from pesawat where kd_pesawat=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pesawat);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String id_pesawat = rs.getString("id_pesawat");
                tfTiketIdPesawat.setText(id_pesawat);
                String nm_pswt = rs.getString("nm_pesawat");
                tfTiketNamaPesawat.setText(nm_pswt);
                String asal_pswt = rs.getString("asal");
                tfTiketAsal.setText(asal_pswt);
                String tujuan_pswt = rs.getString("tujuan");
                tfTiketTujuan.setText(tujuan_pswt);
                String harga_pswt = rs.getString("harga");
                tfTiketHarga.setText(harga_pswt);
                String waktu_pswt = rs.getString("waktu");
                tfTiketWaktu.setText(waktu_pswt);
            }
        } catch(SQLException e){
            System.err.println("error"+e);
        }
    }//GEN-LAST:event_cbbPesawatItemStateChanged

    private void btnSimpanTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanTiketActionPerformed
        try {    
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            PreparedStatement ps;
            if(status==TAMBAH) {
                if(cbbPenumpang.getSelectedIndex()==0||
                        cbbPesawat.getSelectedIndex()==0||
                        cbbKelas.getSelectedIndex()==0||
                        tfBiaya.getText().equals("")||
                        tfTanggal.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Lengkapi Data!");
                } else {            
                String executeQuery = "insert into tiket "
                        + "(id, id_pesawat, kelas, biaya, tanggal) values (?, ?, ?, ?, ?)";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, tfTiketIdPenumpang.getText());
                ps.setString(2, tfTiketIdPesawat.getText());
                ps.setString(3, cbbKelas.getSelectedItem().toString());
                ps.setString(4, tfBiaya.getText());
                ps.setString(5, tfTanggal.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Tambah!");
                panel();
                pnlTiket.setVisible(true);    
                 }
            } else {
                String executeQuery = "update tiket set "
                        + "kelas=?, biaya=?, tanggal=? where id_tiket=?";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, cbbKelas.getSelectedItem().toString());
                ps.setString(2, tfBiaya.getText());
                ps.setString(3, tfTanggal.getText());
                ps.setString(4, tfIdTiket.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Ubah!");
                panel();
                pnlTiket.setVisible(true);    
                status = TAMBAH;
            }                      
        } catch (SQLException ex) {
            System.err.println(ex);
        }                                                      
    }//GEN-LAST:event_btnSimpanTiketActionPerformed

    private void cbbKelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbKelasItemStateChanged
        String kls = (String)cbbKelas.getSelectedItem();
        
        switch(kls){
            case "Ekonomi":
            tfBiaya.setText(String.valueOf(Double.parseDouble(tfTiketHarga.getText())+2133260));
            break;
            case "Bisnis":
            tfBiaya.setText(String.valueOf(Double.parseDouble(tfTiketHarga.getText())+7504130));           
            break;
        }
    }//GEN-LAST:event_cbbKelasItemStateChanged

    private void btnUbahTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahTiketActionPerformed
        status = UBAH;
        int i = tbTiket.getSelectedRow();
        if(i>=0) {
           panel();
           pnlTambahTiket.setVisible(true);
           try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from pesawat");
            while(rs.next()){
                cbbPesawat.addItem(rs.getString("kd_pesawat"));
            }
            con.close();
        } catch(SQLException e){
            System.err.println("error"+e);
        }
         try {
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from penumpang");
            while(rs.next()){
                cbbPenumpang.addItem(rs.getString("nm_penumpang"));
            }
            con.close();
        } catch(SQLException e){
            System.err.println("error"+e);
        }
           //panel();
           TableModel model = tbTiket.getModel();
           tfIdTiket.setText(model.getValueAt(i, 0).toString());
           tfTanggal.setText(model.getValueAt(i, 1).toString());
           cbbPesawat.setSelectedItem(model.getValueAt(i, 3));
           cbbPenumpang.setSelectedItem(model.getValueAt(i, 4));
           cbbKelas.setSelectedItem(model.getValueAt(i, 5));          
           tfBiaya.setText(model.getValueAt(i, 9).toString());
           
           lblInputTiket.setText("Ubah Data Tiket");
           
           cbbPenumpang.setEnabled(false);
           cbbPesawat.setEnabled(false);
       } else {
           JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Diubah!");
       }
    }//GEN-LAST:event_btnUbahTiketActionPerformed

    private void btnPilihTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihTanggalActionPerformed
        dateChooser1.showPopup();
    }//GEN-LAST:event_btnPilihTanggalActionPerformed

    private void tfHargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfHargaKeyTyped
        // validasi untuk Text Field Harga Per Meter dimana hanya boleh menginputkan data berupa angka saja
        char c = evt.getKeyChar();
        if (! ((Character.isDigit(c) ||
                (c == KeyEvent.VK_BACK_SPACE) ||
                (c == KeyEvent.VK_DELETE))
                )) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Input Angka Saja!");
            evt.consume();
        }
    }//GEN-LAST:event_tfHargaKeyTyped

    private void lblNavUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNavUserMouseClicked
        panel();       
        pnlUser.setVisible(true);
        reset();
    }//GEN-LAST:event_lblNavUserMouseClicked

    private void btnCariUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariUserActionPerformed
        refreshData(tfCariUser.getText());
    }//GEN-LAST:event_btnCariUserActionPerformed

    private void btnCariTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTiketActionPerformed
        refreshData(tfCariTiket.getText());
    }//GEN-LAST:event_btnCariTiketActionPerformed

    private void tfCariUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCariUserKeyTyped
        refreshData("");
    }//GEN-LAST:event_tfCariUserKeyTyped

    private void tfCariTiketKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCariTiketKeyTyped
        refreshData("");
    }//GEN-LAST:event_tfCariTiketKeyTyped

    private void btnHapusUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusUserActionPerformed
        int i = tbUser.getSelectedRow();
        if(i>=0) {
        int pilihan = JOptionPane.showConfirmDialog(
            null,
            "Yakin Mau Hapus Nih?",
            "Konfirmasi Dulu Dong Kalau Mau Hapus",
            JOptionPane.YES_NO_OPTION);
        if(pilihan==0) {
            
                try {
                    TableModel model = tbUser.getModel();
                    Koneksi koneksi = new Koneksi();
                    Connection con = koneksi.getConnection();
                    String executeQuery = "delete from user where id_user =?";
                    PreparedStatement ps = con.prepareStatement(executeQuery);
                    ps.setString(1, model.getValueAt(i,0).toString());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println(ex);
                }
            } 
        } else {
                JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Dihapus");
            }
        refreshData("");
    }//GEN-LAST:event_btnHapusUserActionPerformed

    private void btnBatalPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalPesawatActionPerformed
        panel();       
        pnlPesawat.setVisible(true);
        reset();
    }//GEN-LAST:event_btnBatalPesawatActionPerformed

    private void btnBatalPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalPenumpangActionPerformed
        panel();       
        pnlPenumpang.setVisible(true);
        reset();
    }//GEN-LAST:event_btnBatalPenumpangActionPerformed

    private void btnBatalTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalTiketActionPerformed
        panel();       
        pnlTiket.setVisible(true);
        reset();
    }//GEN-LAST:event_btnBatalTiketActionPerformed

    private void btnSimpanUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanUserActionPerformed
        try {            
            Koneksi koneksi = new Koneksi();
            Connection con = koneksi.getConnection();
            PreparedStatement ps;
            if(status==TAMBAH) {
                if(tfNamaUser.getText().equals("")||
                        tfUsername.getText().equals("")||
                        tfPassword.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Lengkapi Data Anda!");
                } else {            
                String executeQuery = "insert into user "
                        + "(nama, username, pass) values (?, ?, ?)";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, tfNamaUser.getText());
                ps.setString(2, tfUsername.getText());
                ps.setString(3, tfPassword.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Tambah!");
                reset();
//                panel();
//                pnlUser.setVisible(true);    
                 }
            } else {
                String executeQuery = "update user set "
                        + "nama=?, username=?, pass=? where id_user=?";
                ps = con.prepareStatement(executeQuery);
                ps.setString(1, tfNamaUser.getText());
                ps.setString(2, tfUsername.getText());
                ps.setString(3, tfPassword.getText());
                ps.setString(4, tfIdUser.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil Ubah!");
                reset();
//                panel();
//                pnlUser.setVisible(true);    
                status = TAMBAH;
            }                      
        } catch (SQLException ex) {
            System.err.println(ex);
        }       
    }//GEN-LAST:event_btnSimpanUserActionPerformed

    private void btnUbahUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahUserActionPerformed
        status = UBAH;
        int i = tbUser.getSelectedRow();
        if(i>=0) {
           TableModel model = tbUser.getModel();
           tfNamaUser.setText(model.getValueAt(i, 0).toString());
           tfUsername.setText(model.getValueAt(i, 1).toString());
           tfPassword.setText(model.getValueAt(i, 2).toString());
           tfIdUser.setText(model.getValueAt(i, 3).toString());
       } else {
           JOptionPane.showMessageDialog(null, "Pilih Data yang Ingin Diubah!");
       }
    }//GEN-LAST:event_btnUbahUserActionPerformed

    private void btnCetakTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakTiketActionPerformed
        try {
        Koneksi koneksi = new Koneksi();
         Connection con= koneksi.getConnection(); 
         String path = "src/report/tiketreport.jasper";
            HashMap<String, Object> parameter = new HashMap();            
            JasperPrint jp = JasperFillManager.fillReport(path, parameter, con);
            JasperViewer viewer = new JasperViewer(jp, false);
        viewer.setVisible(true);
        } catch(Exception e) {
            System.err.println("error report :"+e);
        }
    }//GEN-LAST:event_btnCetakTiketActionPerformed

    private void btnCetakPenumpangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPenumpangActionPerformed
        try {
        Koneksi koneksi = new Koneksi();
         Connection con= koneksi.getConnection(); 
         String path = "src/report/penumpangreport.jasper";
            HashMap<String, Object> parameter = new HashMap();            
            JasperPrint jp = JasperFillManager.fillReport(path, parameter, con);
            JasperViewer viewer = new JasperViewer(jp, false);
        viewer.setVisible(true);
        } catch(Exception e) {
            System.err.println("error report :"+e);
        }
    }//GEN-LAST:event_btnCetakPenumpangActionPerformed

    private void btnCetakPesawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPesawatActionPerformed
        try {
        Koneksi koneksi = new Koneksi();
         Connection con= koneksi.getConnection(); 
         String path = "src/report/pesawatreport.jasper";
            HashMap<String, Object> parameter = new HashMap();            
            JasperPrint jp = JasperFillManager.fillReport(path, parameter, con);
            JasperViewer viewer = new JasperViewer(jp, false);
        viewer.setVisible(true);
        } catch(Exception e) {
            System.err.println("error report :"+e);
        }
    }//GEN-LAST:event_btnCetakPesawatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PenjualanTiketFrame("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalPenumpang;
    private javax.swing.JButton btnBatalPesawat;
    private javax.swing.JButton btnBatalTiket;
    private javax.swing.JButton btnCariPenumpang;
    private javax.swing.JButton btnCariPesawat;
    private javax.swing.JButton btnCariTiket;
    private javax.swing.JButton btnCariUser;
    private javax.swing.JButton btnCetakPenumpang;
    private javax.swing.JButton btnCetakPesawat;
    private javax.swing.JButton btnCetakTiket;
    private javax.swing.JButton btnHapusPenumpang;
    private javax.swing.JButton btnHapusPesawat;
    private javax.swing.JButton btnHapusTiket;
    private javax.swing.JButton btnHapusUser;
    private javax.swing.JButton btnPilihFoto;
    private javax.swing.JButton btnPilihTanggal;
    private javax.swing.JButton btnPilihWaktu;
    private javax.swing.JButton btnSimpanPenumpang;
    private javax.swing.JButton btnSimpanPesawat;
    private javax.swing.JButton btnSimpanTiket;
    private javax.swing.JButton btnSimpanUser;
    private javax.swing.JButton btnTambahPenumpang;
    private javax.swing.JButton btnTambahPesawat;
    private javax.swing.JButton btnTambahTiket;
    private javax.swing.JButton btnUbahPenumpang;
    private javax.swing.JButton btnUbahPesawat;
    private javax.swing.JButton btnUbahTiket;
    private javax.swing.JButton btnUbahUser;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbbKelas;
    private javax.swing.JComboBox<String> cbbPenumpang;
    private javax.swing.JComboBox<String> cbbPesawat;
    private com.raven.datechooser.DateChooser dateChooser1;
    private javax.swing.JFileChooser fChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblInputPenumpang;
    private javax.swing.JLabel lblInputPesawat;
    private javax.swing.JLabel lblInputTiket;
    private javax.swing.JLabel lblNavHome;
    private javax.swing.JLabel lblNavPenumpang;
    private javax.swing.JLabel lblNavPesawat;
    private javax.swing.JLabel lblNavTiket;
    private javax.swing.JLabel lblNavUser;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlBg;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlNav;
    private javax.swing.JPanel pnlPenumpang;
    private javax.swing.JPanel pnlPesawat;
    private javax.swing.JPanel pnlTambahPenumpang;
    private javax.swing.JPanel pnlTambahPesawat;
    private javax.swing.JPanel pnlTambahTiket;
    private javax.swing.JPanel pnlTiket;
    private javax.swing.JPanel pnlUser;
    private javax.swing.JRadioButton rbL;
    private javax.swing.JRadioButton rbP;
    private javax.swing.JTable tbPenumpang;
    private javax.swing.JTable tbPesawat;
    private javax.swing.JTable tbTiket;
    private javax.swing.JTable tbUser;
    private javax.swing.JTextField tfAsal;
    private javax.swing.JTextField tfBiaya;
    private javax.swing.JTextField tfCariPenumpang;
    private javax.swing.JTextField tfCariPesawat;
    private javax.swing.JTextField tfCariTiket;
    private javax.swing.JTextField tfCariUser;
    private javax.swing.JTextField tfHarga;
    private javax.swing.JTextField tfIdPenumpang;
    private javax.swing.JTextField tfIdPesawat;
    private javax.swing.JTextField tfIdTiket;
    private javax.swing.JTextField tfIdUser;
    private javax.swing.JTextField tfNamaPenumpang;
    private javax.swing.JTextField tfNamaPesawat;
    private javax.swing.JTextField tfNamaUser;
    private javax.swing.JTextField tfPassport;
    private javax.swing.JTextField tfPassword;
    private javax.swing.JTextField tfTanggal;
    private javax.swing.JTextField tfTelepon;
    private javax.swing.JTextField tfTiketAsal;
    private javax.swing.JTextField tfTiketHarga;
    private javax.swing.JTextField tfTiketIdPenumpang;
    private javax.swing.JTextField tfTiketIdPesawat;
    private javax.swing.JTextField tfTiketNamaPesawat;
    private javax.swing.JTextField tfTiketTujuan;
    private javax.swing.JTextField tfTiketWaktu;
    private javax.swing.JTextField tfTujuan;
    private javax.swing.JTextField tfUsername;
    private javax.swing.JTextField tfWaktu;
    private com.raven.swing.TimePicker timePicker1;
    // End of variables declaration//GEN-END:variables
}
