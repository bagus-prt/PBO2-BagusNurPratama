/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configdb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.Set;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author BAGUS NUR PRATAMA
 */
public class tbl_agenda {
    private String namadb = "pbo2_2310010457";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi; 
    public Integer VAR_idpetugas = null;
    public String VAR_judul = null;
    public String VAR_jamawal = null;    
    public String VAR_jamakhir = null;
    public String VAR_tanggalawal = null;    
    public String VAR_tanggalakhir = null;
    public String VAR_keterangan = null;
    public boolean validasi = false;
    
    public tbl_agenda(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
   public void SimpanAnggota(Integer ID1, Integer idpetugas, String judul, String jamawal, String jamakhir,
                               String tglawal, String tglakhir, String ket){
        try {
            // gunakan nama tabel yang benar
            String sql = "INSERT INTO tbl_agenda(id_agenda, id_petugas, judul, jam_awal, jam_akhir, "
                       + "tanggal_awal, tanggal_akhir, keterangan) "
                       + "VALUES('"+ID1+"', '"+idpetugas+"', '"+judul+"', '"+jamawal+"', '"+jamakhir+"', '"+tglawal+"', '"+tglakhir+"', '"+ket+"')";

            // cek apakah id sudah ada
            String cekPrimary = "SELECT * FROM tbl_agenda WHERE id_agenda = '"+ID1+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID petugas sudah terdaftar!");                
                this.VAR_idpetugas = data.getInt("id_petugas");
                this.VAR_judul = data.getString("judul");                
                this.VAR_jamawal = data.getString("jam_awal");
                this.VAR_jamakhir = data.getString("jam_akhir");         
                this.VAR_tanggalawal = data.getString("tanggal_awal");
                this.VAR_tanggalakhir = data.getString("tanggal_akhir");
                this.VAR_keterangan = data.getString("keterangan");
                this.validasi = true;
            } else {
                this.validasi = false;
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
   }
        
        public void ubahAnggota(Integer ID1, Integer idpetugas, String judul, String jamawal, 
                                String jamakhir, String tglawal, String tglakhir, String ket){
        try {
            // nama tabel dan kolom disesuaikan
            String sql = "UPDATE tbl_agenda SET id_petugas=?, judul=?, jam_awal=?, "
                    + "jam_akhir=?, tanggal_awal=?, tanggal_akhir=?, keterangan=? WHERE id_agenda=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, idpetugas);
            perintah.setString(2, judul);
            perintah.setString(3, jamawal);
            perintah.setString(4, jamakhir);
            perintah.setString(5, tglawal);
            perintah.setString(6, tglakhir);
            perintah.setString(7, ket);                       
            perintah.setInt(8, ID1);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }        
        
    public void hapusAnggota01(Integer ID1){
        try {
            // hapus dari tabel yang benar
            String sql = "DELETE FROM tbl_agenda WHERE id_agenda='"+ID1+"'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }         
    

    
        public void tampilDataAgenda(JTable komponenTable, String SQL){
            try {
              Statement perintah = koneksi.createStatement();
              ResultSet data = perintah.executeQuery(SQL);
              ResultSetMetaData meta = data.getMetaData();
              int jumKolom = meta.getColumnCount();
              DefaultTableModel modelTable = new DefaultTableModel();
              modelTable.addColumn("id_agenda");
              modelTable.addColumn("id_petugas");
              modelTable.addColumn("judul");
              modelTable.addColumn("jam_awal");
              modelTable.addColumn("jam_akhir");
              modelTable.addColumn("tanggal_awal");
              modelTable.addColumn("tanggal_akhir");
              modelTable.addColumn("keterangan");
              modelTable.getDataVector().clear();
              modelTable.fireTableDataChanged();
              while (data.next() ) {
                  Object[] row = new Object[jumKolom];
                  for(int i = 1; i <= jumKolom; i++ ){
                      row [i - 1] = data.getObject(i);
                  }
                  modelTable.addRow(row);
              }
              komponenTable.setModel(modelTable);
          } catch (Exception e) {
              
          }
        }    
    
    
     public void cetakLaporan(String fileLaporan, String SQL){
         try {
             File file = new File(fileLaporan);
             JasperDesign jasDes = JRXmlLoader.load(file);
             JRDesignQuery query = new JRDesignQuery();
             query.setText(SQL);
             jasDes.setQuery(query);
             JasperReport jr = JasperCompileManager.compileReport(jasDes);
             JasperPrint jp = JasperFillManager.fillReport(jr, null, this.koneksi);
             JasperViewer.viewReport(jp);
             
         } catch (Exception e) {
         }
     }
    
    
    
}
