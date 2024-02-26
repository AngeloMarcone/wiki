package com.example.wiki.domain;
import java.sql.Timestamp;

public class Modifica {

    private int codmod;
    private String fraseoriginale;
    private int codfrase;
    private String pagina;
    private String frasemodificata;
    private int stato;
    private Timestamp dataoramod;
    private String utente;
    private String autore;

    public Modifica(int codmod, String fraseoriginale, int codfrase, String pagina,
                    String frasemodificata, int stato, Timestamp dataoramod,
                    String utente, String autore) {
        this.codmod = codmod;
        this.fraseoriginale = fraseoriginale;
        this.codfrase = codfrase;
        this.pagina = pagina;
        this.frasemodificata = frasemodificata;
        this.stato = stato;
        this.dataoramod = dataoramod;
        this.utente = utente;
        this.autore = autore;
    }

    public int getCodmod() {
        return codmod;
    }

    public void setCodmod(int codmod) {
        this.codmod = codmod;
    }

    public String getFraseoriginale() {
        return fraseoriginale;
    }

    public void setFraseoriginale(String fraseoriginale) {
        this.fraseoriginale = fraseoriginale;
    }

    public int getCodfrase() {
        return codfrase;
    }

    public void setCodfrase(int codfrase) {
        this.codfrase = codfrase;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getFrasemodificata() {
        return frasemodificata;
    }

    public void setFrasemodificata(String frasemodificata) {
        this.frasemodificata = frasemodificata;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public Timestamp getDataoramod() {
        return dataoramod;
    }

    public void setDataoramod(Timestamp dataoramod) {
        this.dataoramod = dataoramod;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }
}

