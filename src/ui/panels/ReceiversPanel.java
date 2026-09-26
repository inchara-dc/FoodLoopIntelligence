package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReceiversPanel extends BaseCRUDPanel {

    private JTextField tfId, tfName, tfOrg, tfUrgency;

    protected String getFormTitle() {

        return "Manage Receivers";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);

        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId      = addField(p, "Receiver ID",      g, 0);

        tfName    = addField(p, "Name",             g, 1);

        tfOrg     = addField(p, "Organization",     g, 2);

        tfUrgency = addField(p, "Urgency (1-5)",    g, 3);

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

        JButton highUrgency = makeBtn(
                "🔴 Show High Urgency",
                new Color(255, 87, 34)      // Deep Orange
        );

        // Visible White Text
        add.setForeground(Color.WHITE);

        del.setForeground(Color.WHITE);

        clr.setForeground(Color.WHITE);

        highUrgency.setForeground(Color.WHITE);

        // Extra Button Styling
        JButton[] buttons = {
                add,
                del,
                clr,
                highUrgency
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

        // Add Buttons Panel
        g.gridx = 0;

        g.gridy = 4;

        g.gridwidth = 2;

        p.add(btns, g);

        // High Urgency Button
        g.gridy = 5;

        p.add(highUrgency, g);

        // Button Actions
        add.addActionListener(e -> insert());

        del.addActionListener(e -> delete());

        clr.addActionListener(e -> clear());

        highUrgency.addActionListener(e -> loadHighUrgency());

        // Table Selection
        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                tfId.setText(
                        model.getValueAt(row, 0).toString()
                );

                tfName.setText(
                        model.getValueAt(row, 1).toString()
                );

                tfOrg.setText(
                        model.getValueAt(row, 2) != null
                                ? model.getValueAt(row, 2).toString()
                                : ""
                );

                tfUrgency.setText(
                        model.getValueAt(row, 3) != null
                                ? model.getValueAt(row, 3).toString()
                                : ""
                );
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT receiver_id, receiver_name, organization, urgency_score FROM Receiver"
        );
    }

    private void loadHighUrgency() {

        fillTableFromQuery(
                "SELECT receiver_name, organization, urgency_score " +
                "FROM Receiver " +
                "WHERE urgency_score >= 4 " +
                "ORDER BY urgency_score DESC"
        );
    }

    private void insert() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO Receiver VALUES (?,?,?,?)"
                )
        ) {

            ps.setInt(
                    1,
                    Integer.parseInt(
                            tfId.getText().trim()
                    )
            );

            ps.setString(
                    2,
                    tfName.getText().trim()
            );

            ps.setString(
                    3,
                    tfOrg.getText().trim()
            );

            ps.setInt(
                    4,
                    Integer.parseInt(
                            tfUrgency.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Receiver added successfully!",
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
                "Delete Receiver ID: "
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
                        "DELETE FROM Receiver WHERE receiver_id=?"
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
                    "Receiver deleted successfully!",
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

        tfName.setText("");

        tfOrg.setText("");

        tfUrgency.setText("");

        table.clearSelection();
    }
}