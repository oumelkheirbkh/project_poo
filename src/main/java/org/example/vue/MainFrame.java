package org.example.vue;

import org.example.vue.GestionRendezVous.GestionRendezVousFrame;
import org.example.vue.GestionTransaction.GestionTransactionFrame;
import org.example.vue.Gestionagents.GestionAgentsFrame;
import org.example.vue.Gestionbiens.GestionBiensImmobiliersFrame;
import org.example.vue.Gestionclients.GestionClientsFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Menu");
        // Création du panneau principal avec un BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(135, 206, 235)); // Couleur bleu clair
        // Création du label pour le titre
        JLabel label = new JLabel("Menu");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER); // Centre le texte horizontalement
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Ajoute une marge autour du label

        // Ajout du label au centre du panneau
        panel.add(label, BorderLayout.NORTH);

        // Création d'un panneau pour les boutons avec un GridLayout
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 0, 10)); // Espacement vertical de 10 pixels entre les boutons
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Ajoute une marge autour du panneau

        // Création des boutons avec des couleurs et une police personnalisées
        JButton[] buttons = {
                createStyledButton("Gestion des biens immobiliers"),
                createStyledButton("Gestion des transactions"),
                createStyledButton("Gestion des clients"),
                createStyledButton("Gestion des agents"),
                createStyledButton("Gestion des rendez-vous"),
                createStyledButton("Quitter")
        };

        // Ajout des boutons au panneau
        for (JButton button : buttons) {
            buttonsPanel.add(button);
        }

        // Ajout du panneau de boutons au panneau principal
        panel.add(buttonsPanel, BorderLayout.CENTER);

        // Ajout du panneau principal à la fenêtre
        add(panel);

        // Définition des préférences de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400); // Taille agrandie
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

        // Définition des actions pour les boutons
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Création d'une nouvelle instance de la deuxième fenêtre
                GestionBiensImmobiliersFrame gestionBiensImmobiliersFrame = new GestionBiensImmobiliersFrame();
                // Rendre la nouvelle fenêtre visible
                gestionBiensImmobiliersFrame.setVisible(true);
                // Cacher la fenêtre actuelle si nécessaire

                dispose();
            }
        });


        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionTransactionFrame gestionTransactionFrame = new GestionTransactionFrame();
                gestionTransactionFrame.setVisible(true);
                dispose();
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Création d'une nouvelle instance de la deuxième fenêtre
                GestionClientsFrame gestionClientsFrame = new GestionClientsFrame();
                // Rendre la nouvelle fenêtre visible
                gestionClientsFrame.setVisible(true);
                // Cacher la fenêtre actuelle si nécessaire

                dispose();
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Création d'une nouvelle instance de la deuxième fenêtre
                GestionAgentsFrame gestionAgentsFrame = new GestionAgentsFrame();
                // Rendre la nouvelle fenêtre visible
                gestionAgentsFrame.setVisible(true);
                // Cacher la fenêtre actuelle si nécessaire

                dispose();
            }
        });

        buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionRendezVousFrame gestionRendezVousFrame = new GestionRendezVousFrame();
                gestionRendezVousFrame.setVisible(true);
                dispose();
            }
        });

        buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Définition des couleurs de survol pour les boutons

    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.black);
        button.setFocusPainted(false); // Enlève la bordure de focus
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Ajoute une bordure noire
        button.setBorderPainted(true); // Affiche la bordure
        button.setPreferredSize(new Dimension(200, 50)); // Taille du bouton

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(135, 206, 235));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.black);

            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
