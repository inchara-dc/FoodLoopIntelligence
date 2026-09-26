package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FoodItemsPanel extends BaseCRUDPanel {

    private JTextField tfId, tfName, tfUnit, tfPerish, tfCat;

    protected String getFormTitle() {

        return "Manage Food Items";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);

        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId     = addField(p, "Food ID",                 g, 0);

        tfName   = addField(p, "Food Name",               g, 1);

        tfUnit   = addField(p, "Unit",                    g, 2);

        tfPerish = addField(p, "Perishability (1-10)",   g, 3);

        tfCat    = addField(p, "Category ID",             g, 4);

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

        // Visible White Text
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

        g.gridy = 5;

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

                tfName.setText(
                        model.getValueAt(row, 1).toString()
                );

                tfUnit.setText(
                        model.getValueAt(row, 2) != null
                                ? model.getValueAt(row, 2).toString()
                                : ""
                );

                tfPerish.setText(
                        model.getValueAt(row, 3) != null
                                ? model.getValueAt(row, 3).toString()
                                : ""
                );

                tfCat.setText(
                        model.getValueAt(row, 4) != null
                                ? model.getValueAt(row, 4).toString()
                                : ""
                );
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT food_id, food_name, unit, perishability_index, category_id FROM FoodItems"
        );
    }

    private void insert() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO FoodItems VALUES (?,?,?,?,?)"
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
                    tfUnit.getText().trim()
            );

            ps.setInt(
                    4,
                    Integer.parseInt(
                            tfPerish.getText().trim()
                    )
            );

            ps.setInt(
                    5,
                    Integer.parseInt(
                            tfCat.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Food item added successfully!",
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
                "Delete Food Item ID: "
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
                        "DELETE FROM FoodItems WHERE food_id=?"
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
                    "Food item deleted successfully!",
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

        tfUnit.setText("");

        tfPerish.setText("");

        tfCat.setText("");

        table.clearSelection();
    }
}