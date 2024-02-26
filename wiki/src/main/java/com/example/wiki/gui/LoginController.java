package com.example.wiki.gui;

import com.example.wiki.DAO.UtenteCorrente;
import com.example.wiki.DAO.UtenteDAO;
import com.example.wiki.DAO.UtenteDAOImpl;
import com.example.wiki.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la schermata di login.
 */
public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField emailField;

    @FXML
    private Label wrongLogin;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    private Stage primaryStage;
    private final UtenteDAO utenteDAO = new UtenteDAOImpl();
    private final SessionManager sessionManager = SessionManager.getInstance();

    /**
     * Imposta il primaryStage per il controller.
     *
     * @param primaryStage Il primaryStage da impostare.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gestisce il pulsante di login.
     * Ottiene i dati dalla schermata di login e verifica le credenziali.
     *
     */
    @FXML
    public void getData() {
        String email = emailField.getText();
        String password = passwordField.getText();
        try {
            checkLogin(email, password, wrongLogin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica le credenziali dell'utente e gestisce se va bene o l'errore del login.
     *
     * @param mail        L'email dell'utente.
     * @param password    La password dell'utente.
     * @param wrongLogin  Label per mostrare eventuali messaggi di errore.
     * @throws IOException Se si verifica un errore durante la navigazione alla pagina successiva.
     */
    private void checkLogin(String mail, String password, Label wrongLogin) throws IOException {
        UtenteCorrente u = (UtenteCorrente) utenteDAO.readFromDatabase(mail, password);

        if (u != null) {
            // Imposta l'UtenteCorrente solo se l'utente è stato trovato
            sessionManager.setUtenteCorrente(u);
            goToHomePage();
        } else {
            wrongLogin.setText("Password o Email errati");
        }
    }

    /**
     * Naviga alla HomePage dopo il login.
     *
     * @throws IOException Se si verifica un errore durante il caricamento della HomePage.
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
     * va alla pagina di registrazione.
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
}
