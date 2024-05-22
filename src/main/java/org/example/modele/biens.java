package org.example.modele;

import org.example.enumeration.TypeBien;

public class biens {
    private int id;
    private TypeBien type;
    private double taille;
    private double prix;
    private String localisation;
    private String description;
    private agents agentImmobilier; // Ajout de l'attribut agentImmobilier

    public biens(int id, TypeBien type, double taille, double prix, String localisation, String description) {
        this.id = id;
        this.type = TypeBien.valueOf(String.valueOf(type));
        this.taille = taille;
        this.prix = prix;
        this.localisation = localisation;
        this.description = description;
        this.agentImmobilier = agentImmobilier;
    }

    public biens(int id, String type, double taille, double prix, String localisation, String description) {

    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public TypeBien getType() {
        return type;
    }
    public void setType(String type) {
        this.type = TypeBien.valueOf(type);
    }
    public double getTaille() {
        return taille;
    }
    public void setTaille(double taille) {
        this.taille = taille;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
    public String getLocalisation() {
        return localisation;
    }
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void afficherBiensGeres(agents agent) {
        if (agent != null) {
            System.out.println("Biens gérés par l'agent immobilier " + agent.getNom() + ":");
            System.out.println("ID\tType\tTaille\tPrix\tLocalisation\tDescription");
            System.out.println("----------------------------------------------------------");
            for (biens bien : agent.getBiensGeres()) {
                System.out.printf("%d\t%s\t%.2f\t%.2f\t%s\t%s\n", bien.getId(), bien.getType(), bien.getTaille(), bien.getPrix(), bien.getLocalisation(), bien.getDescription());
            }
        } else {
            System.out.println("L'agent immobilier spécifié est null.");
        }
    }



}
