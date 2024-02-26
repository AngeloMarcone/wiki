package com.example.wiki.gui;

import com.example.wiki.DAO.PaginaDAO;
import com.example.wiki.DAO.PaginaDAOImpl;
import com.example.wiki.domain.Pagina;
import com.example.wiki.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


public class HomeController implements Initializable {

    @FXML
    private ListView<Pagina> listView;
    @FXML
    private Button loginButton;
    @FXML
    private Button createPageButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField searchBar;
    @FXML
    private Button profileButton;
    private SessionManager sessionManager = SessionManager.getInstance();
    private Stage primaryStage;
    private final PaginaDAO paginaDAO = new PaginaDAOImpl();

    /**
     * Metodo chiamato durante l'inizializzazione del controller.
     * Aggiorna l'interfaccia utente (UI) e configura la visualizzazione delle celle nel ListView.
     * Ottiene le pagine iniziali dal database, le ordina in base alla data di creazione (dal più recente al meno recente)
     * e le aggiunge al ListView per la visualizzazione.
     *
     * @param url            L'URL di inizializzazione.
     * @param resourceBundle ResourceBundle associato all'inizializzazione.
     */
    {
        /**
         * I parametri url e resourceBundle nella firma del metodo initialize sono forniti dal framework JavaFX e vengono
         * automaticamente passati quando il metodo viene chiamato durante l'inizializzazione del controller.
         *
         * url: L'URL di inizializzazione rappresenta l'ubicazione del file FXML associato a questo controller, se applicabile.
         * È utile se si desidera ottenere informazioni sull'ubicazione del file FXML durante l'inizializzazione del controller.
         *
         * resourceBundle: ResourceBundle è un meccanismo utilizzato per localizzare le risorse dell'applicazione, come stringhe o immagini,
         * in base a una determinata localizzazione o lingua. Tuttavia, nel contesto di initialize, il resourceBundle potrebbe essere nullo,
         * poiché il file FXML non richiede necessariamente l'uso di risorse localizzate.
         */
    }//cosa sono URL url, ResourceBundle resourceBundle

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Aggiorna l'interfaccia utente (UI)
        updateUI();

        // Configura la visualizzazione delle celle nel ListView
        listView.setCellFactory(param -> new ListCell<Pagina>() {
            @Override
            protected void updateItem(Pagina item, boolean empty) {
                super.updateItem(item, empty);

                // Se la cella è vuota o non contiene un oggetto Pagina, imposta il testo a null
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Se la cella contiene un oggetto Pagina, crea una stringa formattata con titolo, autore e link
                    String displayText = String.format("Titolo: %s\nAutore: %s\nLink: %s",
                            item.getTitolo(), item.getAutore(), item.getLink());
                    // Imposta il testo della cella con la stringa formattata
                    setText(displayText);
                }
            }
        });

        // Ottiene le pagine iniziali dal database, le ordina in base alla data di creazione (dal più recente al meno recente)
        List<Pagina> initialPages = paginaDAO.getPages();
        initialPages.sort(Comparator.comparing(Pagina::getDataoracreazione).reversed());

        // Aggiunge le pagine ordinate al ListView per la visualizzazione
        if (initialPages != null) {
            listView.getItems().addAll(initialPages);
        }
    }


    /**
     * Aggiorna l'interfaccia utente in base allo stato del login.
     * Se l'utente è loggato, nasconde i pulsanti di login e registrazione,
     * mostra il pulsante di logout e creazione pagina e' visibile solo se si e' autore.
     * Se l'utente non è loggato, nasconde il pulsante di logout e mostra i pulsanti di login e registrazione.
     */
    private void updateUI() {
        if (SessionManager.getInstance().isUtenteLoggato()) {
            loginButton.setVisible(false);
            signupButton.setVisible(false);
            logoutButton.setVisible(true);
            createPageButton.setVisible(sessionManager.getUtenteCorrente().getNomedarte() != null); // Il pulsante di creazione è visibile solo se l'utente è autore
        } else {
            logoutButton.setVisible(false);
            loginButton.setVisible(true);
            signupButton.setVisible(true);
            createPageButton.setVisible(false);
            profileButton.setVisible(false);
        }
    }

    /**
     * Imposta il sessionManager per il controller.
     *
     * @param sessionManager Il gestore di sessione da impostare.
     */
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Imposta il primaryStage per il controller.
     *
     * @param primaryStage Il primaryStage da impostare.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gestisce il pulsante di aggiornamento.(se viene cliccato)
     * Ottiene le pagine aggiornate dal database e aggiorna la visualizzazione nella ListView.
     */
    @FXML
    public void handleRefreshButton(ActionEvent event) {
        List<Pagina> updatedPages = paginaDAO.getPages();
        if (updatedPages != null) {
            listView.getItems().setAll(updatedPages);
        }
    }

    /**
     * Gestisce l'azione del pulsante di aggiornamento (overload). (se non viene cliccato)
     * Ottiene le pagine aggiornate dal database e aggiorna la visualizzazione nel ListView.
     */
    @FXML
    public void handleRefreshButton() {
        List<Pagina> updatedPages = paginaDAO.getPages();
        update(updatedPages);
    }


    /**
     * Aggiorna la visualizzazione nel ListView con le pagine fornite.
     *
     * @param updatedPages Lista di pagine aggiornate da visualizzare nel ListView.
     */
    public void update(List<Pagina> updatedPages) {
        if (updatedPages != null) {
            listView.getItems().setAll(updatedPages);
        }
    }

    /**
     * Gestisce l'azione del pulsante di ricerca.
     * Esegue una ricerca utilizzando il testo nella barra di ricerca e aggiorna la visualizzazione nel ListView.
     *
     */
    @FXML
    public void handleSearchButton() {
        if (searchBar.getText() != null) {
            useSearchBar();
        }
    }

    /**
     * Esegue una ricerca utilizzando il testo nella barra di ricerca e aggiorna la visualizzazione nel ListView.
     */
    public void useSearchBar() {
        String toSearch = searchBar.getText();
        update(paginaDAO.search(toSearch));
    }

    /**
     * Naviga alla pagina di registrazione.
     * Carica il FXML di SignUpPage, imposta il controller, la scena e rende la finestra visibile.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della pagina di registrazione.
     */
    @FXML
    private void goToSignUpPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/SignUpPage.fxml"));
        Parent root = loader.load();
        SignUpController signUpController = loader.getController();
        signUpController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Naviga alla pagina di login.
     * Carica il FXML di LoginPage, imposta il controller, la scena e rende la finestra visibile.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della pagina di login.
     */
    @FXML
    private void goToLoginPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/LoginPage.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Naviga alla pagina di visualizzazione per la pagina corrente.
     * Carica il FXML di VisualizePage, imposta il controller, passa i dati e rende la finestra visibile.
     *
     * @param currentPage La Pagina corrente da visualizzare.
     * @throws IOException Se si verifica un errore durante il caricamento della pagina di visualizzazione.
     * @throws SQLException Se si verifica un errore SQL durante il recupero dei dati della pagina.
     */
    @FXML
    private void goToVisualizePage(Pagina currentPage) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/VisualizePage.fxml"));
        Parent root = loader.load();
        VisualizePageController visualizePageController = loader.getController();
        visualizePageController.setPrimaryStage(primaryStage);

        // Passa i dati al controller della nuova scena
        visualizePageController.setData(currentPage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }

    /**
     * Naviga alla pagina di creazione.
     * Carica il FXML di CreatePage, imposta il controller e rende la finestra visibile.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della pagina di creazione.
     */
    @FXML
    private void goToCreatePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/CreatePage.fxml"));
        Parent root = loader.load();
        CreatePageController createPageController = loader.getController();
        createPageController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }

    /**
     * Naviga alla pagina del profilo.
     * Carica il FXML di ProfilePage, imposta il controller e rende la finestra visibile.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della pagina del profilo.
     */
    @FXML
    private void goToProfilePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/ProfilePage.fxml"));
        Parent root = loader.load();
        ProfilePageController profilePageController = loader.getController();
        profilePageController.setPrimaryStage(primaryStage);

        // Configura la scena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }

    /**
     * Gestisce il click sulla ListView.
     * Ottiene l'elemento selezionato e naviga alla pagina di visualizzazione.
     *
     * @throws IOException Se si verifica un errore durante la navigazione alla pagina di visualizzazione.
     * @throws SQLException Se si verifica un errore SQL durante il recupero dei dati della pagina.
     */
    @FXML
    public void handleListViewClick() throws IOException, SQLException {
        Pagina currentPage = listView.getSelectionModel().getSelectedItem();
        if (currentPage != null) {
            goToVisualizePage(currentPage);
        }
    }

    

    /**
     * Gestisce il click sul pulsante di logout.
     * Esegue il logout tramite il SessionManager e aggiorna l'interfaccia utente.
     */
    @FXML
    public void handleLogoutButton() {
        sessionManager.logout();
        updateUI();
    }




}