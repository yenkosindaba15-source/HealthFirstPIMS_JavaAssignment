package pims.config.view;

import pims.config.view.CashierPanel;
import javax.swing.*;
import java.awt.*;

public class CashierDashboard extends JFrame {
    private JButton btnLogout;

    public CashierDashboard(int userId, String FullName) {
        setTitle("HealthFirst PIMS - Cashier");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Cashier Point of Sale", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel welcomeLabel = new JLabel("Welcome, " + FullName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel textPanel = new JPanel(new GridLayout(2,1));
        textPanel.add(heading);
        textPanel.add(welcomeLabel);
        headerPanel.add(textPanel, BorderLayout.CENTER);

        //Logout button resize
        btnLogout = new JButton("Logout");
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10,10));
        btnLogout.setPreferredSize(new Dimension(90,28));
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> logout());
        logoutPanel.add(btnLogout);

        headerPanel.add(logoutPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(new CashierPanel(userId), BorderLayout.CENTER);

        btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());
    }

    //Logout button function
    private void logout(){
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_NO_OPTION){
            dispose();

            new LoginForm().setVisible(true);
        }
    }
}