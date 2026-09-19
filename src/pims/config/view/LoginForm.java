package pims.config.view;

import pims.config.dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;

    public LoginForm() {
        setTitle("HealthFirst PIMS - Login");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel heading = new JLabel("HealthFirst Pharmacy", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        txtUsername = new JTextField(18);
        txtPassword = new JPasswordField(18);
        btnLogin = new JButton("Login");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Username:"), constraints);

        constraints.gridx = 1;
        formPanel.add(txtUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Password:"), constraints);

        constraints.gridx = 1;
        formPanel.add(txtPassword, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        formPanel.add(btnLogin, constraints);

        add(heading, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(event -> performLogin());
        txtPassword.addActionListener(event -> performLogin());
    }

    //logging in both admin and cashier profiles
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        btnLogin.setEnabled(false);

        try {
            UserDAO dao = new UserDAO();
            UserDAO.UserLoginResult user = dao.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);

                txtPassword.setText("");
                txtPassword.requestFocus();
                return;
            }

            if ("Admin".equalsIgnoreCase(user.role())) {
                new AdminDashboard(user.fullName()).setVisible(true);
            } else if ("Cashier".equalsIgnoreCase(user.role())) {
                new CashierDashboard(user.fullName()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "The account has an unsupported role.", "Role Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Database error: " + exception.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnLogin.setEnabled(true);
        }
    }
}