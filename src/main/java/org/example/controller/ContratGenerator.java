package org.example.controller;

import org.example.modele.biens;
import org.example.modele.client;

public class ContratGenerator {
    public static String genererContratLocation(biens bien, client client, double prix, int duree) {
        String contratLocationTemplate = "Contrat de location\n" +
                "---------------------------------\n" +
                "Nom du client: " + client.getNom() + "\n" +
                "Adresse du bien: " + bien.getLocalisation() + "\n" +
                "Type de bien: " + bien.getType() + "\n" +
                "Prix de location: " + prix + "\n" +
                "Durée du bail: " + duree + " mois\n" +
                "---------------------------------\n";
        return contratLocationTemplate;
    }

    // Méthode pour générer un contrat de vente
    public static String genererContratVente(biens bien, client client, double prix) {
        String contratVenteTemplate = "Contrat de vente\n" +
                "---------------------------------\n" +
                "Nom du client: " + client.getNom() + "\n" +
                "Adresse du bien: " + bien.getLocalisation() + "\n" +
                "Type de bien: " + bien.getType() + "\n" +
                "Prix de vente: " + prix + "\n" +
                "---------------------------------\n";
        return contratVenteTemplate;
    }
}
