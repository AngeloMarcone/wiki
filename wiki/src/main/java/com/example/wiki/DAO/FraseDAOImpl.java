package com.example.wiki.DAO;

import com.example.wiki.domain.Collegamento;
import com.example.wiki.domain.Frase;
import com.example.wiki.util.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FraseDAOImpl implements FraseDAO {

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
     * Recupero una lista di frasi associate a una specifica pagina dal database.
     *
     * @param link Il link della pagina di cui voglio ottenere le frasi.
     * @return Una lista di oggetti Frase associate alla pagina, ordinate per codice frase.
     */
    @Override
    public List<Frase> getFrasi(String link) {
        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getConnection();

            // Costruisco la query SQL per ottenere le frasi legate a una specifica pagina.
            String query = "SELECT * FROM Frase INNER JOIN Pagina ON Frase.pagina = Pagina.link WHERE pagina = ? ORDER BY codfrase";
            PreparedStatement pst = con.prepareStatement(query);

            // Imposto il valore del parametro ? con il link della pagina che mi interessa.
            pst.setString(1, link);

            // Eseguo la query SQL e ottengo il risultato.
            ResultSet res = pst.executeQuery();

            // Creo una lista per le frasi associate alla pagina.
            List<Frase> frasiList = new ArrayList<>();

            // Scorro il risultato e creo gli oggetti Frase corrispondenti.
            while (res.next()) {
                int codfraseDB = res.getInt("codfrase");
                String caratteriDB = res.getString("caratteri");
                String paginaDB = res.getString("pagina");
                Frase f = new Frase(codfraseDB, caratteriDB, paginaDB);
                frasiList.add(f);
            }

            // Restituisco la lista di frasi associate alla pagina.
            return frasiList;

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
     * Inserisco le frasi di una pagina nel database, gestendo anche i collegamenti tra frasi e pagine di riferimento.
     *
     * @param con La connessione al database su cui eseguire le operazioni.
     *            in questo caso la portiamo da un'altra funzione perche' la connessione e' gia aperta e sarebbe un errore
     *            chiuderla.
     * @param link Il link della pagina a cui le frasi appartengono.
     * @param frase La stringa contenente le frasi della pagina (frase e' un unica String che deve essere suddivisa).
     */
    public void insertFrasi(Connection con, String link, String frase) {
        // Query SQL per l'inserimento di frasi nella tabella "Frase".
        String insertFraseQuery = "INSERT INTO Frase(CodFrase, caratteri, Pagina) VALUES(?, ?, ?)";

        // Query SQL per l'inserimento di collegamenti nella tabella "Collegamento".
        String insertCollegamentoQuery = "INSERT INTO Collegamento(codfrase, pagina_frase, pagina_riferimento) VALUES(?, ?, ?)";

        try {
            con.setAutoCommit(false); // Inizia la transazione

            // Preparo gli statements SQL per le due tabelle.
            PreparedStatement pstFrase = con.prepareStatement(insertFraseQuery);
            PreparedStatement pstCollegamento = con.prepareStatement(insertCollegamentoQuery);

            int codFrase = 0;
            int start = 0;
            int end = 0;

            // Itero attraverso la stringa delle frasi e dei collegamenti.
            while (end < frase.length()) {
                end = frase.indexOf('\n', start); //prende l'index del primo \n che trova

                if (end == -1) { //se non trova l'index(end = -1) allora e' un unica frase lunga.
                    end = frase.length(); //mi prendo la lunghezza e la metto in end
                }

                String copia = frase.substring(start, end); //creo una substring che va dal penultimo \n che ho trovato fino all'ultimo \n
                int indiceDollaro = copia.indexOf('$');
                /* per come abbiamno strutturato l'inserimento dei link, il dollaro e' necessario per capire
                    quando termina il link, cio che sta dopo il dollaro e' la frase effettiva che verra mostrata
                 */

                if (indiceDollaro != -1) { //caso in cui c'e' un link
                    String paginaRif = copia.substring(0, indiceDollaro);
                    String myFrase = copia.substring(indiceDollaro + 1);

                    // Inserisco nella tabella "Frase".
                    pstFrase.setInt(1, codFrase);
                    pstFrase.setString(2, myFrase);
                    pstFrase.setString(3, link);
                    pstFrase.executeUpdate();

                    // Inserisco nella tabella "Collegamento".
                    pstCollegamento.setInt(1, codFrase);
                    pstCollegamento.setString(2, link);
                    pstCollegamento.setString(3, paginaRif);
                    pstCollegamento.executeUpdate();
                } else { //caso in cui non c'e' un link
                    // Inserisco nella tabella "Frase".
                    pstFrase.setInt(1, codFrase);
                    pstFrase.setString(2, copia);
                    pstFrase.setString(3, link);
                    pstFrase.executeUpdate();
                }

                codFrase++;
                start = end + 1; //mi posizono al carattere successivo all'ultimo \n trovato
            }

            con.commit(); // Confermo la transazione se tutto e' andato a buon fine

        } catch (SQLException e) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(e);
            try {
                con.rollback(); // Annulla la transazione in caso di errore
            } catch (SQLException rollbackException) {
                handleSQLException(rollbackException);
            }
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione delle operazioni.
            connessioneDatabase.setInstanceNull();
        }
    }

    /**
     * Ottengo l'autore associato a una specifica frase su una determinata pagina dal database.
     *
     * @param codfrase Il codice della frase di cui ottenere l'autore.
     * @param pagina Il link della pagina a cui la frase appartiene.
     * @return Una stringa rappresentante l'autore associato alla frase, o null in caso di errore.
     */
    public String getAutoreByFrase(int codfrase, String pagina) {
        try {
            // Ottengo una connessione al database.
            Connection con = connessioneDatabase.getConnection();

            // Costruisco la query SQL per ottenere l'autore associato a una specifica frase su una pagina.
            String query = "SELECT autore FROM Frase INNER JOIN Pagina ON Frase.pagina = Pagina.link WHERE codfrase = ? AND pagina = ?";
            PreparedStatement pst = con.prepareStatement(query);

            // Imposto i valori dei parametri nella query.
            pst.setInt(1, codfrase);
            pst.setString(2, pagina);

            // Eseguo la query SQL e ottengo il risultato.
            ResultSet res = pst.executeQuery();

            // Estraggo l'autore dalla prima riga del risultato (mi interessa solo autore)
            if (res.next()) {
                String autoreDB = res.getString("autore");
                return autoreDB;
            } else {
                // Restituisco null se non viene trovato alcun autore.
                return null;
            }

        } catch (SQLException ex) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(ex);
            // Restituisco null in caso di errore.
            return null;
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }

    /**
     * Ottengo un collegamento associato a una specifica frase e pagina dal database.
     *
     * @param codfrase Il codice della frase di cui ottenere il collegamento.
     * @param pagina Il link della pagina a cui la frase appartiene.
     * @return Un oggetto Collegamento rappresentante il collegamento associato alla frase, o null se non trovato o in caso di errore.
     */
    public Collegamento fraseconlink(int codfrase, String pagina){
        Connection con = connessioneDatabase.getConnection();
        String query = "SELECT * FROM Frase INNER JOIN Collegamento ON Frase.codfrase = Collegamento.codfrase AND Frase.pagina = Collegamento.pagina_frase WHERE Frase.codfrase = ? AND Frase.pagina = ?";

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, codfrase);
            statement.setString(2, pagina);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Verifica se ci sono risultati
                int codfrasedb = resultSet.getInt("codfrase");
                String paginafrasedb = resultSet.getString("pagina_frase");
                String paginariferimentodb = resultSet.getString("pagina_riferimento");
                Collegamento c = new Collegamento(codfrasedb, paginafrasedb, paginariferimentodb);
                return c;
            } else {
                return null; // Nessun risultato trovato
            }

        } catch (SQLException e) {
            // Gestisco eventuali eccezioni SQL stampando l'errore.
            handleSQLException(e);
            // Restituisco null in caso di errore.
            return null;
        } finally {
            // Chiudo la connessione al database dopo l'esecuzione della query.
            connessioneDatabase.setInstanceNull();
        }
    }



    private void handleSQLException(SQLException ex) {
        ex.printStackTrace();
    }
}