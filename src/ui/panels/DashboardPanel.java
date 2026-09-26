package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardPanel extends JPanel {

    // Dark Theme Colors
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color titleColor = new Color(0, 230, 118);
    private final Color alertBg = new Color(45, 45, 45);
    private final Color alertTextColor = new Color(255, 204, 128);

    public DashboardPanel() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(backgroundColor);

        JLabel title = new JLabel("Dashboard Overview", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(titleColor);
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 15, 15));
        cards.setOpaque(false);

        String[] labels = {
            "Total Donors",
            "Total Donations",
            "Total Receivers",
            "Pickup Schedules",
            "Waste Records",
            "Food Categories"
        };

        String[] queries = {
            "SELECT COUNT(*) FROM Donors",
            "SELECT COUNT(*) FROM Donation",
            "SELECT COUNT(*) FROM Receiver",
            "SELECT COUNT(*) FROM PickupSchedule",
            "SELECT COUNT(*) FROM WasteRecords",
            "SELECT COUNT(*) FROM FoodCategory"
        };

        // Attractive Dark Gradient Colors
        Color[] colors = {
            new Color(46, 125, 50),    // Dark Green
            new Color(25, 118, 210),   // Dark Blue
            new Color(230, 81, 0),     // Dark Orange
            new Color(123, 31, 162),   // Dark Purple
            new Color(198, 40, 40),    // Dark Red
            new Color(0, 121, 107)     // Dark Teal
        };

        for (int i = 0; i < labels.length; i++) {
            cards.add(createCard(labels[i], queries[i], colors[i]));
        }

        add(cards, BorderLayout.CENTER);

        // Expiring Donations Alert Panel
        JPanel alert = new JPanel(new BorderLayout());
        alert.setBackground(alertBg);

        alert.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 167, 38), 2),
                "⚠ Donations Expiring in 3 Days",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(255, 167, 38)
        ));

        JTextArea alertText = new JTextArea(4, 40);
        alertText.setEditable(false);
        alertText.setBackground(alertBg);
        alertText.setForeground(alertTextColor);
        alertText.setFont(new Font("Consolas", Font.PLAIN, 13));

        loadExpiringDonations(alertText);

        JScrollPane scroll = new JScrollPane(alertText);
        scroll.setBorder(null);

        alert.add(scroll);

        add(alert, BorderLayout.SOUTH);
    }

    private JPanel createCard(String label, String query, Color color) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(color);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel num = new JLabel("...", SwingConstants.CENTER);
        num.setFont(new Font("Segoe UI", Font.BOLD, 38));
        num.setForeground(Color.WHITE);

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(240, 240, 240));

        try (
                Connection c = DBConnection.getConnection();
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery(query)
        ) {

            if (r.next()) {
                num.setText(String.valueOf(r.getInt(1)));
            }

        } catch (Exception e) {

            num.setText("N/A");
        }

        card.add(num, BorderLayout.CENTER);
        card.add(lbl, BorderLayout.SOUTH);

        return card;
    }

    private void loadExpiringDonations(JTextArea area) {

        StringBuilder sb = new StringBuilder();

        String sql =
                "SELECT donation_id, food_id, expiry_time " +
                "FROM Donation " +
                "WHERE expiry_time <= CURDATE() + INTERVAL 3 DAY";

        try (
                Connection c = DBConnection.getConnection();
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery(sql)
        ) {

            while (r.next()) {

                sb.append("Donation ID: ")
                  .append(r.getInt(1))
                  .append(" | Food ID: ")
                  .append(r.getInt(2))
                  .append(" | Expiry: ")
                  .append(r.getDate(3))
                  .append("\n");
            }

            if (sb.isEmpty()) {
                sb.append("No donations expiring soon.");
            }

        } catch (Exception e) {

            sb.append("DB Error: ").append(e.getMessage());
        }

        area.setText(sb.toString());
    }
}