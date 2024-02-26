package com.example.wiki.util;

import com.example.wiki.DAO.UtenteCorrente;
/**
 * La classe SessionManager gestisce le informazioni sulla sessione dell'utente corrente.
 * Si occupa di memorizzare e recuperare l'utente corrente, verificare se un utente è loggato e gestire il logout.
 */
public class SessionManager {
    private static SessionManager instance;
    private UtenteCorrente utenteCorrente;

    /**
     * Costruttore privato della classe SessionManager.
     * Si occupa dell'inizializzazione del session manager.
     */
    private SessionManager() {
        // Inizializzazione del session manager
    }

    /**
     * Metodo per ottenere l'istanza della classe SessionManager.
     * Se l'istanza è null, crea una nuova istanza della classe.
     * @return L'istanza della classe SessionManager.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Metodo per ottenere l'utente corrente memorizzato nella sessione.
     * @return L'oggetto UtenteCorrente.
     */
    public UtenteCorrente getUtenteCorrente() {
        return utenteCorrente;
    }

    /**
     * Metodo per impostare l'utente corrente nella sessione.
     * @param utenteCorrente L'oggetto UtenteCorrente da memorizzare.
     */
    public void setUtenteCorrente(UtenteCorrente utenteCorrente) {
        this.utenteCorrente = utenteCorrente;
    }

    /**
     * Metodo per verificare se un utente è attualmente loggato.
     * @return true se un utente è loggato, false altrimenti.
     */
    public boolean isUtenteLoggato() {
        return utenteCorrente != null;
    }

    /**
     * Metodo per eseguire il logout, azzerando l'utente corrente.
     */
    public void logout() {
        utenteCorrente = null;
    }
}
