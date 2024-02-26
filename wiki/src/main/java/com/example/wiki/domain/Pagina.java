package com.example.wiki.domain;

import java.sql.Timestamp;

public class Pagina {

    private String link;
    private String titolo;
    private Timestamp dataoracreazione;
    private String autore;

    public Pagina(String link, String titolo, Timestamp dataoracreazione, String autore){
        this.link = link;
        this.titolo = titolo;
        this.dataoracreazione = dataoracreazione;
        this.autore = autore;
    }

    public String getLink(){
        return this.link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getTitolo(){
        return this.titolo;
    }

    public void setTitolo(String titolo){
        this.titolo = titolo;
    }

    public Timestamp getDataoracreazione(){
        return this.dataoracreazione;
    }

    public void setDataoracreazione(Timestamp dataoracreazione){
        this.dataoracreazione = dataoracreazione;
    }

    public String getAutore(){
        return this.autore;
    }

    public void setAutore(String autore){
        this.autore = autore;
    }
}

