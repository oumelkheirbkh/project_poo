package org.example.vue.Gestionclients;

import org.example.controller.GestionClients;
import org.example.enumeration.TypeClient;
import org.example.modele.client;
import org.example.vue.Gestionbiens.GestionBiensImmobiliersFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AjouterClientFrame  extends JFrame {

    private JTextField nomField;
    private JComboBox<TypeClient> typeComboBox;




    public AjouterClientFrame() throws SQLException {
        super("Ajout d'un nouveau client");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            initComponents(); // Appel de la méthode initComponents() pour initialiser les composants
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        pack(); // Adapter la taille de la fenêtre aux composants
        setSize(400, getHeight()); // Redimensionner la fenêtre
        setLocationRelativeTo(null);
    }

    private void initComponents() throws SQLException {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;



        // nom client
        JLabel nomLabel = new JLabel("nom : ");
        nomField = new JTextField(15);
        nomField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(nomLabel, gbc);
        gbc.gridx = 1;
        panel.add(nomField, gbc);
        // Type
        JLabel typeLabel = new JLabel("Type : ");
        typeComboBox = new JComboBox<>(TypeClient.values());
        typeComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(typeLabel, gbc);
        gbc.gridx = 1;
        panel.add(typeComboBox, gbc);



        // Récupérer la connexion à partir de l'outil de connexion intégré dans IntelliJ
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");

        // Ajouter bouton
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des valeurs des champs
                String nom = nomField.getText();
                TypeClient type = (TypeClient) typeComboBox.getSelectedItem();
                // Création du bien immobilier avec les valeurs entrées
                client client = new client(nom , type);
                GestionClients gestionClients = new GestionClients();
                gestionClients.ajouterClient(nom , type, connection);
                // Afficher un message de succès
                JOptionPane.showMessageDialog(org.example.vue.Gestionclients.AjouterClientFrame.this, "Client  ajouté avec succès");

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
                new GestionClientsFrame().setVisible(true);
            }
        });
        retourButton.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(retourButton, gbc);

    }

    private void clearFields() {

        nomField.setText("");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new org.example.vue.Gestionclients.AjouterClientFrame().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
