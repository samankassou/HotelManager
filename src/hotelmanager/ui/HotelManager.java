package hotelmanager.ui;
import hotelmanager.data.DataManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HotelManager {
    private JFrame mainFrame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private JPanel accueilPanel;
    private ClientsPanel clientsPanel;
    private ChambresPanel chambresPanel;
    private ReservationsPanel reservationsPanel;
    private FacturesPanel facturesPanel;
    
    private DataManager dataManager;
    
    public HotelManager() {
        dataManager = new DataManager();
        initializeUI();
    }
    
    private void initializeUI() {
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
        
        // Initialisation des panneaux spécifiques
        clientsPanel = new ClientsPanel(dataManager, mainFrame);
        chambresPanel = new ChambresPanel(dataManager, mainFrame);
        reservationsPanel = new ReservationsPanel(dataManager, mainFrame);
        facturesPanel = new FacturesPanel(dataManager, mainFrame);
        
        // Ajout des panels au contentPanel avec CardLayout
        contentPanel.add(accueilPanel, "ACCUEIL");
        contentPanel.add(clientsPanel, "CLIENTS");
        contentPanel.add(chambresPanel, "CHAMBRES");
        contentPanel.add(reservationsPanel, "RESERVATIONS");
        contentPanel.add(facturesPanel, "FACTURES");
        
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

        JMenuItem itemFactures = new JMenuItem("Gestion des Factures");
        itemFactures.addActionListener(e -> cardLayout.show(contentPanel, "FACTURES"));
    
        menuNavigation.add(itemAccueil);
        menuNavigation.add(itemClients);
        menuNavigation.add(itemChambres);
        menuNavigation.add(itemReservations);
        menuNavigation.add(itemFactures);
        
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
        facturesButton.addActionListener(e -> cardLayout.show(contentPanel, "FACTURES"));
        
        buttonsPanel.add(clientsButton);
        buttonsPanel.add(chambresButton);
        buttonsPanel.add(reservationsButton);
        buttonsPanel.add(facturesButton);
        
        accueilPanel.add(buttonsPanel, BorderLayout.CENTER);
    }
}