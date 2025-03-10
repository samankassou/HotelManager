package hotelmanager;

public class Chambre {
    private int numero;
    private String type;
    private double tarif;
    private boolean disponible;

    public Chambre(int numero, String type, double tarif, boolean disponible) {
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

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Chambre " + numero + " (" + type + ")";
    }
}