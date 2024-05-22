package org.example.vue.GestionRendezVous;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

public class ModifierRendezVousFrame extends JFrame {
    private JComboBox<String> comboBoxRendezVous;
    private JTextField txtDate, txtTime;
    private JComboBox<String> comboClient, comboAgent, comboBien;
    private JButton btnModifier, btnSupprimer;
    private Connection connection;

    public ModifierRendezVousFrame() {
        setTitle("Modifier ou Supprimer un Rendez-Vous");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initConnection();
        initComponents();
        setVisible(true);
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        comboBoxRendezVous = new JComboBox<>();
        txtDate = new JTextField();
        txtTime = new JTextField();
        comboClient = new JComboBox<>();
        comboAgent = new JComboBox<>();
        comboBien = new JComboBox<>();
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        loadRendezVous();
        loadClients();
        loadAgents();
        loadBiens();

        comboBoxRendezVous.addActionListener(e -> fillFields());

        panel.add(new JLabel("Rendez-Vous:"));
        panel.add(comboBoxRendezVous);
        panel.add(new JLabel("Date (yyyy-mm-dd):"));
        panel.add(txtDate);
        panel.add(new JLabel("Time (HH:mm):"));
        panel.add(txtTime);
        panel.add(new JLabel("Client:"));
        panel.add(comboClient);
        panel.add(new JLabel("Agent:"));
        panel.add(comboAgent);
        panel.add(new JLabel("Bien:"));
        panel.add(comboBien);
        panel.add(btnModifier);
        panel.add(btnSupprimer);

        btnModifier.addActionListener(this::modifyRendezVous);
        btnSupprimer.addActionListener(this::deleteRendezVous);

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
    private void loadRendezVous() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id FROM rendez_vous")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboBoxRendezVous.addItem(String.valueOf(rs.getInt("id")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des rendez-vous: " + e.getMessage());
        }
    }

    private void fillFields() {
        int id = Integer.parseInt((String) comboBoxRendezVous.getSelectedItem());
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM rendez_vous WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtDate.setText(rs.getString("date"));
                txtTime.setText(rs.getString("heure"));
                comboClient.setSelectedItem(rs.getString("client"));
                comboAgent.setSelectedItem(rs.getString("agent"));
                comboBien.setSelectedItem(rs.getString("bien"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du remplissage des champs: " + e.getMessage());
        }
    }

    private void modifyRendezVous(ActionEvent e) {
        int id = Integer.parseInt((String) comboBoxRendezVous.getSelectedItem());
        String date = txtDate.getText();
        String time = txtTime.getText();
        String client = (String) comboClient.getSelectedItem();
        String agent = (String) comboAgent.getSelectedItem();
        String bien = (String) comboBien.getSelectedItem();

        String sql = "UPDATE rendez_vous SET date = ?, heure = ?, client = ?, agent = ?, bien = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setString(2, time);
            ps.setString(3, client);
            ps.setString(4, agent);
            ps.setString(5, bien);
            ps.setInt(6, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rendez-vous modifié avec succès!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du rendez-vous: " + ex.getMessage());
        }
    }

    private void deleteRendezVous(ActionEvent e) {
        int id = Integer.parseInt((String) comboBoxRendezVous.getSelectedItem());
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM rendez_vous WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rendez-vous supprimé avec succès!");
            comboBoxRendezVous.removeItem(comboBoxRendezVous.getSelectedItem());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du rendez-vous: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModifierRendezVousFrame::new);
    }
}
