package hotelmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelManager {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Création de quelques chambres
        Chambre chambre1 = new Chambre(101, "Simple", 50.0, true);
        Chambre chambre2 = new Chambre(102, "Double", 80.0, true);

        // Création d'un client
        Client client1 = new Client("C001", "Dupont", "Jean", "123 Rue Principale", "0123456789", "jean.dupont@example.com");

        try {
            Date dateArrivee = sdf.parse("15/05/2025");
            Date dateDepart = sdf.parse("20/05/2025");

            // Création d'une réservation
            Reservation reservation1 = new Reservation("R001", dateArrivee, dateDepart, 0.0, client1, chambre1);

            // Calcul du prix total (exemple : tarif de la chambre * nombre de nuits)
            long diffInMillies = dateDepart.getTime() - dateArrivee.getTime();
            long nbNuits = diffInMillies / (1000 * 60 * 60 * 24);
            double prixTotal = chambre1.getTarif() * nbNuits;
            reservation1.setPrixTotal(prixTotal);

            // Mise à jour de la disponibilité de la chambre
            chambre1.setDisponible(false);

            // Ajout de la réservation au client
            client1.ajouterReservation(reservation1);

            // Génération d'une facture pour la réservation
            Facture facture1 = new Facture("F001", new Date(), prixTotal, reservation1);
            reservation1.setFacture(facture1);

            // Simulation de la génération du PDF de la facture
            facture1.genererPDF();

            // Affichage des informations
            System.out.println("Réservation créée pour le client : " + client1);
            System.out.println("Chambre réservée : " + chambre1);
            System.out.println("Prix total : " + prixTotal + " €");

        } catch (ParseException e) {
            System.err.println("Erreur dans le format de la date.");
            e.printStackTrace();
        }
    }
}