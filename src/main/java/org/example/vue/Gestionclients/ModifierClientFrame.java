package org.example.vue.Gestionclients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ModifierClientFrame extends JFrame {
    private JComboBox<String> comboBoxClients;
    private JTextField txtNomClient;
    private JTextField txtTypeClient;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private Connection connection;

    public ModifierClientFrame() {
        setTitle("Modifier ou Supprimer un Client");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initConnection();
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        comboBoxClients = new JComboBox<>();
        txtNomClient = new JTextField(20);
        txtTypeClient = new JTextField(20);
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        panel.add(new JLabel("Sélectionner un client:"));
        panel.add(comboBoxClients);
        panel.add(new JLabel("Nom du client:"));
        panel.add(txtNomClient);
        panel.add(new JLabel("Type de client:"));
        panel.add(txtTypeClient);
        panel.add(btnModifier);
        panel.add(btnSupprimer);

        loadClients();

        btnModifier.addActionListener(this::modifierClient);
        btnSupprimer.addActionListener(this::supprimerClient);

        comboBoxClients.addActionListener(e -> {
            String selectedClient = (String) comboBoxClients.getSelectedItem();
            loadClientDetails(selectedClient);
        });

        getContentPane().add(panel);
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    private void loadClients() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT nom FROM clients");
            ResultSet rs = ps.executeQuery();
            comboBoxClients.removeAllItems();
            while (rs.next()) {
                comboBoxClients.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des clients: " + e.getMessage());
        }
    }

    private void loadClientDetails(String clientName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clients WHERE nom = ?");
            ps.setString(1, clientName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNomClient.setText(rs.getString("nom"));
                txtTypeClient.setText(rs.getString("TypeClient"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des détails du client: " + e.getMessage());
        }
    }

    private void modifierClient(ActionEvent e) {
        try {
            String client = (String) comboBoxClients.getSelectedItem();
            PreparedStatement ps = connection.prepareStatement("UPDATE clients SET nom = ?, TypeClient = ? WHERE nom = ?");
            ps.setString(1, txtNomClient.getText());
            ps.setString(2, txtTypeClient.getText());
            ps.setString(3, client);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Client modifié avec succès!");
            loadClients();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du client: " + ex.getMessage());
        }
    }

    private void supprimerClient(ActionEvent e) {
        try {
            String client = (String) comboBoxClients.getSelectedItem();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM clients WHERE nom = ?");
            ps.setString(1, client);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Client supprimé avec succès!");
            loadClients();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du client: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new ModifierClientFrame().setVisible(true);
        });
    }
}

