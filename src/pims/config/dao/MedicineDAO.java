package pims.config.dao;

import pims.config.DBConnection;
import java.sql.*;

public class MedicineDAO {
    public void addMedicine(String name, String company, String type, double price, int stock, int reorder, String expiry, int supplierId) throws Exception{
        String sql = "INSERT INTO medicines " + "(name, company, medicine_type,price,quantity_in_stock,reorder_level,expiry_date,supplier_id) " + "VALUES(?,?,?,?,?,?,?,?)";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, company);
            ps.setString(3, type);
            ps.setDouble(4, price);
            ps.setInt(5, stock);
            ps.setInt(6, reorder);
            ps.setDate(7, java.sql.Date.valueOf(expiry));
            ps.setInt(8, supplierId);

            ps.executeUpdate();
        }
    }
    public ResultSet getAllMedicines() throws Exception {

        String sql =
                """
                SELECT *
                FROM medicines
                ORDER BY medicine_id
                """;

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
    }
    public void updateMedicine(int medicineId, String name, String company, String type, double price, int stock, int reorder, String expiry, int supplierId) throws Exception {
        String sql =
                """
                UPDATE medicines
                SET name=?,
                    company=?,
                    medicine_type=?,
                    price=?,
                    quantity_in_stock=?,
                    reorder_level=?,
                    expiry_date=?,
                    supplier_id=?
                WHERE medicine_id=?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, company);
            ps.setString(3, type);
            ps.setDouble(4, price);
            ps.setInt(5, stock);
            ps.setInt(6, reorder);
            ps.setDate(7, java.sql.Date.valueOf(expiry));
            ps.setInt(8, supplierId);
            ps.setInt(9, medicineId);

            ps.executeUpdate();
        }
    }
    public void deleteMedicine(int medicineId) throws Exception {
        String sql = """
            DELETE FROM medicines
            WHERE medicine_id=?
            """;
        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, medicineId);

            ps.executeUpdate();
        }
    }

}
