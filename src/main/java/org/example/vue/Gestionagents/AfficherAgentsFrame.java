package org.example.vue.Gestionagents;

import org.example.vue.Gestionbiens.GestionBiensImmobiliersFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AfficherAgentsFrame extends JFrame {
    private Connection connection;

    public AfficherAgentsFrame(Connection connection) {
        super("Liste des agents immobiliers");
        this.connection = connection;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Panel principal avec un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); // Couleur de fond principale
        add(mainPanel);

        // Panel pour le titre avec un background de couleur différente
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // Couleur de fond du titre
        JLabel titleLabel = new JLabel("Liste des agents immobiliers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Style de police
        titleLabel.setForeground(Color.WHITE); // Couleur du texte
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Création du modèle de tableau pour stocker les données des biens
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nom de l'agent");
        try {
            String sql = "SELECT * FROM agents";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nomAgent = resultSet.getString("nom");
                    model.addRow(new Object[]{nomAgent});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Création de la table avec le modèle de données
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Style de police pour la table
        table.setForeground(Color.BLACK); // Couleur du texte
        table.setGridColor(Color.GRAY); // Couleur des lignes de la grille
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Style de police pour l'en-tête
        table.getTableHeader().setForeground(Color.WHITE); // Couleur du texte de l'en-tête
        table.getTableHeader().setBackground(new Color(70, 130, 180)); // Couleur de fond de l'en-tête
        table.setRowHeight(40); // Hauteur des lignes
        table.setBorder(new LineBorder(Color.BLACK)); // Bordure de la table

        // Ajout de la table dans un JScrollPane pour permettre le défilement si nécessaire
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE); // Couleur de fond du JScrollPane
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bouton de retour à la page précédente
        JButton retourButton = new JButton("Retour");
        retourButton.setFont(new Font("Arial", Font.BOLD, 16)); // Style de police
        retourButton.setBackground(new Color(70, 130, 180)); // Couleur de fond du bouton
        retourButton.setForeground(Color.BLACK); // Couleur du texte
        retourButton.setFocusPainted(false); // Supprimer le contour de focus
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                new GestionBiensImmobiliersFrame().setVisible(true);
            }
        });

        // Panel pour centrer le bouton
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240)); // Couleur de fond du panel de bouton
        buttonPanel.add(retourButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Se connecter à la base de données
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
            SwingUtilities.invokeLater(() -> new org.example.vue.Gestionagents.AfficherAgentsFrame(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
