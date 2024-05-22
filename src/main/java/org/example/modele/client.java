package org.example.modele;

import org.example.enumeration.TypeBien;
import org.example.enumeration.TypeClient;

import java.util.ArrayList;
import java.util.List;

public class client {

    private  String nom ;
    private List<Transaction> transactions;
    private org.example.enumeration.TypeClient TypeClient;

    public client(String nom, TypeClient TypeClient) {
        this.nom = nom;
        this.TypeClient = TypeClient;
        this.transactions = new ArrayList<>();

    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public TypeClient getType() {
        return TypeClient;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void ajouterTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    public void setType(TypeClient TypeClient) {
        this.TypeClient = TypeClient;
    }

    @Override
    public String toString() {
        return "client{" +
                ", nom='" + nom + '\'' +
                ", TypeClient=" + TypeClient +
                '}';
    }
}
