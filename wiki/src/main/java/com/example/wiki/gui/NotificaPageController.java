package com.example.wiki.gui;

import com.example.wiki.DAO.ModificaDAO;
import com.example.wiki.DAO.ModificaDAOImpl;
import com.example.wiki.domain.Modifica;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la schermata di gestione delle notifiche.
 */
public class NotificaPageController {

    private Modifica modifica;

    private Stage primaryStage;

    /**
     * Imposta il primaryStage per il controller.
     *
     * @param primaryStage Il primaryStage da impostare.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private Button homeButton;

    @FXML
    private Button accettaButton;

    @FXML
    private Button rifiutaButton;

    @FXML
    private TextArea fraseOriginaleLabel;

    @FXML
    private TextArea fraseModificataLabel;

    @FXML
    private Label utenteLabel;

    @FXML
    private Label autoreLabel;

    @FXML
    private Label dataLabel;

    @FXML
    private Label paginaLabel;

    /**
     * Naviga alla homepage dopo la gestione della notifica.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della pagina principale.
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
     * Imposta i dati della modifica sulla schermata.
     *
     * @param mod La modifica da visualizzare.
     */
    public void setData(Modifica mod) {
        if (mod != null) {
            modifica = new Modifica(mod.getCodmod(), mod.getFraseoriginale(), mod.getCodfrase(), mod.getPagina(), mod.getFrasemodificata(), mod.getStato(), mod.getDataoramod(), mod.getUtente(), mod.getAutore());
            autoreLabel.setText(modifica.getAutore());
            utenteLabel.setText(modifica.getUtente());
            dataLabel.setText(String.valueOf(modifica.getDataoramod()));
            paginaLabel.setText(modifica.getPagina());

            fraseModificataLabel.setText(modifica.getFrasemodificata());
            fraseModificataLabel.setEditable(false);

            fraseOriginaleLabel.setText(modifica.getFraseoriginale());
            fraseOriginaleLabel.setEditable(false);
        }
    }

    /**
     * Gestisce il pulsante "Accetta".
     * gestisce la modifica accettandola e cambia scena (Homepage).
     *
     * @throws IOException Se si verifica un errore durante la navigazione alla pagina principale.
     */
    @FXML
    private void handleAccettaButton() throws IOException {
        ModificaDAO modificaDAO = new ModificaDAOImpl();
        modificaDAO.handleModifica(1, modifica.getCodmod());
        goToHomePage();
    }

    /**
     * Gestisce il click del pulsante "Rifiuta".
     * gestisce la modifica rifiutandola e cambia scena (Homepage).
     *
     * @throws IOException Se si verifica un errore durante la navigazione alla pagina principale.
     */
    @FXML
    private void handleRifiutaButton() throws IOException {
        ModificaDAO modificaDAO = new ModificaDAOImpl();
        modificaDAO.handleModifica(0, modifica.getCodmod());
        goToHomePage();
    }
}
