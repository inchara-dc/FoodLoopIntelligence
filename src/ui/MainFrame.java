package ui;

import javax.swing.*;
import java.awt.*;
import ui.panels.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("FOOD LOOP - Food Donation Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1100, 700);

        setLocationRelativeTo(null);

        // Main Background
        getContentPane().setBackground(
                new Color(24, 24, 24)
        );

        // Header
        JLabel header = new JLabel(
                " FOOD LOOP",
                SwingConstants.LEFT
        );

        header.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        header.setForeground(Color.WHITE);

        header.setOpaque(true);

        // Dark Modern Green
        header.setBackground(
                new Color(27, 94, 32)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        add(header, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();

        tabs.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        // Dark Theme Colors
        tabs.setBackground(
                new Color(38, 38, 38)
        );

        tabs.setForeground(Color.BLACK);

        tabs.setOpaque(true);

        // Tabs
        tabs.addTab(
                " Dashboard",
                new DashboardPanel()
        );

        tabs.addTab(
                " Donors",
                new DonorsPanel()
        );

        tabs.addTab(
                " Food Items",
                new FoodItemsPanel()
        );

        tabs.addTab(
                " Food Category",
                new FoodCategoryPanel()
        );

        tabs.addTab(
                "Receivers",
                new ReceiversPanel()
        );

        tabs.addTab(
                " Donations",
                new DonationsPanel()
        );

        tabs.addTab(
                "Pickup",
                new PickupPanel()
        );

        tabs.addTab(
                "Waste Records",
                new WastePanel()
        );

        tabs.addTab(
                " Reports",
                new ReportsPanel()
        );

        add(tabs, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
                "  Food Loop © 2025 | Reducing Food Waste, Fighting Hunger",
                SwingConstants.LEFT
        );

        footer.setFont(
                new Font("Segoe UI", Font.ITALIC, 12)
        );

        footer.setForeground(
                new Color(200, 200, 200)
        );

        footer.setOpaque(true);

        footer.setBackground(
                new Color(33, 33, 33)
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        add(footer, BorderLayout.SOUTH);

        // UI Styling for Better Dark Theme
        UIManager.put(
                "TabbedPane.selected",
                new Color(0, 150, 136)
        );

        UIManager.put(
                "TabbedPane.contentAreaColor",
                new Color(30, 30, 30)
        );

        UIManager.put(
                "TabbedPane.focus",
                new Color(0, 150, 136)
        );

        UIManager.put(
                "TabbedPane.light",
                new Color(45, 45, 45)
        );

        UIManager.put(
                "TabbedPane.highlight",
                new Color(45, 45, 45)
        );

        UIManager.put(
                "TabbedPane.shadow",
                new Color(20, 20, 20)
        );

        UIManager.put(
                "TabbedPane.darkShadow",
                new Color(10, 10, 10)
        );
    }
}