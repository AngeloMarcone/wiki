package com.example.wiki.DAO;

import com.example.wiki.domain.Utente;
import com.example.wiki.domain.UtenteCorrente;
import com.example.wiki.util.ConnessioneDatabase;

import java.sql.*;
public class UtenteDAOImpl implements UtenteDAO {

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
     * Legge le informazioni dell'utente dal database in base all'email e alla password fornite.
     *
     * @param mailGUI    L'email dell'utente da recuperare.
     * @param passwordGUI La password associata all'account dell'utente.
     * @return Un'istanza di UtenteCorrente se l'utente è trovato; altrimenti, restituisce null.
     */
    public UtenteCorrente readFromDatabase(String mailGUI, String passwordGUI) {
        // Query SQL per recuperare le informazioni dell'utente e dell'autore
        String SELECT_UTENTE = "SELECT * FROM Utente WHERE mail = ? AND password = ?";
        String SELECT_AUTORE = "SELECT * FROM Autore WHERE utente = ?";

        try {
            // Ottiene una connessione al database
            Connection con = ConnessioneDatabase.getInstance().getConnection();

            // Prepara la query per l'utente
            PreparedStatement pstUtente = con.prepareStatement(SELECT_UTENTE);
            pstUtente.setString(1, mailGUI);
            pstUtente.setString(2, passwordGUI);

            // Esegue la query per l'utente
            ResultSet resUtente = pstUtente.executeQuery();

            // Se l'utente è trovato
            if (resUtente.next()) {
                // Estrae le informazioni dell'utente dal result set
                String mail = resUtente.getString("mail");
                String nome = resUtente.getString("nome");
                String cognome = resUtente.getString("cognome");
                String password = resUtente.getString("password");

                // Prepara la query per l'autore
                PreparedStatement pstAutore = con.prepareStatement(SELECT_AUTORE);
                pstAutore.setString(1, mail);

                // Esegue la query per l'autore
                ResultSet resAutore = pstAutore.executeQuery();

                // Se ci sono risultati nella tabella Autore
                if (resAutore.next()) {
                    // Estrae le informazioni dell'autore dal result set
                    String nomedarte = resAutore.getString("nomedarte");
                    Timestamp iniziocarriera = resAutore.getTimestamp("annoiniziocarriera");

                    // Restituisce un'istanza di UtenteCorrente con informazioni di utente e autore
                    return new UtenteCorrente(mail, nome, cognome, password, nomedarte, iniziocarriera);
                } else {
                    // Restituisce un'istanza di UtenteCorrente solo con informazioni di utente
                    return new UtenteCorrente(mail, nome, cognome, password, null, null);
                }
            }
        } catch (SQLException ex) {
            // Gestisce eventuali eccezioni SQL
            handleSQLException(ex);
        } finally {
            // Imposta a null l'istanza della connessione al database, garantendo la chiusura della connessione
            connessioneDatabase.setInstanceNull();
        }

        // Restituisce null se nessun utente è trovato o se si verifica un'eccezione
        return null;
    }


    /**
     * Crea un nuovo utente nel database con le informazioni fornite.
     *
     * @param utente L'oggetto Utente contenente le informazioni da inserire nel database.
     */
    public void createUtente(Utente utente) {
        // Query SQL per inserire un nuovo utente nel database
        String query = "INSERT INTO Utente(mail, nome, cognome, password) VALUES(?, ?, ?, ?)";

        try {
            // Ottiene una connessione al database
            Connection con = connessioneDatabase.getConnection();
            PreparedStatement pst = con.prepareStatement(query);

            // Imposta i parametri della query con le informazioni dell'utente
            pst.setString(1, utente.getMail());
            pst.setString(2, utente.getNome());
            pst.setString(3, utente.getCognome());
            pst.setString(4, utente.getPassword());

            // Esegue l'aggiunta del nuovo utente nel database
            pst.executeUpdate();
            System.out.println("Utente creato con successo.");
        } catch (SQLException ex) {
            // Gestisce eventuali eccezioni SQL
            handleSQLException(ex);
        } finally {
            // Imposta a null l'istanza della connessione al database, garantendo la chiusura della connessione
            connessioneDatabase.setInstanceNull();
        }
    }



    private void handleSQLException(SQLException ex) {
        ex.printStackTrace();
    }
}