package org.example;

import org.example.controller.*;
import org.example.enumeration.TypeBien;
import org.example.enumeration.TypeClient;
import org.example.modele.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import static org.example.controller.GestionAgents.modifierBiensAgent;
import static org.example.controller.GestionRendezVous.rendezVousList;
import static org.example.controller.GestionTransactions.creerNouvelleTransaction;

public class Main {

    public static void main(String[] args) throws ParseException, SQLException {
        Scanner scanner = new Scanner(System.in);

        // Récupérer la connexion à partir de l'outil de connexion intégré dans IntelliJ
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/agence", "root", "");
        // Création de l'instance de GestionAgents en lui passant la connexion
        GestionAgents gestionAgents = new GestionAgents(connection);
        GestionBiens gestionBiens = new GestionBiens(connection);
        GestionClients gestionClients = new GestionClients() ;
        client client1 = new client("client1" , TypeClient.ACHETEUR);
        client client2 = new client("client1" , TypeClient.ACHETEUR);
        client client3 = new client("client1" , TypeClient.ACHETEUR);
        client client4 = new client("client1" , TypeClient.ACHETEUR);
        gestionClients.ajouterClient("client1" , TypeClient.ACHETEUR, connection);
        gestionClients.ajouterClient("client2" , TypeClient.ACHETEUR, connection);
        gestionClients.ajouterClient("client3" , TypeClient.ACHETEUR, connection);
        gestionClients.ajouterClient("client4" , TypeClient.ACHETEUR, connection);
        Date dateTransaction = new Date(System.currentTimeMillis());
        GestionTransactions gestionTransactions = new GestionTransactions();

        GestionRendezVous gestionRendezVous = new GestionRendezVous();
        int choix_principale = 0;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Géstion des biens immobiliers");
            System.out.println("2. Géstion des transaction");
            System.out.println("3. Géstion des cliens");
            System.out.println("4. Géstion des agents");
            System.out.println("5. Géstion des Rendez-vous");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            choix_principale = scanner.nextInt();
            switch (choix_principale){
                case 1 :
                    int choix;
                    do {
                        System.out.println("\nMenu:");
                        System.out.println("1. Ajouter un bien immobilier");
                        System.out.println("2. Afficher tous les biens immobiliers");
                        System.out.println("3. Modifier un bien immobilier");
                        System.out.println("4. Supprimer un bien immobilier");
                        System.out.println("5. Rechercher un bien immobilier");
                        System.out.println("0. Quitter");
                        System.out.print("Choix : ");
                        choix = scanner.nextInt();

                        switch (choix) {
                            case 1:
                                System.out.println("Ajout d'un nouveau bien immobilier :");
                                System.out.print("ID : ");
                                int id = scanner.nextInt();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante
                                System.out.println("Type : (APPARTEMENT, VILLA, MAISON, HANGAR)");
                                String typeStr = scanner.nextLine().toUpperCase(); // Convertir en majuscules pour correspondre aux valeurs de l'énumération
                                TypeBien type = TypeBien.valueOf(typeStr);
                                System.out.print("Taille : ");
                                double taille = scanner.nextDouble();
                                System.out.print("Prix : ");
                                double prix = scanner.nextDouble();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante
                                System.out.print("Localisation : ");
                                String localisation = scanner.nextLine();
                                System.out.print("Description : ");
                                String description = scanner.nextLine();

                                biens nouveauBien = new biens(id, type, taille, prix, localisation, description);
                                gestionBiens.ajouterbiens(nouveauBien);
                                break;

                            case 2:
                                gestionBiens.afficherbiens();
                                break;
                            case 3:
                                System.out.print("Entrez l'ID du bien à modifier : ");
                                int idModifier = scanner.nextInt();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante
                                System.out.print("Entrez la nouvelle description : ");
                                String nouvelleDescription = scanner.nextLine();
                                gestionBiens.modifierbiens(idModifier, nouvelleDescription);
                                break;
                            case 4:
                                System.out.print("Entrez l'ID du bien à supprimer : ");
                                int idSupprimer = scanner.nextInt();
                                gestionBiens.supprimerbiens(idSupprimer);
                                break;
                            // Recherche de biens immobiliers
                            case 5:
                                System.out.print("Entrez le type de critère de recherche (prix, type, localisation) : ");
                                String typeCritere = scanner.next();
                                System.out.print("Entrez la valeur du critère de recherche : ");
                                String critere = scanner.next();
                                gestionBiens.rechercherBiens(critere, typeCritere );

                                break;

                            case 0:
                                System.out.println("Fin du programme.");
                                break;
                            default:
                                System.out.println("Choix invalide.");
                        }
                    } while (choix != 0);
                    break;
                case 2:
                    int choixTransaction;
                    do {
                        System.out.println("\nMenu:");
                        System.out.println("1. Ajouter une nouvelle transaction");
                        System.out.println("2. Afficher toutes les transactions");
                        System.out.println("0. Quitter");
                        System.out.print("Votre choix : ");
                        choixTransaction = scanner.nextInt();
                        scanner.nextLine(); // Pour consommer la nouvelle ligne après le choix

                        switch (choixTransaction) {
                            case 1:
                                creerNouvelleTransaction(scanner, gestionClients, gestionTransactions, gestionBiens , gestionAgents);
                                break;
                            case 2:
                                gestionTransactions.afficherTransactions();
                                break;
                            case 0:
                                System.out.println("Fin du programme.");
                                break;
                            default:
                                System.out.println("Choix invalide. Veuillez saisir un nombre entre 0 et 2.");
                        }
                    } while (choixTransaction != 0);
                    break;

                case 3 :
                    int choix_trois;
                    do {
                        System.out.println("\nMenu:");
                        System.out.println("1. Ajouter un client");
                        System.out.println("2. Modifier un client");
                        System.out.println("3. Supprimer un client");
                        System.out.println("4. Afficher tous les clients");
                        System.out.println("5. Ajouter une demande d'un client");
                        System.out.println("6. Afficher les demandes et preferances des clients");
                        System.out.println("0. Quitter");
                        System.out.print("Votre choix_trois : ");
                        choix_trois = scanner.nextInt();
                        scanner.nextLine(); // Pour vider le buffer

                        switch (choix_trois) {
                            case 1:
                                System.out.print("Entrez le nom du client : ");
                                String nomClient = scanner.nextLine();
                                System.out.print("Entrez le TypeClient du client (ACHETEUR, LOCATAIRE, VENDEUR, BAILLEUR) : ");
                                String typeClientStr = scanner.nextLine();
                                TypeClient typeClient = TypeClient.valueOf(typeClientStr.toUpperCase());
                                gestionClients.ajouterClient(nomClient , typeClient, connection);
                                System.out.println("Client ajouté avec succès !");
                                break;
                            case 2:
                                System.out.print("Entrez le nom du client à modifier : ");
                                String nomClientModif = scanner.nextLine();
                                client clientModif = gestionClients.obtenirClientParNom(nomClientModif);
                                if (clientModif != null) {
                                    System.out.print("Entrez le nouveau nom du client : ");
                                    String nouveauNom = scanner.nextLine();
                                    System.out.print("Entrez le nouveau TypeClient du client (ACHETEUR, LOCATAIRE, VENDEUR, BAILLEUR) : ");
                                    String nouveauTypeStr = scanner.nextLine();
                                    TypeClient nouveauTypeClient = TypeClient.valueOf(nouveauTypeStr.toUpperCase());
                                    gestionClients.modifierClient(clientModif, nouveauNom, nouveauTypeClient);
                                    System.out.println("Client modifié avec succès !");
                                } else {
                                    System.out.println("Aucun client trouvé avec le nom spécifié.");
                                }
                                break;
                            case 3:
                                System.out.print("Entrez le nom du client à supprimer : ");
                                String nomClientSuppr = scanner.nextLine();
                                client clientSuppr = gestionClients.obtenirClientParNom(nomClientSuppr);
                                if (clientSuppr != null) {
                                    gestionClients.supprimerClient(clientSuppr);
                                    System.out.println("Client supprimé avec succès !");
                                } else {
                                    System.out.println("Aucun client trouvé avec le nom spécifié.");
                                }
                                break;
                            case 4:
                                gestionClients.afficherClients();
                                break;
                            case 5:
                                System.out.println("Ajouter une demande client :");
                                System.out.print("Nom du client : ");
                                String nomClientdemande = scanner.nextLine();
                                client client = gestionClients.obtenirClientParNom(nomClientdemande);
                                if (client != null) {
                                    System.out.print("Type de bien recherché : ");
                                    String typeBienRecherche = scanner.nextLine();
                                    System.out.print("Budget maximum : ");
                                    double budgetMax = scanner.nextDouble();
                                    System.out.print("Description du bien recherché : ");
                                    String description = scanner.nextLine();
                                    scanner.nextLine(); // Consommer la nouvelle ligne restante après le double

                                }else {
                                    System.out.println("Le client n'existe pas.");
                                }
                                break;
                            case 6 :
                                gestionClients.afficherDemandesClients();
                                break;
                            case 0:
                                System.out.println("Fin du programme.");
                                break;
                            default:
                                System.out.println("choix invalide. Veuillez saisir un nombre entre 1 et 5.");
                        }
                    } while (choix_trois != 0);
                    break;
                case 4 :
                    int choix_deux = 0;
                    do {
                        System.out.println("\nMenu gestion des agents immobiliers:");
                        System.out.println("1. Ajouter un agent immobilier");
                        System.out.println("2. Afficher tous les agents immobiliers");
                        System.out.println("3. Afficher la liste des biens d'un agent");
                        System.out.println("4. Affecter un bien immobilier a un agent donné");
                        System.out.println("5. Supprimer un agent donné");
                        System.out.println("6. Modifier un agent donné");
                        System.out.println("7. Supprimer un bien immobilier d'un agent donné");
                        System.out.println("8. Modifier un bien immobilier d'un agent donné");
                        System.out.println("0. Retour");
                        System.out.print("Choix : ");
                        choix_deux = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne

                        switch (choix_deux) {
                            case 1:
                                System.out.print("Entrez le nom de l'agent immobilier : ");
                                String nomAgent = scanner.nextLine();
                                agents agent = new agents(nomAgent);
                                gestionAgents.ajouterAgent(agent);
                                break;
                            case 2:
                                // Affichage de tous les agents immobiliers
                                gestionAgents.afficherAgents();
                                break;
                            case 3 :
                                System.out.print("Entrez le nom de l'agent immobilier : ");
                                String nomAgentBien = scanner.nextLine();
                                agents agentBiem = gestionAgents.obtenirAgentParNom(nomAgentBien);
                                if (agentBiem != null) {
                                    gestionAgents.afficherBiensAgent(agentBiem);
                                } else {
                                    System.out.println("Aucun agent immobilier trouvé avec le nom spécifié.");
                                }
                                break;
                            case 4 :
                                // Affecter un bien à un agent immobilier
                                System.out.print("Entrez le nom de l'agent immobilier : ");
                                String agentaffect = scanner.nextLine();
                                agents agentaffecté = gestionAgents.obtenirAgentParNom(agentaffect);
                                if (agentaffect != null) {
                                    System.out.print("Entrez l'ID du bien immobilier : ");
                                    int idBien = scanner.nextInt();
                                    scanner.nextLine(); // Pour consommer le retour à la ligne

                                    biens bien = gestionBiens.obtenirBienParID(idBien);
                                    if (bien != null) {
                                        gestionAgents.affecterBienAAgent(bien, agentaffecté);
                                        System.out.println("Le bien a été affecté à l'agent avec succès !");
                                    } else {
                                        System.out.println("Aucun bien immobilier trouvé avec l'ID spécifié.");
                                    }
                                } else {
                                    System.out.println("Aucun agent immobilier trouvé avec le nom spécifié.");
                                }
                                break;
                            case 5:
                                System.out.print("Entrez le nom de l'agent immobilier à supprimer : ");
                                String nomAgentSuppression = scanner.nextLine();
                                agents agentSuppression = gestionAgents.obtenirAgentParNom(nomAgentSuppression);
                                if (agentSuppression != null) {
                                    gestionAgents.supprimerAgent(agentSuppression);
                                    System.out.println("Agent supprimé avec succès !");
                                } else {
                                    System.out.println("Aucun agent trouvé avec le nom spécifié.");
                                }
                                break;
                            case 6:
                                System.out.print("Entrez le nom de l'agent immobilier à modifier : ");
                                String nomAgentModification = scanner.nextLine();
                                agents agentModification = gestionAgents.obtenirAgentParNom(nomAgentModification);
                                if (agentModification != null) {
                                    System.out.print("Entrez le nouveau nom de l'agent immobilier : ");
                                    String nouveauNom = scanner.nextLine();
                                    gestionAgents.modifierAgent(agentModification, nouveauNom);
                                    System.out.println("Agent modifié avec succès !");
                                } else {
                                    System.out.println("Aucun agent trouvé avec le nom spécifié.");
                                }
                                break;
                            case 7:
                                // Affecter un bien à un agent immobilier
                                System.out.print("Entrez le nom de l'agent immobilier : ");
                                String agentsupbien = scanner.nextLine();
                                agents agentasup = gestionAgents.obtenirAgentParNom(agentsupbien);

                                if (agentasup != null) {
                                    System.out.print("Entrez l'ID du bien immobilier : ");
                                    int idBien = scanner.nextInt();
                                    scanner.nextLine();
                                    biens bien = gestionBiens.obtenirBienParID(idBien);
                                    if (bien != null) {
                                        gestionAgents.supprimerBienGere(agentasup , bien);
                                        System.out.println("Le bien a été supprimé de la liste des bien de cet agent avec succès !");
                                    } else {
                                        System.out.println("Aucun bien immobilier trouvé avec l'ID spécifié.");
                                    }
                                } else {
                                    System.out.println("Aucun agent immobilier trouvé avec le nom spécifié.");
                                }
                                break;
                            case 8:
                                // modifier la liste des bien d'un agents
                                modifierBiensAgent(scanner, gestionAgents, gestionBiens);
                                break;
                            case 0 :
                                System.out.println("Retour au menu principal.");
                                break;
                            default:
                                System.out.println("Choix invalide.");
                        }
                    } while (choix_deux != 0);
                    break;

                case 5 :
                    int choix_rendzvous = 0 ;
                    do {
                        System.out.println("\n=== Menu Gestion des Rendez-vous ===");
                        System.out.println("1. Ajouter un rendez-vous");
                        System.out.println("2. Afficher tous les rendez-vous");
                        System.out.println("3. Modifier un rendez-vous");
                        System.out.println("3. Supprimer un rendez-vous");
                        System.out.println("0. Retour au menu principal");
                        System.out.print("Choix : ");
                        choix_rendzvous = scanner.nextInt();
                        scanner.nextLine();
                        switch ( choix_rendzvous ){
                            case 1:
                                // Demander à l'utilisateur de saisir les informations nécessaires
                                System.out.println("Entrez l'ID du rendez-vous : ");
                                int id = scanner.nextInt();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante après nextInt()

                                Calendar calendar = Calendar.getInstance();

                                System.out.println("Entrez l'année du rendez-vous : ");
                                int annee = scanner.nextInt();
                                calendar.set(Calendar.YEAR, annee);

                                System.out.println("Entrez le mois du rendez-vous (1-12) : ");
                                int mois = scanner.nextInt();
                                calendar.set(Calendar.MONTH, mois - 1); // Mois - 1 car les mois dans Calendar commencent à 0

                                System.out.println("Entrez le jour du rendez-vous (1-31) : ");
                                int jour = scanner.nextInt();
                                calendar.set(Calendar.DAY_OF_MONTH, jour);

                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante après nextInt()

                                System.out.println("Entrez l'heure du rendez-vous (hh:mm) : ");
                                String heure = scanner.nextLine();

                                System.out.println("Entrez l'ID du bien associé au rendez-vous : ");
                                gestionBiens.afficherbiens();
                                int idBien = scanner.nextInt();
                                biens bien = gestionBiens.obtenirBienParID(idBien);
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante après nextInt()

                                System.out.println("Entrez le nom du client associé au rendez-vous : ");
                                gestionClients.afficherClients();
                                String nomClient = scanner.nextLine();
                                client client = gestionClients.obtenirClientParNom(nomClient);

                                System.out.println("Entrez le nom de l'agent immobilier associé au rendez-vous : ");
                                gestionAgents.afficherAgents();
                                String nomAgent = scanner.nextLine();
                                agents agent = gestionAgents.obtenirAgentParNom(nomAgent);

                                RendezVous rendezVous = new RendezVous(id, calendar.getTime(), heure, bien, client, agent);
                                GestionRendezVous.ajouterRendezVous(rendezVous);
                                break;
                            case 2:
                                gestionRendezVous.afficherRendezVous();
                                break;
                            case 3 :
                                System.out.println("Entrez l'ID du rendez-vous à modifier : ");
                                int idRendezVous = scanner.nextInt();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante après nextInt()
                                RendezVous rendezVousAModifier = null;
                                for (RendezVous rendezVouss : rendezVousList) {
                                    if (rendezVouss.getId() == idRendezVous) {
                                        rendezVousAModifier = rendezVouss;
                                        break;
                                    }
                                }
                                // Afficher le menu de modification du rendez-vous
                                System.out.println("Qu'est-ce que vous voulez modifier ?");
                                System.out.println("1. Date du rendez-vous");
                                System.out.println("2. Heure du rendez-vous");
                                System.out.println("3. Date et heure du rendez-vous");
                                System.out.print("Choix : ");
                                int choixModification = scanner.nextInt();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante après nextInt()

                                switch (choixModification) {
                                    case 1:
                                        // Modifier la date du rendez-vous
                                        System.out.println("Entrez la nouvelle date du rendez-vous (format : dd/MM/yyyy) : ");
                                        String nouvelleDateStr = scanner.nextLine();
                                        // Convertir la nouvelle date en objet Date
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        java.util.Date nouvelleDate = null;
                                        try {
                                            nouvelleDate = dateFormat.parse(nouvelleDateStr);
                                            rendezVousAModifier.setDate(nouvelleDate);
                                            System.out.println("La date du rendez-vous a été modifiée avec succès.");
                                        } catch (ParseException e) {
                                            System.out.println("Format de date incorrect. Le rendez-vous n'a pas été modifié.");
                                        }
                                        break;
                                    case 2:
                                        // Modifier l'heure du rendez-vous
                                        System.out.println("Entrez la nouvelle heure du rendez-vous : ");
                                        String nouvelleHeure = scanner.nextLine();
                                        rendezVousAModifier.setHeure(nouvelleHeure);
                                        System.out.println("L'heure du rendez-vous a été modifiée avec succès.");
                                        break;
                                    case 3:
                                        // Modifier à la fois la date et l'heure du rendez-vous
                                        System.out.println("Entrez la nouvelle date du rendez-vous (format : dd/MM/yyyy) : ");
                                        String nouvelleDateStr2 = scanner.nextLine();
                                        // Convertir la nouvelle date en objet Date
                                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                                        java.util.Date nouvelleDate2 = null;
                                        try {
                                            nouvelleDate2 = dateFormat2.parse(nouvelleDateStr2);
                                            rendezVousAModifier.setDate(nouvelleDate2);
                                            System.out.println("Entrez la nouvelle heure du rendez-vous : ");
                                            String nouvelleHeure2 = scanner.nextLine();
                                            rendezVousAModifier.setHeure(nouvelleHeure2);
                                            System.out.println("La date et l'heure du rendez-vous ont été modifiées avec succès.");
                                        } catch (ParseException e) {
                                            System.out.println("Format de date incorrect. Le rendez-vous n'a pas été modifié.");
                                        }
                                        break;
                                    default:
                                        System.out.println("Choix invalide. Le rendez-vous n'a pas été modifié.");
                                }
                                break;
                            case 4:
                                // Demander à l'utilisateur l'ID du rendez-vous à supprimer
                                System.out.print("Entrez l'ID du rendez-vous à supprimer : ");
                                int idRendezVousASupprimer = scanner.nextInt();
                                scanner.nextLine(); // Pour consommer la nouvelle ligne restante après nextInt()

                                boolean rendezVousTrouve = false;
                                for (RendezVous rendezVoous : rendezVousList) {
                                    if (rendezVoous.getId() == idRendezVousASupprimer) {
                                        // Supprimer le rendez-vous de la liste
                                        gestionRendezVous.supprimerRendezVous(idRendezVousASupprimer);
                                        rendezVousTrouve = true;
                                        System.out.println("Le rendez-vous a été supprimé avec succès.");
                                        break; // Sortir de la boucle une fois que le rendez-vous est supprimé
                                    }
                                }
                                if (!rendezVousTrouve) {
                                    System.out.println("Aucun rendez-vous trouvé avec l'ID spécifié. Rien n'a été supprimé.");
                                }

                                break;
                            case 0:
                                System.out.println("Retour au menu principal");
                                break;
                        }
                    } while (choix_rendzvous !=0);
                    break;
                case 0 :
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: ");
            }
        }
        while (choix_principale !=0);
        scanner.close();
    }
}