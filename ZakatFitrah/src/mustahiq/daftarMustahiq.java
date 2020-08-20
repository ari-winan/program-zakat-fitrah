/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mustahiq;

/**
 *
 * @author Bernafas
 */

//library
import muzakki.*;
import aplikasizakat.koneksi;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.Date; 
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
//library laporan
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class daftarMustahiq extends javax.swing.JFrame {
    koneksi k=new koneksi(); 
    private DefaultTableModel model;
    private DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
    private DecimalFormat formatter;
    private boolean checkTable=false;
    public static String noMustahiq;
    public static String sql,logMuzakki;
    public static int totUangMuzakki,totBerasMuzakki,totUangMustahiq,totBerasMustahiq;
    ResultSet rsLog;
    Statement stLog;
    public daftarMustahiq() {
        initComponents();
        k.konek();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //memanggil fungsi, cari aja di bawah yang namanya sama.
        inisialisasiKolom();
        tampilkanData();
    }
    
    //fungsi tampilkan data di dalam table
    void tampilkanData(){
        sql="select * from dt_mustahiq";
        logMuzakki="select * from log_muzakki";
        try{
            k.st=k.conn.createStatement();
            k.rs=k.st.executeQuery(sql);
            stLog=k.conn.createStatement();
            rsLog=stLog.executeQuery(logMuzakki);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Load data gagal");
            return;
        }
        mengisiTable();
        settingKolom();
        symbols.setGroupingSeparator('.');
        Format formatter=new DecimalFormat("###,###.##", symbols);
        
        txtUangMustahiq.setText("Rp. "+formatter.format(totUangMustahiq));
        txtBerasMustahiq.setText(totBerasMustahiq+" Kg");
    }
    
    //digunakan untuk mengatur jumlah dan lebar kolom
    void settingKolom(){
        DefaultTableCellRenderer rataKanan=new DefaultTableCellRenderer();
        rataKanan.setHorizontalAlignment(SwingConstants.RIGHT);
        tblDaftarMustahiq.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn col=null;
        col=tblDaftarMustahiq.getColumnModel().getColumn(0);
        col.setPreferredWidth(50);
        col=tblDaftarMustahiq.getColumnModel().getColumn(1);
        col.setPreferredWidth(100);
        col=tblDaftarMustahiq.getColumnModel().getColumn(2);
        col.setPreferredWidth(200);
        col=tblDaftarMustahiq.getColumnModel().getColumn(3);
        col.setPreferredWidth(250);
        col=tblDaftarMustahiq.getColumnModel().getColumn(4);
        col.setPreferredWidth(300);
        col=tblDaftarMustahiq.getColumnModel().getColumn(5);
        col.setPreferredWidth(130);
        col=tblDaftarMustahiq.getColumnModel().getColumn(6);
        col.setPreferredWidth(130);
        col=tblDaftarMustahiq.getColumnModel().getColumn(7);
        col.setPreferredWidth(190);
 
        col.setCellRenderer(rataKanan);
        tblDaftarMustahiq.getColumnModel().getColumn(5).setCellRenderer(rataKanan);
        col=tblDaftarMustahiq.getColumnModel().getColumn(5);
        tblDaftarMustahiq.getColumnModel().getColumn(6).setCellRenderer(rataKanan);
        col=tblDaftarMustahiq.getColumnModel().getColumn(6);
    }
    
    //mengatur label pada table 
    void inisialisasiKolom(){
        model=new DefaultTableModel();
        tblDaftarMustahiq.setModel(model);
        model.addColumn("NO");
        model.addColumn("TANGGAL");
        model.addColumn("GOLONGAN");
        model.addColumn("NAMA MUSTAHIQ");
        model.addColumn("ALAMAT");
        model.addColumn("UANG");
        model.addColumn("BERAS");
        model.addColumn("PANITIA");        
        
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
    }
    
    //isi table
    void mengisiTable(){
        symbols.setGroupingSeparator('.');
        Format formatter=new DecimalFormat("###,###.##", symbols);
         try {
             while(k.rs.next()){
                 Object[] obj=new Object[8]; //object = 8 berarti jumlah data nya ada 8
                 obj[0]=k.rs.getString("nomor"); //data 1 (dimulai dari 0)
                 obj[1]=k.rs.getString("tglInput"); //data 2 
                 obj[2]=k.rs.getString("gol").toUpperCase();// data 3 
                 obj[3]=k.rs.getString("namaMustahiq");// data 4
                 obj[4]=k.rs.getString("alamat");
                 obj[5]=k.rs.getString("jmlUang");
                 obj[6]=k.rs.getString("jmlBeras");
                 obj[7]=k.rs.getString("panitia"); //data 8
                 
                 totUangMustahiq=totUangMustahiq+Integer.parseInt(k.rs.getString("jmlUang"));
                 totBerasMustahiq=totBerasMustahiq+Integer.parseInt(k.rs.getString("jmlBeras"));
                 model.addRow(obj);
             }
             rsLog.last();
             txtTerUang.setText("Rp. "+formatter.format(rsLog.getInt("totalUang")));
             txtTerBeras.setText(rsLog.getInt("totalBeras")+" Kg");
             totUangMuzakki=rsLog.getInt("totalUang");
             totBerasMuzakki=rsLog.getInt("totalBeras");
             
             //jScrollPane1.setViewportView(tblDaftarMuzakki); 
         } catch (SQLException e) {
             JOptionPane.showMessageDialog(null,"Data gagal diload");
         }
       
    }
    //untuk mencari data yang telah diinput
    void fungsiPencarian(){
        
                inisialisasiKolom();
        //memanggil table pada database, "cari data dari dt_mustahiq di mana namaMustahiq seperti 'huruf yang dimasukkan'
        sql="select * from dt_mustahiq where namaMustahiq like '%"+txtCari.getText().toString().toUpperCase()+"%'";
        //mengeksekusi data
        try{
            k.st=k.conn.createStatement();
            k.rs=k.st.executeQuery(sql);
        //jika gagal
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Data Tidak Ketemu");
            return;
        }
        mengisiTable();
        settingKolom();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnCari = new javax.swing.JButton();
        lblTanggal = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnTambahMustahiq = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDaftarMustahiq = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtBerasMustahiq = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtUangMustahiq = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnRefMustahiq = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtTerBeras = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTerUang = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DAFTAR MUZAKKI");
        setBackground(new java.awt.Color(204, 204, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 204, 102));

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muzakki/search.png"))); // NOI18N
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        lblTanggal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTanggal.setForeground(new java.awt.Color(0, 102, 51));
        lblTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arabic Typesetting", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NAMA");

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/exit.png"))); // NOI18N
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muzakki/trash.png"))); // NOI18N
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambahMustahiq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muzakki/save(1).png"))); // NOI18N
        btnTambahMustahiq.setToolTipText("");
        btnTambahMustahiq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahMustahiqActionPerformed(evt);
            }
        });

        tblDaftarMustahiq.setBackground(new java.awt.Color(153, 255, 102));
        tblDaftarMustahiq.setRowHeight(25);
        tblDaftarMustahiq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDaftarMustahiqMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDaftarMustahiq);

        jPanel3.setBackground(new java.awt.Color(153, 255, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TOTAL PENYALURAN MUSTAHIQ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(153, 0, 0))); // NOI18N

        txtBerasMustahiq.setEditable(false);
        txtBerasMustahiq.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBerasMustahiq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setText("ZAKAT BERAS :");

        txtUangMustahiq.setEditable(false);
        txtUangMustahiq.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUangMustahiq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setText("ZAKAT UANG :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 60, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUangMustahiq, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBerasMustahiq, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUangMustahiq, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBerasMustahiq, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnRefMustahiq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/restart.png"))); // NOI18N
        btnRefMustahiq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefMustahiqActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(153, 255, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SISA ZAKAT TERKUMPUL", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(153, 0, 0))); // NOI18N

        txtTerBeras.setEditable(false);
        txtTerBeras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTerBeras.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("ZAKAT BERAS :");

        txtTerUang.setEditable(false);
        txtTerUang.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTerUang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setText("ZAKAT UANG :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 28, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTerUang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTerBeras, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTerUang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTerBeras, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muzakki/print.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 204, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText(".::");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DAFTAR MUZAKKI");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 204, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("::.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))
                            .addComponent(lblTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(334, 334, 334)
                        .addComponent(btnRefMustahiq)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(14, 14, 14)
                                .addComponent(btnTambahMustahiq)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHapus)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addComponent(lblTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefMustahiq))
                    .addComponent(btnCari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnTambahMustahiq, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(17, 17, 17))
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel16))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
       DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
       Date date=new Date();
       lblTanggal.setText("Tanggal : "+dateFormat.format(date).toString());

    }//GEN-LAST:event_formWindowOpened

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        fungsiPencarian();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        totUangMustahiq=0;
        totBerasMustahiq=0;
        new index.mainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        //menghapus data 
        //checkTable == false, jika table belum diklik maka
        if (checkTable == false) {
                JOptionPane.showMessageDialog(null,"Belum ada data yang dipilih");
            //jika sudah diklik
            }else{
                
                if(JOptionPane.showConfirmDialog(null,"Yakin data akan dihapus?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ 
                //mengambil kode dari kolom yang sudah diklik
                String nomor=tblDaftarMustahiq.getValueAt(tblDaftarMustahiq.getSelectedRow(), 0).toString();
                String sql="delete from dt_mustahiq where nomor='"+nomor+"'";
                
         
                String zakatUang=tblDaftarMustahiq.getValueAt(tblDaftarMustahiq.getSelectedRow(),5).toString();
                String zakatBeras=tblDaftarMustahiq.getValueAt(tblDaftarMustahiq.getSelectedRow(),6).toString();
                String updateLog = "UPDATE log_muzakki SET totalUang='"+zakatUang+"',totalBeras='"+zakatBeras+"'";
            try{
                try{
                    k.st=k.conn.createStatement();
                    k.st.execute(sql);
                    JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(this, "Gagal Dihapus");
                    return;
                }
                try{
                    k.st=k.conn.createStatement();
                    k.st.execute(updateLog);
                    JOptionPane.showMessageDialog(this, "berhasil");
                }catch(SQLException e){
                    return;
                }
                JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "Gagal Dihapus");
            }
        }
    }
                
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tblDaftarMustahiqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDaftarMustahiqMouseClicked
        // TODO add your handling code here:
        checkTable=true;
    }//GEN-LAST:event_tblDaftarMustahiqMouseClicked

    private void btnTambahMustahiqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahMustahiqActionPerformed
        // TODO add your handling code here:
        new inputMustahiq().setVisible(true);
    }//GEN-LAST:event_btnTambahMustahiqActionPerformed

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        // TODO add your handling code here:
         if (evt.getKeyChar()==evt.VK_ENTER){
             fungsiPencarian();
         }
    }//GEN-LAST:event_txtCariKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        totUangMustahiq=0;
        totBerasMustahiq=0;
    }//GEN-LAST:event_formWindowClosed

    private void btnRefMustahiqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefMustahiqActionPerformed
        // TODO add your handling code here:
        totUangMustahiq=0;
        totBerasMustahiq=0;
        inisialisasiKolom();
        tampilkanData();
    }//GEN-LAST:event_btnRefMustahiqActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //untuk tombol print
        try{
            HashMap parameter = new HashMap();
            Class.forName("com.mysql.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql:"+"///db_zakat","root","");
            File file = new File("src/mustahiq/Laporan.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, cn);
            JasperViewer.viewReport(jp, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
        }catch (JRException ex) {
            Logger.getLogger(daftarMustahiq.class.getName()).log(Level.SEVERE, null, ex);
        
    }//GEN-LAST:event_jButton1ActionPerformed
    }
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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(daftarMustahiq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(daftarMustahiq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(daftarMustahiq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(daftarMustahiq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new daftarMustahiq().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnRefMustahiq;
    private javax.swing.JButton btnTambahMustahiq;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JTable tblDaftarMustahiq;
    private javax.swing.JTextField txtBerasMustahiq;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtTerBeras;
    private javax.swing.JTextField txtTerUang;
    private javax.swing.JTextField txtUangMustahiq;
    // End of variables declaration//GEN-END:variables
}
