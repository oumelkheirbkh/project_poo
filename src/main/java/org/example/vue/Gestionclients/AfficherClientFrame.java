package org.example.vue.Gestionclients;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AfficherClientFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public AfficherClientFrame(Connection connection) {
        setTitle("Liste des Clients");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents(connection);
        setVisible(true);
    }

    private void initComponents(Connection connection) {
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 206, 235));
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Liste des Clients");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Table model setup
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Nom");
        tableModel.addColumn("Type de Client");

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setGridColor(Color.GRAY);
        table.setShowVerticalLines(true);

        // ScrollPane for table
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Return button
        JButton returnButton = new JButton("Retour");
        returnButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnButton.setBackground(new Color(70, 130, 180));
        returnButton.setForeground(Color.WHITE);
        returnButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadClientsData(connection);
    }

    private void loadClientsData(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nom, TypeClient FROM clients")) {
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getString("nom"),
                        resultSet.getString("TypeClient")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "")) {
                new AfficherClientFrame(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
