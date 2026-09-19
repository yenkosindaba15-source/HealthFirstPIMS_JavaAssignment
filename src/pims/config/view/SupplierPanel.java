package pims.config.view;

import pims.config.dao.SupplierDAO;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class SupplierPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;

    private int selectedSupplierId = -1;

    private JTextField txtName;
    private JTextField txtContact;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextArea txtAddress;

    public SupplierPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        txtName = new JTextField();
        txtContact = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();

        txtAddress = new JTextArea(3, 20);

        btnSave = new JButton("Save Supplier");
        btnUpdate = new JButton("Update Supplier");
        btnDelete = new JButton("Delete Supplier");

        formPanel.add(new JLabel("Supplier Name"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Contact Person"));
        formPanel.add(txtContact);

        formPanel.add(new JLabel("Phone"));
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Address"));
        formPanel.add(new JScrollPane(txtAddress));

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
        tableModel.addColumn("Contact");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Email");

        table = new JTable(tableModel);

        add( new JScrollPane(table), BorderLayout.CENTER);
        loadSuppliers();

        table.getSelectionModel().addListSelectionListener(
                e -> {
                    int row = table.getSelectedRow();
                    if(row >= 0){
                        selectedSupplierId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        txtName.setText(tableModel.getValueAt(row,1).toString());
                        txtContact.setText(tableModel.getValueAt(row,2).toString());
                        txtPhone.setText(tableModel.getValueAt(row,3).toString());
                        txtEmail.setText(tableModel.getValueAt(row,4).toString());
                    }
                }
        );
        btnSave.addActionListener(e -> saveSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
    }

    private void saveSupplier() {
        try {
            String name = txtName.getText().trim();
            String contact = txtContact.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String address = txtAddress.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Supplier name is required.");
                return;
            }
            SupplierDAO dao = new SupplierDAO();
            dao.addSupplier(name, contact, phone, email, address);

            JOptionPane.showMessageDialog(this, "Supplier added successfully.");
            clearFields();
            loadSuppliers();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateSupplier(){
        if(selectedSupplierId == -1){
            JOptionPane.showMessageDialog(this, "Select a supplier first.");
            return;
        }
        try{
            SupplierDAO dao = new SupplierDAO();
            dao.updateSupplier(selectedSupplierId, txtName.getText(), txtContact.getText(), txtPhone.getText(), txtEmail.getText(), txtAddress.getText());
            JOptionPane.showMessageDialog(this, "Supplier Updated.");

            loadSuppliers();
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteSupplier(){
        if(selectedSupplierId == -1){
            JOptionPane.showMessageDialog(this,"Select a supplier first.");
            return;
        }
        try{
            SupplierDAO dao = new SupplierDAO();
            dao.deleteSupplier(selectedSupplierId);
            JOptionPane.showMessageDialog(this, "Supplier deleted.");

            clearFields();
            loadSuppliers();

            selectedSupplierId = -1;
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void loadSuppliers(){
        try{
            tableModel.setRowCount(0);
            SupplierDAO dao = new SupplierDAO();
            ResultSet rs = dao.getAllSuppliers();

            while (rs.next()){
                tableModel.addRow(new Object[]{
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_person"),
                        rs.getString("phone"),
                        rs.getString("email"),
                });
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void clearFields() {

        txtName.setText("");
        txtContact.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");

        txtName.requestFocus();
    }
}