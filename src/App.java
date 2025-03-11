import hotelmanager.Chambre;
import hotelmanager.Client;
import hotelmanager.Reservation;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class App {
    private JFrame mainFrame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels pour chaque section
    private JPanel clientsPanel;
    private JPanel chambresPanel;
    private JPanel accueilPanel;

        // Modèles de données
    private List<Chambre> chambres = new ArrayList<>();
    private JTable chambresTable;

    private List<Client> clients = new ArrayList<>();
    private JTable clientsTable;

    private List<Reservation> reservations = new ArrayList<>();
    private JTable reservationsTable;
    private JPanel reservationsPanel;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new App().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public void initialize() {
        // Configuration de la fenêtre principale
        mainFrame = new JFrame("Système de Gestion Hôtelière");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        
        // Création du menu
        createMenu();
        
        // Configuration du panel principal avec CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Création des différents panels
        createAccueilPanel();
        createClientsPanel();
        createChambresPanel();
        createReservationsPanel();
        
        // Ajout des panels au contentPanel avec CardLayout
        contentPanel.add(accueilPanel, "ACCUEIL");
        contentPanel.add(clientsPanel, "CLIENTS");
        contentPanel.add(chambresPanel, "CHAMBRES");
        contentPanel.add(reservationsPanel, "RESERVATIONS");
        
        // Afficher le panel d'accueil par défaut
        cardLayout.show(contentPanel, "ACCUEIL");
        
        mainFrame.add(contentPanel);
        mainFrame.setVisible(true);
    }


    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));
        menuFichier.add(itemQuitter);
        
        // Menu Navigation
        JMenu menuNavigation = new JMenu("Navigation");
        JMenuItem itemAccueil = new JMenuItem("Accueil");
        itemAccueil.addActionListener(e -> cardLayout.show(contentPanel, "ACCUEIL"));
        
        JMenuItem itemClients = new JMenuItem("Gestion des Clients");
        itemClients.addActionListener(e -> cardLayout.show(contentPanel, "CLIENTS"));
        
        JMenuItem itemChambres = new JMenuItem("Gestion des Chambres");
        itemChambres.addActionListener(e -> cardLayout.show(contentPanel, "CHAMBRES"));

        JMenuItem itemReservations = new JMenuItem("Gestion des Réservations");
        itemReservations.addActionListener(e -> cardLayout.show(contentPanel, "RESERVATIONS"));
    
        
        menuNavigation.add(itemAccueil);
        menuNavigation.add(itemClients);
        menuNavigation.add(itemChambres);
        menuNavigation.add(itemReservations);
        
        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, 
                "Système de Gestion Hôtelière v1.0", 
                "À propos", 
                JOptionPane.INFORMATION_MESSAGE));
        menuAide.add(itemAPropos);
        
        // Ajout des menus à la barre
        menuBar.add(menuFichier);
        menuBar.add(menuNavigation);
        menuBar.add(menuAide);
        
        mainFrame.setJMenuBar(menuBar);
    }

    private void createAccueilPanel() {
        accueilPanel = new JPanel();
        accueilPanel.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("<html><h1>Bienvenue dans le Système de Gestion Hôtelière</h1></html>", JLabel.CENTER);
        accueilPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JButton clientsButton = new JButton("Gestion des Clients");
        clientsButton.addActionListener(e -> cardLayout.show(contentPanel, "CLIENTS"));
        
        JButton chambresButton = new JButton("Gestion des Chambres");
        chambresButton.addActionListener(e -> cardLayout.show(contentPanel, "CHAMBRES"));
        
        JButton reservationsButton = new JButton("Réservations");
        reservationsButton.addActionListener(e -> cardLayout.show(contentPanel, "RESERVATIONS"));

        JButton facturesButton = new JButton("Factures");
        
        buttonsPanel.add(clientsButton);
        buttonsPanel.add(chambresButton);
        buttonsPanel.add(reservationsButton);
        buttonsPanel.add(facturesButton);
        
        accueilPanel.add(buttonsPanel, BorderLayout.CENTER);
    }
    
    private void createClientsPanel() {
        clientsPanel = new JPanel();
        clientsPanel.setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Clients", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        clientsPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Tableau pour afficher les clients
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
        Object[][] data = new Object[0][5]; 
        
        clientsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(scrollPane, BorderLayout.CENTER);
        


        // Panel pour les boutons d'action
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> showAddClientDialog());
        
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(e -> modifierClientSelectionne());
        
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> supprimerClientSelectionne());
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        clientsPanel.add(actionsPanel, BorderLayout.SOUTH);

         // Ajouter quelques clients de démonstration
         clients.add(new hotelmanager.Client("CL001", "Dupont", "Jean", "1 rue de Paris", "0123456789", "jean.dupont@email.com"));
         clients.add(new hotelmanager.Client("CL002", "Martin", "Sophie", "15 avenue des Champs", "0987654321", "sophie.martin@email.com"));
         clients.add(new hotelmanager.Client("CL003", "Dubois", "Pierre", "8 boulevard Victor Hugo", "0665544332", "pierre.dubois@email.com"));
         
         // Rafraîchir l'affichage
         rafraichirTableauClients();
    }

    private void showAddClientDialog() {
        JDialog dialog = new JDialog(mainFrame, "Ajouter un client", true);
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
            boolean idExiste = clients.stream()
                .anyMatch(c -> c.getId().equals(id));
                
            if (idExiste) {
                JOptionPane.showMessageDialog(dialog,
                    "Un client avec cet ID existe déjà.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Créer et ajouter le nouveau client
            hotelmanager.Client nouveauClient = new hotelmanager.Client(id, nom, prenom, adresse, telephone, email);
            clients.add(nouveauClient);
            
            // Rafraîchir l'affichage
            rafraichirTableauClients();
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void createChambresPanel() {
        chambresPanel = new JPanel();
        chambresPanel.setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Chambres", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        chambresPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Tableau pour afficher les chambres
        String[] columnNames = {"Numéro", "Type", "Tarif", "Disponible"};
        Object[][] data = new Object[0][4]; // Initialisation avec un tableau vide
        
        chambresTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(chambresTable);
        chambresPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel pour les boutons d'action
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> showAddChambreDialog());
        
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(e -> modifierChambreSelectionnee());

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> supprimerChambreSelectionnee());
        
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        chambresPanel.add(actionsPanel, BorderLayout.SOUTH);

        // Ajouter quelques chambres de démonstration
        chambres.add(new Chambre(101, "Simple", 25000, true));
        chambres.add(new Chambre(102, "Double", 35000, true));
        chambres.add(new Chambre(103, "Suite", 50000, false));

        // Rafraîchir l'affichage
        rafraichirTableauChambres();
    }
    
    private void showAddChambreDialog() {
        JDialog dialog = new JDialog(mainFrame, "Ajouter une chambre", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Numéro:"));
        JTextField numeroField = new JTextField();
        formPanel.add(numeroField);
        
        formPanel.add(new JLabel("Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Simple", "Double", "Suite"});
        formPanel.add(typeCombo);
        
        formPanel.add(new JLabel("Tarif:"));
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
                boolean numeroExiste = chambres.stream()
                    .anyMatch(c -> c.getNumero() == numero);
                
                if (numeroExiste) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Une chambre avec ce numéro existe déjà.", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Créer et ajouter la nouvelle chambre
                Chambre nouvelleChambre = new Chambre(numero, type, tarif, disponible);
                chambres.add(nouvelleChambre);
                
                // Rafraîchir l'affichage
                rafraichirTableauChambres();
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
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void rafraichirTableauChambres() {
        // Création d'un nouveau modèle de données pour le tableau
        Object[][] data = new Object[chambres.size()][4];
        
        for (int i = 0; i < chambres.size(); i++) {
            Chambre chambre = chambres.get(i);
            data[i][0] = chambre.getNumero();
            data[i][1] = chambre.getType();
            data[i][2] = chambre.getTarif();
            data[i][3] = chambre.isDisponible();
        }
        
        // Création et application du nouveau modèle au tableau
        String[] columnNames = {"Numéro", "Type", "Tarif", "Disponible"};
        chambresTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void supprimerChambreSelectionnee() {
        int row = chambresTable.getSelectedRow();
        if (row >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(mainFrame, 
                "Êtes-vous sûr de vouloir supprimer cette chambre ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmation == JOptionPane.YES_OPTION) {
                chambres.remove(row);
                rafraichirTableauChambres();
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, 
                "Veuillez sélectionner une chambre à supprimer.", 
                "Aucune sélection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modifierChambreSelectionnee() {
        int selectedRow = chambresTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainFrame,
                "Veuillez sélectionner une chambre à modifier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Récupérer la chambre sélectionnée
        Chambre chambreAModifier = chambres.get(selectedRow);
        
        // Créer la boîte de dialogue de modification
        JDialog dialog = new JDialog(mainFrame, "Modifier une chambre", true);
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
        formPanel.add(new JLabel("Tarif:"));
        JTextField tarifField = new JTextField(String.valueOf(chambreAModifier.getTarif()));
        formPanel.add(tarifField);
        
        // Disponibilité
        formPanel.add(new JLabel("Disponible:"));
        JCheckBox disponibleCheck = new JCheckBox();
        disponibleCheck.setSelected(chambreAModifier.isDisponible());
        formPanel.add(disponibleCheck);
        
        // Boutons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                int tarif = Integer.parseInt(tarifField.getText());
                boolean disponible = disponibleCheck.isSelected();
                
                // Mettre à jour les propriétés de la chambre
                chambreAModifier.setType(type);
                chambreAModifier.setTarif(tarif);
                chambreAModifier.setDisponible(disponible);
                
                // Rafraîchir l'affichage
                rafraichirTableauChambres();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(mainFrame,
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
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void rafraichirTableauClients() {
        // Création d'un nouveau modèle de données pour le tableau
        Object[][] data = new Object[clients.size()][5];
        
        for (int i = 0; i < clients.size(); i++) {
            hotelmanager.Client client = clients.get(i);
            data[i][0] = client.getId();
            data[i][1] = client.getNom();
            data[i][2] = client.getPrenom();
            data[i][3] = client.getTelephone();
            data[i][4] = client.getEmail();
        }
        
        // Création et application du nouveau modèle au tableau
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
        clientsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void supprimerClientSelectionne() {
        int row = clientsTable.getSelectedRow();
        if (row >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(mainFrame, 
                "Êtes-vous sûr de vouloir supprimer ce client ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmation == JOptionPane.YES_OPTION) {
                clients.remove(row);
                rafraichirTableauClients();
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, 
                "Veuillez sélectionner un client à supprimer.", 
                "Aucune sélection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void modifierClientSelectionne() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainFrame,
                "Veuillez sélectionner un client à modifier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Récupérer le client sélectionné
        hotelmanager.Client clientAModifier = clients.get(selectedRow);
        
        // Créer la boîte de dialogue de modification
        JDialog dialog = new JDialog(mainFrame, "Modifier un client", true);
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
            rafraichirTableauClients();
            dialog.dispose();
            
            JOptionPane.showMessageDialog(mainFrame,
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
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void createReservationsPanel() {
        reservationsPanel = new JPanel();
        reservationsPanel.setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Réservations", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        reservationsPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Tableau pour afficher les réservations
        String[] columnNames = {"ID", "Client", "Chambre", "Date d'arrivée", "Date de départ", "Prix total"};
        Object[][] data = new Object[0][6]; // Initialisation avec un tableau vide
        
        reservationsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        reservationsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel pour les boutons d'action
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> showAddReservationDialog());
        
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(e -> modifierReservationSelectionnee());
        
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> supprimerReservationSelectionnee());
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        reservationsPanel.add(actionsPanel, BorderLayout.SOUTH);
    
        // Ajouter quelques réservations de démonstration si nous avons des clients et des chambres
        if (!clients.isEmpty() && !chambres.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                // Réservation 1
                Date dateArrivee1 = sdf.parse("20/03/2025");
                Date dateDepart1 = sdf.parse("25/03/2025");
                Reservation reservation1 = new Reservation("RES001", dateArrivee1, dateDepart1, 
                                                         375000, clients.get(0), chambres.get(0));
                reservations.add(reservation1);
                
                // Réservation 2
                Date dateArrivee2 = sdf.parse("15/04/2025");
                Date dateDepart2 = sdf.parse("20/04/2025");
                Reservation reservation2 = new Reservation("RES002", dateArrivee2, dateDepart2, 
                                                         500000, clients.get(1), chambres.get(1));
                reservations.add(reservation2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        // Rafraîchir l'affichage
        rafraichirTableauReservations();
    }

    private void showAddReservationDialog() {
        // Vérifier qu'il y a des clients et des chambres disponibles
        if (clients.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Veuillez d'abord ajouter des clients.", 
                "Aucun client", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Chambre> chambresDisponibles = new ArrayList<>();
        for (Chambre chambre : chambres) {
            if (chambre.isDisponible()) {
                chambresDisponibles.add(chambre);
            }
        }
        
        if (chambresDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Il n'y a pas de chambres disponibles.", 
                "Aucune chambre disponible", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Créer la boîte de dialogue
        JDialog dialog = new JDialog(mainFrame, "Ajouter une réservation", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID de réservation
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        formPanel.add(idField);
        
        // Sélection du client
        formPanel.add(new JLabel("Client:"));
        JComboBox<Client> clientCombo = new JComboBox<>(clients.toArray(new Client[0]));
        formPanel.add(clientCombo);
        
        // Sélection de la chambre
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
        formPanel.add(new JLabel("Prix total:"));
        JTextField prixTotalField = new JTextField();
        formPanel.add(prixTotalField);
        
        // Ajouter un listener pour calculer automatiquement le prix total en fonction de la chambre et des dates
        chambreCombo.addActionListener(e -> {
            try {
                Chambre chambreSelectionnee = (Chambre) chambreCombo.getSelectedItem();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                
                if (!dateArriveeField.getText().isEmpty() && !dateDepartField.getText().isEmpty()) {
                    Date dateArrivee = sdf.parse(dateArriveeField.getText());
                    Date dateDepart = sdf.parse(dateDepartField.getText());
                    
                    // Calculer le nombre de jours
                    long diff = dateDepart.getTime() - dateArrivee.getTime();
                    int nbJours = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 pour inclure le jour d'arrivée
                    
                    // Calculer le prix total
                    double prixTotal = chambreSelectionnee.getTarif() * nbJours;
                    prixTotalField.setText(String.valueOf(prixTotal));
                }
            } catch (ParseException ex) {
                // Si les dates ne sont pas encore valides, ne rien faire
            }
        });
        
        // Ajouter un listener pour recalculer le prix quand les dates changent
        ActionListener dateListener = e -> {
            try {
                Chambre chambreSelectionnee = (Chambre) chambreCombo.getSelectedItem();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                
                if (!dateArriveeField.getText().isEmpty() && !dateDepartField.getText().isEmpty()) {
                    Date dateArrivee = sdf.parse(dateArriveeField.getText());
                    Date dateDepart = sdf.parse(dateDepartField.getText());
                    
                    // Vérifier que la date de départ est après la date d'arrivée
                    if (dateDepart.before(dateArrivee)) {
                        JOptionPane.showMessageDialog(dialog,
                            "La date de départ doit être après la date d'arrivée.",
                            "Erreur de dates",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Calculer le nombre de jours
                    long diff = dateDepart.getTime() - dateArrivee.getTime();
                    int nbJours = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 pour inclure le jour d'arrivée
                    
                    // Calculer le prix total
                    double prixTotal = chambreSelectionnee.getTarif() * nbJours;
                    prixTotalField.setText(String.valueOf(prixTotal));
                }
            } catch (ParseException ex) {
                // Si les dates ne sont pas encore valides, ne rien faire
            }
        };
        
        // Ajouter le listener aux champs de date
        dateArriveeField.addActionListener(dateListener);
        dateDepartField.addActionListener(dateListener);
        
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
            
            // Vérifier que l'ID n'existe pas déjà
            boolean idExiste = reservations.stream()
                .anyMatch(r -> r.getId().equals(id));
            
            if (idExiste) {
                JOptionPane.showMessageDialog(dialog, 
                    "Une réservation avec cet ID existe déjà.", 
                    "Erreur", 
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
                
                // Créer et ajouter la nouvelle réservation
                Reservation nouvelleReservation = new Reservation(id, dateArrivee, dateDepart, 
                                                               prixTotal, clientSelectionne, chambreSelectionnee);
                reservations.add(nouvelleReservation);
                
                // Marquer la chambre comme non disponible
                chambreSelectionnee.setDisponible(false);
                
                // Ajouter la réservation au client
                clientSelectionne.ajouterReservation(nouvelleReservation);
                
                // Rafraîchir les tableaux
                rafraichirTableauReservations();
                rafraichirTableauChambres();
                
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
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void rafraichirTableauReservations() {
        // Création d'un nouveau modèle de données pour le tableau
        Object[][] data = new Object[reservations.size()][6];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            data[i][0] = reservation.getId();
            data[i][1] = reservation.getClient().getNom() + " " + reservation.getClient().getPrenom();
            data[i][2] = "Chambre " + reservation.getChambre().getNumero();
            data[i][3] = sdf.format(reservation.getDateArrivee());
            data[i][4] = sdf.format(reservation.getDateDepart());
            data[i][5] = reservation.getPrixTotal() + " FCFA";
        }
        
        // Création et application du nouveau modèle au tableau
        String[] columnNames = {"ID", "Client", "Chambre", "Date d'arrivée", "Date de départ", "Prix total"};
        reservationsTable.setModel(new DefaultTableModel(data, columnNames));
    }

    private void supprimerReservationSelectionnee() {
        int row = reservationsTable.getSelectedRow();
        if (row >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(mainFrame, 
                "Êtes-vous sûr de vouloir supprimer cette réservation ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmation == JOptionPane.YES_OPTION) {
                Reservation reservation = reservations.get(row);
                
                // Rendre la chambre disponible à nouveau
                Chambre chambre = reservation.getChambre();
                chambre.setDisponible(true);
                
                // Supprimer la réservation
                reservations.remove(row);
                
                // Rafraîchir les tableaux
                rafraichirTableauReservations();
                rafraichirTableauChambres();
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, 
                "Veuillez sélectionner une réservation à supprimer.", 
                "Aucune sélection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modifierReservationSelectionnee() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainFrame,
                "Veuillez sélectionner une réservation à modifier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Récupérer la réservation sélectionnée
        Reservation reservationAModifier = reservations.get(selectedRow);
        
        // Créer une liste de chambres disponibles (incluant celle actuellement réservée)
        List<Chambre> chambresDisponibles = new ArrayList<>();
        for (Chambre chambre : chambres) {
            if (chambre.isDisponible() || chambre.getNumero() == reservationAModifier.getChambre().getNumero()) {
                chambresDisponibles.add(chambre);
            }
        }
        
        // Créer la boîte de dialogue de modification
        JDialog dialog = new JDialog(mainFrame, "Modifier une réservation", true);
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
        JComboBox<Client> clientCombo = new JComboBox<>(clients.toArray(new Client[0]));
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
        formPanel.add(new JLabel("Prix total:"));
        JTextField prixTotalField = new JTextField(String.valueOf(reservationAModifier.getPrixTotal()));
        formPanel.add(prixTotalField);
        
        // Ajouter un listener pour calculer automatiquement le prix total en fonction de la chambre et des dates
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
                        double prixTotal = chambreSelectionnee.getTarif() * nbJours;
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
                    // Libérer l'ancienne chambre
                    ancienneChambSelectionnee.setDisponible(true);
                    
                    // Réserver la nouvelle chambre
                    nouvelleChambSelectionnee.setDisponible(false);
                }
                
                // Mettre à jour les propriétés de la réservation
                reservationAModifier.setClient(clientSelectionne);
                reservationAModifier.setChambre(nouvelleChambSelectionnee);
                reservationAModifier.setDateArrivee(dateArrivee);
                reservationAModifier.setDateDepart(dateDepart);
                reservationAModifier.setPrixTotal(prixTotal);
                
                // Rafraîchir les tableaux
                rafraichirTableauReservations();
                rafraichirTableauChambres();
                
                dialog.dispose();
                
                JOptionPane.showMessageDialog(mainFrame,
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
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
}