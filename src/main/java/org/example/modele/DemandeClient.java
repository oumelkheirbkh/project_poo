package org.example.modele;

import org.example.modele.client;

public class DemandeClient {
    private org.example.modele.client client;
    private String typeBienRecherche;
    private double budgetMax;
    private String description ;

    public DemandeClient(client client, String typeBienRecherche, double budgetMax ,  String description ) {
        this.client = client;
        this.typeBienRecherche = typeBienRecherche;
        this.budgetMax = budgetMax;
        this.description= description;
    }

    public client getClient() {
        return client;
    }

    public String getTypeBienRecherche() {
        return typeBienRecherche;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudgetMax() {
        return budgetMax;
    }
}

