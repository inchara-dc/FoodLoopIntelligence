package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class WastePanel extends BaseCRUDPanel {

    private JTextField tfId, tfDonation, tfReason, tfDate;

    protected String getFormTitle() {

        return "Manage Waste Records";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);

        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId = addField(p, "Waste ID", g, 0);

        tfDonation = addField(p, "Donation ID", g, 1);

        tfReason = addField(p, "Reason", g, 2);

        tfDate = addField(
                p,
                "Discarded Date (YYYY-MM-DD)",
                g,
                3
        );

        // Buttons Panel
        JPanel btns = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        5
                )
        );

        btns.setOpaque(false);

        // Dark Theme Buttons
        JButton add = makeBtn(
                "➕ Record Waste",
                new Color(183, 28, 28)
        );

        JButton del = makeBtn(
                "🗑 Delete",
                new Color(69, 90, 100)
        );

        JButton clr = makeBtn(
                "🔄 Clear",
                new Color(55, 71, 79)
        );

        // BLACK TEXT COLOR
        add.setForeground(Color.BLACK);

        del.setForeground(Color.BLACK);

        clr.setForeground(Color.BLACK);

        // Extra Button Styling
        JButton[] buttons = {
                add,
                del,
                clr
        };

        for (JButton b : buttons) {

            b.setFont(
                    new Font("Segoe UI", Font.BOLD, 14)
            );

            b.setFocusPainted(false);

            b.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            b.setBorder(
                    BorderFactory.createEmptyBorder(
                            10,
                            20,
                            10,
                            20
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

        // Add Panel
        g.gridx = 0;

        g.gridy = 4;

        g.gridwidth = 2;

        p.add(btns, g);

        // Button Actions
        add.addActionListener(e -> insert());

        del.addActionListener(e -> delete());

        clr.addActionListener(e -> clear());

        // Table Selection
        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                tfId.setText(
                        model.getValueAt(row, 0).toString()
                );

                tfDonation.setText(
                        model.getValueAt(row, 1).toString()
                );

                tfReason.setText(
                        model.getValueAt(row, 2) != null
                                ? model.getValueAt(row, 2).toString()
                                : ""
                );

                tfDate.setText(
                        model.getValueAt(row, 3) != null
                                ? model.getValueAt(row, 3).toString()
                                : ""
                );
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT waste_id, donation_id, reason, discarded_time FROM WasteRecords"
        );
    }

    private void insert() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO WasteRecords VALUES (?,?,?,?)"
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
                            tfDonation.getText().trim()
                    )
            );

            ps.setString(
                    3,
                    tfReason.getText().trim()
            );

            ps.setDate(
                    4,
                    java.sql.Date.valueOf(
                            tfDate.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Waste recorded successfully!",
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
                "Delete Waste Record ID: "
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
                        "DELETE FROM WasteRecords WHERE waste_id=?"
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
                    "Record deleted successfully!",
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

        tfDonation.setText("");

        tfReason.setText("");

        tfDate.setText("");

        table.clearSelection();
    }
}