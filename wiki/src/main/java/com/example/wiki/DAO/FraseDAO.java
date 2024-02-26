package com.example.wiki.DAO;

import com.example.wiki.domain.Collegamento;
import com.example.wiki.domain.Frase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface FraseDAO {
    List<Frase> getFrasi(String linkGui);

    void insertFrasi(Connection con, String link, String frase);

    String getAutoreByFrase(int codfrase, String pagina);

    Collegamento fraseconlink(int codfrase, String pagina) throws SQLException;

}
