package com.example.wiki.domain;
import java.sql.Timestamp;

public class Autore extends Utente{
    String nomedarte;

    Timestamp iniziocarriera;

    public Autore(String mail, String nome, String cognome, String password, String nomedarte, Timestamp iniziocarriera) {
        super(mail, nome, cognome, password);
        this.nomedarte = nomedarte;
        this.iniziocarriera = iniziocarriera;
    }

    public String getNomedarte() {
        return nomedarte;
    }

    public void setNomedarte(String nomedarte) {
        this.nomedarte = nomedarte;
    }

    public Timestamp getIniziocarriera() {
        return iniziocarriera;
    }

    public void setIniziocarriera(Timestamp iniziocarriera) {
        this.iniziocarriera = iniziocarriera;
    }
}
