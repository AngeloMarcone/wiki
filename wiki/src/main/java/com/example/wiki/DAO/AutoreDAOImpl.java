package com.example.wiki.DAO;

import com.example.wiki.util.ConnessioneDatabase;

import java.sql.*;
public class AutoreDAOImpl implements AutoreDAO {

    // Inizializza la connessione al database utilizzando la classe ConnessioneDatabase.
    // Se la connessione non può essere stabilita a causa di un'eccezione SQL, viene lanciata un'eccezione RuntimeException.
    // La connessione al database è gestita tramite il singleton pattern per assicurare un'unica istanza di ConnessioneDatabase.
    private ConnessioneDatabase connessioneDatabase;

    {
        try {
            // Ottengo un'istanza della classe ConnessioneDatabase.
            connessioneDatabase = ConnessioneDatabase.getInstance();
        } catch (SQLException e) {
            //Se si verifica un'eccezione mentre prendo l'istanza lancio exception
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserisco un nuovo autore nel database.
     *
     * @param nomeDArteGUI Il nome d'arte che ho ottenuto dall'interfaccia grafica.
     * @param timestampGUI Il timestamp rappresentante l'anno di inizio della carriera, che ho ottenuto dall'interfaccia grafica.
     * @param mailGUI L'indirizzo email dell'autore, che ho ottenuto dall'interfaccia grafica.
     */
    public void insertIntoAutore(String nomeDArteGUI, Timestamp timestampGUI, String mailGUI) {
        // Costruisco la query SQL per l'inserimento del nuovo autore nel database.
        String insertAutore = "INSERT INTO Autore(nomedarte, annoiniziocarriera, utente) VALUES(?, ?, ?)";

        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getConnection();

            // Preparo lo statement SQL per l'inserimento dell'autore.
            PreparedStatement pstPagina = con.prepareStatement(insertAutore);

            // Imposto i parametri dello statement con i valori ottenuti dall'interfaccia grafica.
            pstPagina.setString(1, nomeDArteGUI);
            pstPagina.setTimestamp(2, timestampGUI);
            pstPagina.setString(3, mailGUI);

            // Eseguo l'aggiornamento del database per inserire il nuovo autore.
            pstPagina.executeUpdate();

            // Stampo un messaggio di conferma dell'inserimento dell'autore.
            System.out.println("Autore Inserito");
        } catch (SQLException e) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(e);
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }

    /**
     * Gestisce un'eccezione di tipo SQLException stampando il trace dell'errore sulla console.
     *
     * @param ex L'eccezione di tipo SQLException da gestire.
     */
    private void handleSQLException(SQLException ex) {
        // Stampa il trace dell'eccezione sulla console.
        ex.printStackTrace();
    }
}