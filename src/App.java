import java.awt.*;
import javax.swing.*;

public class App {
    private JFrame mainFrame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels pour chaque section
    private JPanel clientsPanel;
    private JPanel chambresPanel;
    private JPanel accueilPanel;
    
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
        
        // Ajout des panels au contentPanel avec CardLayout
        contentPanel.add(accueilPanel, "ACCUEIL");
        contentPanel.add(clientsPanel, "CLIENTS");
        contentPanel.add(chambresPanel, "CHAMBRES");
        
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
        
        menuNavigation.add(itemAccueil);
        menuNavigation.add(itemClients);
        menuNavigation.add(itemChambres);
        
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
        Object[][] data = {}; // Données vides pour commencer
        
        JTable clientsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel pour les boutons d'action
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> showAddClientDialog());
        
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        clientsPanel.add(actionsPanel, BorderLayout.SOUTH);
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
            // Ici, code pour sauvegarder le client
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
        Object[][] data = {}; // Données vides pour commencer
        
        JTable chambresTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(chambresTable);
        chambresPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel pour les boutons d'action
        JPanel actionsPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> showAddChambreDialog());
        
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        
        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        
        chambresPanel.add(actionsPanel, BorderLayout.SOUTH);
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
            // Ici, code pour sauvegarder la chambre
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
}