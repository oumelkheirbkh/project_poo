package org.example.controller;

import org.example.modele.RendezVous;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionRendezVous {
    public static List<RendezVous> rendezVousList;

    public GestionRendezVous() {
        rendezVousList = new ArrayList<>();
    }

    public static void ajouterRendezVous(RendezVous rendezVous) {
        rendezVousList.add(rendezVous);
    }

    public void modifierRendezVous(int id, Date nouvelleDate, String nouvelleHeure) {
        for (RendezVous rendezVous : rendezVousList) {
            if (rendezVous.getId() == id) {
                rendezVous.setDate(nouvelleDate);
                rendezVous.setHeure(nouvelleHeure);
                break;
            }
        }
    }
    public void rechercherRendezVous(int id , RendezVous rendezVousAModifier){
        for (RendezVous rendezVous : rendezVousList){
            if (rendezVous.getId() == id) {
                rendezVousAModifier = rendezVous;
                break;
            }
        }
    }
    public void supprimerRendezVous(int id) {
        rendezVousList.removeIf(rendezVous -> rendezVous.getId() == id);
    }

    public void afficherRendezVous() {
        System.out.println("Liste des rendez-vous pour les visites de biens immobiliers :");
        System.out.println("ID\tDate\tHeure\tBien\tClient\tAgent");
        for (RendezVous rendezVous : rendezVousList) {
            System.out.println(rendezVous.getId() + "\t" + rendezVous.getDate() + "\t" +
                    rendezVous.getHeure() + "\t" + rendezVous.getBien().getId() + "\t" +
                    rendezVous.getClient().getNom() + "\t" + rendezVous.getAgent().getNom());
        }
    }
}
