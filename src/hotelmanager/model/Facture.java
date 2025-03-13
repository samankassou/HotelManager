package hotelmanager.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Facture {
    private String id;
    private Date dateFacturation;
    private int montantTotal;
    private int montantBase;
    private Map<String, Integer> fraisSupplementaires;
    private Reservation reservation;
    private boolean estPayee;
    private String methodePaiement;

    public Facture(String id, Date dateFacturation, Reservation reservation) {
        this.id = id;
        this.dateFacturation = dateFacturation;
        this.reservation = reservation;
        this.fraisSupplementaires = new HashMap<>();
        this.estPayee = false;
        this.methodePaiement = "";
        
        // Calcul automatique du montant de base selon la durée et le tarif
        this.montantBase = calculerMontantBase();
        this.montantTotal = this.montantBase;
    }
    
    // Calcul du montant de base (tarif * nombre de nuits)
    private int calculerMontantBase() {
        // Calcul du nombre de nuits entre les dates d'arrivée et de départ
        long diffMillis = reservation.getDateDepart().getTime() - reservation.getDateArrivee().getTime();
        int nombreNuits = (int) TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
        
        // En cas de séjour d'une seule journée
        if (nombreNuits == 0) nombreNuits = 1;
        
        // Montant = tarif de la chambre * nombre de nuits
        return reservation.getChambre().getTarif() * nombreNuits;
    }
    
    // Ajouter un frais supplémentaire
    public void ajouterFraisSupplementaire(String description, int montant) {
        fraisSupplementaires.put(description, montant);
        recalculerMontantTotal();
    }
    
    // Supprimer un frais supplémentaire
    public void supprimerFraisSupplementaire(String description) {
        fraisSupplementaires.remove(description);
        recalculerMontantTotal();
    }
    
    // Recalculer le montant total en fonction des frais supplémentaires
    private void recalculerMontantTotal() {
        int sommeSupplements = fraisSupplementaires.values().stream().mapToInt(Integer::intValue).sum();
        this.montantTotal = this.montantBase + sommeSupplements;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getDateFacturation() { return dateFacturation; }
    public void setDateFacturation(Date dateFacturation) { this.dateFacturation = dateFacturation; }

    public int getMontantTotal() { return montantTotal; }
    
    public int getMontantBase() { return montantBase; }
    
    public Map<String, Integer> getFraisSupplementaires() { return fraisSupplementaires; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { 
        this.reservation = reservation;
        this.montantBase = calculerMontantBase();
        recalculerMontantTotal();
    }
    
    public boolean isEstPayee() { return estPayee; }
    public void setEstPayee(boolean estPayee) { this.estPayee = estPayee; }
    
    public String getMethodePaiement() { return methodePaiement; }
    public void setMethodePaiement(String methodePaiement) { this.methodePaiement = methodePaiement; }

    public void genererPDF() {
        // Simulation de la génération du PDF (sera implémenté avec iText)
        System.out.println("Génération du PDF pour la facture " + id);
    }
    
    @Override
    public String toString() {
        return "Facture " + id + " - " + montantTotal + " FCFA" + (estPayee ? " (Payée)" : " (Non payée)");
    }
}