package com.example.wiki.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La classe ConnessioneDatabase è un singleton che gestisce la connessione al database PostgreSQL.
 * Si occupa di caricare il driver del database, avviare la connessione, fornire l'istanza della classe e chiudere la connessione.
 */
public class ConnessioneDatabase {

    private static ConnessioneDatabase instance;
    private Connection connection;

    private String url = "jdbc:postgresql://localhost:5432/wiki2.0";
    private String user = "postgres";
    private String password = "ciao";

    /**
     * Costruttore privato della classe. Carica il driver del database e avvia la connessione.
     * Gestisce ClassNotFoundException e SQLException.
     */
    private ConnessioneDatabase() {
        try {
            // Carica il driver del database
            Class.forName("org.postgresql.Driver");
            // Avvia la connessione
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            handleSQLException((SQLException) e);
        }
    }

    /**
     * Metodo per ottenere l'istanza della classe ConnessioneDatabase.
     * Se l'istanza è null, crea una nuova istanza della classe.
     * @return L'istanza della classe ConnessioneDatabase.
     * @throws SQLException Eccezione di SQL in caso di errore durante la creazione dell'istanza.
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    /**
     * Metodo per impostare l'istanza a null. Utile per gestire la chiusura dell'applicazione e di alcune funzioni.
     */
    public void setInstanceNull() {
        instance = null;
    }

    /**
     * Metodo per ottenere l'oggetto Connection, che rappresenta la connessione al database.
     * @return L'oggetto Connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Metodo per chiudere la connessione al database.
     * Gestisce SQLException.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Metodo privato per gestire le eccezioni di tipo SQLException.
     * Stampa l'eccezione.
     * @param ex L'eccezione di tipo SQLException.
     */
    private void handleSQLException(SQLException ex) {
        ex.printStackTrace();
    }
}
