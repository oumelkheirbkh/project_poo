package org.example.vue.Gestionagents;

import org.example.controller.GestionAgents;
import org.example.modele.agents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AjouterAgentFrame extends JFrame {
    private JTextField nomTextField;
    private JButton ajouterButton;
    private GestionAgents gestionAgents;

    public AjouterAgentFrame(GestionAgents gestionAgents) {
        super("Ajouter un agent immobilier");
        this.gestionAgents = gestionAgents;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        JPanel nomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nomPanel.add(new JLabel("Nom de l'agent:"));
        nomTextField = new JTextField(20);
        nomPanel.add(nomTextField);
        mainPanel.add(nomPanel);

        ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterAgent();
            }
        });
        mainPanel.add(ajouterButton);
    }

    private void ajouterAgent() {
        String nom = nomTextField.getText().trim();

        if (!nom.isEmpty()) {
            try {
                agents nouvelAgent = new agents(nom);
                gestionAgents.ajouterAgent(nouvelAgent);
                JOptionPane.showMessageDialog(this, "Agent immobilier ajouté avec succès.");
                dispose(); // Ferme la fenêtre après l'ajout
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'agent immobilier.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom pour l'agent immobilier.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Exemple de création de la connexion à la base de données
            Connection connection = null;
            try {
                // Création de la connexion à la base de données
                connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");

                // Création de l'instance de GestionAgents
                GestionAgents gestionAgents = new GestionAgents(connection);

                // Création de l'instance de AjouterAgentFrame
                new AjouterAgentFrame(gestionAgents);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
