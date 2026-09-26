package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DonorsPanel extends BaseCRUDPanel {

    private JTextField tfId, tfName, tfType, tfScore;

    protected String getFormTitle() {

        return "Manage Donors";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId    = addField(p, "Donor ID",           g, 0);
        tfName  = addField(p, "Name",               g, 1);
        tfType  = addField(p, "Type",               g, 2);
        tfScore = addField(p, "Reliability (1-5)", g, 3);

        // Buttons Panel
        JPanel btns = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        btns.setOpaque(false);

        // Attractive Dark Buttons
        JButton add = makeBtn(
                "➕ Add",
                new Color(0, 150, 136)     // Dark Teal
        );

        JButton del = makeBtn(
                "🗑 Delete",
                new Color(198, 40, 40)     // Dark Red
        );

        JButton clr = makeBtn(
                "🔄 Clear",
                new Color(55, 71, 79)      // Dark Blue Grey
        );

        // Perfect Visible White Text
        add.setForeground(Color.WHITE);
        del.setForeground(Color.WHITE);
        clr.setForeground(Color.WHITE);

        // Extra Button Styling
        JButton[] buttons = {add, del, clr};

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

        // Add Panel
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;

        p.add(btns, g);

        // Button Actions
        add.addActionListener(e -> insertDonor());

        del.addActionListener(e -> deleteDonor());

        clr.addActionListener(e -> clearFields());

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

                tfType.setText(
                        model.getValueAt(row, 2).toString()
                );

                tfScore.setText(
                        model.getValueAt(row, 3) != null
                                ? model.getValueAt(row, 3).toString()
                                : ""
                );
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT D_ID, D_NAME, D_TYPE, RELIABILITY_SCORE FROM Donors"
        );
    }

    private void insertDonor() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO Donors VALUES (?,?,?,?)"
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
                    tfType.getText().trim()
            );

            ps.setInt(
                    4,
                    Integer.parseInt(
                            tfScore.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Donor added successfully!",
                    true
            );

            clearFields();

            loadData();

        } catch (Exception e) {

            showMsg(
                    e.getMessage(),
                    false
            );
        }
    }

    private void deleteDonor() {

        if (tfId.getText().isBlank()) {

            showMsg(
                    "Select a donor first.",
                    false
            );

            return;
        }

        // Dark Dialog Styling
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
                "Delete Donor ID: "
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
                        "DELETE FROM Donors WHERE D_ID=?"
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
                    "Donor deleted successfully!",
                    true
            );

            clearFields();

            loadData();

        } catch (Exception e) {

            showMsg(
                    e.getMessage(),
                    false
            );
        }
    }

    private void clearFields() {

        tfId.setText("");

        tfName.setText("");

        tfType.setText("");

        tfScore.setText("");

        table.clearSelection();
    }
}