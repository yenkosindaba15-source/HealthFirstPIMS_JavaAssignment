package pims.config.view;

import pims.config.dao.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManagementPanel extends JPanel {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JTextField txtFullName;
    private final JComboBox<String> cmbRole;

    private final JButton btnSave;
    private final JButton btnUpdate;
    private final JButton btnDelete;
    private final JButton btnClear;

    private final JTable table;
    private final DefaultTableModel tableModel;

    private int selectedUserId = -1;
    private String selectedUsername = "";

    public UserManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("User Management", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 22));

        add(heading, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtFullName = new JTextField();

        cmbRole = new JComboBox<>(new String[]{"Admin", "Cashier"});

        btnSave = new JButton("Add User");
        btnUpdate = new JButton("Update User");
        btnDelete = new JButton("Delete User");
        btnClear = new JButton("Clear");

        formPanel.add(new JLabel("Username"));
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Role"));
        formPanel.add(cmbRole);
        formPanel.add(new JLabel("Full Name"));
        formPanel.add(txtFullName);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);

        contentPanel.add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{
                "User ID",
                "Username",
                "Role",
                "Full Name"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Users"));

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedUser();
            }
        });

        btnSave.addActionListener(e -> saveUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearFields());

        loadUsers();
    }

    private void loadSelectedUser() {
        int viewRow = table.getSelectedRow();

        if (viewRow == -1) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);

        selectedUserId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
        selectedUsername = tableModel.getValueAt(modelRow, 1).toString();
        txtUsername.setText(selectedUsername);
        cmbRole.setSelectedItem(tableModel.getValueAt(modelRow, 2));
        txtFullName.setText(tableModel.getValueAt(modelRow, 3).toString());
        txtPassword.setText("");
    }

    private void saveUser() {
        try {
            String username = txtUsername.getText().trim().toLowerCase();
            String password = new String(txtPassword.getPassword());
            String role = cmbRole.getSelectedItem().toString();
            String fullName = txtFullName.getText().trim();

            if(username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill in all fields.");
                return;
            }

            UserDAO dao = new UserDAO();
            dao.addUser(username, password, role, fullName);

            JOptionPane.showMessageDialog(this, "User added successfully.");

            clearFields();
            loadUsers();

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateUser() {
        if(selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            dao.updateUser(selectedUserId, txtUsername.getText().trim().toLowerCase(), new String(txtPassword.getPassword()), cmbRole.getSelectedItem().toString(), txtFullName.getText().trim());

            JOptionPane.showMessageDialog(this, "User updated successfully.");

            clearFields();
            loadUsers();

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteUser() {
        if(selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }

        if(selectedUsername.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "Admin account cannot be deleted.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Delete selected user?", "Confirm", JOptionPane.YES_NO_OPTION);

        if(option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            dao.deleteUser(selectedUserId);

            JOptionPane.showMessageDialog(this, "User deleted successfully.");

            clearFields();
            loadUsers();

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void loadUsers() {
        try {
            tableModel.setRowCount(0);

            UserDAO dao = new UserDAO();

            for(UserDAO.UserListItem user : dao.getAllUsers()) {
                tableModel.addRow(
                        new Object[]{
                                user.userId(),
                                user.username(),
                                user.role(),
                                user.fullName()
                        }
                );
            }

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");

        cmbRole.setSelectedIndex(0);

        selectedUserId = -1;
        selectedUsername = "";

        table.clearSelection();

        txtUsername.requestFocus();
    }
}