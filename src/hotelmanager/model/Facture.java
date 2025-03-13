package hotelmanager.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

// Imports iText
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

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
        // Définir le chemin du fichier PDF
        String outputDir = System.getProperty("user.home") + File.separator + "Documents";
        File directory = new File(outputDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        String filePath = outputDir + File.separator + "Facture_" + this.id + ".pdf";
        
        try {
            // Initialisation du document PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // En-tête de la facture
            Paragraph header = new Paragraph("HÔTEL MANAGER")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(header);
            
            Paragraph subHeader = new Paragraph("Facture")
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(subHeader);
            
            // Informations générales
            document.add(new Paragraph("\n"));
            
            // Tableau des informations de facture
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            infoTable.setWidth(UnitValue.createPercentValue(100));
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            addInfoRow(infoTable, "N° Facture:", this.id);
            addInfoRow(infoTable, "Date:", sdf.format(this.dateFacturation));
            addInfoRow(infoTable, "Client:", this.reservation.getClient().getPrenom() + " " + this.reservation.getClient().getNom());
            addInfoRow(infoTable, "Adresse client:", this.reservation.getClient().getAdresse());
            addInfoRow(infoTable, "Téléphone:", this.reservation.getClient().getTelephone());
            addInfoRow(infoTable, "Email:", this.reservation.getClient().getEmail());
            
            document.add(infoTable);
            
            // Détails de réservation
            document.add(new Paragraph("\nDétails de la réservation").setBold());
            
            Table reservationTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            reservationTable.setWidth(UnitValue.createPercentValue(100));
            
            addInfoRow(reservationTable, "Chambre:", "N°" + this.reservation.getChambre().getNumero() + " (" + this.reservation.getChambre().getType() + ")");
            addInfoRow(reservationTable, "Période:", sdf.format(this.reservation.getDateArrivee()) + " au " + sdf.format(this.reservation.getDateDepart()));
            
            // Calcul du nombre de nuits
            long diffMillis = this.reservation.getDateDepart().getTime() - this.reservation.getDateArrivee().getTime();
            int nombreNuits = (int) TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
            if (nombreNuits == 0) nombreNuits = 1;
            
            addInfoRow(reservationTable, "Nombre de nuits:", String.valueOf(nombreNuits));
            addInfoRow(reservationTable, "Tarif par nuit:", this.reservation.getChambre().getTarif() + " FCFA");
            
            document.add(reservationTable);
            
            // Détails des montants
            document.add(new Paragraph("\nDétail de facturation").setBold());
            
            Table montantsTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}));
            montantsTable.setWidth(UnitValue.createPercentValue(100));
            
            addMontantRow(montantsTable, "Montant de base (hébergement):", this.montantBase + " FCFA");
            
            // Ajouter les frais supplémentaires
            for (Entry<String, Integer> frais : this.fraisSupplementaires.entrySet()) {
                addMontantRow(montantsTable, frais.getKey() + ":", frais.getValue() + " FCFA");
            }
            
            // Total
            Cell totalLabelCell = new Cell()
                    .add(new Paragraph("TOTAL:").setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER);
                    
            Cell totalValueCell = new Cell()
                    .add(new Paragraph(this.montantTotal + " FCFA").setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER);
                    
            montantsTable.addCell(totalLabelCell);
            montantsTable.addCell(totalValueCell);
            
            document.add(montantsTable);
            
            // Statut de paiement
            document.add(new Paragraph("\n"));
            Paragraph statusParagraph = new Paragraph("Statut: " + (this.estPayee ? "PAYÉE" : "NON PAYÉE"))
                    .setBold()
                    .setFontColor(this.estPayee ? new DeviceRgb(0, 128, 0) : new DeviceRgb(178, 34, 34));
            document.add(statusParagraph);
            
            // Méthode de paiement si payée
            if (this.estPayee && !this.methodePaiement.isEmpty()) {
                document.add(new Paragraph("Méthode de paiement: " + this.methodePaiement));
            }
            
            // Pied de page
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Nous vous remercions de votre confiance!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic());
            document.add(new Paragraph("Pour toute question concernant cette facture, veuillez nous contacter.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10));
            
            // Fermer le document
            document.close();
            
            System.out.println("Facture PDF générée avec succès : " + filePath);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }
    
    // Méthode utilitaire pour ajouter une ligne d'information
    private void addInfoRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setBold())
                .setBorder(Border.NO_BORDER);
        
        Cell valueCell = new Cell()
                .add(new Paragraph(value))
                .setBorder(Border.NO_BORDER);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    // Méthode utilitaire pour ajouter une ligne de montant
    private void addMontantRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER);
        
        Cell valueCell = new Cell()
                .add(new Paragraph(value))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    @Override
    public String toString() {
        return "Facture " + id + " - " + montantTotal + " FCFA" + (estPayee ? " (Payée)" : " (Non payée)");
    }
}