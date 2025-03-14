package hotelmanager.ui;
import hotelmanager.data.DataManager;
import hotelmanager.model.Chambre;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ChambresPanel extends JPanel {
    private DataManager dataManager;
    private JFrame parentFrame;
    private JTable chambresTable;
    
    public ChambresPanel(DataManager dataManager, JFrame parentFrame) {
        this.dataManager = dataManager;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout());
        createComponents();
        refreshTable();
    }
    
    private void createComponents() {
        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Chambres", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Tableau des chambres
        String[] columnNames = {"Numéro", "Type", "Tarif (FCFA)", "Disponible"};
        Object[][] data = new Object[0][4];
        
        chambresTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(chambresTable);
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
       
        
        addButton.addActionListener(e -> showAddChambreDialog());
        editButton.addActionListener(e -> modifierChambreSelectionnee());
        deleteButton.addActionListener(e -> supprimerChambreSelectionnee());
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        List<Chambre> chambres = dataManager.getChambres();
        Object[][] data = new Object[chambres.size()][4];
        
        for (int i = 0; i < chambres.size(); i++) {
            Chambre chambre = chambres.get(i);
            data[i][0] = chambre.getNumero();
            data[i][1] = chambre.getType();
            data[i][2] = chambre.getTarif();
            data[i][3] = chambre.isDisponible();
        }
        
        String[] columnNames = {"Numéro", "Type", "Tarif (FCFA)", "Disponible"};
        chambresTable.setModel(new DefaultTableModel(data, columnNames));
    }
    
    private void showAddChambreDialog() {
        JDialog dialog = new JDialog(parentFrame, "Ajouter une chambre", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Numéro:"));
        JTextField numeroField = new JTextField();
        formPanel.add(numeroField);
        
        formPanel.add(new JLabel("Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Simple", "Double", "Suite"});
        formPanel.add(typeCombo);
        
        formPanel.add(new JLabel("Tarif (FCFA):"));
        JTextField tarifField = new JTextField();
        formPanel.add(tarifField);
        
        formPanel.add(new JLabel("Disponible:"));
        JCheckBox disponibleCheck = new JCheckBox();
        disponibleCheck.setSelected(true);
        formPanel.add(disponibleCheck);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            try {
                int numero = Integer.parseInt(numeroField.getText());
                String type = (String) typeCombo.getSelectedItem();
                int tarif = Integer.parseInt(tarifField.getText());
                boolean disponible = disponibleCheck.isSelected();
                
                // Vérifier que le numéro de chambre n'existe pas déjà
                if (!dataManager.isChambreNumeroUnique(numero)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Une chambre avec ce numéro existe déjà.", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Créer et ajouter la nouvelle chambre
                Chambre nouvelleChambre = new Chambre(numero, type, tarif, disponible);
                dataManager.addChambre(nouvelleChambre);
                
                // Rafraîchir l'affichage
                refreshTable();
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Veuillez saisir des valeurs numériques valides pour le numéro et le tarif.", 
                    "Erreur de saisie", 
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
    
    private void modifierChambreSelectionnee() {
        int selectedRow = chambresTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(parentFrame,
                "Veuillez sélectionner une chambre à modifier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Récupérer la chambre sélectionnée
        Chambre chambreAModifier = dataManager.getChambre(selectedRow);
        
        // Vérifier si la chambre est actuellement réservée
        boolean estReservee = dataManager.isChambreReservee(chambreAModifier);
        
        // Créer la boîte de dialogue de modification
        JDialog dialog = new JDialog(parentFrame, "Modifier une chambre", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Le numéro de chambre n'est pas modifiable (clé primaire)
        formPanel.add(new JLabel("Numéro:"));
        JTextField numeroField = new JTextField(String.valueOf(chambreAModifier.getNumero()));
        numeroField.setEditable(false);
        numeroField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(numeroField);
        
        // Type de chambre
        formPanel.add(new JLabel("Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Simple", "Double", "Suite"});
        typeCombo.setSelectedItem(chambreAModifier.getType());
        formPanel.add(typeCombo);
        
        // Tarif
        formPanel.add(new JLabel("Tarif (FCFA):"));
        JTextField tarifField = new JTextField(String.valueOf(chambreAModifier.getTarif()));
        formPanel.add(tarifField);
        
        // Disponibilité
        formPanel.add(new JLabel("Disponible:"));
        JCheckBox disponibleCheck = new JCheckBox();
        disponibleCheck.setSelected(chambreAModifier.isDisponible());
        
        // Désactiver la possibilité de rendre disponible une chambre réservée
        if (estReservee) {
            disponibleCheck.setEnabled(false);
            disponibleCheck.setSelected(false);
            disponibleCheck.setToolTipText("Cette chambre est actuellement réservée");
        }
        
        formPanel.add(disponibleCheck);
        
        // Boutons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                int tarif = Integer.parseInt(tarifField.getText());
                boolean disponible = disponibleCheck.isSelected();
                
                // Empêcher de rendre disponible une chambre réservée
                if (estReservee && disponible) {
                    JOptionPane.showMessageDialog(dialog,
                        "Impossible de rendre disponible une chambre actuellement réservée.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Mettre à jour les propriétés de la chambre
                chambreAModifier.setType(type);
                chambreAModifier.setTarif(tarif);
                chambreAModifier.setDisponible(disponible);
                
                // Rafraîchir l'affichage
                refreshTable();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(parentFrame,
                    "La chambre a été modifiée avec succès.",
                    "Modification réussie",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez saisir une valeur numérique valide pour le tarif.",
                    "Erreur de saisie",
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
    
    private void supprimerChambreSelectionnee() {
        int row = chambresTable.getSelectedRow();
        if (row >= 0) {
            Chambre chambreASupprimer = dataManager.getChambre(row);
            
            // Vérifier si la chambre est actuellement réservée
            if (dataManager.isChambreReservee(chambreASupprimer)) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Impossible de supprimer une chambre actuellement réservée.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirmation = JOptionPane.showConfirmDialog(parentFrame, 
                "Êtes-vous sûr de vouloir supprimer cette chambre ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmation == JOptionPane.YES_OPTION) {
                dataManager.removeChambre(row);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, 
                "Veuillez sélectionner une chambre à supprimer.", 
                "Aucune sélection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}