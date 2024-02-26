package com.example.wiki.domain;

public class Utente {

    private String mail;
    private String nome;
    private String cognome;
    private String password;

    public Utente(String mail, String nome, String cognome, String password) {
        this.mail = mail;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
    }

    public String getMail() {
        return this.mail;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getPassword() {
        return this.password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "mail: " + this.mail + " nome: " + this.nome + " cognome: " + this.cognome + " password: " + this.password;
    }
}