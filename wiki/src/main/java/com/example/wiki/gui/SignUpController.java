package com.example.wiki.gui;

import com.example.wiki.DAO.UtenteDAO;
import com.example.wiki.DAO.UtenteDAOImpl;
import com.example.wiki.domain.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la pagina di registrazione (SignUpPage.fxml).
 */
public class SignUpController {

    @FXML
    private TextField nome;

    @FXML
    private TextField cognome;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button signupButton;

    @FXML
    private Label wrongSignUp;

    private Stage primaryStage;
    private UtenteDAO utenteDAO = new UtenteDAOImpl();

    /**
     * Imposta il primary stage per il controller.
     *
     * @param primaryStage Il primary stage dell'applicazione.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Metodo chiamato quando viene premuto il pulsante di registrazione.
     * Controlla la validità dei dati inseriti, crea un nuovo utente e naviga alla pagina di login.
     *
     * @throws IOException Se si verifica un errore durante la navigazione alla pagina di login.
     */
    public void writeData() throws IOException {
        String nomeInserito = nome.getText();
        String cognomeInserito = cognome.getText();
        String emailInserita = email.getText();
        String passwordInserita = password.getText();

        if (isValidEmail(emailInserita) && isValidPassword(passwordInserita)) {
            Utente nuovoUtente = new Utente(emailInserita, cognomeInserito, nomeInserito, passwordInserita);
            utenteDAO.createUtente(nuovoUtente);
            goToLoginPage();
        } else {
            wrongSignUp.setText("Email o password non valide.");
            showInvalidInputAlert();
        }
    }

    /**
     * Mostra un avviso in caso di dati inseriti non validi.
     */
    private void showInvalidInputAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Errore di validazione");
        alert.setHeaderText(null);
        alert.setContentText("Email o password non valide. La password deve contenere una lettera minuscola, una maiuscola, un numero e la lunghezza deve essere maggiore di 8 caratteri.");
        alert.showAndWait();
    }

    /**
     * Controlla se un indirizzo email è valido.
     *
     * @param email Indirizzo email da controllare.
     * @return True se l'indirizzo email è valido, altrimenti False.
     */
    private boolean isValidEmail(String email) {
        /*
        *Questa funzione utilizza un'espressione regolare (regex) per verificare se la stringa email segue il formato
        *standard di un indirizzo email.
         */
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    /**
     * Controlla se una password è valida.
     *
     * @param password Password da controllare.
     * @return True se la password è valida, altrimenti False.
     */
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    /**
     * Naviga alla pagina di login.
     *
     * @throws IOException Se si verifica un errore durante la navigazione.
     */
    private void goToLoginPage() throws IOException {
        // Carica il FXML della pagina di login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/LoginPage.fxml"));
        Parent root = loader.load();

        // Imposta il controller della pagina di login
        LoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);

        // Configura la scena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }

    /**
     * Naviga alla homepage.
     *
     * @throws IOException Se si verifica un errore durante la navigazione alla homepage.
     */
    @FXML
    private void goToHomePage() throws IOException {
        // Carica il FXML della homepage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wiki/HomePage.fxml"));
        Parent root = loader.load();

        // Imposta il controller della homepage
        HomeController homeController = loader.getController();
        homeController.setPrimaryStage(primaryStage);

        // Configura la scena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Rendi la finestra visibile
        primaryStage.show();
    }
}
