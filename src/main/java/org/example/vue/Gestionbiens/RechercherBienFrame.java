package org.example.vue.Gestionbiens;

import org.example.controller.GestionBiens;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RechercherBienFrame extends JFrame {
    private JComboBox<String> critereComboBox;
    private JTextField valeurTextField;
    private JButton rechercherButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public RechercherBienFrame(Connection connection) {
        super("Rechercher des biens immobiliers");
        this.connection = connection;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // Panel pour les critères de recherche
        JPanel criterePanel = new JPanel();
        criterePanel.add(new JLabel("Critère de recherche:"));
        critereComboBox = new JComboBox<>(new String[]{"Prix", "Type", "Localisation"});
        criterePanel.add(critereComboBox);
        mainPanel.add(criterePanel);

        // Panel pour la valeur de recherche
        JPanel valeurPanel = new JPanel();
        valeurPanel.add(new JLabel("Valeur de recherche:"));
        valeurTextField = new JTextField(20);
        valeurPanel.add(valeurTextField);
        mainPanel.add(valeurPanel);

        // Bouton de recherche
        rechercherButton = new JButton("Rechercher");
        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherBiens();
            }
        });
        mainPanel.add(rechercherButton);

        // Panel pour afficher les résultats de recherche
        JPanel resultPanel = new JPanel(new BorderLayout());
        mainPanel.add(resultPanel);

        // Table pour afficher les résultats de recherche
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Type");
        tableModel.addColumn("Taille");
        tableModel.addColumn("Prix");
        tableModel.addColumn("Localisation");
        tableModel.addColumn("Description");
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultTable.setForeground(Color.BLACK);
        resultTable.setGridColor(Color.GRAY);
        resultTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        resultTable.getTableHeader().setForeground(Color.WHITE);
        resultTable.getTableHeader().setBackground(new Color(70, 130, 180));
        resultTable.setRowHeight(40);
        resultTable.setBorder(new LineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        // Bouton de retour
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre actuelle
                new GestionBiensImmobiliersFrame().setVisible(true);
            }
        });
        mainPanel.add(retourButton);
    }

    private void rechercherBiens() {
        String critere = valeurTextField.getText().trim();
        String typeCritere = critereComboBox.getSelectedItem().toString().toLowerCase();

        try {
            GestionBiens gestionBiens = new GestionBiens(connection);
            gestionBiens.rechercherBiens(critere, typeCritere);

            // Effacer les lignes précédentes
            tableModel.setRowCount(0);

            // Sélectionner tous les biens de la table temp_biens
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM temp_biens");

            // Remplir le modèle de tableau avec les résultats de la requête
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                double taille = resultSet.getDouble("taille");
                double prix = resultSet.getDouble("prix");
                String localisation = resultSet.getString("localisation");
                String description = resultSet.getString("description");

                tableModel.addRow(new Object[]{id, type, taille, prix, localisation, description});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche des biens");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
                new RechercherBienFrame(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
