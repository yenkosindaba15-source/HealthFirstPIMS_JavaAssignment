package pims.config.view;

import pims.config.dao.ReportDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsPanel extends JPanel {
    private final JTabbedPane reportTabs;

    private final JTable salesTable;
    private final JTable itemSalesTable;
    private final JTable lowStockTable;
    private final JTable expiryTable;

    private final DefaultTableModel salesModel;
    private final DefaultTableModel itemSalesModel;
    private final DefaultTableModel lowStockModel;
    private final DefaultTableModel expiryModel;

    private final JButton btnRefreshAll;

    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("Pharmacy Reports", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 22));

        btnRefreshAll = new JButton("Refresh All Reports");

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(heading, BorderLayout.CENTER);
        headerPanel.add(btnRefreshAll, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        reportTabs = new JTabbedPane();

        salesModel = createReadOnlyModel(
                new String[]{
                        "Sale ID",
                        "Sale Date",
                        "Total Amount",
                        "Cashier"
                }
        );

        salesTable = new JTable(salesModel);

        reportTabs.addTab("Sales", createTablePanel(salesTable, "Completed Sales"));

        itemSalesModel = createReadOnlyModel(
                new String[]{
                        "Medicine ID",
                        "Medicine",
                        "Quantity Sold",
                        "Sales Amount"
                }
        );

        itemSalesTable = new JTable(itemSalesModel);

        reportTabs.addTab("Item-Wise Sales", createTablePanel(itemSalesTable, "Item-Wise Sales Summary"));

        lowStockModel = createReadOnlyModel(
                new String[]{
                        "Medicine ID",
                        "Medicine",
                        "Company",
                        "Current Stock",
                        "Reorder Level"
                }
        );

        lowStockTable = new JTable(lowStockModel);

        reportTabs.addTab("Low Stock", createTablePanel(lowStockTable, "Medicines Requiring Reorder"));

        expiryModel = createReadOnlyModel(
                new String[]{
                        "Medicine ID",
                        "Medicine",
                        "Company",
                        "Expiry Date",
                        "Current Stock"
                }
        );

        expiryTable = new JTable(expiryModel);

        reportTabs.addTab("Expiry", createTablePanel(expiryTable, "Expired or Soon-To-Expire Medicines"));
        add(reportTabs, BorderLayout.CENTER);

        btnRefreshAll.addActionListener(e -> loadAllReports());
        loadAllReports();
    }
    private DefaultTableModel createReadOnlyModel(String[] columnNames) {
        return new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel createTablePanel(JTable table, String title) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadAllReports() {
        loadSalesReport();
        loadItemWiseSalesReport();
        loadLowStockReport();
        loadExpiryReport();
    }

    private void loadSalesReport() {
        salesModel.setRowCount(0);

        try {
            ReportDAO dao = new ReportDAO();
            ResultSet rs = dao.getSalesReport();

            while (rs.next()) {
                salesModel.addRow(
                        new Object[]{
                                rs.getInt("sale_id"),
                                rs.getTimestamp("sale_date"),
                                String.format("R%.2f", rs.getDouble("total_amount")),
                                rs.getString("cashier_name")
                        }
                );
            }

        } catch (Exception ex) {
            showError("Unable to load sales report.", ex);
        }
    }

    private void loadItemWiseSalesReport() {
        itemSalesModel.setRowCount(0);

        try {
            ReportDAO dao = new ReportDAO();
            ResultSet rs = dao.getItemWiseSalesReport();

            while (rs.next()) {
                itemSalesModel.addRow(
                        new Object[]{
                                rs.getInt("medicine_id"),
                                rs.getString("medicine_name"),
                                rs.getInt("total_quantity_sold"),
                                String.format("R%.2f", rs.getDouble("total_sales_amount"))
                        }
                );
            }

        } catch (Exception ex) {
            showError("Unable to load item-wise sales report.", ex);
        }
    }

    private void loadLowStockReport() {
        lowStockModel.setRowCount(0);

        try {
            ReportDAO dao = new ReportDAO();
            ResultSet rs = dao.getLowStockReport();

            while (rs.next()) {
                lowStockModel.addRow(
                        new Object[]{
                                rs.getInt("medicine_id"),
                                rs.getString("name"),
                                rs.getString("company"),
                                rs.getInt("quantity_in_stock"),
                                rs.getInt("reorder_level")
                        });
            }

        } catch (Exception ex) {
            showError("Unable to load low-stock report.", ex);
        }
    }

    private void loadExpiryReport() {
        expiryModel.setRowCount(0);

        try {
            ReportDAO dao = new ReportDAO();
            ResultSet rs = dao.getExpiryReport();

            while (rs.next()) {
                expiryModel.addRow(
                        new Object[]{
                                rs.getInt("medicine_id"),
                                rs.getString("name"),
                                rs.getString("company"),
                                rs.getDate("expiry_date"),
                                rs.getInt(
                                        "quantity_in_stock"
                                )});
            }

        } catch (Exception ex) {
            showError("Unable to load expiry report.", ex);
        }
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(this, message + "\n" + ex.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
    }
}