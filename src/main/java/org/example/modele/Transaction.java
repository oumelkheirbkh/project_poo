package org.example.modele;
import org.example.enumeration.TypeTransaction;

import java.util.Date;

public class Transaction {
        private int id;
        private Date date;
        private double prix;
        private TypeTransaction type;
        private biens bien;
        private org.example.modele.client client;
        private agents agent ;

        // Constructeur, getters, setters...

        public Transaction(int id, TypeTransaction type, biens bien, client client , Date date ,double prix , agents agent) {
            this.id = id;
            this.type = type;
            this.bien = bien;
            this.client = client;
            this.date = date;
            this.prix= prix;
            this.agent=agent;
        }

    public Transaction(int idTransaction, String nomTransaction, client client) {

    }

    public agents getAgent() {
        return agent;
    }

    public void setAgent(agents agent) {
        this.agent = agent;
    }

    public int getId() {
            return id;
        }

    public Date getDate() {
        return date;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public client getClient() {
            return client;
        }
        public TypeTransaction getType(){
            return type;
        }
        public biens getBien(){
            return bien;
        }

}

