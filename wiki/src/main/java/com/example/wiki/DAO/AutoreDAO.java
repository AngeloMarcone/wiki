package com.example.wiki.DAO;

import java.sql.Timestamp;

public interface AutoreDAO {

    void insertIntoAutore(String nomeDArteGUI, Timestamp timestampGUI, String mailGUI);

}