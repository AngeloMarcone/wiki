package com.example.wiki.DAO;

import com.example.wiki.domain.Modifica;
import com.example.wiki.domain.UtenteCorrente;
import com.example.wiki.util.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModificaDAOImpl implements ModificaDAO{

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
     * Ottiene una lista di modifiche effettuate dall'utente corrente.
     *
     * @param u L'utente corrente di cui ottenere le modifiche.
     * @return Una lista di oggetti Modifica associate all'utente corrente.
     */
    public List<Modifica> getMod(UtenteCorrente u) {
        // Query SQL per ottenere le modifiche in base all'utente corrente.
        String query = "SELECT * FROM Modifica WHERE utente = ?";
        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(query);

            // Imposto il valore del parametro nella query.
            pst.setString(1, u.getMail());

            // Eseguo la query SQL e ottengo il risultato.
            ResultSet res = pst.executeQuery();

            // Creo una lista per le modifiche.
            List<Modifica> modList = new ArrayList<>();
            while (res.next()) {
                // Estraggo le informazioni dal risultato e creo un oggetto Modifica.
                int codmod = res.getInt("codmod");
                String fraseoriginale = res.getString("fraseoriginale");
                int codfrase = res.getInt("codfrase");
                String pagina = res.getString("pagina");
                String frasemodificata = res.getString("frasemodificata");
                int stato = res.getInt("stato");
                Timestamp dataoramod = res.getTimestamp("dataoramod");
                String utente = res.getString("utente");
                String autore = res.getString("autore");
                Modifica m = new Modifica(codmod, fraseoriginale, codfrase, pagina, frasemodificata, stato, dataoramod, utente, autore);
                //aggiungo alla lista
                modList.add(m);
            }
            return modList;
        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
            // Restituisco una lista vuota in caso di errore.
            return new ArrayList<>();
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }


    /**
     * Ottiene una lista di modifiche non ancora notificate a un autore.
     *
     * @param u L'utente corrente di cui ottenere le notifiche.
     * @return Una lista di oggetti Modifica non ancora notificate all'autore.
     */
    public List<Modifica> getNotifiche(UtenteCorrente u) {
        // Query SQL per ottenere le notifiche in base all'autore e con stato nullo.
        String query = "SELECT * FROM Modifica WHERE autore = ? AND stato IS NULL";
        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(query);

            // Imposto il valore del parametro nella query.
            pst.setString(1, u.getNomedarte());

            // Eseguo la query SQL e ottengo il risultato.
            ResultSet res = pst.executeQuery();

            // Creo una lista per le notifiche.
            List<Modifica> modList = new ArrayList<>();
            while (res.next()) {
                // Estraggo le informazioni dal risultato e creo un oggetto Modifica.
                int codmod = res.getInt("codmod");
                String fraseoriginale = res.getString("fraseoriginale");
                int codfrase = res.getInt("codfrase");
                String pagina = res.getString("pagina");
                String frasemodificata = res.getString("frasemodificata");
                int stato = res.getInt("stato");
                Timestamp dataoramod = res.getTimestamp("dataoramod");
                String utente = res.getString("utente");
                String autore = res.getString("autore");
                Modifica m = new Modifica(codmod, fraseoriginale, codfrase, pagina, frasemodificata, stato, dataoramod, utente, autore);
                modList.add(m);
            }
            return modList;
        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
            // Restituisco una lista vuota in caso di errore.
            return new ArrayList<>();
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }


    /**
     * Gestisce l'aggiornamento dello stato di una modifica nel database.
     * cambia lo stato in caso di accettazione o rifiuto
     *
     * @param b           Lo stato da impostare nella modifica (può essere 1 o 0).
     * @param codmodGUI   Il codice della modifica da aggiornare.
     */
    public void handleModifica(int b, int codmodGUI) {
        // Query SQL per aggiornare lo stato di una modifica.
        String query = "UPDATE modifica SET stato = ? WHERE codmod = ?";

        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, b);
            pst.setInt(2, codmodGUI);

            // Eseguo l'aggiornamento nel database.
            int rowsUpdated = pst.executeUpdate();

            // Verifico se l'aggiornamento è avvenuto con successo.
            if (rowsUpdated > 0) {
                System.out.println("Aggiornamento effettuato con successo.");
            } else {
                System.out.println("Nessuna riga aggiornata. Potrebbe non esistere un record con quel codice.");
            }

        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }


    /**
     * Inserisce una nuova modifica nel database.
     *
     * @param fraseoriginale   La frase originale da modificare.
     * @param codfrase         Il codice della frase da modificare.
     * @param pagina           La pagina a cui appartiene la frase.
     * @param frasemodificata   La frase modificata.
     * @param dataoramod        La data e l'ora della modifica.
     * @param utente            L'utente che ha effettuato la modifica.
     * @param autore            L'autore associato alla modifica.
     */
    public void insertModifica(String fraseoriginale, int codfrase, String pagina,
                               String frasemodificata, Timestamp dataoramod,
                               String utente, String autore) {

        // Query SQL per inserire una nuova modifica.
        String query = "INSERT INTO modifica (fraseoriginale, codfrase, pagina, frasemodificata, dataoramod, utente, autore) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(query);

            // Imposto i valori del PreparedStatement utilizzando i parametri del metodo.
            pst.setString(1, fraseoriginale);
            pst.setInt(2, codfrase);
            pst.setString(3, pagina);
            pst.setString(4, frasemodificata);
            // Imposto il valore di default per lo stato.
            pst.setTimestamp(5, dataoramod);
            pst.setString(6, utente);
            pst.setString(7, autore);

            // Eseguo l'inserimento nel database.
            int rowsInserted = pst.executeUpdate();

            // Verifico se l'inserimento è avvenuto con successo.
            if (rowsInserted > 0) {
                System.out.println("Inserimento effettuato con successo.");
            } else {
                System.out.println("Nessuna riga inserita. Potrebbe esserci un problema nell'inserimento del record.");
            }

        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }



    private void handleSQLException(SQLException ex) {
        ex.printStackTrace();
    }
}
