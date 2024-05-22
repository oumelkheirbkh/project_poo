package org.example.vue.GestionTransaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AjouterTransactionFrame extends JFrame {
    private JComboBox<String> comboTypeTransaction, comboBiens, comboClients, comboAgents;
    private JTextField txtPrix, txtDate;
    private JButton btnAjouter, btnRetour;
    private Connection connection;

    public AjouterTransactionFrame() {
        setTitle("Ajouter une Transaction");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initConnection();
        initComponents();
        setVisible(true);
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
            System.out.println("Connected to database.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        comboTypeTransaction = new JComboBox<>(new String[]{"VENTE", "LOCATION"});
        comboBiens = new JComboBox<>();
        comboClients = new JComboBox<>();
        comboAgents = new JComboBox<>();
        txtPrix = new JTextField();
        txtDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        btnAjouter = new JButton("Ajouter Transaction");
        btnRetour = new JButton("Retour");
        setupButtonStyles();

        btnAjouter.addActionListener(this::ajouterTransaction);
        btnRetour.addActionListener(e -> this.dispose());

        formPanel.add(new JLabel("Type de Transaction:"));
        formPanel.add(comboTypeTransaction);
        formPanel.add(new JLabel("Bien:"));
        formPanel.add(comboBiens);
        formPanel.add(new JLabel("Client:"));
        formPanel.add(comboClients);
        formPanel.add(new JLabel("Agent:"));
        formPanel.add(comboAgents);
        formPanel.add(new JLabel("Prix:"));
        formPanel.add(txtPrix);
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(txtDate);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnRetour);

        panel.add(formPanel);
        panel.add(buttonPanel);

        add(panel);

        loadBiens();
        loadClients();
        loadAgents();
    }
    private void loadBiens() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, type FROM biens")) {
            ResultSet rs = ps.executeQuery();
            comboBiens.removeAllItems();  // Clear previous items
            while (rs.next()) {
                String bien = rs.getInt("id") + " - " + rs.getString("type");
                comboBiens.addItem(bien);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des biens: " + e.getMessage());
        }
    }

    private void loadClients() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT nom FROM clients")) {
            ResultSet rs = ps.executeQuery();
            comboClients.removeAllItems();  // Clear previous items
            while (rs.next()) {
                comboClients.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des clients: " + e.getMessage());
        }
    }

    private void loadAgents() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT nom FROM agents")) {
            ResultSet rs = ps.executeQuery();
            comboAgents.removeAllItems();  // Clear previous items
            while (rs.next()) {
                comboAgents.addItem(rs.getString("nom"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des agents: " + e.getMessage());
        }
    }

    private void setupButtonStyles() {
        btnAjouter.setBackground(new Color(70, 130, 180));
        btnAjouter.setForeground(Color.WHITE);

        btnRetour.setBackground(new Color(192, 192, 192));
        btnRetour.setForeground(Color.BLACK);
    }

    private void ajouterTransaction(ActionEvent e) {
        String selectedBien = (String) comboBiens.getSelectedItem();
        int idBien = Integer.parseInt(selectedBien.split(" - ")[0]);
        String client = (String) comboClients.getSelectedItem();
        String agent = (String) comboAgents.getSelectedItem();
        double prix = Double.parseDouble(txtPrix.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(txtDate.getText(), formatter);
        String type = (String) comboTypeTransaction.getSelectedItem();

        String sql = "INSERT INTO transactions (type, bien, client, agent, prix, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setInt(2, idBien);
            ps.setString(3, client);
            ps.setString(4, agent);
            ps.setDouble(5, prix);
            ps.setDate(6, java.sql.Date.valueOf(date));
            ps.executeUpdate();
            showTransactionReport(type, idBien, client, agent, prix, date);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTransactionReport(String type, int bien, String client, String agent, double prix, LocalDate date) {
        JDialog reportDialog = new JDialog(this, "Détails de la Transaction", true);
        reportDialog.setSize(500, 300);
        reportDialog.setLocationRelativeTo(this);

        // Using HTML to style the content
        String reportHtml = String.format(
                "<html><body style='font-family: Arial; color: black;'>" +
                        "<h1 style='color: blue; text-align: center;'>Rapport de Transaction</h1>" +
                        "<p><b>Type de Transaction:</b> %s</p>" +
                        "<p><b>ID du Bien:</b> %d</p>" +
                        "<p><b>Client:</b> %s</p>" +
                        "<p><b>Agent:</b> %s</p>" +
                        "<p><b>Prix:</b> %.2f EUR</p>" +
                        "<p><b>Date:</b> %s</p>" +
                        "</body></html>",
                type, bien, client, agent, prix, date.toString());

        JLabel contentLabel = new JLabel(reportHtml);
        contentLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reportDialog.add(contentLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> reportDialog.dispose());
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);

        reportDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AjouterTransactionFrame::new);
    }
}
