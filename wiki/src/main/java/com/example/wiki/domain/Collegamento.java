package com.example.wiki.domain;

public class Collegamento {
    private int codFrase;
    private String paginaFrase;
    private String paginaRiferimento;

    // Costruttore
    public Collegamento(int codFrase, String paginaFrase, String paginaRiferimento) {
        this.codFrase = codFrase;
        this.paginaFrase = paginaFrase;
        this.paginaRiferimento = paginaRiferimento;
    }

    // Getter e setter per codFrase
    public int getCodFrase() {
        return codFrase;
    }

    public void setCodFrase(int codFrase) {
        this.codFrase = codFrase;
    }

    // Getter e setter per paginaFrase
    public String getPaginaFrase() {
        return paginaFrase;
    }

    public void setPaginaFrase(String paginaFrase) {
        this.paginaFrase = paginaFrase;
    }

    // Getter e setter per paginaRiferimento
    public String getPaginaRiferimento() {
        return paginaRiferimento;
    }

    public void setPaginaRiferimento(String paginaRiferimento) {
        this.paginaRiferimento = paginaRiferimento;
    }
}
