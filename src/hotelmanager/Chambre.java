package hotelmanager;

public class Chambre {
    private int numero;
    private String type;
    private int tarif; // Changer de double à int pour FCFA
    private boolean disponible;

    public Chambre(int numero, String type, int tarif, boolean disponible) {
        this.numero = numero;
        this.type = type;
        this.tarif = tarif;
        this.disponible = disponible;
    }

    // Getters et Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getTarif() { return tarif; } // Changé de double à int
    public void setTarif(int tarif) { this.tarif = tarif; } // Changé de double à int

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Chambre " + numero + " (" + type + ")";
    }
}