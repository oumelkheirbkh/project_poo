package org.example.controller;

import org.example.enumeration.TypeTransaction;
import org.example.modele.Transaction;
import org.example.modele.agents;
import org.example.modele.biens;
import org.example.modele.client;

import java.sql.SQLException;
import java.util.*;

public class GestionTransactions {
    private List<Transaction> transactions;

    public GestionTransactions() {
        this.transactions = new ArrayList<>();
    }

    public static void creerNouvelleTransaction(Scanner scanner, GestionClients gestionClients, GestionTransactions gestionTransactions , GestionBiens gestionBiens , GestionAgents gestionAgents) throws SQLException {
        // Demander les détails de la transaction à l'utilisateur
        System.out.print("Entrez l'ID de la transaction : ");
        int idTransaction = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après l'ID
        // Demander le type de transaction à l'utilisateur
        System.out.println("Types de transaction :");
        for (TypeTransaction type : TypeTransaction.values()) {
            System.out.println(type);
        }
        System.out.print("Choisissez le type de transaction : ");
        String typeTransactionStr = scanner.nextLine().toUpperCase(); // Convertir l'entrée en majuscules
        TypeTransaction typeTransaction = TypeTransaction.valueOf(typeTransactionStr);

        // Afficher la liste des clients pour sélectionner celui associé à la transaction
        System.out.println("Liste des clients :");
        gestionClients.afficherClients();
        System.out.print("Entrez le nom du client associé à la transaction : ");
        String nomClient = scanner.nextLine();
        client client = gestionClients.obtenirClientParNom(nomClient);
        if (client != null) {
            // Sélectionner le bien associé à la transaction
            System.out.println("Liste des biens :");
            gestionBiens.afficherbiens();
            System.out.print("Entrez l'ID du bien associé à la transaction : ");
            int idBien = scanner.nextInt();

            System.out.println("Entrez la date associée à la transaction : ");
            System.out.print("Jour : ");
            int jour = scanner.nextInt();
            System.out.print("Mois : ");
            int mois = scanner.nextInt();
            System.out.print("Année : ");
            int annee = scanner.nextInt();

            Calendar calendar = Calendar.getInstance();
            calendar.set(annee, mois - 1, jour); // Mois - 1 car les mois dans Calendar commencent à 0
            Date dateTransaction = calendar.getTime();
            biens bien = gestionBiens.obtenirBienParID(idBien);
            if (bien != null) {
                double prix = 0;
                int duree = 0;

                // Si la transaction est une location, demander la durée du bail
                if (typeTransaction == TypeTransaction.LOCATION) {
                    System.out.print("Entrez la durée du bail (en mois) : ");
                    duree = scanner.nextInt();
                    scanner.nextLine(); // Pour consommer la nouvelle ligne après la durée du bail
                }
                System.out.println("Entrez le prix associé");
                 prix = scanner.nextDouble();
                scanner.nextLine(); // Pour consommer la nouvelle ligne après l'ID du bien
                agents agents = gestionAgents.obtenirAgentParBien(bien);
                Transaction transaction = new Transaction(idTransaction, typeTransaction, bien, client ,dateTransaction , prix , agents);
                // Ajouter la transaction au gestionnaire de transactions
                gestionTransactions.ajouterTransaction(transaction);
                // Afficher un message en fonction du type de transaction
                switch (typeTransaction) {
                    case VENTE:
                        String contratVente = ContratGenerator.genererContratVente(bien, client, prix);
                        System.out.println("Nouvelle transaction de vente ajoutée avec succès.\nContrat de vente généré :\n" + contratVente);
                        break;
                    case LOCATION:
                        String contratLocation = ContratGenerator.genererContratLocation(bien, client, prix ,duree);
                        System.out.println("Nouvelle transaction de location ajoutée avec succès.\nContrat de location généré :\n" + contratLocation);
                        break;
                    case CONTRAT:
                        System.out.println("Nouvelle transaction de contrat ajoutée avec succès.");
                        break;
                    default:
                        System.out.println("Type de transaction non reconnu.");
                }
            } else {
                System.out.println("Bien non trouvé.");
            }
        } else {
            System.out.println("Client non trouvé.");
        }
    }


    public void ajouterTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    public void afficherTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction enregistrée.");
        } else {
            System.out.println("Liste des transactions :");
            System.out.println("ID\tType\t\tBien\t\tClient\t\tDate\t\t\tPrix\t\tAgent");
            System.out.println("-----------------------------------------------------------------------------------------------------------------");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getId() + "\t" + transaction.getType() + "\t" + transaction.getBien().getType() + "\t" + transaction.getClient().getNom() + "\t" + transaction.getDate() + "\t" + transaction.getPrix() + "\t"+ transaction.getAgent().getNom());
            }
        }
    }



}
