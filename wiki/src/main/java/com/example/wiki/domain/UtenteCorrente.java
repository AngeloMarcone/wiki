package com.example.wiki.domain;

import com.example.wiki.domain.Autore;
import java.sql.Timestamp;

public class UtenteCorrente extends Autore {
    public UtenteCorrente(String mail, String nome, String cognome, String password, String nomedarte, Timestamp datainiziocarriera) {
        super(mail, nome, cognome, password,nomedarte, datainiziocarriera);
    }
}
