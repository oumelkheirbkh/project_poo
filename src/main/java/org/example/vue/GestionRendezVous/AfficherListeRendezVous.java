package org.example.vue.GestionRendezVous;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AfficherListeRendezVous extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    public AfficherListeRendezVous() {
        setTitle("Liste des Rendez-vous");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        initConnection();
        initComponents();
        setVisible(true);
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent user from editing cells
            }
        };
        tableModel.addColumn("ID");
        tableModel.addColumn("Date");
        tableModel.addColumn("Heure");
        tableModel.addColumn("Client");
        tableModel.addColumn("Agent");
        tableModel.addColumn("Bien");

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(135, 206, 250));
        table.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRetour.setBackground(new Color(192, 192, 192));
        btnRetour.setForeground(Color.BLACK);
        btnRetour.addActionListener(e -> dispose()); // Close the window when clicked

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnRetour);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        loadRendezVous();
    }

    private void loadRendezVous() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT r.id, r.date, r.heure, r.client, r.agent, b.type AS bien_type FROM rendez_vous r JOIN biens b ON r.bien = b.id");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("heure"),
                        rs.getString("client"),
                        rs.getString("agent"),
                        rs.getString("bien_type")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des rendez-vous: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AfficherListeRendezVous::new);
    }
}
