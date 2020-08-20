/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muzakki;

/**
 *
 * @author Bernafas
 */
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class datfarMuzakki extends javax.swing.JFrame {
    koneksi k=new koneksi();
    private DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
    private DecimalFormat formatter;
    DefaultTableModel model;
    private boolean checkTable=false;
    public static String noEdit;
    public static String sql;
    public static int totalJiwa,totalUang,totalBeras;
    public datfarMuzakki() {
        initComponents();
        k.konek();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        inisialisasiKolom();
        tampilkanData();
    }
    void tampilkanData(){
        sql="select * from dt_muzakki";
        try{
            k.st=k.conn.createStatement();
            k.rs=k.st.executeQuery(sql);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Load data gagal");
            return;
        }
        mengisiTable();
        settingKolom();
        
        symbols.setGroupingSeparator('.');
        Format formatter=new DecimalFormat("###,###.##", symbols);
        txtTotalJiwa.setText(Integer.toString(totalJiwa));
        txtTotalZakatUang.setText("Rp. "+formatter.format(totalUang));
        txtTotalZakatBeras.setText(Integer.toString(totalBeras)+" Kg");   
    }
    void settingKolom(){
        DefaultTableCellRenderer rataKanan=new DefaultTableCellRenderer();
        rataKanan.setHorizontalAlignment(SwingConstants.RIGHT);
        tblDaftarMuzakki.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn col=null;
        col=tblDaftarMuzakki.getColumnModel().getColumn(0);
        col.setPreferredWidth(50);
        col=tblDaftarMuzakki.getColumnModel().getColumn(1);
        col.setPreferredWidth(100);
        col=tblDaftarMuzakki.getColumnModel().getColumn(2);
        col.setPreferredWidth(250);
        col=tblDaftarMuzakki.getColumnModel().getColumn(3);
        col.setPreferredWidth(400);
        col=tblDaftarMuzakki.getColumnModel().getColumn(4);
        col.setPreferredWidth(80);
        col=tblDaftarMuzakki.getColumnModel().getColumn(5);
        col.setPreferredWidth(150);
        col=tblDaftarMuzakki.getColumnModel().getColumn(6);
        col.setPreferredWidth(100);
        col=tblDaftarMuzakki.getColumnModel().getColumn(7);
        col.setPreferredWidth(220);

        col.setCellRenderer(rataKanan);
        tblDaftarMuzakki.getColumnModel().getColumn(4).setCellRenderer(rataKanan);
        col=tblDaftarMuzakki.getColumnModel().getColumn(4);
        tblDaftarMuzakki.getColumnModel().getColumn(5).setCellRenderer(rataKanan);
        col=tblDaftarMuzakki.getColumnModel().getColumn(5);
        tblDaftarMuzakki.getColumnModel().getColumn(6).setCellRenderer(rataKanan);
        col=tblDaftarMuzakki.getColumnModel().getColumn(6);
       
    }
    void inisialisasiKolom(){
        model=new DefaultTableModel();
        tblDaftarMuzakki.setModel(model);
        model.addColumn("NO");
        model.addColumn("TANGGAL");
        model.addColumn("NAMA MUZAKKI");
        model.addColumn("ALAMAT");
        model.addColumn("JIWA");
        model.addColumn("ZAKAT UANG");
        model.addColumn("ZAKAT BERAS (Kg)");
        model.addColumn("PANITIA");
        
        
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
    }
    void mengisiTable(){
         try {
             while(k.rs.next()){
                 Object[] obj=new Object[8];
                 obj[0]=k.rs.getString("nomor");
                 obj[1]=k.rs.getString("tglInput");
                 obj[2]=k.rs.getString("namaMuzakki").toUpperCase();
                 obj[3]=k.rs.getString("Alamat");
                 obj[4]=k.rs.getString("jmlJiwa")+" Orang";
                 obj[5]=k.rs.getString("jmlZakat");
                 obj[6]=k.rs.getString("jmlBeras");
                 obj[7]=k.rs.getString("panitia");
                 totalJiwa=totalJiwa+Integer.parseInt(k.rs.getString("jmlJiwa"));
                 totalUang=totalUang+Integer.parseInt(k.rs.getString("jmlZakat"));
                 totalBeras=totalBeras+Integer.parseInt(k.rs.getString("jmlBeras"));
                 model.addRow(obj);
             }
            jScrollPane1.setViewportView(tblDaftarMuzakki); 
         } catch (SQLException e) {
             JOptionPane.showMessageDialog(null,"Data gagal diload");
         }
    }
    void fungsiPencarian(){
                inisialisasiKolom();
        sql="select * from dt_muzakki where namaMuzakki like '%"+txtCari.getText().toString().toUpperCase()+"%'";
        try{
            k.st=k.conn.createStatement();
            k.rs=k.st.executeQuery(sql);
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
        btnTambahMuzakki = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDaftarMuzakki = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtTotalJiwa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTotalZakatBeras = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTotalZakatUang = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

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

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arabic Typesetting", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NAMA :");

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

        btnTambahMuzakki.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muzakki/save(1).png"))); // NOI18N
        btnTambahMuzakki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahMuzakkiActionPerformed(evt);
            }
        });

        tblDaftarMuzakki.setBackground(new java.awt.Color(153, 255, 102));
        tblDaftarMuzakki.setRowHeight(25);
        jScrollPane1.setViewportView(tblDaftarMuzakki);
        tblDaftarMuzakki.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDaftarMuzakkiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDaftarMuzakki);

        jPanel2.setBackground(new java.awt.Color(153, 255, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TOTAL", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(153, 0, 0))); // NOI18N

        txtTotalJiwa.setEditable(false);
        txtTotalJiwa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalJiwa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("JIWA :");

        txtTotalZakatBeras.setEditable(false);
        txtTotalZakatBeras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalZakatBeras.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("ZAKAT BERAS :");

        txtTotalZakatUang.setEditable(false);
        txtTotalZakatUang.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalZakatUang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("ZAKAT UANG :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalZakatUang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalZakatBeras, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalJiwa, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotalZakatUang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotalZakatBeras, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotalJiwa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/restart.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muzakki/print.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 204, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText(".::");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
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
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTambahMuzakki, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 164, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(279, 279, 279))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(btnTambahMuzakki, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
        totalUang=0;
        totalBeras=0;
        totalJiwa=0;
        new index.mainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (checkTable == false) {
                JOptionPane.showMessageDialog(null,"Belum ada data yang dipilih");
                //checkTable = true;
            }
        if(JOptionPane.showConfirmDialog(null,"Yakin data akan dihapus?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            String nomor=tblDaftarMuzakki.getValueAt(tblDaftarMuzakki.getSelectedRow(), 0).toString();
            String sql="delete from dt_muzakki where nomor='"+nomor+"'";
            // Digunakan untuk update tabel lain
            //String zakatUang=tblDaftarMuzakki.getValueAt(tblDaftarMuzakki.getSelectedRow(),5).toString();
            //String zakatBeras=tblDaftarMuzakki.getValueAt(tblDaftarMuzakki.getSelectedRow(),6).toString();
            //String updateLog="update log_muzakki set totalUang=totalUang-'"+zakatUang+"',totalBeras=totalBeras-'"+zakatBeras+"'";
            try{
                try{
                    k.st=k.conn.createStatement();
                    k.st.execute(sql);
                //JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
                }catch(SQLException e){
                //JOptionPane.showMessageDialog(this, "Gagal Dihapus");
                    return;
                }
            /*
            try{
                k.st=k.conn.createStatement();
                k.st.execute(updateLog);
                //JOptionPane.showMessageDialog(this, "Data Berhasil Di Update");
            }catch(SQLException e){
                //JOptionPane.showMessageDialog(this, "Gagal Di Update");
                return;
            }
            */
                JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "Gagal Dihapus");
            }
            }       
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tblDaftarMuzakkiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDaftarMuzakkiMouseClicked
        // TODO add your handling code here:
        checkTable=true;
    }//GEN-LAST:event_tblDaftarMuzakkiMouseClicked

    private void btnTambahMuzakkiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahMuzakkiActionPerformed
        // TODO add your handling code here:
        new inputMuzakki().setVisible(true);
    }//GEN-LAST:event_btnTambahMuzakkiActionPerformed

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        // TODO add your handling code here:
         if (evt.getKeyChar()==evt.VK_ENTER){
             fungsiPencarian();
         }
    }//GEN-LAST:event_txtCariKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        totalJiwa=0;
        totalUang=0;
        totalBeras=0;
    }//GEN-LAST:event_formWindowClosed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        totalJiwa=0;
        totalUang=0;
        totalBeras=0;
        inisialisasiKolom();
        tampilkanData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
            HashMap parameter = new HashMap();
            Class.forName("com.mysql.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql:"+"///db_zakat","root","");
            File file = new File("src/muzakki/Laporan.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, cn);
            JasperViewer.viewReport(jp, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(datfarMuzakki.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(datfarMuzakki.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(datfarMuzakki.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(datfarMuzakki.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(datfarMuzakki.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new datfarMuzakki().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTambahMuzakki;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JTable tblDaftarMuzakki;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtTotalJiwa;
    private javax.swing.JTextField txtTotalZakatBeras;
    private javax.swing.JTextField txtTotalZakatUang;
    // End of variables declaration//GEN-END:variables
}
