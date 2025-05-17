package org.example.regicidegrafico_juliomalpica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación JavaFX para el juego "REGICIDE".
 *
 * Esta clase carga la interfaz gráfica definida en un archivo FXML
 * y configura la ventana principal de la aplicación (MENÚ).
 */

public class HelloApplication extends Application {
    /**
     * Metodo de entrada de la aplicación JavaFX.
     * Carga la interfaz desde el archivo FXML (hello-view.fxml),
     * crea la escena, configura la ventana principal y la muestra.
     *
     * @param stage El escenario principal proporcionado por JavaFX.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        stage.setTitle("REGICIDE");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Metodo principal que lanza la aplicación JavaFX.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        launch();
    }
}