package com.example.wiki.gui;

import com.example.wiki.DAO.FraseDAO;
import com.example.wiki.DAO.FraseDAOImpl;
import com.example.wiki.DAO.PaginaDAO;
import com.example.wiki.DAO.PaginaDAOImpl;
import com.example.wiki.domain.Collegamento;
import com.example.wiki.domain.Frase;
import com.example.wiki.domain.Pagina;
import com.example.wiki.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
public class VisualizePageController {

    @FXML
    private Button editPageButton;

    @FXML
    private TextFlow textFlow;

    @FXML
    private Label titoloField;

    @FXML
    private Label lastUpdateField;

    // Riferimento alla finestra principale dell'applicazione
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // sessionmanager per gestire lo stato dell'utente
    private SessionManager sessionManager = SessionManager.getInstance();

    // Lista di frasi associate alla pagina attuale
    List<Frase> frasi;

    // Oggetto DAO per interagire con il database delle frasi
    private final FraseDAO fraseDAO = new FraseDAOImpl();

    // Metodo chiamato durante l'inizializzazione del controller
    public void initialize(){
        // Imposta la visibilità del pulsante di modifica in base allo stato dell'utente (loggato o meno)
        editPageButton.setVisible(sessionManager.isUtenteLoggato());
    }

    // Metodo per impostare i dati della pagina attuale nel controller
    public void setData(Pagina currentPage) throws SQLException {
        if (currentPage != null) {
            // Imposta il titolo della pagina
            titoloField.setText(currentPage.getTitolo());

            // Ottieni le frasi associate alla pagina dal database
            frasi = fraseDAO.getFrasi(currentPage.getLink());

            // Aggiorna l'interfaccia utente con le frasi ottenute
            updateTextFlow(frasi);

            // Imposta l'ultima data di aggiornamento della pagina
            lastUpdateField.setText(String.valueOf(currentPage.getDataoracreazione()));
        }
    }
    /**
     * Aggiorna il TextFlow con le frasi ottenute dal database.
     * Ogni frase viene rappresentata come un nodo del textflow o un hyperlink nel TextFlow.
     * @param frasi Lista di frasi da visualizzare.
     * @throws SQLException Eccezione SQL in caso di errore durante l'interazione con il database.
     */
    private void updateTextFlow(List<Frase> frasi) throws SQLException {
        // Pulisce il TextFlow prima di aggiungere nuovi elementi
        textFlow.getChildren().clear();

        // Itera attraverso la lista di frasi ottenute dal database
        for (Frase f : frasi) {
            // Ottiene il testo della frase
            String text = f.getCaratteri();

            // Cerca un collegamento associato alla frase nel database
            Collegamento c = fraseDAO.fraseconlink(f.getCodfrase(), f.getPagina());

            if (c != null) { // Se la frase ha un link
                String textNode = text + "\n";
                Hyperlink link = new Hyperlink(textNode);

                // stile per rendere il link blu e sottolineato
                link.setStyle("-fx-text-fill: blue; -fx-underline: true;");

                // Azione da eseguire quando il link viene cliccato
                link.setOnAction(e -> {
                    PaginaDAO paginaDAO = new PaginaDAOImpl();
                    try {
                        // Naviga alla pagina collegata quando il link viene cliccato
                        goToVisualizePage(paginaDAO.getPageByLink(c.getPaginaRiferimento()));
                    } catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                // Aggiunge il link e un nuovo nodo di testo per separare le frasi
                textFlow.getChildren().addAll(link, new Text("\n"));
            } else {
                // Se la frase non è un link, aggiunge il nodo di testo al TextFlow
                Text textNode = new Text(text + "\n");
                textFlow.getChildren().add(textNode);
            }
        }
    }


    /**
     * Naviga alla pagina di visualizzazione delle frasi, impostando i dati della pagina corrente.
     * VIENE USATO SOLO PER PASSARE DA UN LINK AD UN ALTRO (non c'e' bisogno di cambiare scena dato che ga siamo in visualize page)
     * @param currentPage Pagina da visualizzare.
     * @throws IOException Eccezione di input/output in caso di errore durante la navigazione.
     * @throws SQLException Eccezione SQL in caso di errore durante l'interazione con il database.
     */
    private void goToVisualizePage(Pagina currentPage) throws IOException, SQLException {
        // Imposta i dati della pagina corrente
        setData(currentPage);
    }



    /**
     * Naviga alla HomePage dell'applicazione.
     * Carica il file FXML della HomePage, imposta il controller e la scena, quindi rende la finestra visibile.
     * @throws IOException Eccezione di input/output in caso di errore durante la navigazione.
     */
    @FXML
    private void goToHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/HomePage.fxml"));
        Parent root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Naviga alla pagina di modifica (EditPage) dell'applicazione.
     * Carica il file FXML di EditPage, imposta il controller, i dati da visualizzare e la scena.
     * @throws IOException Eccezione di input/output in caso di errore durante la navigazione.
     */
    @FXML
    private void goToEditPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/EditPage.fxml"));
        Parent root = loader.load();
        EditPageController editPageController = loader.getController();
        editPageController.setPrimaryStage(primaryStage);

        // Verifica che frasi non e' null
        if (frasi != null) {
            editPageController.setData(frasi);

            // Configura la scena
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // Rendi la finestra visibile
            primaryStage.show();
        } else {
            System.err.println("frasi e' null.");
        }
    }



}
