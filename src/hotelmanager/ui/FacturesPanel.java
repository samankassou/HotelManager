package hotelmanager.ui;

import hotelmanager.data.DataManager;
import hotelmanager.model.Facture;
import hotelmanager.model.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class FacturesPanel extends JPanel {
    private DataManager dataManager;
    private JFrame parentFrame;
    private JTable facturesTable;
    private JPanel detailsPanel;
    
    public FacturesPanel(DataManager dataManager, JFrame parentFrame) {
        this.dataManager = dataManager;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        createComponents();
        refreshTable();
    }
    
    private void createComponents() {
        // Panel de titre
        JLabel titleLabel = new JLabel("Gestion des Factures", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        // Créer un JSplitPane pour diviser l'écran entre la liste et les détails
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6); // 60% de la largeur pour la liste
        
        // Panel de gauche - Liste des factures
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        
        // Options de filtrage
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtrer par"));
        
        JButton toutesBtn = new JButton("Toutes");
        JButton payeesBtn = new JButton("Payées");
        JButton nonPayeesBtn = new JButton("Non payées");
        
        toutesBtn.addActionListener(e -> refreshTable());
        payeesBtn.addActionListener(e -> filtrerFactures(true));
        nonPayeesBtn.addActionListener(e -> filtrerFactures(false));
        
        filterPanel.add(toutesBtn);
        filterPanel.add(payeesBtn);
        filterPanel.add(nonPayeesBtn);
        
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Tableau des factures
        String[] columnNames = {"ID", "Client", "Montant (FCFA)", "Date", "Statut"};
        facturesTable = new JTable(new DefaultTableModel(columnNames, 0)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        
        // Personnaliser l'affichage pour les statuts
        facturesTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                String status = (String) value;
                if ("Payée".equals(status)) {
                    c.setForeground(new Color(0, 128, 0)); // Vert
                } else {
                    c.setForeground(new Color(178, 34, 34)); // Rouge
                }
                
                return c;
            }
        });
        
        // Ajouter un listener pour afficher les détails quand on clique sur une facture
        facturesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = facturesTable.getSelectedRow();
                if (row >= 0) {
                    afficherDetailsFacture(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(facturesTable);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de droite - Détails de la facture
        detailsPanel = new JPanel(new BorderLayout(5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails de la Facture"));
        
        // Initialiser avec un message par défaut
        JLabel defaultLabel = new JLabel("Sélectionnez une facture pour voir les détails", JLabel.CENTER);
        detailsPanel.add(defaultLabel, BorderLayout.CENTER);
        
        // Configurer le JSplitPane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(detailsPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Panel d'actions en bas
        JPanel actionsPanel = new JPanel();
        JButton genererPdfBtn = new JButton("Générer PDF");
        JButton marquerPayeeBtn = new JButton("Marquer comme payée");
        JButton modifierBtn = new JButton("Modifier");
        JButton annulerBtn = new JButton("Annuler facture");
        
        genererPdfBtn.addActionListener(e -> genererPdfFactureSelectionnee());
        marquerPayeeBtn.addActionListener(e -> marquerFactureCommePaye());
        modifierBtn.addActionListener(e -> modifierFactureSelectionnee());
        annulerBtn.addActionListener(e -> annulerFactureSelectionnee());
        
        actionsPanel.add(genererPdfBtn);
        actionsPanel.add(marquerPayeeBtn);
        actionsPanel.add(modifierBtn);
        actionsPanel.add(annulerBtn);
        
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        List<Facture> factures = dataManager.getFactures();
        afficherFacturesFiltrees(factures);
    }
    
    private void filtrerFactures(boolean estPayee) {
        List<Facture> toutesFactures = dataManager.getFactures();
        List<Facture> facturesFiltrees = new ArrayList<>();
        
        for (Facture facture : toutesFactures) {
            if (facture.isEstPayee() == estPayee) {
                facturesFiltrees.add(facture);
            }
        }
        
        afficherFacturesFiltrees(facturesFiltrees);
    }
    
    private void afficherFacturesFiltrees(List<Facture> facturesFiltrees) {
        DefaultTableModel model = (DefaultTableModel) facturesTable.getModel();
        model.setRowCount(0); // Vider le tableau
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Facture facture : facturesFiltrees) {
            String clientNom = facture.getReservation().getClient().getNom() + " " + 
                              facture.getReservation().getClient().getPrenom();
            String status = facture.isEstPayee() ? "Payée" : "Non payée";
            
            model.addRow(new Object[]{
                facture.getId(),
                clientNom,
                facture.getMontantTotal() + " FCFA",
                sdf.format(facture.getDateFacturation()),
                status
            });
        }
    }
    
    private void afficherDetailsFacture(int factureIndex) {
        Facture facture = dataManager.getFacture(factureIndex);
        if (facture == null) return;
        
        // Vider le panel de détails
        detailsPanel.removeAll();
        
        // Créer un nouveau panel avec GridBagLayout pour plus de flexibilité
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Ajouter les informations de base
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // ID de facture et date
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("<html><b>Facture N°:</b></html>"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(new JLabel(facture.getId()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("<html><b>Date:</b></html>"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(new JLabel(sdf.format(facture.getDateFacturation())), gbc);
        
        // Informations sur le client
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("<html><b>Client:</b></html>"), gbc);
        
        gbc.gridx = 1;
        String clientInfo = facture.getReservation().getClient().getPrenom() + " " + 
                           facture.getReservation().getClient().getNom();
        infoPanel.add(new JLabel(clientInfo), gbc);
        
        // Informations sur la chambre
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("<html><b>Chambre:</b></html>"), gbc);
        
        gbc.gridx = 1;
        String chambreInfo = "N°" + facture.getReservation().getChambre().getNumero() + 
                            " (" + facture.getReservation().getChambre().getType() + ")";
        infoPanel.add(new JLabel(chambreInfo), gbc);
        
        // Dates de séjour
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("<html><b>Période:</b></html>"), gbc);
        
        gbc.gridx = 1;
        String periode = sdf.format(facture.getReservation().getDateArrivee()) + 
                        " au " + sdf.format(facture.getReservation().getDateDepart());
        infoPanel.add(new JLabel(periode), gbc);
        
        // Ligne de séparation
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1; // Réinitialiser gridwidth
        
        // Détails des montants
        gbc.gridx = 0; gbc.gridy = 6;
        infoPanel.add(new JLabel("<html><b>Montant de base:</b></html>"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(new JLabel(facture.getMontantBase() + " FCFA"), gbc);
        
        // Frais supplémentaires
        int row = 7;
        for (Entry<String, Integer> frais : facture.getFraisSupplementaires().entrySet()) {
            gbc.gridx = 0; gbc.gridy = row;
            infoPanel.add(new JLabel("<html><i>" + frais.getKey() + ":</i></html>"), gbc);
            
            gbc.gridx = 1;
            infoPanel.add(new JLabel(frais.getValue() + " FCFA"), gbc);
            row++;
        }
        
        // Ligne de séparation
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1; // Réinitialiser gridwidth
        row++;
        
        // Montant total
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(new JLabel("<html><b>TOTAL:</b></html>"), gbc);
        
        gbc.gridx = 1;
        JLabel totalLabel = new JLabel(facture.getMontantTotal() + " FCFA");
        totalLabel.setFont(new Font(totalLabel.getFont().getName(), Font.BOLD, 14));
        infoPanel.add(totalLabel, gbc);
        row++;
        
        // Statut de paiement
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(new JLabel("<html><b>Statut:</b></html>"), gbc);
        
        gbc.gridx = 1;
        JLabel statusLabel = new JLabel(facture.isEstPayee() ? "Payée" : "Non payée");
        statusLabel.setForeground(facture.isEstPayee() ? new Color(0, 128, 0) : new Color(178, 34, 34));
        infoPanel.add(statusLabel, gbc);
        row++;
        
        // Méthode de paiement (si payée)
        if (facture.isEstPayee()) {
            gbc.gridx = 0; gbc.gridy = row;
            infoPanel.add(new JLabel("<html><b>Méthode de paiement:</b></html>"), gbc);
            
            gbc.gridx = 1;
            infoPanel.add(new JLabel(facture.getMethodePaiement()), gbc);
        }
        
        // Ajouter le panel d'informations au panel de détails
        detailsPanel.add(new JScrollPane(infoPanel), BorderLayout.CENTER);
        
        // Rafraîchir l'affichage
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
    
    private void genererPdfFactureSelectionnee() {
        int row = facturesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(parentFrame,
                "Veuillez sélectionner une facture.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Facture facture = dataManager.getFacture(row);
        if (facture == null) return;
        
        // À ce stade, normalement, nous utiliserions iText pour générer un vrai PDF
        // C'est une simulation pour l'instant
        facture.genererPDF();
        
        JOptionPane.showMessageDialog(parentFrame,
            "Le PDF de la facture " + facture.getId() + " a été généré avec succès.\n" +
            "Il se trouve dans le répertoire des documents de l'application.",
            "PDF généré",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void marquerFactureCommePaye() {
        int row = facturesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(parentFrame,
                "Veuillez sélectionner une facture.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Facture facture = dataManager.getFacture(row);
        if (facture == null) return;
        
        if (facture.isEstPayee()) {
            JOptionPane.showMessageDialog(parentFrame,
                "Cette facture est déjà marquée comme payée.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Créer une boîte de dialogue pour la méthode de paiement
        JDialog dialog = new JDialog(parentFrame, "Méthode de Paiement", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Montant à payer:"));
        JLabel montantLabel = new JLabel(facture.getMontantTotal() + " FCFA");
        montantLabel.setFont(new Font(montantLabel.getFont().getName(), Font.BOLD, 14));
        formPanel.add(montantLabel);
        
        formPanel.add(new JLabel("Méthode de paiement:"));
        JComboBox<String> methodePaiementCombo = new JComboBox<>(
            new String[]{"Espèces", "Carte bancaire", "Virement bancaire", "Chèque", "Mobile Money"}
        );
        formPanel.add(methodePaiementCombo);
        
        JPanel buttonPanel = new JPanel();
        JButton validerBtn = new JButton("Valider");
        JButton annulerBtn = new JButton("Annuler");
        
        validerBtn.addActionListener(e -> {
            String methodePaiement = (String) methodePaiementCombo.getSelectedItem();
            
            facture.setEstPayee(true);
            facture.setMethodePaiement(methodePaiement);
            
            // Rafraîchir l'affichage
            refreshTable();
            afficherDetailsFacture(row);
            
            dialog.dispose();
            
            JOptionPane.showMessageDialog(parentFrame,
                "La facture a été marquée comme payée.",
                "Paiement enregistré",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        annulerBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(validerBtn);
        buttonPanel.add(annulerBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private void modifierFactureSelectionnee() {

    }

    private void annulerFactureSelectionnee(){
        
    }
}