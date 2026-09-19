package pims.config.view;

import pims.config.dao.MedicineDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MedicinePanel extends JPanel {
    private JTextField txtName;
    private JTextField txtCompany;
    private JTextField txtType;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JTextField txtReorder;
    private JTextField txtExpiry;
    private JTextField txtSupplierId;

    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;

    private JTable table;
    private DefaultTableModel tableModel;

    private int selectedMedicineId = -1;

    public MedicinePanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        txtName = new JTextField();
        txtCompany = new JTextField();
        txtType = new JTextField();
        txtPrice = new JTextField();
        txtStock = new JTextField();
        txtReorder = new JTextField();
        txtExpiry = new JTextField();
        txtSupplierId = new JTextField();

        btnSave = new JButton("Add Medicine");
        btnUpdate = new JButton("Update Medicine");
        btnDelete = new JButton("Delete Medicine");

        formPanel.add(new JLabel("Medicine Name"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Company"));
        formPanel.add(txtCompany);

        formPanel.add(new JLabel("Type"));
        formPanel.add(txtType);

        formPanel.add(new JLabel("Price"));
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Stock"));
        formPanel.add(txtStock);

        formPanel.add(new JLabel("Reorder Level"));
        formPanel.add(txtReorder);

        formPanel.add(new JLabel("Expiry (yyyy-mm-dd)"));
        formPanel.add(txtExpiry);

        formPanel.add(new JLabel("Supplier ID"));
        formPanel.add(txtSupplierId);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Company");
        tableModel.addColumn("Type");
        tableModel.addColumn("Price");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Reorder");
        tableModel.addColumn("Expiry");
        tableModel.addColumn("Supplier ID");

        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadMedicines();

        table.getSelectionModel().addListSelectionListener(e -> {
                    int row = table.getSelectedRow();

                    if (row >= 0) {
                        selectedMedicineId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        txtName.setText(tableModel.getValueAt(row, 1).toString());
                        txtCompany.setText(tableModel.getValueAt(row, 2).toString());
                        txtType.setText(tableModel.getValueAt(row, 3).toString());
                        txtPrice.setText(tableModel.getValueAt(row, 4).toString());
                        txtStock.setText(tableModel.getValueAt(row, 5).toString());
                        txtReorder.setText(tableModel.getValueAt(row, 6).toString());
                        txtExpiry.setText(tableModel.getValueAt(row, 7).toString());
                        txtSupplierId.setText(tableModel.getValueAt(row, 8).toString());
                    }
                }
        );

        btnSave.addActionListener(e -> saveMedicine());
        btnUpdate.addActionListener(e -> updateMedicine());
        btnDelete.addActionListener(e -> deleteMedicine());
    }

    //adding medicine
    private void saveMedicine() {
        try {
            MedicineDAO dao = new MedicineDAO();
            dao.addMedicine(txtName.getText(), txtCompany.getText(), txtType.getText(), Double.parseDouble(txtPrice.getText()), Integer.parseInt(txtStock.getText()), Integer.parseInt(txtReorder.getText()), txtExpiry.getText(), Integer.parseInt(txtSupplierId.getText()));
            JOptionPane.showMessageDialog(this, "Medicine added successfully.");

            clearFields();
            loadMedicines();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    //updating medicine
    private void updateMedicine() {
        if (selectedMedicineId == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine first.");
            return;
        }
        try {
            MedicineDAO dao = new MedicineDAO();
            dao.updateMedicine(selectedMedicineId, txtName.getText(), txtCompany.getText(), txtType.getText(), Double.parseDouble(txtPrice.getText()), Integer.parseInt(txtStock.getText()), Integer.parseInt(txtReorder.getText()), txtExpiry.getText(), Integer.parseInt(txtSupplierId.getText()));

            JOptionPane.showMessageDialog(this, "Medicine updated.");

            loadMedicines();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());

        }
    }

    //deleting medicine
    private void deleteMedicine() {
        if (selectedMedicineId == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine first.");
            return;
        }

        try {
            MedicineDAO dao = new MedicineDAO();
            dao.deleteMedicine(selectedMedicineId);

            JOptionPane.showMessageDialog(this, "Medicine deleted.");

            clearFields();
            loadMedicines();

            selectedMedicineId = -1;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    private void loadMedicines() {
        try {
            tableModel.setRowCount(0);

            MedicineDAO dao = new MedicineDAO();
            ResultSet rs = dao.getAllMedicines();

            while (rs.next()) {
                tableModel.addRow(
                        new Object[]{
                                rs.getInt("medicine_id"),
                                rs.getString("name"),
                                rs.getString("company"),
                                rs.getString("medicine_type"),
                                rs.getDouble("price"),
                                rs.getInt("quantity_in_stock"),
                                rs.getInt("reorder_level"),
                                rs.getDate("expiry_date"),
                                rs.getInt("supplier_id")
                        });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    private void clearFields() {
        txtName.setText("");
        txtCompany.setText("");
        txtType.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        txtReorder.setText("");
        txtExpiry.setText("");
        txtSupplierId.setText("");

        txtName.requestFocus();
    }
}