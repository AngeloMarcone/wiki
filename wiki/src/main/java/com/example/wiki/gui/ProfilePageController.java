package com.example.wiki.gui;

import com.example.wiki.DAO.*;
import com.example.wiki.domain.Modifica;
import com.example.wiki.domain.Pagina;
import com.example.wiki.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Controller per la pagina del profilo utente.
 */
public class ProfilePageController {

    @FXML
    private ListView<Pagina> myPageListView;

    @FXML
    private ListView<Modifica> modificheListView;

    @FXML
    private ListView<Modifica> notificheListView;

    @FXML
    private Label nomeCognomeLabel;

    @FXML
    private Label nomedarteLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label carrieraLabel;

    @FXML
    private Label notificheLabel;

    @FXML
    private Button homeButton;

    @FXML
    private Label notificheText;

    @FXML
    private Label pagineText;

    @FXML
    private Button diventaAutoreButton;

    private SessionManager sessionManager = SessionManager.getInstance();
    private Stage primaryStage;
    private final PaginaDAO paginaDAO = new PaginaDAOImpl();
    private final ModificaDAO modificaDAO = new ModificaDAOImpl();

    /**
     * Imposta il primaryStage per il controller.
     *
     * @param primaryStage Il primaryStage da impostare.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Aggiorna l'interfaccia utente con i dati più recenti.
     */
    private void updateUI() {
        updatemodificheListView();

        if (sessionManager.getUtenteCorrente().getNomedarte() != null) {
            updateMyPageListView();
            updateNotificheListView();
            diventaAutoreButton.setVisible(false);
        } else {
            pagineText.setVisible(false);
            notificheText.setVisible(false);
            myPageListView.setVisible(false);
            notificheListView.setVisible(false);
        }
    }

    /**
     * Aggiorna la ListView delle modifiche dell'utente corrente.
     * Ottiene le modifiche dall'oggetto ModificaDAO e le ordina in base alla data in modo decrescente.
     * Aggiorna quindi la ListView con le modifiche ordinate.
     */
    private void updatemodificheListView() {
        // Ottieni le modifiche dell'utente corrente
        List<Modifica> mods = modificaDAO.getMod(sessionManager.getUtenteCorrente());

        // Ordina le modifiche in base alla data in modo decrescente
        Collections.sort(mods, Comparator.comparing(Modifica::getDataoramod).reversed());

        // Pulisce la ListView delle modifiche e la riempie con le modifiche ordinate
        modificheListView.getItems().clear();
        if (mods != null) {
            modificheListView.getItems().addAll(mods);
        }
    }

    /**
     * Aggiorna la ListView delle notifiche.
     */
    private void updateNotificheListView() {
        List<Modifica> notifiche = modificaDAO.getNotifiche(sessionManager.getUtenteCorrente());

        notificheListView.getItems().clear();
        if (notifiche != null) {
            notificheListView.getItems().addAll(notifiche);
        }
    }

    /**
     * Aggiorna la ListView delle pagine dell'autore.
     */
    private void updateMyPageListView() {
        List<Pagina> initialPages = paginaDAO.getPagesByAutore(sessionManager.getUtenteCorrente());

        myPageListView.getItems().clear();
        if (initialPages != null) {
            myPageListView.getItems().addAll(initialPages);
        }
    }

    /**
     * Gestisce il click sulla ListView delle pagine.
     *
     * @throws IOException Se si verifica un errore durante la navigazione alla pagina di visualizzazione.
     */
    @FXML
    public void handleListViewClick() throws IOException {
        Pagina currentPage = myPageListView.getSelectionModel().getSelectedItem();
        if (currentPage != null) {
            try {
                goToVisualizePage(currentPage);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Naviga alla pagina di visualizzazione della pagina.
     *
     * @param currentPage La pagina da visualizzare.
     * @throws IOException Se si verifica un errore durante la navigazione.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati del database.
     */
    private void goToVisualizePage(Pagina currentPage) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/VisualizePage.fxml"));
        Parent root = loader.load();

        VisualizePageController visualizePageController = loader.getController();
        visualizePageController.setPrimaryStage(primaryStage);

        visualizePageController.setData(currentPage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * Metodo per navigare alla HomePage.
     * Carica il file FXML della HomePage, imposta il controller di HomePage e configura la scena.
     * Infine, rende la finestra visibile.
     *
     * @throws IOException Se si verifica un errore durante il caricamento del file FXML.
     */
    @FXML
    private void goToHomePage() throws IOException {
        // Carica il FXML della HomePage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/HomePage.fxml"));
        Parent root = loader.load();

        // Imposta il controller della HomePage
        HomeController homeController = loader.getController();
        homeController.setPrimaryStage(primaryStage);

        // Configura la scena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }


    /**
     * Gestisce l'azione del pulsante "Diventa Autore".
     * Apre un dialog che richiede all'utente di inserire il nome d'arte.
     * Dopo l'inserimento, effettua l'inserimento nel database tramite AutoreDAO.
     * Se l'inserimento ha successo, mostra un popup di successo, effettua il logout e naviga alla HomePage.
     * In caso di errore, mostra un popup di errore.
     */
    @FXML
    public void handleDiventaAutoreButton() {
        AutoreDAO autoreDAO = new AutoreDAOImpl();

        // Apre una finestra di dialogo per inserire il nome d'arte
        // Creazione di un oggetto TextInputDialog per raccogliere input testuale dall'utente
        TextInputDialog dialog = new TextInputDialog();
        // Personalizzazione del dialogo - Impostazione del titolo e rimozione dell'intestazione
        dialog.setTitle("Diventa Autore");
        dialog.setHeaderText(null);
        dialog.setContentText("Inserisci il tuo nome d'arte:");

        // Visualizzazione del dialogo e acquisizione dell'input dell'utente
        Optional<String> result = dialog.showAndWait();

        // Utilizzo del risultato (se l'utente ha premuto "OK")
        // Utilizzo del risultato (se l'utente ha premuto "OK")
        result.ifPresent(nomeDArte -> {
            // Ottieni l'orario corrente
            LocalTime localTime = LocalTime.now();

            // Crea un oggetto Timestamp combinando la data corrente con l'orario
            Timestamp timestamp = Timestamp.valueOf(LocalDate.now().atTime(localTime));

            try {
                // Effettua l'inserimento nel database tramite AutoreDAO
                autoreDAO.insertIntoAutore(nomeDArte, timestamp, sessionManager.getUtenteCorrente().getMail());

                // Mostra un popup di successo
                showAlert(Alert.AlertType.INFORMATION, "Ora sei un autore", "Successo");

                // Effettua il logout e naviga alla HomePage
                sessionManager.logout();
                goToHomePage();
            } catch (Exception e) {
                // In caso di errore, mostra un popup di errore
                showAlert(Alert.AlertType.ERROR, "Qualcosa è andato storto, riprova", "Errore");

                // Stampa l'eccezione per ulteriori informazioni di debug
                e.printStackTrace();
            }
        });
    }

    /**
     * Mostra una finestra di avviso con il tipo di avviso specificato, testo del contenuto e titolo.
     *
     * @param alertType Tipo di avviso (INFORMATION, ERROR, ecc.).
     * @param contentText Testo del contenuto della finestra di avviso.
     * @param title Titolo della finestra di avviso.
     */
    private void showAlert(Alert.AlertType alertType, String contentText, String title) {
        // Crea un nuovo oggetto Alert con il tipo specificato
        Alert alert = new Alert(alertType);

        // Imposta il titolo della finestra di avviso
        alert.setTitle(title);

        // Imposta l'intestazione della finestra di avviso a null
        alert.setHeaderText(null);

        // Imposta il testo del contenuto della finestra di avviso
        alert.setContentText(contentText);

        // Mostra la finestra di avviso e attendi finché l'utente non la chiude
        alert.showAndWait();
    }

    @FXML
    public void handleNotificaListViewClick(MouseEvent event) throws IOException {
        Modifica mod = notificheListView.getSelectionModel().getSelectedItem();

        if(mod != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/NotificaPage.fxml"));
            Parent root = loader.load();

            NotificaPageController notificaPageController = loader.getController();
            notificaPageController.setPrimaryStage(primaryStage);
            notificaPageController.setData(mod);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Metodo di inizializzazione chiamato automaticamente quando il controller viene caricato.
     * Inizializza l'interfaccia utente con i dati dell'utente corrente, le ListView per le pagine, le modifiche e le notifiche,
     * e imposta i factory per personalizzare l'aspetto delle celle nelle ListView.
     */
    @FXML
    private void initialize() {
        // Imposta i dati dell'utente corrente nelle Label dell'interfaccia utente
        nomeCognomeLabel.setText(sessionManager.getUtenteCorrente().getNome() + " " + sessionManager.getUtenteCorrente().getCognome());
        nomedarteLabel.setText(sessionManager.getUtenteCorrente().getNomedarte());
        emailLabel.setText(sessionManager.getUtenteCorrente().getMail());
        carrieraLabel.setText(String.valueOf(sessionManager.getUtenteCorrente().getIniziocarriera()));

        // Imposta i factory per personalizzare l'aspetto delle celle nella ListView delle pagine
        myPageListView.setCellFactory(param -> new ListCell<Pagina>() {
            @Override
            protected void updateItem(Pagina item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Personalizza il testo visualizzato per ogni cella
                    String displayText = String.format("Titolo: %s\nAutore: %s\nLink: %s",
                            item.getTitolo(), item.getAutore(), item.getLink());
                    setText(displayText);
                }
            }
        });

        // Imposta i factory per personalizzare l'aspetto delle celle nella ListView delle modifiche
        modificheListView.setCellFactory(param -> new ListCell<Modifica>() {
            @Override
            protected void updateItem(Modifica item, boolean empty) {
                super.updateItem(item, empty);

                // Controlla se la cella è vuota o se l'oggetto associato è nullo
                if (empty || item == null) {
                    // Imposta il testo della cella su null se la cella è vuota o l'oggetto è nullo
                    setText(null);
                } else {
                    String stato;
                    // Determina lo stato della modifica in base al valore dell'attributo "stato" dell'oggetto "item"
                    if (item.getStato() == 1) {
                        stato = "Approvato";
                    } else if (item.getStato() == 0) {
                        stato = "Pending";
                    } else {
                        stato = "Non-Approvato";
                    }
                    // Formatta una stringa contenente le informazioni della modifica
                    String displayText = String.format(
                            "Frase Originale: %s\nFrase Modificata: %s\nStato: %s\nOra Modifica: %s",
                            item.getFraseoriginale(), item.getFrasemodificata(), stato, String.valueOf(item.getDataoramod())
                    );

                    // Imposta il testo della cella con le informazioni formattate
                    setText(displayText);
                }

            }
        });

        // Imposta i factory per personalizzare l'aspetto delle celle nella ListView delle notifiche
        notificheListView.setCellFactory(param -> new ListCell<Modifica>() {
            @Override
            protected void updateItem(Modifica item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Personalizza il testo visualizzato per ogni cella
                    String displayText = String.format("Frase Originale: %s\nFrase Modificata: %s\nOra Modifica: %s",
                            item.getFraseoriginale(), item.getFrasemodificata(), String.valueOf(item.getDataoramod()));
                    setText(displayText);
                }
            }
        });

        // Aggiorna l'interfaccia utente con i dati
        updateUI();
    }

}
