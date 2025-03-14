package hotelmanager.ui;
import hotelmanager.data.DataManager;
import hotelmanager.model.Chambre;
import hotelmanager.model.Client;
import hotelmanager.model.Reservation;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReservationsPanel extends JPanel {
    private DataManager dataManager;
    private JFrame parentFrame;
    private JTable reservationsTable;
    
    public ReservationsPanel(DataManager dataManager, JFrame parentFrame) {
        this.dataManager = dataManager;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout());
        createComponents();
        refreshTable();
    }
    
    private void createComponents() {
        // Panel de titre
        JLabel titleLabel = new JLabel("Gestion des Réservations", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Ajouter une barre d'outils pour les options de tri
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Trier par"));
        
        JButton triClientBtn = new JButton("Client");
        triClientBtn.addActionListener(e -> trierReservationsParClient());
        
        JButton triDateBtn = new JButton("Date d'arrivée");
        triDateBtn.addActionListener(e -> trierReservationsParDate());
        
        JButton triChambreBtn = new JButton("Chambre");
        triChambreBtn.addActionListener(e -> trierReservationsParChambre());
        
        JButton resetTriBtn = new JButton("Réinitialiser");
        resetTriBtn.addActionListener(e -> refreshTable());
        
        filterPanel.add(triClientBtn);
        filterPanel.add(triDateBtn);
        filterPanel.add(triChambreBtn);
        filterPanel.add(resetTriBtn);
        
        // Panel pour le titre et les filtres
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(filterPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Tableau des réservations
        String[] columnNames = {"ID", "Client", "Chambre", "Date d'arrivée", "Date de départ", "Prix total (FCFA)"};
        Object[][] data = new Object[0][6];
        
        reservationsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel d'actions
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        // Mettre le bouton supprimer en rouge
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
       
        
        addButton.addActionListener(e -> showAddReservationDialog());
        editButton.addActionListener(e -> modifierReservationSelectionnee());
        deleteButton.addActionListener(e -> supprimerReservationSelectionnee());
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        List<Reservation> reservations = dataManager.getReservations();
        afficherReservationsTriees(reservations);
    }
    
    private void trierReservationsParClient() {
        List<Reservation> reservationsTriees = new ArrayList<>(dataManager.getReservations());
        
        // Trier la liste par nom de client
        reservationsTriees.sort((r1, r2) -> {
            String nomClient1 = r1.getClient().getNom() + " " + r1.getClient().getPrenom();
            String nomClient2 = r2.getClient().getNom() + " " + r2.getClient().getPrenom();
            return nomClient1.compareTo(nomClient2);
        });
        
        // Afficher les réservations triées
        afficherReservationsTriees(reservationsTriees);
    }
    
    private void trierReservationsParDate() {
        List<Reservation> reservationsTriees = new ArrayList<>(dataManager.getReservations());
        
        // Trier la liste par date d'arrivée
        reservationsTriees.sort((r1, r2) -> r1.getDateArrivee().compareTo(r2.getDateArrivee()));
        
        // Afficher les réservations triées
        afficherReservationsTriees(reservationsTriees);
    }
    
    private void trierReservationsParChambre() {
        List<Reservation> reservationsTriees = new ArrayList<>(dataManager.getReservations());
        
        // Trier la liste par numéro de chambre
        reservationsTriees.sort((r1, r2) -> 
            Integer.compare(r1.getChambre().getNumero(), r2.getChambre().getNumero()));
        
        // Afficher les réservations triées
        afficherReservationsTriees(reservationsTriees);
    }
    
    private void afficherReservationsTriees(List<Reservation> reservationsTriees) {
        // Création d'un nouveau modèle de données pour le tableau
        Object[][] data = new Object[reservationsTriees.size()][6];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int i = 0; i < reservationsTriees.size(); i++) {
            Reservation reservation = reservationsTriees.get(i);
            data[i][0] = reservation.getId();
            data[i][1] = reservation.getClient().getNom() + " " + reservation.getClient().getPrenom();
            data[i][2] = "Chambre " + reservation.getChambre().getNumero();
            data[i][3] = sdf.format(reservation.getDateArrivee());
            data[i][4] = sdf.format(reservation.getDateDepart());
            data[i][5] = reservation.getPrixTotal() + " FCFA";
        }
        
        // Création et application du nouveau modèle au tableau
        String[] columnNames = {"ID", "Client", "Chambre", "Date d'arrivée", "Date de départ", "Prix total (FCFA)"};
        reservationsTable.setModel(new DefaultTableModel(data, columnNames));
    }
    
    private void showAddReservationDialog() {
        // Vérifier qu'il existe des clients et des chambres disponibles
        if (dataManager.getClients().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Vous devez d'abord créer au moins un client.", 
                "Aucun client", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Chambre> chambresDisponibles = dataManager.getChambresDisponibles();
        if (chambresDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Aucune chambre disponible pour une réservation.", 
                "Aucune chambre disponible", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(parentFrame, "Ajouter une réservation", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID de réservation
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField("RES" + (dataManager.getReservations().size() + 1));
        formPanel.add(idField);
        
        // Sélection du client
        formPanel.add(new JLabel("Client:"));
        JComboBox<Client> clientCombo = new JComboBox<>(dataManager.getClients().toArray(new Client[0]));
        formPanel.add(clientCombo);
        
        // Sélection de la chambre (uniquement chambres disponibles)
        formPanel.add(new JLabel("Chambre:"));
        JComboBox<Chambre> chambreCombo = new JComboBox<>(chambresDisponibles.toArray(new Chambre[0]));
        formPanel.add(chambreCombo);
        
        // Date d'arrivée
        formPanel.add(new JLabel("Date d'arrivée (jj/mm/aaaa):"));
        JTextField dateArriveeField = new JTextField();
        formPanel.add(dateArriveeField);
        
        // Date de départ
        formPanel.add(new JLabel("Date de départ (jj/mm/aaaa):"));
        JTextField dateDepartField = new JTextField();
        formPanel.add(dateDepartField);
        
        // Prix total
        formPanel.add(new JLabel("Prix total (FCFA):"));
        JTextField prixTotalField = new JTextField("0");
        formPanel.add(prixTotalField);
        
        // Ajouter un listener pour calculer automatiquement le prix total
        ActionListener calculPrixListener = e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Chambre chambreSelectionnee = (Chambre) chambreCombo.getSelectedItem();
                
                if (chambreSelectionnee != null && !dateArriveeField.getText().isEmpty() && !dateDepartField.getText().isEmpty()) {
                    Date dateArrivee = sdf.parse(dateArriveeField.getText());
                    Date dateDepart = sdf.parse(dateDepartField.getText());
                    
                    // Vérifier que la date de départ est après la date d'arrivée
                    if (!dateDepart.before(dateArrivee)) {
                        // Calculer le nombre de jours
                        long diff = dateDepart.getTime() - dateArrivee.getTime();
                        int nbJours = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 pour inclure le jour d'arrivée
                        
                        // Calculer le prix total
                        int prixTotal = chambreSelectionnee.getTarif() * nbJours;
                        prixTotalField.setText(String.valueOf(prixTotal));
                    }
                }
            } catch (ParseException ex) {
                // Si les dates ne sont pas encore valides, ne rien faire
            }
        };
        
        // Ajouter le listener aux champs concernés
        chambreCombo.addActionListener(calculPrixListener);
        dateArriveeField.addActionListener(calculPrixListener);
        dateDepartField.addActionListener(calculPrixListener);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            String id = idField.getText();
            Client clientSelectionne = (Client) clientCombo.getSelectedItem();
            Chambre chambreSelectionnee = (Chambre) chambreCombo.getSelectedItem();
            String dateArriveeStr = dateArriveeField.getText();
            String dateDepartStr = dateDepartField.getText();
            String prixTotalStr = prixTotalField.getText();
            
            // Vérifier que les champs obligatoires sont remplis
            if (id.isEmpty() || dateArriveeStr.isEmpty() || dateDepartStr.isEmpty() || prixTotalStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez remplir tous les champs.",
                    "Champs manquants",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Parser les dates et le prix
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateArrivee = sdf.parse(dateArriveeStr);
                Date dateDepart = sdf.parse(dateDepartStr);
                int prixTotal = Integer.parseInt(prixTotalStr);
                
                // Vérifier que la date de départ est après la date d'arrivée
                if (dateDepart.before(dateArrivee)) {
                    JOptionPane.showMessageDialog(dialog,
                        "La date de départ doit être après la date d'arrivée.",
                        "Erreur de dates",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Vérifier que l'ID est unique
                if (!dataManager.isReservationIdUnique(id)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Une réservation avec cet ID existe déjà.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Créer et ajouter la nouvelle réservation
                Reservation nouvelleReservation = new Reservation(id, dateArrivee, dateDepart, 
                        prixTotal, clientSelectionne, chambreSelectionnee);
                dataManager.addReservation(nouvelleReservation);
                
                // Rafraîchir l'affichage
                refreshTable();
                dialog.dispose();
                
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Format de date incorrect. Utilisez le format jj/mm/aaaa.",
                    "Erreur de format",
                    JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Format de prix incorrect.",
                    "Erreur de format",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    private void modifierReservationSelectionnee() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(parentFrame,
                "Veuillez sélectionner une réservation à modifier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Récupérer la réservation sélectionnée
        List<Reservation> reservations = dataManager.getReservations();
        if (reservations.size() <= selectedRow) {
            JOptionPane.showMessageDialog(parentFrame,
                "La réservation sélectionnée n'existe plus.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Reservation reservationAModifier = reservations.get(selectedRow);
        
        // Créer une liste de chambres disponibles (incluant celle actuellement réservée)
        List<Chambre> chambresDisponibles = new ArrayList<>();
        for (Chambre chambre : dataManager.getChambres()) {
            if (chambre.isDisponible() || chambre.getNumero() == reservationAModifier.getChambre().getNumero()) {
                chambresDisponibles.add(chambre);
            }
        }
        
        JDialog dialog = new JDialog(parentFrame, "Modifier une réservation", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID de réservation (non modifiable)
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField(reservationAModifier.getId());
        idField.setEditable(false);
        idField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(idField);
        
        // Sélection du client
        formPanel.add(new JLabel("Client:"));
        JComboBox<Client> clientCombo = new JComboBox<>(dataManager.getClients().toArray(new Client[0]));
        clientCombo.setSelectedItem(reservationAModifier.getClient());
        formPanel.add(clientCombo);
        
        // Sélection de la chambre
        formPanel.add(new JLabel("Chambre:"));
        JComboBox<Chambre> chambreCombo = new JComboBox<>(chambresDisponibles.toArray(new Chambre[0]));
        chambreCombo.setSelectedItem(reservationAModifier.getChambre());
        formPanel.add(chambreCombo);
        
        // Date d'arrivée
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        formPanel.add(new JLabel("Date d'arrivée (jj/mm/aaaa):"));
        JTextField dateArriveeField = new JTextField(sdf.format(reservationAModifier.getDateArrivee()));
        formPanel.add(dateArriveeField);
        
        // Date de départ
        formPanel.add(new JLabel("Date de départ (jj/mm/aaaa):"));
        JTextField dateDepartField = new JTextField(sdf.format(reservationAModifier.getDateDepart()));
        formPanel.add(dateDepartField);
        
        // Prix total
        formPanel.add(new JLabel("Prix total (FCFA):"));
        JTextField prixTotalField = new JTextField(String.valueOf(reservationAModifier.getPrixTotal()));
        formPanel.add(prixTotalField);
        
        // Ajouter un listener pour calculer automatiquement le prix total
        ActionListener calculPrixListener = e -> {
            try {
                Chambre chambreSelectionnee = (Chambre) chambreCombo.getSelectedItem();
                
                if (!dateArriveeField.getText().isEmpty() && !dateDepartField.getText().isEmpty()) {
                    Date dateArrivee = sdf.parse(dateArriveeField.getText());
                    Date dateDepart = sdf.parse(dateDepartField.getText());
                    
                    // Vérifier que la date de départ est après la date d'arrivée
                    if (!dateDepart.before(dateArrivee)) {
                        // Calculer le nombre de jours
                        long diff = dateDepart.getTime() - dateArrivee.getTime();
                        int nbJours = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 pour inclure le jour d'arrivée
                        
                        // Calculer le prix total
                        int prixTotal = chambreSelectionnee.getTarif() * nbJours;
                        prixTotalField.setText(String.valueOf(prixTotal));
                    }
                }
            } catch (ParseException ex) {
                // Si les dates ne sont pas encore valides, ne rien faire
            }
        };
        
        // Ajouter le listener aux champs concernés
        chambreCombo.addActionListener(calculPrixListener);
        dateArriveeField.addActionListener(calculPrixListener);
        dateDepartField.addActionListener(calculPrixListener);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            Client clientSelectionne = (Client) clientCombo.getSelectedItem();
            Chambre nouvelleChambSelectionnee = (Chambre) chambreCombo.getSelectedItem();
            Chambre ancienneChambSelectionnee = reservationAModifier.getChambre();
            String dateArriveeStr = dateArriveeField.getText();
            String dateDepartStr = dateDepartField.getText();
            String prixTotalStr = prixTotalField.getText();
            
            // Vérifier que les champs obligatoires sont remplis
            if (dateArriveeStr.isEmpty() || dateDepartStr.isEmpty() || prixTotalStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez remplir tous les champs.",
                    "Champs manquants",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Parser les dates et le prix
                Date dateArrivee = sdf.parse(dateArriveeStr);
                Date dateDepart = sdf.parse(dateDepartStr);
                int prixTotal = Integer.parseInt(prixTotalStr);
                
                // Vérifier que la date de départ est après la date d'arrivée
                if (dateDepart.before(dateArrivee)) {
                    JOptionPane.showMessageDialog(dialog,
                        "La date de départ doit être après la date d'arrivée.",
                        "Erreur de dates",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Si la chambre a changé, mettre à jour les disponibilités
                if (nouvelleChambSelectionnee.getNumero() != ancienneChambSelectionnee.getNumero()) {
                    dataManager.updateReservationChambre(reservationAModifier, nouvelleChambSelectionnee);
                }
                
                // Mettre à jour les propriétés de la réservation
                reservationAModifier.setClient(clientSelectionne);
                reservationAModifier.setDateArrivee(dateArrivee);
                reservationAModifier.setDateDepart(dateDepart);
                reservationAModifier.setPrixTotal(prixTotal);
                
                // Rafraîchir l'affichage
                refreshTable();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(parentFrame,
                    "La réservation a été modifiée avec succès.",
                    "Modification réussie",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Format de date incorrect. Utilisez le format jj/mm/aaaa.",
                    "Erreur de format",
                    JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Format de prix incorrect.",
                    "Erreur de format",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    private void supprimerReservationSelectionnee() {
        int row = reservationsTable.getSelectedRow();
        if (row >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(parentFrame, 
                "Êtes-vous sûr de vouloir supprimer cette réservation ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmation == JOptionPane.YES_OPTION) {
                dataManager.removeReservation(row);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, 
                "Veuillez sélectionner une réservation à supprimer.", 
                "Aucune sélection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}