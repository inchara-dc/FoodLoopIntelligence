package ui.panels;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ReportsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Reports & Analytics", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(34, 139, 34));
        add(title, BorderLayout.NORTH);

        // Report buttons
        JPanel btnPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createTitledBorder("Select Report"));

        String[][] reports = {
            {"Donor Donation Count",     "SELECT D.D_NAME AS Donor, COUNT(DO.donation_id) AS Total_Donations FROM Donors D JOIN Donation DO ON D.D_ID = DO.donor_id GROUP BY D.D_NAME"},
            {"All Donations with Donor", "SELECT D.D_NAME, DO.food_id, DO.quantity, DO.donation_time FROM Donors D JOIN Donation DO ON D.D_ID = DO.donor_id"},
            {"Expiring in 3 Days",       "SELECT donation_id, food_id, expiry_time FROM Donation WHERE expiry_time <= CURDATE() + INTERVAL 3 DAY"},
            {"High Urgency Receivers",   "SELECT receiver_name, organization, urgency_score FROM Receiver WHERE urgency_score >= 4 ORDER BY urgency_score DESC"},
            {"Total Food Donated",       "SELECT SUM(quantity) AS Total_Food_Donated FROM Donation"},
            {"All Waste Records",        "SELECT donation_id, reason, discarded_time FROM WasteRecords"},
            {"Pickup Schedule",          "SELECT volunteer_name, pickup_time, status FROM PickupSchedule ORDER BY pickup_time"},
            {"All Food Items & Category", "SELECT fi.food_name, fi.unit, fi.perishability_index, fc.category_name FROM FoodItems fi JOIN FoodCategory fc ON fi.category_id = fc.category_id"},
            {"Completed Pickups",        "SELECT pickup_id, volunteer_name, pickup_time FROM PickupSchedule WHERE status = 'Completed'"}
        };

        Color[] colors = {
            new Color(63, 81, 181), new Color(0, 150, 136), new Color(255, 152, 0),
            new Color(244, 67, 54), new Color(76, 175, 80), new Color(121, 85, 72),
            new Color(33, 150, 243), new Color(156, 39, 176), new Color(0, 188, 212)
        };

        for (int i = 0; i < reports.length; i++) {
            final String sql = reports[i][1];

            JButton btn = new JButton("<html><center>" + reports[i][0] + "</center></html>");

            btn.setBackground(colors[i]);

            // CHANGED TEXT COLOR TO BLACK
            btn.setForeground(Color.BLACK);

            btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addActionListener(e -> runReport(sql));

            btnPanel.add(btn);
        }

        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(34, 139, 34));
        table.getTableHeader().setForeground(Color.WHITE);

        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(200, 230, 200));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Results"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, btnPanel, scroll);
        split.setDividerLocation(160);

        add(split, BorderLayout.CENTER);
    }

    private void runReport(String sql) {

        model.setRowCount(0);
        model.setColumnCount(0);

        try (
            Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery(sql)
        ) {

            ResultSetMetaData meta = r.getMetaData();
            int cols = meta.getColumnCount();

            for (int i = 1; i <= cols; i++) {
                model.addColumn(meta.getColumnName(i));
            }

            while (r.next()) {

                Object[] row = new Object[cols];

                for (int i = 0; i < cols; i++) {
                    row[i] = r.getObject(i + 1);
                }

                model.addRow(row);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "DB Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}