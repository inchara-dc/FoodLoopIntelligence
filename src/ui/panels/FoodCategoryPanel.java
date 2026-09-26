package ui.panels;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FoodCategoryPanel extends BaseCRUDPanel {

    private JTextField tfId, tfName, tfShelf;

    protected String getFormTitle() {

        return "Manage Food Categories";
    }

    protected void buildForm(JPanel p) {

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 10, 6, 10);

        g.fill = GridBagConstraints.HORIZONTAL;

        // Input Fields
        tfId    = addField(p, "Category ID",       g, 0);

        tfName  = addField(p, "Category Name",     g, 1);

        tfShelf = addField(p, "Shelf Life (days)", g, 2);

        // Buttons Panel
        JPanel btns = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        btns.setOpaque(false);

        // Dark Theme Buttons
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

        // Visible Button Text
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

        g.gridy = 3;

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

                tfShelf.setText(
                        model.getValueAt(row, 2).toString()
                );
            }
        });
    }

    protected void loadData() {

        fillTableFromQuery(
                "SELECT category_id, category_name, shelf_life FROM FoodCategory"
        );
    }

    private void insert() {

        try (
                Connection c = DBConnection.getConnection();

                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO FoodCategory VALUES (?,?,?)"
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

            ps.setInt(
                    3,
                    Integer.parseInt(
                            tfShelf.getText().trim()
                    )
            );

            ps.executeUpdate();

            showMsg(
                    "Category added successfully!",
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
                "Delete Category ID: "
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
                        "DELETE FROM FoodCategory WHERE category_id=?"
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
                    "Category deleted successfully!",
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

        tfShelf.setText("");

        table.clearSelection();
    }
}