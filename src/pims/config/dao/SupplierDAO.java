package pims.config.dao;

import pims.config.DBConnection;
import java.sql.*;


public class SupplierDAO {
    public void addSupplier(String name, String contact, String phone, String email, String address) throws Exception{
        String sql = "INSERT INTO suppliers " + "(name, contact_person, phone, email, address) " + "VALUES (?,?,?,?,?)";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, address);

            ps.executeUpdate();
        }
    }

    public ResultSet getAllSuppliers() throws Exception{
        String sql = "SELECT * FROM suppliers " + "ORDER BY supplier_id";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
    }
    public void updateSupplier(int supplierId, String name, String contact, String phone, String email, String address) throws Exception {
        String sql =
                """
                UPDATE suppliers
                SET name=?,
                    contact_person=?,
                    phone=?,
                    email=?,
                    address=?
                WHERE supplier_id=?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, address);
            ps.setInt(6, supplierId);

            ps.executeUpdate();
        }
    }
    public void deleteSupplier(int supplierId) throws Exception{
        String sql =
                """
                DELETE FROM suppliers
                WHERE supplier_id=?
                """;
        try(
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setInt(1, supplierId);

            ps.executeUpdate();
        }
    }
}
