package com.example.wiki.DAO;

import com.example.wiki.domain.Modifica;

import java.sql.Timestamp;
import java.util.List;

public interface ModificaDAO {

    List<Modifica> getMod(UtenteCorrente u);

    List<Modifica> getNotifiche(UtenteCorrente u);

    void handleModifica(int b, int codmodGUI);

    void insertModifica(String fraseoriginale, int codfrase, String pagina,
                        String frasemodificata, Timestamp dataoramod,
                        String utente, String autore);
}
