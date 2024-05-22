package org.example.controller;

import org.example.enumeration.TypeClient;
import org.example.modele.DemandeClient;
import org.example.modele.Interaction;
import org.example.modele.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionClients {
    private List<client> clients;
    private List<Interaction> interactions;
    private List<DemandeClient> demandesClients;


    public GestionClients() {
        this.clients = new ArrayList<>();
        this.interactions = new ArrayList<>();
        this.demandesClients = new ArrayList<>();
    }
    public client obtenirClientParNom(String nomClient) {
        for (client client : clients) {
            if (client.getNom().equals(nomClient)) {
                return client;
            }
        }
        return null; // Aucun client trouvé avec le nom spécifié
    }
    // Ajouter un client

    public void ajouterClient(String nom, TypeClient typeClient, Connection connection) {
        // Assuming `client` is just a placeholder for creating the object
        // and you don't need to store it as a local collection.
        client newClient = new client(nom, typeClient);

        // SQL query to insert a new client into the database
        String sql = "INSERT INTO clients (nom, TypeClient) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, typeClient.toString()); // Assuming TypeClient has a valid toString() method
            preparedStatement.executeUpdate();
            System.out.println("Client ajouté avec succès : " + nom);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }

    // Mettre à jour les informations d'un client
    public void modifierClient( client  client, String nom, TypeClient TypeClient) {
        client.setNom(nom);
        client.setType(TypeClient);
    }

    // Supprimer un client
    public void supprimerClient(client client) {
        clients.remove(client);
    }
    public void afficherClients() {
        System.out.println("Liste des clients :");
        System.out.println("Nom\tType");
        System.out.println("-------------------------");
        for (client client : clients) {
            System.out.println(client.getNom() + "\t" + client.getType());
        }
    }
    // Obtenir la liste des clients
    public List<client> getListeClients() {
        return clients;
    }

    // Ajouter une interaction
    public void ajouterInteraction(Interaction interaction) {
        interactions.add(interaction);
    }

    // Obtenir l'historique des interactions
    public List<Interaction> getHistoriqueInteractions() {
        return interactions;
    }
    public void ajouterDemandeClient(String nomClient, String typeBien, double budgetMax, String description, Connection connection) throws SQLException {
        String sql = "INSERT INTO demandes_client (client, typeBienRecherche, budgetMax, description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nomClient);
            preparedStatement.setString(2, typeBien);
            preparedStatement.setDouble(3, budgetMax);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
        }
    }
    public void afficherDemandesClients() {
        System.out.println("Liste des demandes des clients :");
        System.out.println("client\tType de bien recherché\tBudget maximum\tdescription");
        System.out.println("------------------------------------------------------");
        for (DemandeClient demande : demandesClients) {
            System.out.printf("%s\t%s\t\t%.2f\t%s\n", demande.getClient().getNom(), demande.getTypeBienRecherche(), demande.getBudgetMax(),demande.getDescription());
        }
    }



}
