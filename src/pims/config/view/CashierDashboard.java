package pims.config.view;

import pims.config.view.CashierPanel;
import javax.swing.*;
import java.awt.*;

public class CashierDashboard extends JFrame {

    public CashierDashboard(String FullName) {
        setTitle("HealthFirst PIMS - Cashier");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Cashier Point of Sale", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel welcomeLabel = new JLabel("Welcome, " + FullName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.add(heading);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);
        add(new CashierPanel(), BorderLayout.CENTER);
    }
}