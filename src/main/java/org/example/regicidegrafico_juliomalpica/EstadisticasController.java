package org.example.regicidegrafico_juliomalpica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controlador de la vista de estadísticas para la aplicación "REGICIDE".
 * Esta clase se encarga de cargar y mostrar en una tabla los datos de partidas anteriores
 * guardadas en un archivo CSV. También gestiona el botón para volver al menú principal.
 *
 * El archivo debe tener el siguiente formato, separado por "||" y con cabecera:
 * Fecha || CartasJugadas || VidaEnemigo || EstadoMano || Resultado
 */

public class EstadisticasController {
    @FXML
    private TableView<EstadisticaPartida> tablaEstadisticas;
    @FXML
    private TableColumn<EstadisticaPartida, String> colFecha;
    @FXML
    private TableColumn<EstadisticaPartida, Integer> colCartasJugadas;
    @FXML
    private TableColumn<EstadisticaPartida, Integer> colVidaEnemigo;
    @FXML
    private TableColumn<EstadisticaPartida, String> colEstadoMano;
    @FXML
    private TableColumn<EstadisticaPartida, String> colResultado;
    @FXML
    private Button btnVolver;

    private static final String ARCHIVO_ESTADISTICAS = "src/main/resources/estadisticas.csv";

    /**
     * Inicializa los componentes de la interfaz gráfica y carga los datos de estadísticas
     * desde el archivo CSV.
     */
    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCartasJugadas.setCellValueFactory(new PropertyValueFactory<>("cartasJugadas"));
        colVidaEnemigo.setCellValueFactory(new PropertyValueFactory<>("vidaEnemigo"));
        colEstadoMano.setCellValueFactory(new PropertyValueFactory<>("estadoMano"));
        colResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));

        //Cargar los datos
        tablaEstadisticas.setItems(cargarEstadisticas());

        //Boton de volver
        btnVolver.setOnAction(event -> volverMenuPrincipal());
    }

    /**
     * Cambia la escena actual para volver al menú principal.
     */
    private void volverMenuPrincipal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();

            if (stage != null) {
                stage.setScene(new Scene(fxmlLoader.load()));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el menú principal: " + e);
            e.printStackTrace();
        }
    }
    /**
     * Carga las estadísticas de partidas desde el archivo CSV especificado.
     * @return Una lista observable de objetos (EstadisticaPartida).
     */
    private ObservableList<EstadisticaPartida> cargarEstadisticas() {
        ObservableList<EstadisticaPartida> datos = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_ESTADISTICAS))) {
            String linea = br.readLine(); //Saltar la cabecera

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|\\|");
                if (partes.length >= 5) {
                    datos.add(new EstadisticaPartida(
                            partes[0].trim(),
                            Integer.parseInt(partes[1].trim()),
                            Integer.parseInt(partes[2].trim()),
                            partes[3].trim(),
                            partes[4].trim()
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
        }
        return datos;
    }

    /**
     * Clase interna que representa los datos de una partida jugada.
     */
    //Datos de una partida
    public static class EstadisticaPartida {
        private final String fecha;
        private final int cartasJugadas;
        private final int vidaEnemigo;
        private final String estadoMano;
        private final String resultado;

        /**
         * Constructor de la clase EstadisticaPartida.
         *
         * @param fecha Fecha en la que se jugó la partida.
         * @param cartasJugadas Número de cartas jugadas durante la partida.
         * @param vidaEnemigo Vida restante del enemigo al finalizar la partida.
         * @param estadoMano Estado de la mano del jugador al finalizar la partida.
         * @param resultado Resultado de la partida (Victoria o Derrota).
         */
        public EstadisticaPartida(String fecha, int cartasJugadas, int vidaEnemigo, String estadoMano, String resultado) {
            this.fecha = fecha;
            this.cartasJugadas = cartasJugadas;
            this.vidaEnemigo = vidaEnemigo;
            this.estadoMano = estadoMano;
            this.resultado = resultado;
        }

        /**
         * Obtiene la fecha en la que se realizó la partida.
         * @return la fecha de la partida como cadena de texto
         */
        public String getFecha() { return fecha; }
        /**
         * Obtiene el número de cartas jugadas durante la partida.
         * @return la cantidad de cartas jugadas
         */
        public int getCartasJugadas() { return cartasJugadas; }
        /**
         * Obtiene la cantidad de vida que le queda al enemigo al finalizar la partida.
         * @return los puntos de vida restantes del enemigo
         */
        public int getVidaEnemigo() { return vidaEnemigo; }
        /**
         * Obtiene el estado de la mano del jugador al final de la partida.
         * @return una cadena que representa el estado de la mano
         */
        public String getEstadoMano() { return estadoMano; }
        /**
         * Obtiene el resultado final de la partida (victorio o derrota).
         * @return el resultado de la partida como cadena de texto
         */
        public String getResultado() { return resultado; }
    }
}
