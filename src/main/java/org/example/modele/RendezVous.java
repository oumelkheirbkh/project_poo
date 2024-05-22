package org.example.modele;

import org.example.modele.agents;
import org.example.modele.biens;
import org.example.modele.client;

import java.util.Date;

public class RendezVous {
    private int id;
    private Date date;
    private String heure;
    private biens bien;
    private org.example.modele.client client;
    private agents agent;

    public RendezVous(int id, Date date, String heure, biens bien, client client, agents agent) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.bien = bien;
        this.client = client;
        this.agent = agent;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public biens getBien() {
        return bien;
    }

    public void setBien(biens bien) {
        this.bien = bien;
    }

    public client getClient() {
        return client;
    }

    public void setClient(client client) {
        this.client = client;
    }

    public agents getAgent() {
        return agent;
    }

    public void setAgent(agents agent) {
        this.agent = agent;
    }
}
