package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PickupPanel extends BaseCRUDPanel {

    private JTextField tfId, tfDonation, tfVolunteer, tfTime;

    private JComboBox<String> cbStatus;

    protected String getFormTitle() {

        return "Manage Pickup Schedules";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);

        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId = addField(p, "Pickup ID", g, 0);

        tfDonation = addField(p, "Donation ID", g, 1);

        tfVolunteer = addField(p, "Volunteer Name", g, 2);

        tfTime = addField(
                p,
                "Pickup Date (YYYY-MM-DD)",
                g,
                3
        );

        // Status Label
        g.gridx = 0;

        g.gridy = 4;

        g.weightx = 0;

        JLabel statusLbl = new JLabel("Status:");

        statusLbl.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        statusLbl.setForeground(Color.WHITE);

        p.add(statusLbl, g);

        // Status ComboBox
        g.gridx = 1;

        g.weightx = 1;

        cbStatus = new JComboBox<>(
                new String[]{
                        "Scheduled",
                        "Completed",
                        "Cancelled"
                }
        );

        cbStatus.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        cbStatus.setBackground(
                new Color(45, 45, 45)
        );

        cbStatus.setForeground(Color.WHITE);

        p.add(cbStatus, g);

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
                "➕ Add",
                new Color(0, 150, 136)      // Dark Teal
        );

        JButton upd = makeBtn(
                "✏️ Update Status",
                new Color(25, 118, 210)     // Dark Blue
        );

        JButton del = makeBtn(
                "🗑 Delete",
                new Color(198, 40, 40)      // Dark Red
        );

        JButton clr = makeBtn(
                "🔄 Clear",
                new Color(55, 71, 79)       // Dark Blue Grey
        );

        // Visible White Text
        add.setForeground(Color.WHITE);

        upd.setForeground(Color.WHITE);

        del.setForeground(Color.WHITE);

        clr.setForeground(Color.WHITE);

        // Extra Button Styling
        JButton[] buttons = {
                add,
                upd,
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

        btns.add(upd);

        btns.add(del);

        btns.add(clr);

        // Add Buttons Panel
        g.gridx = 0;

        g.gridy = 5;

        g.gridwidth = 2;

        p.add(btns, g);

        // Button Actions
        add.addActionListener(e -> insert());

        upd.addActionListener(e -> updateStatus());

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

                tfVolunteer.setText(
                        model.getValueAt(row, 2) != null
                                ? model.getValueAt(row, 2).toString()
                                : ""
                );

                tfTime.setText(
                        model.getValueAt(row, 3) != null
                                ? model.getValueAt(row, 3).toString()
                                : ""
                );

                if (model.getValueAt(row, 4) != null) {

                    cbStatus.setSelectedItem(
                            model.getValueAt(row, 4).toString()
                    );
                }
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT pickup_id, donation_id, volunteer_name, pickup_time, status " +
                "FROM PickupSchedule " +
                "ORDER BY pickup_time"
        );
    }

    private void insert() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO PickupSchedule VALUES (?,?,?,?,?)"
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
                    tfVolunteer.getText().trim()
            );

            ps.setDate(
                    4,
                    java.sql.Date.valueOf(
                            tfTime.getText().trim()
                    )
            );

            ps.setString(
                    5,
                    cbStatus.getSelectedItem().toString()
            );

            ps.executeUpdate();

            showMsg(
                    "Pickup scheduled successfully!",
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

    private void updateStatus() {

        if (tfId.getText().isBlank()) {

            showMsg(
                    "Select a record first.",
                    false
            );

            return;
        }

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "UPDATE PickupSchedule " +
                        "SET status=? " +
                        "WHERE pickup_id=?"
                )
        ) {

            ps.setString(
                    1,
                    cbStatus.getSelectedItem().toString()
            );

            ps.setInt(
                    2,
                    Integer.parseInt(
                            tfId.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Status updated successfully!",
                    true
            );

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
                "Delete Pickup ID: "
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
                        "DELETE FROM PickupSchedule WHERE pickup_id=?"
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

        tfVolunteer.setText("");

        tfTime.setText("");

        cbStatus.setSelectedIndex(0);

        table.clearSelection();
    }
}