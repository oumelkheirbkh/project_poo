package org.example.vue.GestionRendezVous;

import org.example.vue.Gestionclients.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestionRendezVousFrame extends JFrame {
        public GestionRendezVousFrame() {
            super("Gestion des  rendez-vous des clients");

            // Création des composants de la deuxième fenêtre
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
                    createStyledButton("Ajouter un rendez-vous"),
                    createStyledButton("modifier ou supprimer un rendez-vous"),
                    createStyledButton("Afficher la liste de tout les clients"),
                    createStyledButton("Retour a la page précédente"),
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
                    AjouterRendezVousFrame ajouterRendezVousFrame = null;
                    ajouterRendezVousFrame = new AjouterRendezVousFrame();
                    // Rendre la nouvelle fenêtre visible
                   ajouterRendezVousFrame.setVisible(true);
                    // Cacher la fenêtre actuelle si nécessaire
                    setVisible(false);
                }
            });


            buttons[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Créer et afficher la fenêtre d'ajout de demande client
                    ModifierRendezVousFrame modifierRendezVousFrame= null;
                    modifierRendezVousFrame = new ModifierRendezVousFrame();
                    modifierRendezVousFrame.setVisible(true);
                }
            });

            buttons[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Création d'une nouvelle instance de AfficherBienFrame et affichage de la fenêtre

                    Connection connection = null;
                    try {
                        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    Connection finalConnection = connection;
                    SwingUtilities.invokeLater(() -> {
                        new AfficherListeRendezVous();
                    });
                }
            });


            buttons[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Fermer la fenêtre actuelle
                    dispose();

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
                    new org.example.vue.GestionRendezVous.GestionRendezVousFrame().setVisible(true);
                }
            });
        }


}
