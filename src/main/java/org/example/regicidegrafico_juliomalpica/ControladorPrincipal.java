package org.example.regicidegrafico_juliomalpica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Controlador principal del menú de inicio para la aplicación "REGICIDE".
 *
 * Esta clase gestiona la interacción con los botones de la interfaz principal:
 * iniciar una nueva partida, cargar una existente, ver estadísticas,
 * salir del juego, abrir el tutorial en línea o mostrar las reglas del juego en PDF.
 * Utiliza archivos FXML para cargar las distintas vistas.
 */
public class ControladorPrincipal {
    @FXML
    private Button btnNuevaPartida;
    @FXML
    private Button btnCargarPartida;
    @FXML
    private Button btnVerEstadisticas;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnTutorial;
    @FXML
    private Button btnReglas;

    /**
     * Inicializa los eventos de los botones una vez que la vista ha sido cargada.
     * Se asignan los controladores de eventos correspondientes a cada botón.
     */
    @FXML
    public void initialize(){
        btnNuevaPartida.setOnAction(event -> nuevaPartida());
        btnCargarPartida.setOnAction(event -> cargarPartida());
        btnVerEstadisticas.setOnAction(event -> mostrarEstadisticas());
        btnSalir.setOnAction(event -> salirJuego());
        btnTutorial.setOnAction(event -> mostrarAyuda());
        btnReglas.setOnAction(event -> mostrarReglas());
    }

    /**
     * Inicia una nueva partida solicitando al usuario que ingrese su nombre.
     * Si el nombre es válido, se crea una nueva instancia de Partida,
     * se cargan las colecciones necesarias y se cambia a la vista del juego.
     */
    @FXML
    protected void nuevaPartida() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nueva Partida");
        dialogo.setHeaderText("Introduce el nombre del jugador");
        dialogo.setContentText("Nombre:");
        //Obtener el nombre
        dialogo.showAndWait().ifPresent(nombreJugador -> {
            if (nombreJugador.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nombre inválido");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, introduce un nombre válido.");
                alert.showAndWait();
                return;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partida.fxml"));
                Parent root = fxmlLoader.load();

                Partida partida = new Partida(nombreJugador);
                partida.generarBaraja();
                partida.prepararColecciones();

                //pasar al controlador de la vista partida
                PartidaController controller = fxmlLoader.getController();
                controller.setPartida(partida);

                Stage stage = (Stage) btnNuevaPartida.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "No se pudo iniciar la partida").showAndWait();
            }
        });
    }

    /**
     * Carga una partida previamente guardada desde el sistema de archivos.
     * Si no se encuentra ninguna partida, se notifica al usuario y se crea una nueva partida.
     */
    @FXML
    protected void cargarPartida(){
        try {
            Partida partida = Partida.cargarPartida();
            if (partida == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No hay partida guardada");
                alert.setHeaderText(null);
                alert.setContentText("No se encontró ninguna partida guardada. Se creará una nueva partida.");
                alert.showAndWait();
                //Si no hay guardada,crear una nueva
                nuevaPartida();
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partida.fxml"));
            Parent root = fxmlLoader.load();

            //Obtener el controlador y pasarle la partida
            PartidaController controller = fxmlLoader.getController();
            controller.cargarUltimaPartida();

            Stage stage = (Stage) btnCargarPartida.getScene().getWindow();
            if (stage != null) {
                stage.setScene(new Scene(root));
            }

        } catch (Exception e) {
            System.out.println("Excepción al cargar partida: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Cambia la escena actual para mostrar la pantalla de estadísticas.
     */
    @FXML
    protected void mostrarEstadisticas(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("estadisticas.fxml"));
            Stage stage = (Stage) btnVerEstadisticas.getScene().getWindow();

            if (stage != null) {
                stage.setScene(new Scene(fxmlLoader.load()));
            }

        } catch (IOException e) {
            System.out.println("Excepción al mostrar estadísticas: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Cierra la ventana principal de la aplicación.
     */
    @FXML
    protected void salirJuego(){
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Abre el navegador predeterminado con un enlace a un video de ayuda.
     */
    @FXML
    protected void mostrarAyuda(){
        try {
            Desktop.getDesktop().browse(new java.net.URI("http://youtube.com/watch?v=eSIbxYEYzLw"));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el enlace del tutorial").showAndWait();
        }
    }

    /**
     * Abre el archivo PDF con las reglas del juego usando la aplicación predeterminada del sistema.
     */
    @FXML
    protected void mostrarReglas(){
        try {
            File archivoReglas = new File("src/main/resources/Reglas_Regicide.pdf");
            Desktop.getDesktop().open(archivoReglas);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el archivo de reglas").showAndWait();
        }
    }
}