package com.example.wiki.DAO;

import com.example.wiki.domain.Pagina;
import java.util.List;

public interface PaginaDAO {
    List<Pagina> getPages();
    void createPage(String link, String titolo, String frase);
    List<Pagina> getPagesByAutore(UtenteCorrente u);
    Pagina getPageByLink(String link);
    List<Pagina> search(String toSearch);
}
