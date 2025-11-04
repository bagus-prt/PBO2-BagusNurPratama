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

public class tbl_petugas {
    private String namadb = "pbo2_2310010457";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;
    public Integer VAR_noKtp = null;
    public String VAR_nama = null;
    public String VAR_alamat = null;
    public Integer VAR_noHp = null;
    public String VAR_username = null;
    public String VAR_password = null;
    public boolean validasi = false;

    public tbl_petugas(){
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }

    public void SimpanAnggota(Integer ID, Integer noKtp, String nama, String alamat,
                              Integer noHp, String username, String password){
        try {
            // gunakan nama tabel yang benar
            String sql = "INSERT INTO tbl_petugas(id_petugas, no_ktp, nama, alamat, no_hp, username, password) "
                       + "VALUES('"+ID+"', '"+noKtp+"', '"+nama+"', '"+alamat+"', '"+noHp+"', '"+username+"', '"+password+"')";

            // cek apakah id sudah ada
            String cekPrimary = "SELECT * FROM tbl_petugas WHERE id_petugas = '"+ID+"'";

            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID petugas sudah terdaftar!");
                this.VAR_noKtp = data.getInt("no_ktp");
                this.VAR_nama = data.getString("nama");
                this.VAR_alamat = data.getString("alamat");
                this.VAR_noHp = data.getInt("no_hp");
                this.VAR_username = data.getString("username");
                this.VAR_password = data.getString("password");
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

    public void ubahAnggota(Integer ID, Integer noKtp, String nama, String alamat,
                            Integer noHp, String username, String password){
        try {
            // nama tabel dan kolom disesuaikan
            String sql = "UPDATE tbl_petugas SET no_ktp=?, nama=?, alamat=?, no_hp=?, username=?, password=? WHERE id_petugas=?";

            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, noKtp);
            perintah.setString(2, nama);
            perintah.setString(3, alamat);
            perintah.setInt(4, noHp);
            perintah.setString(5, username);
            perintah.setString(6, password);
            perintah.setInt(7, ID);
            perintah.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void hapusAnggota01(Integer ID){
        try {
            // hapus dari tabel yang benar
            String sql = "DELETE FROM tbl_petugas WHERE id_petugas='"+ID+"'";

            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public ResultSet getDataPetugas() {
    ResultSet rs = null;
    try {
        String sql = "SELECT id_petugas, nama FROM tbl_petugas";
        Statement perintah = koneksi.createStatement();
        rs = perintah.executeQuery(sql);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mengambil data petugas: " + e.getMessage());
    }
        return rs;
    }
    
        public void tampilDataAgenda(JTable komponenTable, String SQL){
            try {
              Statement perintah = koneksi.createStatement();
              ResultSet data = perintah.executeQuery(SQL);
              ResultSetMetaData meta = data.getMetaData();
              int jumKolom = meta.getColumnCount();
              DefaultTableModel modelTable = new DefaultTableModel();              
              modelTable.addColumn("id_petugas");
              modelTable.addColumn("no_ktp");
              modelTable.addColumn("nama");
              modelTable.addColumn("alamat");
              modelTable.addColumn("no_hp");
              modelTable.addColumn("username");
              modelTable.addColumn("password");
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
    
    
    
}
