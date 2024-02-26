package com.example.wiki.gui;

import com.example.wiki.DAO.PaginaDAO;
import com.example.wiki.DAO.PaginaDAOImpl;
import com.example.wiki.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la creazione di una nuova pagina.
 */
public class CreatePageController {

    private Stage primaryStage;

    @FXML
    private TextField link;

    @FXML
    private TextField titolo;

    @FXML
    private TextArea frase;

    @FXML
    private Button createPageButton;

    @FXML
    private Button homeButton;

    private final PaginaDAO paginaDAO = new PaginaDAOImpl();

    private SessionManager sessionManager = SessionManager.getInstance();

    /**
     * Imposta il primaryStage per il controller.
     *
     * @param primaryStage Lo Stage principale dell'applicazione.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gestisce l'azione del pulsante per la creazione di una nuova pagina.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della pagina successiva.
     */
    public void createPage() throws IOException {
        paginaDAO.createPage(link.getText(), titolo.getText(), frase.getText());
        goToHomePage();
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
