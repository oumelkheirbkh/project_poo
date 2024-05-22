package org.example.vue.Gestionbiens;

import org.example.controller.GestionBiens;
import org.example.enumeration.TypeBien;
import org.example.modele.biens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AjouterBienFrame extends JFrame {
    private JTextField idField;
    private JComboBox<TypeBien> typeComboBox;
    private JTextField tailleField;
    private JTextField prixField;
    private JTextField localisationField;
    private JTextField descriptionField;

    public AjouterBienFrame() throws SQLException {
        super("Ajout d'un nouveau bien immobilier");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents(); // Appel de la méthode initComponents() pour initialiser les composants
        pack(); // Adapter la taille de la fenêtre aux composants
        setSize(400, getHeight()); // Redimensionner la fenêtre
        setLocationRelativeTo(null);
    }

    private void initComponents() throws SQLException {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        JLabel idLabel = new JLabel("ID : ");
        idField = new JTextField(15);
        idField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        // Type
        JLabel typeLabel = new JLabel("Type : ");
        typeComboBox = new JComboBox<>(TypeBien.values());
        typeComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(typeLabel, gbc);
        gbc.gridx = 1;
        panel.add(typeComboBox, gbc);

        // Taille
        JLabel tailleLabel = new JLabel("Taille : ");
        tailleField = new JTextField(15);
        tailleField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(tailleLabel, gbc);
        gbc.gridx = 1;
        panel.add(tailleField, gbc);

        // Prix
        JLabel prixLabel = new JLabel("Prix : ");
        prixField = new JTextField(15);
        prixField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(prixLabel, gbc);
        gbc.gridx = 1;
        panel.add(prixField, gbc);

        // Localisation
        JLabel localisationLabel = new JLabel("Localisation : ");
        localisationField = new JTextField(15);
        localisationField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(localisationLabel, gbc);
        gbc.gridx = 1;
        panel.add(localisationField, gbc);

        // Description
        JLabel descriptionLabel = new JLabel("Description : ");
        descriptionField = new JTextField(15);
        descriptionField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        panel.add(descriptionField, gbc);

        // Récupérer la connexion à partir de l'outil de connexion intégré dans IntelliJ
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");

        // Ajouter bouton
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des valeurs des champs
                int id = Integer.parseInt(idField.getText());
                TypeBien type = (TypeBien) typeComboBox.getSelectedItem();
                double taille = Double.parseDouble(tailleField.getText());
                double prix = Double.parseDouble(prixField.getText());
                String localisation = localisationField.getText();
                String description = descriptionField.getText();

                // Création du bien immobilier avec les valeurs entrées
                biens nouveauBien = new biens(id, type, taille, prix, localisation, description);
                GestionBiens gestionBiens = new GestionBiens(connection);
                try {
                    gestionBiens.ajouterbiens(nouveauBien);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                // Afficher un message de succès
                JOptionPane.showMessageDialog(AjouterBienFrame.this, "Bien immobilier ajouté avec succès");

                // Effacer les champs après l'ajout
                clearFields();
            }
        });
        addButton.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 10, 5); // Ajustement des marges pour le bouton
        panel.add(addButton, gbc);

        add(panel);

        // Retour bouton
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                new GestionBiensImmobiliersFrame().setVisible(true);
            }
        });
        retourButton.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(retourButton, gbc);

    }

    private void clearFields() {
        idField.setText("");
        tailleField.setText("");
        prixField.setText("");
        localisationField.setText("");
        descriptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AjouterBienFrame().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
