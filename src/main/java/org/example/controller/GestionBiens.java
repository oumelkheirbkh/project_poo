package org.example.controller;

import org.example.modele.biens;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionBiens {
    private List<biens> biensImmobilierList;
    private Connection connection;

    public GestionBiens(Connection connection) {
        this.connection = connection;
        biensImmobilierList = new ArrayList<>();
    }

    public Connection getConnection() {
        return connection;
    }

    public void ajouterbiens(biens bien) throws SQLException {
        biensImmobilierList.add(bien);
        // Ajouter le bien à la base de données
        String sql = "INSERT INTO biens (id, type, taille, prix, localisation, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bien.getId());
            preparedStatement.setString(2, String.valueOf(bien.getType()));
            preparedStatement.setDouble(3, bien.getTaille());
            preparedStatement.setDouble(4, bien.getPrix());
            preparedStatement.setString(5, bien.getLocalisation());
            preparedStatement.setString(6, bien.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    public void modifierbiens(int id, String nouvelleDescription) throws SQLException {
        // Modifier le bien dans la liste
        for (biens bien : biensImmobilierList) {
            if (bien.getId() == id) {
                bien.setDescription(nouvelleDescription);
                // Modifier le bien dans la base de données
                String sql = "UPDATE biens SET description = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, nouvelleDescription);
                    preparedStatement.setInt(2, id);
                    preparedStatement.executeUpdate();
                }
                break;
            }
        }
    }

    public void modifierbiens(biens bien) throws SQLException {
        String sql = "UPDATE biens SET type=?, taille=?, prix=?, localisation=?, description=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, bien.getType().name());
            preparedStatement.setDouble(2, bien.getTaille());
            preparedStatement.setDouble(3, bien.getPrix());
            preparedStatement.setString(4, bien.getLocalisation());
            preparedStatement.setString(5, bien.getDescription());
            preparedStatement.setInt(6, bien.getId());
            preparedStatement.executeUpdate();
        }
    }
    public void supprimerbiens(int id) throws SQLException {
        // Supprimer le bien de la liste
        biensImmobilierList.removeIf(bien -> bien.getId() == id);
        // Supprimer le bien de la base de données
        String sql = "DELETE FROM biens WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    public List<biens> getBiensImmobilierList() {
        return biensImmobilierList;
    }

    public void afficherbiens() throws SQLException {
        System.out.println("ID\tType\tTaille\tPrix\tLocalisation\tDescription");
        System.out.println("----------------------------------------------------------");
        String sql = "SELECT * FROM biens";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                double taille = resultSet.getDouble("taille");
                double prix = resultSet.getDouble("prix");
                String localisation = resultSet.getString("localisation");
                String description = resultSet.getString("description");
                System.out.printf("%d\t%s\t%.2f\t%.2f\t%s\t%s\n", id, type, taille, prix, localisation, description);
            }
        }

    }
    private void afficherTableTempBiens() throws SQLException {
        String selectQuery = "SELECT * FROM temp_biens";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = selectStatement.executeQuery()) {
            System.out.println("ID\tType\tTaille\tPrix\tLocalisation\tDescription");
            System.out.println("----------------------------------------------------------");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double taille = resultSet.getDouble("taille");
                double prix = resultSet.getDouble("prix");
                String localisation = resultSet.getString("localisation");
                String description = resultSet.getString("description");

                // Afficher chaque ligne de la table temporaire
                System.out.printf("%d\t%.2f\t%.2f\t%s\t%s\n", id, taille, prix, localisation, description);
            }
        }
    }

    public void rechercherBiens(String critere, String typeCritere) throws SQLException {
        String sql;
        String truncateQuery = "TRUNCATE TABLE temp_biens";
        try (PreparedStatement truncateStatement = connection.prepareStatement(truncateQuery)) {
            truncateStatement.executeUpdate();
        }
        switch (typeCritere) {
            case "prix":
                sql = "INSERT INTO temp_biens (id, taille,type, prix, localisation, description) SELECT id, taille,type , prix, localisation, description FROM biens WHERE prix = ?";
                break;
            case "type":
                sql = "INSERT INTO temp_biens (id, taille, type ,prix, localisation, description) SELECT id, taille,type, prix, localisation, description FROM biens WHERE type = ?";
                break;
            case "localisation":
                sql = "INSERT INTO temp_biens (id, taille , type, prix, localisation, description) SELECT id, taille , type, prix, localisation, description FROM biens WHERE localisation = ?";
                break;
            default:
                // Critère invalide, retourner
                return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Paramétrer la requête SQL avec le critère
            preparedStatement.setString(1, critere);
            preparedStatement.executeUpdate();

            // Afficher le contenu de la table temporaire
            String selectQuery = "SELECT id, taille, type, prix, localisation, description FROM temp_biens";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = selectStatement.executeQuery()) {
                System.out.println("ID\tTaille\tPrix\tLocalisation\tDescription");
                System.out.println("----------------------------------------------------------");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    double taille = resultSet.getDouble("taille");
                    String type  = resultSet.getString("type");
                    double prix = resultSet.getDouble("prix");
                    String localisation = resultSet.getString("localisation");
                    String description = resultSet.getString("description");

                    // Afficher chaque ligne de la table temporaire
                    System.out.printf("%d\t%.2f\t%.2f\t%s\t%s\n", id, taille, prix, localisation, description);
                }
            }
            // Vider la table temporaire


        }
    }

    // Méthode utilitaire pour vérifier si une chaîne est numérique


    public biens obtenirBienParID(int id) {
        for (biens bien : biensImmobilierList) {
            if (bien.getId() == id) {
                return bien;
            }
        }
        return null; // Si aucun bien n'a été trouvé avec l'ID donné
    }
}
