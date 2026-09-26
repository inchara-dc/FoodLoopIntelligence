package ui.panels;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public abstract class BaseCRUDPanel extends JPanel {

    protected JTable table;
    protected DefaultTableModel model;
    protected JPanel formPanel;

    // Dark Theme Colors
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color panelColor = new Color(35, 35, 35);
    private final Color borderColor = new Color(0, 200, 120);
    private final Color headerColor = new Color(0, 121, 107);
    private final Color tableBg = new Color(28, 28, 28);
    private final Color textColor = new Color(240, 240, 240);
    private final Color selectionColor = new Color(46, 125, 50);

    public BaseCRUDPanel() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(backgroundColor);

        model = new DefaultTableModel() {

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Table Colors
        table.setBackground(tableBg);
        table.setForeground(textColor);
        table.setGridColor(new Color(60, 60, 60));
        table.setSelectionBackground(selectionColor);
        table.setSelectionForeground(Color.WHITE);

        // Header Styling
        JTableHeader header = table.getTableHeader();

        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(headerColor);
        header.setForeground(Color.BLACK);

        // Scroll Pane
        JScrollPane scroll = new JScrollPane(table);

        scroll.getViewport().setBackground(tableBg);

        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                "Records",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 13),
                borderColor
        ));

        // Form Panel
        formPanel = new JPanel(new GridBagLayout());

        formPanel.setBackground(panelColor);

        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                getFormTitle(),
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                borderColor
        ));

        buildForm(formPanel);

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formPanel,
                scroll
        );

        split.setDividerLocation(200);
        split.setBackground(backgroundColor);

        add(split, BorderLayout.CENTER);

        loadData();
    }

    protected abstract String getFormTitle();

    protected abstract void buildForm(JPanel p);

    protected abstract void loadData();

    protected JTextField addField(
            JPanel p,
            String label,
            GridBagConstraints gbc,
            int row
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label + ":");

        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(textColor);

        p.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        JTextField tf = new JTextField(15);

        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        tf.setBackground(new Color(50, 50, 50));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);

        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        p.add(tf, gbc);

        return tf;
    }

    protected JButton makeBtn(String text, Color bg) {

        JButton btn = new JButton(text);

        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btn.setFocusPainted(false);

        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        return btn;
    }

    protected void showMsg(String msg, boolean success) {

        UIManager.put("OptionPane.background", panelColor);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("OptionPane.messageForeground", textColor);

        JOptionPane.showMessageDialog(
                this,
                msg,
                success ? "Success" : "Error",
                success
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.ERROR_MESSAGE
        );
    }

    protected void fillTableFromQuery(String sql) {

        model.setRowCount(0);

        try (
                Connection c = DBConnection.getConnection();
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery(sql)
        ) {

            ResultSetMetaData meta = r.getMetaData();

            int cols = meta.getColumnCount();

            if (model.getColumnCount() == 0) {

                for (int i = 1; i <= cols; i++) {

                    model.addColumn(meta.getColumnName(i));
                }
            }

            while (r.next()) {

                Object[] row = new Object[cols];

                for (int i = 0; i < cols; i++) {

                    row[i] = r.getObject(i + 1);
                }

                model.addRow(row);
            }

        } catch (Exception e) {

            showMsg("DB Error: " + e.getMessage(), false);
        }
    }
}