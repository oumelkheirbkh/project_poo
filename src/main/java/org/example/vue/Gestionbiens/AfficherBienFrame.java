package org.example.vue.Gestionbiens;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AfficherBienFrame extends JFrame {
    private Connection connection;

    public AfficherBienFrame(Connection connection) {
        super("Liste des biens immobiliers");
        this.connection = connection;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Panel pour le titre avec un background de couleur différente
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 206, 235)); // Couleur de fond
        JLabel titleLabel = new JLabel("Liste des biens immobiliers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Style de police
        titleLabel.setForeground(Color.WHITE); // Couleur du texte
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Création du modèle de tableau pour stocker les données des biens
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Type");
        model.addColumn("Taille");
        model.addColumn("Prix");
        model.addColumn("Localisation");
        model.addColumn("Description");

        try {
            String sql = "SELECT * FROM biens";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String type = resultSet.getString("type");
                    double taille = resultSet.getDouble("taille");
                    double prix = resultSet.getDouble("prix");
                    String localisation = resultSet.getString("localisation");
                    String description = resultSet.getString("description");
                    model.addRow(new Object[]{id, type, taille, prix, localisation, description});
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
        add(scrollPane, BorderLayout.CENTER);

        // Bouton de retour à la page précédente
        JButton retourButton = new JButton("Retour");
        retourButton.setFont(new Font("Arial", Font.BOLD, 16)); // Style de police
        retourButton.setBackground(new Color(70, 130, 180)); // Couleur de fond
        retourButton.setForeground(Color.black); // Couleur du texte
        retourButton.setBorderPainted(false); // Supprimer la bordure
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                new GestionBiensImmobiliersFrame().setVisible(true);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel pour centrer le bouton
        buttonPanel.add(retourButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Se connecter à la base de données
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
            SwingUtilities.invokeLater(() -> new AfficherBienFrame(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
