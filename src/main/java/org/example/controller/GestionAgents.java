package org.example.controller;

import org.example.modele.agents;
import org.example.modele.biens;

import java.sql.*;
import java.util.*;

public class GestionAgents {

    private List<agents> agentsList;
    private Map<agents, List<biens>> biensParAgent;
    private Connection connection; // Ajout d'un attribut pour la connexion

    public GestionAgents(Connection connection) {
        this.connection = connection; // Initialisation de la connexion
        agentsList = new ArrayList<>();
        biensParAgent = new HashMap<>();
    }

    public void ajouterAgent(agents agent) throws SQLException {
        agentsList.add(agent);
        // Ajouter l'agent à la base de données
        String sql = "INSERT INTO agents (nom) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agent.getNom());
            preparedStatement.executeUpdate();
        }
    }

    public void supprimerAgent(agents agent) throws SQLException {
        agentsList.remove(agent);
        biensParAgent.remove(agent);
        // Supprimer l'agent de la base de données
        String sql = "DELETE FROM agents WHERE nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agent.getNom());
            preparedStatement.executeUpdate();
        }
    }

    public void modifierAgent(agents agent, String nouveauNom) {
        agent.setNom(nouveauNom);
    }

    public agents obtenirAgentParNom(String nomAgent) {
        for (agents agent : agentsList) {
            if (agent.getNom().equals(nomAgent)) {
                return agent;
            }
        }
        return null; // Aucun agent trouvé avec le nom spécifié
    }

    // Méthode pour afficher tous les agents immobiliers
    public void afficherAgents() {
        System.out.println("Liste des agents immobiliers :");
        for (agents agent : agentsList) {
            System.out.println(agent.getNom());
        }
    }

    // Méthode pour affecter un bien à un agent
    public void affecterBienAAgent(biens bien, agents agent) {
        biensParAgent.computeIfAbsent(agent, k -> new ArrayList<>()).add(bien);
    }
    public void affecterBienAgent(int idBien, String nomAgent) throws SQLException {
        String insertQuery = "INSERT INTO agent_biens (id_bien, nom_agent) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, idBien);
            preparedStatement.setString(2, nomAgent);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    // Méthode pour afficher les biens d'un agent donné
    public void afficherBiensAgent(agents agent) {
        List<biens> biensAgent = biensParAgent.get(agent);
        if (biensAgent != null) {
            System.out.println("Liste des biens de l'agent " + agent.getNom() + ":");
            System.out.println("ID\tType\tTaille\tPrix\tLocalisation\tDescription");
            System.out.println("----------------------------------------------------------");
            for (biens bien : biensAgent) {
                System.out.printf("%d\t%s\t%.2f\t%.2f\t%s\t%s\n", bien.getId(), bien.getType(), bien.getTaille(), bien.getPrix(), bien.getLocalisation(), bien.getDescription());
            }
        } else {
            System.out.println("Cet agent ne gère aucun bien.");
        }
    }

    // Méthode pour supprimer un bien géré par un agent
    public void supprimerBienGere(agents agent, biens bien) {
        List<biens> biensAgent = biensParAgent.get(agent);
        if (biensAgent != null) {
            biensAgent.remove(bien);
        }
    }

    // Méthode pour modifier les biens d'un agent
    public static void modifierBiensAgent(Scanner scanner, GestionAgents gestionAgents, GestionBiens gestionBiens) {
        System.out.print("Entrez le nom de l'agent immobilier : ");
        String nomAgent = scanner.nextLine();
        agents agent = gestionAgents.obtenirAgentParNom(nomAgent);

        if (agent != null) {
            System.out.print("Entrez l'ID du bien immobilier : ");
            int idBien = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne après l'ID

            // Obtenir le bien par son ID
            biens bien = gestionBiens.obtenirBienParID(idBien);
            if (bien != null) {
                // Si le bien existe, vérifier s'il doit être ajouté ou supprimé
                System.out.print("Voulez-vous ajouter (1) ou supprimer (2) ce bien de la liste de l'agent ? ");
                int choixAjoutSuppression = scanner.nextInt();
                scanner.nextLine(); // Pour consommer la nouvelle ligne après le choix

                // Modifier la liste des biens de l'agent en conséquence
                if (choixAjoutSuppression == 1) {
                    gestionAgents.affecterBienAAgent(bien, agent);
                    System.out.println("Le bien a été ajouté à la liste de l'agent.");
                } else if (choixAjoutSuppression == 2) {
                    gestionAgents.supprimerBienGere(agent, bien);
                    System.out.println("Le bien a été supprimé de la liste de l'agent.");
                } else {
                    System.out.println("Choix invalide.");
                }
            } else {
                System.out.println("Aucun bien trouvé avec l'ID spécifié.");
            }
        } else {
            System.out.println("Aucun agent immobilier trouvé avec le nom spécifié.");
        }
    }

    // Méthode pour obtenir l'agent qui gère un bien donné
    public agents obtenirAgentParBien(biens bien) {
        for (Map.Entry<agents, List<biens>> entry : biensParAgent.entrySet()) {
            if (entry.getValue().contains(bien)) {
                return entry.getKey();
            }
        }
        return null; // Aucun agent trouvé pour ce bien
    }
}
