package pims.config.view;

import pims.config.view.AdminDashboard;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private JButton btnLogout;

    public AdminDashboard(String FullName) {
        setTitle("HealthFirst PIMS - Administrator");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Administrator Dashboard", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel welcomeLabel = new JLabel("Welcome, " + FullName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        btnLogout = new JButton("Logout");
        btnLogout.setPreferredSize(new Dimension(90,25));
        btnLogout.addActionListener(e -> logout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Medicines", new MedicinePanel());
        tabs.addTab("Suppliers",new SupplierPanel());
        tabs.addTab("Users", new UserManagementPanel());
        tabs.addTab("Reports", new ReportsPanel());

        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel textPanel = new JPanel(new GridLayout(2,1));
        textPanel.add(heading);
        textPanel.add(welcomeLabel);
        headerPanel.add(textPanel, BorderLayout.CENTER);

        //Logout button resize
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10,10));
        btnLogout.setPreferredSize(new Dimension(90,28));
        btnLogout.setFocusPainted(false);
        logoutPanel.add(btnLogout);

        headerPanel.add(logoutPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
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