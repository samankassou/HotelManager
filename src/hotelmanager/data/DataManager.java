package hotelmanager.data;
import hotelmanager.model.Chambre;
import hotelmanager.model.Client;
import hotelmanager.model.Reservation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataManager {
    private List<Client> clients;
    private List<Chambre> chambres;
    private List<Reservation> reservations;
    
    public DataManager() {
        clients = new ArrayList<>();
        chambres = new ArrayList<>();
        reservations = new ArrayList<>();
        
        // Charger des données de démonstration
        loadDemoData();
    }
    
    // Méthodes pour les clients
    public List<Client> getClients() {
        return clients;
    }
    
    public void addClient(Client client) {
        clients.add(client);
    }
    
    public boolean removeClient(Client client) {
        return clients.remove(client);
    }
    
    public boolean removeClient(int index) {
        if (index >= 0 && index < clients.size()) {
            clients.remove(index);
            return true;
        }
        return false;
    }
    
    public Client getClient(int index) {
        if (index >= 0 && index < clients.size()) {
            return clients.get(index);
        }
        return null;
    }
    
    public boolean isClientIdUnique(String id) {
        return !clients.stream().anyMatch(c -> c.getId().equals(id));
    }
    
    // Méthodes pour les chambres
    public List<Chambre> getChambres() {
        return chambres;
    }
    
    public List<Chambre> getChambresDisponibles() {
        List<Chambre> chambresDisponibles = new ArrayList<>();
        for (Chambre chambre : chambres) {
            if (chambre.isDisponible()) {
                chambresDisponibles.add(chambre);
            }
        }
        return chambresDisponibles;
    }
    
    public void addChambre(Chambre chambre) {
        chambres.add(chambre);
    }
    
    public boolean removeChambre(Chambre chambre) {
        return chambres.remove(chambre);
    }
    
    public boolean removeChambre(int index) {
        if (index >= 0 && index < chambres.size()) {
            chambres.remove(index);
            return true;
        }
        return false;
    }
    
    public Chambre getChambre(int index) {
        if (index >= 0 && index < chambres.size()) {
            return chambres.get(index);
        }
        return null;
    }
    
    public boolean isChambreNumeroUnique(int numero) {
        return !chambres.stream().anyMatch(c -> c.getNumero() == numero);
    }
    
    public boolean isChambreReservee(Chambre chambre) {
        return reservations.stream()
                .anyMatch(r -> r.getChambre().getNumero() == chambre.getNumero());
    }
    
    // Méthodes pour les réservations
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        // Mise à jour de l'état de la chambre
        reservation.getChambre().setDisponible(false);
    }
    
    public boolean removeReservation(Reservation reservation) {
        // Rendre la chambre disponible
        reservation.getChambre().setDisponible(true);
        return reservations.remove(reservation);
    }
    
    public boolean removeReservation(int index) {
        if (index >= 0 && index < reservations.size()) {
            // Rendre la chambre disponible
            reservations.get(index).getChambre().setDisponible(true);
            reservations.remove(index);
            return true;
        }
        return false;
    }
    
    public Reservation getReservation(int index) {
        if (index >= 0 && index < reservations.size()) {
            return reservations.get(index);
        }
        return null;
    }
    
    public boolean isReservationIdUnique(String id) {
        return !reservations.stream().anyMatch(r -> r.getId().equals(id));
    }
    
    public void updateReservationChambre(Reservation reservation, Chambre nouvelleChambre) {
        Chambre ancienneChambre = reservation.getChambre();
        if (ancienneChambre.getNumero() != nouvelleChambre.getNumero()) {
            // Libérer l'ancienne chambre
            ancienneChambre.setDisponible(true);
            // Réserver la nouvelle chambre
            nouvelleChambre.setDisponible(false);
            // Mettre à jour la réservation
            reservation.setChambre(nouvelleChambre);
        }
    }
    
    // Charger des données de démonstration
    private void loadDemoData() {
        // Clients de démonstration
        clients.add(new Client("CL001", "Dupont", "Jean", "1 rue de Paris", "0123456789", "jean.dupont@email.com"));
        clients.add(new Client("CL002", "Martin", "Sophie", "15 avenue des Champs", "0987654321", "sophie.martin@email.com"));
        clients.add(new Client("CL003", "Dubois", "Pierre", "8 boulevard Victor Hugo", "0665544332", "pierre.dubois@email.com"));
        
        // Chambres de démonstration
        chambres.add(new Chambre(101, "Simple", 25000, true));
        chambres.add(new Chambre(102, "Double", 35000, true));
        chambres.add(new Chambre(103, "Suite", 50000, true));
        
        // Réservations de démonstration
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateArrivee1 = sdf.parse("20/03/2025");
            Date dateDepart1 = sdf.parse("25/03/2025");
            Reservation reservation1 = new Reservation("RES001", dateArrivee1, dateDepart1, 125000, clients.get(0), chambres.get(0));
            reservations.add(reservation1);
            chambres.get(0).setDisponible(false);
            
            Date dateArrivee2 = sdf.parse("15/04/2025");
            Date dateDepart2 = sdf.parse("20/04/2025");
            Reservation reservation2 = new Reservation("RES002", dateArrivee2, dateDepart2, 175000, clients.get(1), chambres.get(1));
            reservations.add(reservation2);
            chambres.get(1).setDisponible(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}