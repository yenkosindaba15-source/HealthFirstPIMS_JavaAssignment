package pims.config.view;

import pims.config.view.AdminDashboard;
import pims.config.view.SupplierPanel;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard(String FullName) {
        setTitle("HealthFirst PIMS - Administrator");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Administrator Dashboard", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel welcomeLabel = new JLabel("Welcome, " + FullName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Medicines", new MedicinePanel());
        tabs.addTab("Suppliers",new SupplierPanel());
        tabs.addTab("Users", new UserManagementPanel());
        tabs.addTab("Reports", new ReportsPanel());

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.add(heading);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}