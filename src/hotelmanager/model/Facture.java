package hotelmanager.model;

import java.util.Date;

public class Facture {
    private String id;
    private Date dateFacturation;
    private double montant;
    private Reservation reservation;

    public Facture(String id, Date dateFacturation, double montant, Reservation reservation) {
        this.id = id;
        this.dateFacturation = dateFacturation;
        this.montant = montant;
        this.reservation = reservation;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getDateFacturation() { return dateFacturation; }
    public void setDateFacturation(Date dateFacturation) { this.dateFacturation = dateFacturation; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public void genererPDF() {
        // Simulation de la génération du PDF
        System.out.println("Génération du PDF pour la facture " + id);
    }
}