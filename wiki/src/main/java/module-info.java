module com.example.wiki {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.wiki to javafx.fxml;
    opens com.example.wiki.gui to javafx.fxml;
    exports com.example.wiki;
}
