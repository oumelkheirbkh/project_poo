package org.example.vue.Gestionagents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AffecterBienAgentFrame extends JFrame {
    private JComboBox<String> agentsComboBox;
    private JTable biensTable;
    private JButton affecterButton;
    private DefaultTableModel tableModel;
    private Connection connection;

    public AffecterBienAgentFrame(Connection connection) {
        super("Affecter des biens à un agent");
        this.connection = connection;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Panel pour la sélection de l'agent
        JPanel agentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agentPanel.setBackground(Color.WHITE);
        agentPanel.add(new JLabel("<html><font color='black'><b>Sélectionner un agent:</b></font></html>"));
        agentsComboBox = new JComboBox<>();
        loadAgents(); // Charger les agents depuis la base de données
        agentPanel.add(agentsComboBox);
        mainPanel.add(agentPanel, BorderLayout.NORTH);

        // Panel pour la sélection des biens
        JPanel biensPanel = new JPanel(new BorderLayout());
        biensPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(70, 130, 180), 2), "Sélectionner les biens à affecter",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)));
        biensPanel.setBackground(Color.WHITE);
        mainPanel.add(biensPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Type");
        tableModel.addColumn("Taille");
        tableModel.addColumn("Prix");
        tableModel.addColumn("Localisation");
        tableModel.addColumn("Description");

        loadBiens(); // Charger les biens depuis la base de données

        biensTable = new JTable(tableModel);
        biensTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        biensTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(biensTable);
        biensPanel.add(scrollPane, BorderLayout.CENTER);

        // Bouton pour affecter les biens à l'agent sélectionné
        affecterButton = new JButton("Affecter");
        affecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                affecterBiens();
            }
        });
        mainPanel.add(affecterButton, BorderLayout.SOUTH);

// Styling pour le bouton "Affecter"
        affecterButton.setBackground(new Color(70, 130, 180)); // Couleur de fond bleue
        affecterButton.setForeground(Color.BLACK); // Couleur du texte en noir
        affecterButton.setFocusPainted(false);
        affecterButton.setFont(new Font("Arial", Font.BOLD, 14));

    }

    // Charger les agents depuis la base de données et les afficher dans la combobox
    private void loadAgents() {
        try {
            String sql = "SELECT nom FROM agents";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    agentsComboBox.addItem(resultSet.getString("nom"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des agents.");
        }
    }

    // Charger les biens depuis la base de données et les afficher dans le tableau
    private void loadBiens() {
        try {
            String sql = "SELECT * FROM biens";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getInt("id"),
                            resultSet.getString("type"),
                            resultSet.getDouble("taille"),
                            resultSet.getDouble("prix"),
                            resultSet.getString("localisation"),
                            resultSet.getString("description")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des biens.");
        }
    }

    // Affecter les biens sélectionnés à l'agent sélectionné
    private void affecterBiens() {
        String agentSelectionne = (String) agentsComboBox.getSelectedItem();
        if (agentSelectionne != null) {
            int[] selectedRows = biensTable.getSelectedRows();
            if (selectedRows.length > 0) {
                try {
                    String sql = "INSERT INTO agent_biens (id_bien, nom_agent) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        for (int row : selectedRows) {
                            int idBien = (int) tableModel.getValueAt(row, 0);
                            preparedStatement.setInt(1, idBien);
                            preparedStatement.setString(2, agentSelectionne);
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                    JOptionPane.showMessageDialog(this, "Biens affectés à l'agent avec succès.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'affectation des biens à l'agent.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un bien à affecter.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un agent.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Connexion à la base de données
                String url = "jdbc:mariadb://localhost:3306/agence";
                String utilisateur = "root";
                String motDePasse = "";
                Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse);

                // Création de l'instance de AffecterBienAgentFrame
                new AffecterBienAgentFrame(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
