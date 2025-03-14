package hotelmanager.ui;
import hotelmanager.data.DataManager;
import hotelmanager.model.Client;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientsPanel extends JPanel {
    private DataManager dataManager;
    private JFrame parentFrame;
    private JTable clientsTable;
    
    public ClientsPanel(DataManager dataManager, JFrame parentFrame) {
        this.dataManager = dataManager;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout());
        createComponents();
        refreshTable();
    }
    
    private void createComponents() {
        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Clients", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Tableau des clients
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
        Object[][] data = new Object[0][5];
        
        clientsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel d'actions
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        
        addButton.addActionListener(e -> showAddClientDialog());
        editButton.addActionListener(e -> modifierClientSelectionne());
        deleteButton.addActionListener(e -> supprimerClientSelectionne());

        // Mettre le bouton supprimer en rouge
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
       
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        List<Client> clients = dataManager.getClients();
        Object[][] data = new Object[clients.size()][5];
        
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            data[i][0] = client.getId();
            data[i][1] = client.getNom();
            data[i][2] = client.getPrenom();
            data[i][3] = client.getTelephone();
            data[i][4] = client.getEmail();
        }
        
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
        clientsTable.setModel(new DefaultTableModel(data, columnNames));
    }
    
    private void showAddClientDialog() {
        JDialog dialog = new JDialog(parentFrame, "Ajouter un client", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        formPanel.add(idField);
        
        formPanel.add(new JLabel("Nom:"));
        JTextField nomField = new JTextField();
        formPanel.add(nomField);
        
        formPanel.add(new JLabel("Prénom:"));
        JTextField prenomField = new JTextField();
        formPanel.add(prenomField);
        
        formPanel.add(new JLabel("Adresse:"));
        JTextField adresseField = new JTextField();
        formPanel.add(adresseField);
        
        formPanel.add(new JLabel("Téléphone:"));
        JTextField telephoneField = new JTextField();
        formPanel.add(telephoneField);
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            String id = idField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String adresse = adresseField.getText();
            String telephone = telephoneField.getText();
            String email = emailField.getText();
            
            // Vérifier que les champs obligatoires sont remplis
            if (id.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez remplir au moins l'ID, le nom et le prénom.",
                    "Champs manquants",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier que l'ID n'existe pas déjà
            if (!dataManager.isClientIdUnique(id)) {
                JOptionPane.showMessageDialog(dialog,
                    "Un client avec cet ID existe déjà.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Créer et ajouter le nouveau client
            Client nouveauClient = new Client(id, nom, prenom, adresse, telephone, email);
            dataManager.addClient(nouveauClient);
            
            // Rafraîchir l'affichage
            refreshTable();
            dialog.dispose();
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
    
    private void modifierClientSelectionne() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(parentFrame,
                "Veuillez sélectionner un client à modifier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Récupérer le client sélectionné
        Client clientAModifier = dataManager.getClient(selectedRow);
        
        // Créer la boîte de dialogue de modification
        JDialog dialog = new JDialog(parentFrame, "Modifier un client", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // L'ID n'est pas modifiable (clé primaire)
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField(clientAModifier.getId());
        idField.setEditable(false);
        idField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(idField);
        
        formPanel.add(new JLabel("Nom:"));
        JTextField nomField = new JTextField(clientAModifier.getNom());
        formPanel.add(nomField);
        
        formPanel.add(new JLabel("Prénom:"));
        JTextField prenomField = new JTextField(clientAModifier.getPrenom());
        formPanel.add(prenomField);
        
        formPanel.add(new JLabel("Adresse:"));
        JTextField adresseField = new JTextField(clientAModifier.getAdresse());
        formPanel.add(adresseField);
        
        formPanel.add(new JLabel("Téléphone:"));
        JTextField telephoneField = new JTextField(clientAModifier.getTelephone());
        formPanel.add(telephoneField);
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(clientAModifier.getEmail());
        formPanel.add(emailField);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String adresse = adresseField.getText();
            String telephone = telephoneField.getText();
            String email = emailField.getText();
            
            // Vérifier que les champs obligatoires sont remplis
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez remplir au moins le nom et le prénom.",
                    "Champs manquants",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Mettre à jour les propriétés du client
            clientAModifier.setNom(nom);
            clientAModifier.setPrenom(prenom);
            clientAModifier.setAdresse(adresse);
            clientAModifier.setTelephone(telephone);
            clientAModifier.setEmail(email);
            
            // Rafraîchir l'affichage
            refreshTable();
            dialog.dispose();
            
            JOptionPane.showMessageDialog(parentFrame,
                "Le client a été modifié avec succès.",
                "Modification réussie",
                JOptionPane.INFORMATION_MESSAGE);
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
    
    private void supprimerClientSelectionne() {
        int row = clientsTable.getSelectedRow();
        if (row >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(parentFrame, 
                "Êtes-vous sûr de vouloir supprimer ce client ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmation == JOptionPane.YES_OPTION) {
                dataManager.removeClient(row);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, 
                "Veuillez sélectionner un client à supprimer.", 
                "Aucune sélection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}