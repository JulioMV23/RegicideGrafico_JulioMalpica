module org.example.regicidegrafico_juliomalpica {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;


    opens org.example.regicidegrafico_juliomalpica to javafx.fxml,com.google.gson;
    exports org.example.regicidegrafico_juliomalpica;
}