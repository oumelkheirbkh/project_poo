package org.example.vue.GestionTransaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AfficherListeTransactionFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public AfficherListeTransactionFrame() {
        setTitle("Liste des Transactions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Table model setup
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Type");
        tableModel.addColumn("Bien");
        tableModel.addColumn("Client");
        tableModel.addColumn("Agent");
        tableModel.addColumn("Prix");
        tableModel.addColumn("Date");

        // Table setup
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Return button
        JButton returnButton = new JButton("Retour");
        returnButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadData();
    }

    private void loadData() {
        try (Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "")) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM transactions");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getInt("bien"), // Assuming 'bien' is stored as ID
                        rs.getString("client"),
                        rs.getString("agent"),
                        rs.getDouble("prix"),
                        rs.getDate("date").toString()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des transactions: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AfficherListeTransactionFrame::new);
    }
}
