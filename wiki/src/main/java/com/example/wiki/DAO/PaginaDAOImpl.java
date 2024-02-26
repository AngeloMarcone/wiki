package com.example.wiki.DAO;

import com.example.wiki.domain.Pagina;
import com.example.wiki.util.ConnessioneDatabase;
import com.example.wiki.util.SessionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PaginaDAOImpl implements PaginaDAO{

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
     * Ottiene una lista di tutte le pagine presenti nel database.
     *
     * @return Una lista di oggetti Pagina.
     */
    public List<Pagina> getPages() {
        // Query SQL per ottenere tutte le pagine dal database.
        String query = "SELECT * FROM Pagina";
        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(query);

            // Eseguo la query SQL e ottengo il risultato.
            ResultSet res = pst.executeQuery();

            // Creo una lista per le pagine.
            List<Pagina> pageList = new ArrayList<>();
            while (res.next()) {
                // Estraggo le informazioni dal risultato e creo un oggetto Pagina.
                String linkDB = res.getString("link");
                String titoloDB = res.getString("titolo");
                Timestamp dataoracreazioneDB = res.getTimestamp("dataoracreazione");
                String autoreDB = res.getString("autore");
                Pagina p = new Pagina(linkDB, titoloDB, dataoracreazioneDB, autoreDB);
                //aggiungo alla lista la pagina
                pageList.add(p);
            }
            return pageList;
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
     * Crea una nuova pagina nel database con il link, titolo e la frase forniti.
     *
     * @param link   Il link univoco associato alla pagina, ottenuto dall'interfaccia grafica.
     * @param titolo Il titolo della pagina, ottenuto dall'interfaccia grafica.
     * @param frase  La frase associata alla pagina, ottenuta dall'interfaccia grafica.
     */
    public void createPage(String link, String titolo, String frase) {
        FraseDAO fraseDAO = new FraseDAOImpl();
        Connection con = null;
        try {
            con = connessioneDatabase.getConnection();
            con.setAutoCommit(false);
            //dato che e' un operazione di inserimento, in caso di errore dobbiamo fare rollback
            //(annullamento di tutte le modifiche apportate al database)
            //con.setAutoCommit(false); imposta l'autocommit su false, il che significa che le modifiche al database
            //non vengono confermate automaticamente ma solo quando viene chiamato con.commit();

            // Inserimento Pagina
            insertPagina(con, link, titolo);

            // Inserimento Frasi
            fraseDAO.insertFrasi(con, link, frase);

            con.commit(); //tutto e' andato liscio, le modifiche diventano permanenti
        } catch (SQLException ex) {
            handleSQLException(ex);
            rollbackTransaction(con);
        } finally {
            connessioneDatabase.setInstanceNull();
        }
    }

    /**
     * Inserisce una nuova pagina nel database con il link e il titolo forniti.
     *
     * @param con    La connessione al database.
     * @param link   Il link univoco associato alla pagina, ottenuto dall'interfaccia grafica.
     * @param titolo Il titolo della pagina, ottenuto dall'interfaccia grafica.
     * @throws SQLException Se si verifica un'eccezione SQL durante l'inserimento della pagina nel database.
     */
    private void insertPagina(Connection con, String link, String titolo) throws SQLException {
        // Query SQL per l'inserimento della pagina nel database.
        String insertPaginaQuery = "INSERT INTO Pagina(link, titolo, dataoracreazione, autore) VALUES(?, ?, ?, ?)";

        // Ottengo l'istanza del SessionManager per gestire l'utente corrente.
        SessionManager sessionManager = SessionManager.getInstance();

        try {
            // Preparo la query SQL.
            PreparedStatement pstPagina = con.prepareStatement(insertPaginaQuery);

            // Imposto i valori dei parametri nella query.
            pstPagina.setString(1, link);
            pstPagina.setString(2, titolo);
            // Imposto la data e l'ora correnti come timestamp per la creazione della pagina.
            LocalTime localTime = LocalTime.now();
            Timestamp timestamp = Timestamp.valueOf(LocalDate.now().atTime(localTime));
            pstPagina.setTimestamp(3, timestamp);
            // Imposto l'autore della pagina come l'utente corrente.
            pstPagina.setString(4, sessionManager.getUtenteCorrente().getNomedarte());

            // Eseguo la query per inserire la nuova pagina nel database.
            pstPagina.executeUpdate();

            System.out.println("Pagina creata con successo.");
        } catch (SQLException e) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(e);
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }

    /**
     * Annulla la transazione corrente, ripristinando lo stato del database al momento dell'inizio della transazione.
     *
     * @param con La connessione al database.
     */
    private void rollbackTransaction(Connection con) {
        try {
            // Verifico se la connessione non è nulla prima di eseguire il rollback.
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException e) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(e);
        }
    }


    /**
     * Ottiene una lista di pagine create dall'autore corrente.
     *
     * @param u L'utente corrente.
     * @return Una lista di pagine create dall'autore corrente.
     */
    public List<Pagina> getPagesByAutore(UtenteCorrente u) {
        // Query per selezionare le pagine create dall'autore corrente.
        String query = "SELECT * FROM Pagina WHERE autore = ?";

        try {
            // Ottiengo la connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();
            // Preparo la query SQL.
            PreparedStatement pst = con.prepareStatement(query);
            // Imposto il parametro della query con il nome d'arte dell'autore corrente.
            pst.setString(1, u.getNomedarte());

            try (ResultSet res = pst.executeQuery()) {
                // Lista per memorizzare le pagine.
                List<Pagina> pageList = new ArrayList<>();

                // Itero sui risultati della query.
                while (res.next()) {
                    // Ottiengo i dati della pagina dal risultato della query.
                    String linkDB = res.getString("link");
                    String titoloDB = res.getString("titolo");
                    Timestamp dataoracreazioneDB = res.getTimestamp("dataoracreazione");
                    String autoreDB = res.getString("autore");

                    // Creo un oggetto Pagina e lo aggiungo alla lista.
                    Pagina p = new Pagina(linkDB, titoloDB, dataoracreazioneDB, autoreDB);
                    pageList.add(p);
                }

                // Restituisco la lista di pagine create dall'autore corrente.
                return pageList;
            }

        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
            // Restituisco una lista vuota in caso di errore.
            return new ArrayList<>();
        } finally {
            // Chiudo la connessione al database.
            connessioneDatabase.setInstanceNull();
        }
    }


    /**
     * Ottiene una pagina in base al link fornito.
     *
     * @param link Il link della pagina da cercare.
     * @return La pagina corrispondente al link specificato.
     */
    public Pagina getPageByLink(String link) {
        // Query per selezionare una pagina in base al link.
        String query = "SELECT * FROM Pagina WHERE link = ?";

        try (Connection con = connessioneDatabase.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Imposto il parametro della query con il link fornito.
            pst.setString(1, link);

            try (ResultSet res = pst.executeQuery()) {
                // Verifico se esiste una pagina con il link specificato.
                if (res.next()) {
                    // Ottienho i dati della pagina dal risultato della query.
                    String linkDB = res.getString("link");
                    String titoloDB = res.getString("titolo");
                    Timestamp dataoracreazioneDB = res.getTimestamp("dataoracreazione");
                    String autoreDB = res.getString("autore");

                    // Creo un oggetto Pagina e lo restituisco.
                    return new Pagina(linkDB, titoloDB, dataoracreazioneDB, autoreDB);
                } else {
                    // Se non esiste una pagina con il link specificato, restituisco null.
                    return null;
                }
            }

        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
            // Restituisco null in caso di errore.
            return null;
        } finally {
            // Chiudo la connessione al database.
            connessioneDatabase.setInstanceNull();
        }
    }


    /**
     * Funzione che uso per cercare le pagine in base a una stringa(titolo).
     *
     * @param toSearch Qui ci metto la stringa che voglio cercare nei titoli delle pagine.
     * @return Quindi, mi restituisce una lista di pagine che corrispondono alla mia ricerca.
     */
    public List<Pagina> search(String toSearch) {
        // Preparo la query SQL per selezionare le pagine con un titolo che contiene toSearch.
        String query = "SELECT * FROM Pagina WHERE titolo ILIKE ?";
        //ILIKE leva il case sensitive e opera come l'operatore LIKE

        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getInstance().getConnection();

            // Qui, sto preparando la query SQL per cercare corrispondenze parziali nel titolo delle pagine.
            // L'uso delle percentuali prima e dopo la stringa di ricerca indica che la corrispondenza può avvenire ovunque nel titolo.
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "%" + toSearch + "%");

            // Eseguo la query.
            ResultSet res = pst.executeQuery();

            // Inizializzo una lista per metterci le pagine trovate.
            List<Pagina> pageList = new ArrayList<>();

            // Itero sui risultati della query.
            while (res.next()) {
                // Prendo i dati della pagina dal risultato della query.
                String linkDB = res.getString("link");
                String titoloDB = res.getString("titolo");
                Timestamp dataoracreazioneDB = res.getTimestamp("dataoracreazione");
                String autoreDB = res.getString("autore");

                // Costruisco un oggetto Pagina e lo aggiungo alla mia lista.
                Pagina p = new Pagina(linkDB, titoloDB, dataoracreazioneDB, autoreDB);
                pageList.add(p);
            }

            // Alla fine, restituisco questa lista con le pagine che corrispondono alla mia ricerca.
            return pageList;

        } catch (SQLException ex) {
            // greve, se c'è qualche problema con il database, gestisco l'errore e torno una lista vuota.
            handleSQLException(ex);
            return new ArrayList<>();

        } finally {
            // Infine, chiudo la connessione al database.
            connessioneDatabase.setInstanceNull();
        }
    }


    private void handleSQLException(SQLException ex) {
        ex.printStackTrace();
    }


}
