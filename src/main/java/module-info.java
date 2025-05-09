module org.example.regicidegrafico_juliomalpica {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.regicidegrafico_juliomalpica to javafx.fxml;
    exports org.example.regicidegrafico_juliomalpica;
}