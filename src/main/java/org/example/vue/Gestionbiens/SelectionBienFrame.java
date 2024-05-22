package org.example.vue.Gestionbiens;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SelectionBienFrame extends JFrame {
    private Connection connection;
    private DefaultTableModel model;

    public SelectionBienFrame() throws SQLException {
        super("Sélectionner un bien à modifier ou supprimer");

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
        JLabel titleLabel = new JLabel("Sélectionner un bien à modifier ou supprimer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Style de police
        titleLabel.setForeground(Color.WHITE); // Couleur du texte
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Création du modèle de tableau pour stocker les données des biens
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Type");
        model.addColumn("Taille");
        model.addColumn("Prix");
        model.addColumn("Localisation");
        model.addColumn("Description");

        // Récupération des données à partir de la base de données
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
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
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des biens");
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

        // Bouton de modification
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) model.getValueAt(selectedRow, 0);
                    String type = (String) model.getValueAt(selectedRow, 1);
                    double taille = (double) model.getValueAt(selectedRow, 2);
                    double prix = (double) model.getValueAt(selectedRow, 3);
                    String localisation = (String) model.getValueAt(selectedRow, 4);
                    String description = (String) model.getValueAt(selectedRow, 5);
                    try {
                        new ModifierBienFrame(id, type, taille, prix, localisation, description).setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(SelectionBienFrame.this, "Veuillez sélectionner un bien à modifier.");
                }
            }
        });

        // Bouton de suppression
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) model.getValueAt(selectedRow, 0);
                    try {
                        String deleteSQL = "DELETE FROM biens WHERE id=?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                            preparedStatement.setInt(1, id);
                            preparedStatement.executeUpdate();
                            model.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(SelectionBienFrame.this, "Bien immobilier supprimé avec succès");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(SelectionBienFrame.this, "Erreur lors de la suppression du bien immobilier");
                    }
                } else {
                    JOptionPane.showMessageDialog(SelectionBienFrame.this, "Veuillez sélectionner un bien à supprimer.");
                }
            }
        });

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(modifierButton);
        buttonPanel.add(supprimerButton);
        add(buttonPanel, BorderLayout.SOUTH);
        // Bouton de retour
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                new GestionBiensImmobiliersFrame().setVisible(true);
            }
        });
        buttonPanel.add(retourButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SelectionBienFrame();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
