package org.example.modele;

import java.util.Date;

public class Interaction {
    private Date date;
    private int idClient;
    private String description;

    public Interaction(Date date, int idClient, String description) {
        this.date = date;
        this.idClient = idClient;
        this.description = description;
    }

    // Getters et setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
