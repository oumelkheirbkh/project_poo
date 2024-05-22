package org.example.vue.GestionTransaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ModifierTransactionFrame extends JFrame {
    private JComboBox<String> comboBoxTransactions;
    private JTextField txtType, txtPrix, txtDate;
    private JComboBox<String> comboBiens, comboClients, comboAgents;
    private JButton btnModifier, btnSupprimer, btnRetour;
    private Connection connection;

    public ModifierTransactionFrame() {
        setTitle("Modifier ou Supprimer une Transaction");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel dataPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        dataPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        comboBoxTransactions = new JComboBox<>();
        txtType = new JTextField();
        txtPrix = new JTextField();
        txtDate = new JTextField();
        comboBiens = new JComboBox<>();
        comboClients = new JComboBox<>();
        comboAgents = new JComboBox<>();
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        btnRetour = new JButton("Retour");

        loadTransactions();
        loadBiens();
        loadClients();
        loadAgents();

        dataPanel.add(new JLabel("Transaction:"));
        dataPanel.add(comboBoxTransactions);
        dataPanel.add(new JLabel("Type:"));
        dataPanel.add(txtType);
        dataPanel.add(new JLabel("Bien:"));
        dataPanel.add(comboBiens);
        dataPanel.add(new JLabel("Client:"));
        dataPanel.add(comboClients);
        dataPanel.add(new JLabel("Agent:"));
        dataPanel.add(comboAgents);
        dataPanel.add(new JLabel("Prix:"));
        dataPanel.add(txtPrix);
        dataPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        dataPanel.add(txtDate);
        dataPanel.add(btnModifier);
        dataPanel.add(btnSupprimer);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnRetour);

        btnModifier.addActionListener(e -> modifierTransaction());
        btnSupprimer.addActionListener(e -> supprimerTransaction());
        btnRetour.addActionListener(e -> dispose());

        comboBoxTransactions.addActionListener(e -> loadTransactionDetails((String) comboBoxTransactions.getSelectedItem()));

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private void loadTransactions() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id FROM transactions ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            comboBoxTransactions.removeAllItems();
            while (rs.next()) {
                comboBoxTransactions.addItem(String.valueOf(rs.getInt("id")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des transactions: " + e.getMessage());
        }
    }

    private void loadTransactionDetails(String transactionId) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM transactions WHERE id = ?");
            ps.setInt(1, Integer.parseInt(transactionId));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtType.setText(rs.getString("type"));
                comboBiens.setSelectedItem(rs.getInt("bien") + " - " + findBienTypeById(rs.getInt("bien")));
                comboClients.setSelectedItem(rs.getString("client"));
                comboAgents.setSelectedItem(rs.getString("agent"));
                txtPrix.setText(String.valueOf(rs.getDouble("prix")));
                txtDate.setText(rs.getString("date").toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des détails de la transaction: " + e.getMessage());
        }
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

    // Additional method to find bien type by id
    private String findBienTypeById(int bienId) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT type FROM biens WHERE id = ?")) {
            ps.setInt(1, bienId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";  // return empty if not found or error
    }
    private void modifierTransaction() {
        String id = (String) comboBoxTransactions.getSelectedItem();
        if (id == null || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une transaction à modifier.");
            return;
        }

        String type = txtType.getText();
        String bienId = ((String) comboBiens.getSelectedItem()).split(" - ")[0];
        String client = (String) comboClients.getSelectedItem();
        String agent = (String) comboAgents.getSelectedItem();
        String prixStr = txtPrix.getText();
        String date = txtDate.getText();

        // Validation example, could be extended
        if (type.isEmpty() || prixStr.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs nécessaires.");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le format du prix est invalide.");
            return;
        }

        try {
            String sql = "UPDATE transactions SET type = ?, bien = ?, client = ?, agent = ?, prix = ?, date = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, type);
            ps.setInt(2, Integer.parseInt(bienId));
            ps.setString(3, client);
            ps.setString(4, agent);
            ps.setDouble(5, prix);
            ps.setDate(6, java.sql.Date.valueOf(date));
            ps.setInt(7, Integer.parseInt(id));

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Transaction modifiée avec succès!");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun changement effectué. Vérifiez les données entrées.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la transaction: " + ex.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "La date doit être au format YYYY-MM-DD.");
        }
    }


    private void supprimerTransaction() {
        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette transaction ?", "Supprimer Transaction", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            String id = (String) comboBoxTransactions.getSelectedItem();
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM transactions WHERE id = ?");
                ps.setInt(1, Integer.parseInt(id));
                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Transaction supprimée avec succès!");
                    comboBoxTransactions.removeItem(id); // Remove from JComboBox
                    clearFields(); // Clear input fields after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune transaction supprimée. Vérifiez l'ID sélectionné.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la transaction: " + ex.getMessage());
            }
        }
    }

    private void clearFields() {
        txtType.setText("");
        comboBiens.setSelectedIndex(0);
        comboClients.setSelectedIndex(0);
        comboAgents.setSelectedIndex(0);
        txtPrix.setText("");
        txtDate.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModifierTransactionFrame::new);
    }
}
