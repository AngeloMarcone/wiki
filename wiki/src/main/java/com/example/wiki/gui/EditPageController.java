package com.example.wiki.gui;

import com.example.wiki.DAO.FraseDAO;
import com.example.wiki.DAO.FraseDAOImpl;
import com.example.wiki.DAO.ModificaDAO;
import com.example.wiki.DAO.ModificaDAOImpl;
import com.example.wiki.domain.Frase;
import com.example.wiki.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Controller per la pagina di modifica di una frase.
 */
public class EditPageController {

    List<Frase> frasipag = new ArrayList<>();

    @FXML
    private ListView<Frase> frasiVecchie;

    @FXML
    private TextField fraseModificata;

    @FXML
    private Button editButton;

    @FXML
    private Button homePageButton;

    private Stage primaryStage;

    private SessionManager sessionManager = SessionManager.getInstance();

    Frase fraseScelta;

    /**
     * Imposta il primaryStage per il controller.
     *
     * @param primaryStage Lo Stage principale dell'applicazione.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Metodo chiamato durante l'inizializzazione del controller.
     * Configura la visualizzazione delle frasi nel ListView frasiVecchie.
     */
    public void initialize() {
        /*
         * Configura una nuova factory per personalizzare la creazione delle celle nel ListView frasiVecchie.
         * Una factory, nel contesto della programmazione, è una funzione o un'istanza di una classe che ha il compito di creare e restituire nuovi oggetti di un certo tipo
         * Nel nostro caso, stiamo definendo una factory che, data una certa configurazione (param), crea e restituisce una nuova cella di tipo ListCell<Frase>.
         * Questo ci consente di personalizzare l'aspetto e il comportamento delle celle nel ListView.
         * la factory è definita utilizzando una lambda expression, che è una sintassi per definire funzioni anonime (senza un nome);
         */
        frasiVecchie.setCellFactory(param -> new ListCell<Frase>() {
            // Metodo chiamato ogni volta che una cella deve essere aggiornata con nuovi dati
            @Override
            protected void updateItem(Frase item, boolean empty) {
                // usiamo il costruttore per iniziallizzare gli oggetti. usiamo super perche' la funzione sta in ListCell
                // che e' la classe madre.
                super.updateItem(item, empty);

                // Se la cella è vuota o non contiene un oggetto Frase, imposta il testo a null
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Se la cella contiene un oggetto Frase, estrae i caratteri e usiamo una formattazione fatta da noi
                    // ovvero scegliamo cosa mostrare
                    String displayText = String.format("%s", item.getCaratteri());
                    // Imposta il testo della cella con la stringa formattata
                    setText(displayText);
                }
            }
        });
    }


    /**
     * Imposta i dati nel ListView.
     *
     * @param frasi Lista di frasi da visualizzare.
     */
    public void setData(List<Frase> frasi) {
        frasipag.addAll(frasi);
        populateListView(frasipag);
    }

    /**
     * Popola il ListView con i dati delle frasi.
     *
     * @param dataList Lista di frasi da visualizzare.
     */
    private void populateListView(List<Frase> dataList) {
        /*
         * Creazione di un nuovo ObservableList a partire dalla lista esistente dataList.
         * Un ObservableList è una lista "osservabile" che notifica automaticamente gli oggetti interessati quando subisce modifiche.
         * In questo contesto, items sarà utilizzato per contenere e gestire le frasi da visualizzare nel ListView.
         * Quando si aggiungono, rimuovono o modificano elementi in items, JavaFX automaticamente notificherà gli elementi grafici,
         * garantendo che il ListView frasiVecchie si aggiorni automaticamente in risposta alle modifiche della lista.
         */
        ObservableList<Frase> items = FXCollections.observableArrayList(dataList);
        frasiVecchie.setItems(items);
    }

    /**
     * Gestisce il click del mouse sul ListView per selezionare una frase.
     */
    @FXML
    private void handleMouseClick() {
        fraseScelta = frasiVecchie.getSelectionModel().getSelectedItem();
    }

    /**
     * Gestisce l'azione del pulsante di modifica.
     * Se una frase è selezionata (fraseScelta != null), ottiene l'autore della frase dal database,
     * registra la modifica nel database e naviga alla HomePage.
     * Se nessuna frase è selezionata, stampa un messaggio di avviso nella console.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della HomePage.
     */
    @FXML
    private void handleEditButton() throws IOException {
        if (fraseScelta != null) {
            // Ottiene l'autore della frase selezionata dal database
            FraseDAO fraseDAO = new FraseDAOImpl();
            String autoreDB = fraseDAO.getAutoreByFrase(fraseScelta.getCodfrase(), fraseScelta.getPagina());

            // inserisce la modifica nel database utilizzando ModificaDAO
            ModificaDAO modificaDAO = new ModificaDAOImpl();
            Date currentDate = new Date();
            Timestamp dataoramod = new Timestamp(currentDate.getTime());
            modificaDAO.insertModifica(fraseScelta.getCaratteri(), fraseScelta.getCodfrase(), fraseScelta.getPagina(),
                    fraseModificata.getText(), dataoramod, sessionManager.getUtenteCorrente().getMail(), autoreDB);

            // Naviga alla HomePage
            goToHomePage();
        } else {
            // Nessuna frase selezionata, stampa un messaggio di avviso nella console
            System.out.println("Nessun elemento selezionato.");
        }
    }


    /**
     * Naviga verso la HomePage dell'applicazione.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della HomePage.
     */
    @FXML
    private void goToHomePage() throws IOException {
        // Carica il FXML di HomePage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/HomePage.fxml"));
        Parent root = loader.load();

        // Imposta il controller di HomePage
        HomeController homeController = loader.getController();
        homeController.setPrimaryStage(primaryStage);

        // Configura la scena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }
}
