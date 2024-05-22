package org.example.vue.Gestionclients;

import org.example.controller.GestionClients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;



public class AjouterDemandeClient extends JFrame {
    private JTextField txtNomClient;
    private JTextField txtTypeBien;
    private JTextField txtBudgetMax;
    private JTextArea txtDescription;
    private JButton btnAjouter;


    public AjouterDemandeClient() throws SQLException {
        setTitle("Ajouter une Demande Client");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();

    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNomClient = new JTextField();
        txtTypeBien = new JTextField();
        txtBudgetMax = new JTextField();
        txtDescription = new JTextArea(5, 20);
        btnAjouter = new JButton("Ajouter Demande");

        panel.add(new JLabel("Nom du Client:"));
        panel.add(txtNomClient);
        panel.add(new JLabel("Type de Bien:"));
        panel.add(txtTypeBien);
        panel.add(new JLabel("Budget Maximal:"));
        panel.add(txtBudgetMax);
        panel.add(new JLabel("Description:"));
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        panel.add(scrollPane);
        panel.add(btnAjouter);

        btnAjouter.addActionListener(this::ajouterDemandeClientAction);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private boolean clientExists(String nomClient, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM clients WHERE nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nomClient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    private void ajouterDemandeClientAction(ActionEvent e) {
        String nomClient = txtNomClient.getText().trim();  // Utiliser trim() pour enlever les espaces superflus
        String typeBien = txtTypeBien.getText().trim();
        double budgetMax = Double.parseDouble(txtBudgetMax.getText().trim());
        String description = txtDescription.getText().trim();

        try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");) {
            if (clientExists(nomClient, connection)) {
                GestionClients gestionClients = new GestionClients();
                gestionClients.ajouterDemandeClient(nomClient, typeBien, budgetMax, description, connection);
                JOptionPane.showMessageDialog(this, "Demande ajoutée avec succès!");
            } else {
                JOptionPane.showMessageDialog(this, "Le client n'existe pas dans la base de données.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion ou de l'opération: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }




    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new AjouterDemandeClient().setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
