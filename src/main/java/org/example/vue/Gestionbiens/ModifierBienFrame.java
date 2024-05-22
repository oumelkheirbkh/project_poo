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

public class ModifierBienFrame extends JFrame {
    private JTextField idField;
    private JComboBox<TypeBien> typeComboBox;
    private JTextField tailleField;
    private JTextField prixField;
    private JTextField localisationField;
    private JTextField descriptionField;

    public ModifierBienFrame(int id, String type, double taille, double prix, String localisation, String description) throws SQLException {
        super("Modification d'un bien immobilier");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents(id, type, taille, prix, localisation, description);
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents(int id, String type, double taille, double prix, String localisation, String description) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID : ");
        idField = new JTextField(15);
        idField.setEditable(false);
        idField.setText(String.valueOf(id));
        idField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        JLabel typeLabel = new JLabel("Type : ");
        typeComboBox = new JComboBox<>(TypeBien.values());
        typeComboBox.setSelectedItem(TypeBien.valueOf(type));
        typeComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(typeLabel, gbc);
        gbc.gridx = 1;
        panel.add(typeComboBox, gbc);

        JLabel tailleLabel = new JLabel("Taille : ");
        tailleField = new JTextField(15);
        tailleField.setText(String.valueOf(taille));
        tailleField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(tailleLabel, gbc);
        gbc.gridx = 1;
        panel.add(tailleField, gbc);

        JLabel prixLabel = new JLabel("Prix : ");
        prixField = new JTextField(15);
        prixField.setText(String.valueOf(prix));
        prixField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(prixLabel, gbc);
        gbc.gridx = 1;
        panel.add(prixField, gbc);

        JLabel localisationLabel = new JLabel("Localisation : ");
        localisationField = new JTextField(15);
        localisationField.setText(localisation);
        localisationField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(localisationLabel, gbc);
        gbc.gridx = 1;
        panel.add(localisationField, gbc);

        JLabel descriptionLabel = new JLabel("Description : ");
        descriptionField = new JTextField(15);
        descriptionField.setText(description);
        descriptionField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        panel.add(descriptionField, gbc);

        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
            JButton modifierButton = new JButton("Modifier");
            modifierButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = Integer.parseInt(idField.getText());
                    TypeBien type = (TypeBien) typeComboBox.getSelectedItem();
                    double taille = Double.parseDouble(tailleField.getText());
                    double prix = Double.parseDouble(prixField.getText());
                    String localisation = localisationField.getText();
                    String description = descriptionField.getText();

                    biens bien = new biens(id, type, taille, prix, localisation, description);
                    GestionBiens gestionBiens = new GestionBiens(connection);
                    try {
                        gestionBiens.modifierbiens(bien);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(ModifierBienFrame.this, "Bien immobilier modifié avec succès");
                }
            });
            modifierButton.setPreferredSize(new Dimension(200, 30));
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 5, 10, 5);
            panel.add(modifierButton, gbc);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ModifierBienFrame(1, "Appartement", 100.0, 200000.0, "Paris", "Bel appartement à vendre").setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
