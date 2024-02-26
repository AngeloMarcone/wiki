package com.example.wiki.DAO;

import com.example.wiki.domain.Utente;

public interface UtenteDAO {
    Utente readFromDatabase(String mailGUI, String passwordGUI);
    void createUtente(Utente utente);
    // Altri metodi se necessario
}
