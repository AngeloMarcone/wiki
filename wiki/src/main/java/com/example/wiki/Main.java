package com.example.wiki;

import com.example.wiki.gui.HomeController;
import com.example.wiki.util.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * La classe Main rappresenta l'entry point dell'applicazione JavaFX.
 * Estende la classe Application e implementa il metodo start, che viene chiamato per avviare l'applicazione.
 */
public class Main extends Application {

    /**
     * Metodo start, chiamato per avviare l'applicazione JavaFX.
     * @param primaryStage Lo stage principale dell'applicazione.
     * @throws Exception Possibili eccezioni durante l'avvio dell'applicazione.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inizializza il SessionManager
        SessionManager sessionManager = SessionManager.getInstance();

        // Carica l'interfaccia utente principale (HomePage.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();

        // Imposta il controller della Home
        HomeController homeController = loader.getController();

        // Passa il SessionManager
        homeController.setSessionManager(sessionManager);
        // Passa al homeController lo stage corrente
        homeController.setPrimaryStage(primaryStage);

        // Configura la scena
        Scene scene = new Scene(root, 650, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AT Wiki");
        Image icon = new Image(getClass().getResourceAsStream("/com/example/wiki/logowiki.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);

        // Mostra la finestra principale
        primaryStage.show();
    }

    /**
     * Metodo main, punto di ingresso per avviare l'applicazione.
     * @param args Gli argomenti passati all'avvio dell'applicazione.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
