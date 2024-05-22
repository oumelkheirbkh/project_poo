package org.example.modele;



import java.util.ArrayList;
import java.util.List;

public class agents {
    private  String nom ;
    private List<biens> biensGeres;
    public agents(String nom) {
        this.nom = nom;
        this.biensGeres = new ArrayList<>();
    }


    public String getNom() {
        return nom;
    }

    public List<biens> getBiensGeres() {
        return biensGeres;
    }

    public void setBiensGeres(List<biens> biensGeres) {
        this.biensGeres = biensGeres;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }
}
