package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DonationsPanel extends BaseCRUDPanel {

    private JTextField tfId, tfDonor, tfFood, tfQty, tfDate, tfExpiry;

    protected String getFormTitle() {

        return "Manage Donations";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);

        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId     = addField(p, "Donation ID",                  g, 0);

        tfDonor  = addField(p, "Donor ID",                     g, 1);

        tfFood   = addField(p, "Food ID",                      g, 2);

        tfQty    = addField(p, "Quantity",                     g, 3);

        tfDate   = addField(p, "Donation Date (YYYY-MM-DD)",  g, 4);

        tfExpiry = addField(p, "Expiry Date (YYYY-MM-DD)",    g, 5);

        // Buttons Panel
        JPanel btns = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        btns.setOpaque(false);

        // Dark Theme Buttons
        JButton add = makeBtn(
                "➕ Add",
                new Color(0, 150, 136)      // Dark Teal
        );

        JButton del = makeBtn(
                "🗑 Delete",
                new Color(198, 40, 40)      // Dark Red
        );

        JButton clr = makeBtn(
                "🔄 Clear",
                new Color(55, 71, 79)       // Dark Blue Grey
        );

        JButton expiring = makeBtn(
                "⚠️ Expiring Soon",
                new Color(255, 140, 0)      // Deep Orange
        );

        // Visible White Text
        add.setForeground(Color.WHITE);

        del.setForeground(Color.WHITE);

        clr.setForeground(Color.WHITE);

        expiring.setForeground(Color.WHITE);

        // Extra Button Styling
        JButton[] buttons = {
                add,
                del,
                clr,
                expiring
        };

        for (JButton b : buttons) {

            b.setFont(new Font("Segoe UI", Font.BOLD, 14));

            b.setFocusPainted(false);

            b.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            b.setBorder(
                    BorderFactory.createEmptyBorder(
                            10, 20, 10, 20
                    )
            );

            b.setOpaque(true);

            b.setContentAreaFilled(true);

            b.setBorderPainted(false);
        }

        // Add Buttons
        btns.add(add);

        btns.add(del);

        btns.add(clr);

        btns.add(expiring);

        // Add Panel
        g.gridx = 0;

        g.gridy = 6;

        g.gridwidth = 2;

        p.add(btns, g);

        // Button Actions
        add.addActionListener(e -> insert());

        del.addActionListener(e -> delete());

        clr.addActionListener(e -> clear());

        expiring.addActionListener(e -> loadExpiring());

        // Table Selection
        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                tfId.setText(
                        model.getValueAt(row, 0).toString()
                );

                tfDonor.setText(
                        model.getValueAt(row, 1).toString()
                );

                tfFood.setText(
                        model.getValueAt(row, 2).toString()
                );

                tfQty.setText(
                        model.getValueAt(row, 3).toString()
                );

                tfDate.setText(
                        model.getValueAt(row, 4).toString()
                );

                tfExpiry.setText(
                        model.getValueAt(row, 5).toString()
                );
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT donation_id, donor_id, food_id, quantity, donation_time, expiry_time FROM Donation"
        );
    }

    private void loadExpiring() {

        fillTableFromQuery(
                "SELECT donation_id, food_id, expiry_time " +
                "FROM Donation " +
                "WHERE expiry_time <= CURDATE() + INTERVAL 3 DAY"
        );
    }

    private void insert() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO Donation VALUES (?,?,?,?,?,?)"
                )
        ) {

            ps.setInt(
                    1,
                    Integer.parseInt(
                            tfId.getText().trim()
                    )
            );

            ps.setInt(
                    2,
                    Integer.parseInt(
                            tfDonor.getText().trim()
                    )
            );

            ps.setInt(
                    3,
                    Integer.parseInt(
                            tfFood.getText().trim()
                    )
            );

            ps.setInt(
                    4,
                    Integer.parseInt(
                            tfQty.getText().trim()
                    )
            );

            ps.setDate(
                    5,
                    java.sql.Date.valueOf(
                            tfDate.getText().trim()
                    )
            );

            ps.setDate(
                    6,
                    java.sql.Date.valueOf(
                            tfExpiry.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Donation recorded successfully!",
                    true
            );

            clear();

            loadData();

        } catch (Exception e) {

            showMsg(
                    e.getMessage(),
                    false
            );
        }
    }

    private void delete() {

        if (tfId.getText().isBlank()) {

            showMsg(
                    "Select a record first.",
                    false
            );

            return;
        }

        // Dark Confirmation Dialog
        UIManager.put(
                "OptionPane.background",
                new Color(35, 35, 35)
        );

        UIManager.put(
                "Panel.background",
                new Color(35, 35, 35)
        );

        UIManager.put(
                "OptionPane.messageForeground",
                Color.WHITE
        );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete Donation ID: "
                        + tfId.getText()
                        + " ?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {

            return;
        }

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM Donation WHERE donation_id=?"
                )
        ) {

            ps.setInt(
                    1,
                    Integer.parseInt(
                            tfId.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Donation deleted successfully!",
                    true
            );

            clear();

            loadData();

        } catch (Exception e) {

            showMsg(
                    e.getMessage(),
                    false
            );
        }
    }

    private void clear() {

        tfId.setText("");

        tfDonor.setText("");

        tfFood.setText("");

        tfQty.setText("");

        tfDate.setText("");

        tfExpiry.setText("");

        table.clearSelection();
    }
}