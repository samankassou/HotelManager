package hotelmanager.model;

import java.util.Date;

public class Reservation {
    private String id;
    private Date dateArrivee;
    private Date dateDepart;
    private int prixTotal;
    private Client client;
    private Chambre chambre;
    private Facture facture;

    public Reservation(String id, Date dateArrivee, Date dateDepart, int prixTotal, Client client, Chambre chambre) {
        this.id = id;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.prixTotal = prixTotal;
        this.client = client;
        this.chambre = chambre;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(Date dateArrivee) { this.dateArrivee = dateArrivee; }

    public Date getDateDepart() { return dateDepart; }
    public void setDateDepart(Date dateDepart) { this.dateDepart = dateDepart; }

    public int getPrixTotal() { return prixTotal; }
    public void setPrixTotal(int prixTotal) { this.prixTotal = prixTotal; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }

    public Facture getFacture() { return facture; }
    public void setFacture(Facture facture) { this.facture = facture; }
}