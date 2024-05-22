package org.example.vue.GestionRendezVous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AjouterRendezVousFrame extends JFrame {
    private JTextField txtDate, txtTime;
    private JComboBox<String> comboClient, comboAgent, comboBien;
    private JButton btnAjouter, btnRetour;
    private Connection connection;

    public AjouterRendezVousFrame() {
        setTitle("Ajouter un Rendez-Vous");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initConnection();
        initComponents();
        setVisible(true);
    }

    private void initConnection() {
        try {
            // Modify with your actual database connection details
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        txtDate = new JTextField();
        txtTime = new JTextField();
        comboClient = new JComboBox<>();
        comboAgent = new JComboBox<>();
        comboBien = new JComboBox<>();
        btnAjouter = new JButton("Ajouter");
        btnRetour = new JButton("Retour");

        panel.add(new JLabel("Date (yyyy-mm-dd):"));
        panel.add(txtDate);
        panel.add(new JLabel("Heure (HH:mm):"));
        panel.add(txtTime);
        panel.add(new JLabel("Client:"));
        panel.add(comboClient);
        panel.add(new JLabel("Agent:"));
        panel.add(comboAgent);
        panel.add(new JLabel("Bien:"));
        panel.add(comboBien);
        panel.add(btnAjouter);
        panel.add(btnRetour);

        btnAjouter.addActionListener(this::ajouterRendezVous);
        btnRetour.addActionListener(e -> dispose());

        loadClients(); // Assume method to populate clients
        loadAgents();  // Assume method to populate agents
        loadBiens();   // Assume method to populate properties

        getContentPane().add(panel);
    }
    private void loadClients() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT nom FROM clients")) {
            ResultSet rs = ps.executeQuery();
            comboClient.removeAllItems();  // Clear previous items
            while (rs.next()) {
                comboClient.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des clients: " + e.getMessage());
        }
    }

    private void loadAgents() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT nom FROM agents")) {
            ResultSet rs = ps.executeQuery();
            comboAgent.removeAllItems();  // Clear previous items
            while (rs.next()) {
                comboAgent.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des agents: " + e.getMessage());
        }
    }

    private void loadBiens() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, type, localisation FROM biens")) {
            ResultSet rs = ps.executeQuery();
            comboBien.removeAllItems();  // Clear previous items
            while (rs.next()) {
                String displayText = rs.getInt("id") + " - " + rs.getString("type") + " - " + rs.getString("localisation");
                comboBien.addItem(displayText);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des biens: " + e.getMessage());
        }
    }

    private void ajouterRendezVous(ActionEvent e) {
        String date = txtDate.getText();
        String time = txtTime.getText();
        String client = (String) comboClient.getSelectedItem();
        String agent = (String) comboAgent.getSelectedItem();

        // Extract the numeric ID from the bien string ("1 - Apartment - Location" becomes "1")
        String selectedBien = (String) comboBien.getSelectedItem();
        int bienId = Integer.parseInt(selectedBien.split(" - ")[0]);

        String sql = "INSERT INTO rendez_vous (date, heure, client, agent, bien) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setString(2, time);
            ps.setString(3, client);
            ps.setString(4, agent);
            ps.setInt(5, bienId);  // Use bienId here, which is the actual integer ID
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rendez-vous ajouté avec succès!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du rendez-vous: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AjouterRendezVousFrame::new);
    }
}
