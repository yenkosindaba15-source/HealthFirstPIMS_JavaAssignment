package pims.config.dao;

import pims.config.DBConnection;
import java.sql.*;


public class SalesDAO {
    public int createSale(double totalAmount, int userId) throws Exception {
        String sql = """
                INSERT INTO sales
                (total_amount, user_id)
                VALUES (?, ?)
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setDouble(1, totalAmount);
            ps.setInt(2, userId);

            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();

            if(rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }

    public void addSaleItem(int saleId, int medicineId, int quantity, double price) throws Exception {
        String sql = """
                INSERT INTO sale_items
                (
                    sale_id,
                    medicine_id,
                    quantity_sold,
                    price_at_sale
                )
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, saleId);
            ps.setInt(2, medicineId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);

            ps.executeUpdate();
        }
    }

    public void reduceStock(int medicineId, int quantity) throws Exception {
        String sql = """
                UPDATE medicines
                SET quantity_in_stock =
                    quantity_in_stock - ?
                WHERE medicine_id = ?
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, quantity);
            ps.setInt(2, medicineId);

            ps.executeUpdate();
        }
    }
}