package com.example.wiki.domain;

public class Frase {
    private int codfrase;
    private String caratteri;
    private String pagina;

    // Costruttore
    public Frase(int codfrase, String caratteri, String pagina) {
        this.codfrase = codfrase;
        this.caratteri = caratteri;
        this.pagina = pagina;
    }

    public int getCodfrase() {
        return codfrase;
    }

    public void setCodfrase(int codfrase) {
        this.codfrase = codfrase;
    }

    public String getCaratteri() {
        return caratteri;
    }

    public void setCaratteri(String caratteri) {
        this.caratteri = caratteri;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }
}