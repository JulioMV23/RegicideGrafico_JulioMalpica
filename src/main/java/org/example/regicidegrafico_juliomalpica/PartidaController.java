package org.example.regicidegrafico_juliomalpica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Controlador de la interfaz gráfica para la partida del juego Regicide.
 * Gestiona la interacción con la interfaz de usuario, actualización de la
 * vista, eventos de botones y la lógica de juego relacionada con la visualización.
 */
public class PartidaController {
    /** ImageViews para mostrar los mazos y cartas visibles en la interfaz */
    @FXML
    private ImageView imgvMazoPosada, imgvMazoCastillo, imgvMazoCarJugadas, imgvMazoCarDescartadas, imgvCastillo, imgvCartaJugada, imgvComodin;

    /** ImageViews para las cartas en la mano del jugador */
    @FXML
    private ImageView imgvCarta1, imgvCarta2, imgvCarta3, imgvCarta4, imgvCarta5, imgvCarta6, imgvCarta7, imgvCarta8;

    /** Botones para acciones del juego */
    @FXML
    private Button btnRendirse, btnSalir, btnReglasJuego;

    /** Etiquetas que muestran información del jugador y estado del juego */
    @FXML
    private Label lblNombreJugador, lblContVida, lblContAtaque, lblContDefensa,
            lblContCastillo, lblContPosada, lblContJugadas, lblContDescartes, lblComodines;

    /** Objeto Partida que contiene la lógica y datos de la partida actual */
    private Partida partida;
    /** Lista de ImageViews que representan las cartas en mano */
    private ArrayList<ImageView> cartasMano = new ArrayList<>();
    /** Imagen que representa el reverso de las cartas */
    private Image imagenReverso;
    /** Indica si el jugador está en modo defensa */
    private boolean enModoDefensa = false;
    /** Suma total de defensa acumulada durante el modo defensa */
    private int defensaTotal = 0;
    /** Lista de cartas usadas para defensa en el turno actual */
    private ArrayList<Carta> cartasDefensa = new ArrayList<>();

    private int comodinesDisponibles = 2;
    private ArrayList<Integer> cartasSeleccionadas = new ArrayList<>();

    /**
     * Inicializa el controlador, configurando imágenes, listas, y eventos de botones.
     * Este metodo se ejecuta automáticamente tras cargar el FXML.
     */
    @FXML
    public void initialize() {
        //Configurar imagen tapa
        URL url = getClass().getResource("/org/example/regicidegrafico_juliomalpica/imagenes/tapa.jpg");
        if (url == null) {
            throw new IllegalStateException("No se encontró tapa.jpg en el classpath");
        }
        imagenReverso = new Image(url.toExternalForm());


        //Lista de ImageViews para las cartas en mano
        cartasMano.add(imgvCarta1);
        cartasMano.add(imgvCarta2);
        cartasMano.add(imgvCarta3);
        cartasMano.add(imgvCarta4);
        cartasMano.add(imgvCarta5);
        cartasMano.add(imgvCarta6);
        cartasMano.add(imgvCarta7);
        cartasMano.add(imgvCarta8);

        //Configurar acciones de los botones
        btnRendirse.setOnAction(event -> rendirse());
        btnSalir.setOnAction(event -> salir());
        btnReglasJuego.setOnAction(event -> mostrarReglas());

        //Imagenes de los mazos (imagen reverso carta)
        imgvMazoPosada.setImage(imagenReverso);
        imgvMazoCastillo.setImage(imagenReverso);
        imgvMazoCarJugadas.setImage(imagenReverso);
        imgvMazoCarDescartadas.setImage(imagenReverso);

        //Iniciar con este texto
        lblComodines.setText("Comodines : " + comodinesDisponibles);
    }

    /**
     * Establece la partida actual que se va a mostrar y manipular.
     * Actualiza la interfaz con la información de la partida.
     *
     * @param partida la instancia de Partida que se va a controlar
     */
    public void setPartida(Partida partida) {
        this.partida = partida;
        lblNombreJugador.setText(partida.getNombreJugador());
        actualizarInterfaz();
    }

    /**
     * Carga la última partida guardada desde almacenamiento persistente.
     * Actualiza la interfaz si la partida existe, o muestra un error en caso contrario.
     */
    public void cargarUltimaPartida() {
        this.partida = Partida.cargarPartida();
        if (this.partida != null) {
            lblNombreJugador.setText(partida.getNombreJugador());
            actualizarInterfaz();
        } else {
            new Alert(Alert.AlertType.ERROR, "No se encontró ninguna partida guardada.").showAndWait();
        }
    }

    /**
     * Actualiza todos los componentes visuales de la interfaz con los datos actuales de la partida.
     * Incluye estadísticas, imágenes de cartas y estado de los mazos.
     */
    private void actualizarInterfaz() {
        //Establecer nombre de jugador en el label
        lblNombreJugador.setText(partida.getNombreJugador());

        //Actualizar estadísticas del enemigo
        lblContVida.setText(String.valueOf(partida.getVidaEnemigo()));
        lblContAtaque.setText(String.valueOf(partida.getDanioEnemigo()));

        //Defensa total
        lblContDefensa.setText(String.valueOf(defensaTotal));

        //Actualizar contadores de mazos
        lblContCastillo.setText(String.valueOf(partida.getCastillo().size()));
        lblContPosada.setText(String.valueOf(partida.getMazoPosada().size()));
        lblContJugadas.setText(String.valueOf(partida.getMazoCartasJugadas().size()));
        lblContDescartes.setText(String.valueOf(partida.getMazoCartasDescartadas().size()));

        //Mostrar imagen o dejar vacíos los mazos si están en 0
        imgvMazoCastillo.setImage(partida.getCastillo().isEmpty() ? null : imagenReverso);
        imgvMazoPosada.setImage(partida.getMazoPosada().isEmpty() ? null : imagenReverso);
        imgvMazoCarJugadas.setImage(partida.getMazoCartasJugadas().isEmpty() ? null : imagenReverso);
        imgvMazoCarDescartadas.setImage(partida.getMazoCartasDescartadas().isEmpty() ? null : imagenReverso);

        actualizarCartasMano();

        //Actualizar carta jugada (si hay alguna)
        if (!partida.getMazoCartasJugadas().isEmpty()) {
            Carta ultimaCarta = partida.getMazoCartasJugadas().get(partida.getMazoCartasJugadas().size() - 1);
            imgvCartaJugada.setImage(cargarImagenCarta(ultimaCarta));
        } else {
            imgvCartaJugada.setImage(null);
        }

        //Actualizar imagen del castillo (enemigo actual)
        if (!partida.getCastillo().isEmpty()) {
            imgvCastillo.setImage(cargarImagenCarta(partida.getCastillo().get(0)));
        } else {
            imgvCastillo.setImage(null);
        }
    }

    /**
     * Actualiza las cartas visibles en la mano del jugador.
     * Habilita o deshabilita las cartas según la cantidad disponible(si hay imagen se podra hacer click,
     * en caso contrario se deshabilita).
     */
    private void actualizarCartasMano() {
        ArrayList<Carta> mano = partida.getMano();
        for (int i = 0; i < cartasMano.size(); i++) {
            StackPane contenedor = getStackPaneCarta(cartasMano.get(i));
            if (i < mano.size()) {
                cartasMano.get(i).setImage(cargarImagenCarta(mano.get(i)));
                contenedor.setDisable(false);
            } else {
                cartasMano.get(i).setImage(null);
                contenedor.setDisable(true);
            }
        }
    }

    /**
     * Carga la imagen asociada a una carta específica.
     * Si no encuentra la imagen, devuelve la imagen del reverso.
     *
     * @param carta la carta de la que se quiere cargar la imagen
     * @return la imagen de la carta o el reverso en caso de error
     */
    private Image cargarImagenCarta(Carta carta) {
        if (carta == null) return null;

        try {
            String nombreArchivo = carta.getNumero() + "_" + carta.getPalo() + ".jpg";
            String rutaCompleta = "/org/example/regicidegrafico_juliomalpica/cartas/" + nombreArchivo;

            URL url = getClass().getResource(rutaCompleta);
            if (url != null) {
                return new Image(url.toExternalForm());
            } else {
                System.err.println("No se encontró la imagen: " + rutaCompleta);
                return imagenReverso;
            }

        } catch (Exception e) {
            System.err.println("Error al cargar imagen de carta: " + e.getMessage());
            return imagenReverso;
        }
    }

    /**
     * Obtiene el contenedor StackPane padre de un ImageView dado.
     * Utilizado para habilitar/deshabilitar la interacción con la carta.
     * @param imageView la ImageView de la carta
     * @return el StackPane contenedor de la carta
     */
    private StackPane getStackPaneCarta(ImageView imageView) {
        return (StackPane) imageView.getParent();
    }

    /**
     * Gestiona la lógica para jugar una carta en el índice dado, considerando los modos ataque y defensa.
     * Actualiza la interfaz y controla la finalización de la partida.
     *
     * @param indiceCarta índice de la carta en la mano que se quiere jugar
     */
    @FXML
    private void jugarCarta(int indiceCarta) {
        if (indiceCarta < 0 || indiceCarta >= partida.getMano().size()) return;

        Carta cartaSeleccionada = partida.getMano().get(indiceCarta);

        if (!enModoDefensa) {
            //Modo ataque
            ArrayList<Integer> indices = new ArrayList<>();
            indices.add(indiceCarta);
            partida.jugarCarta(indices);
            actualizarInterfaz();

            //Si no quedan cartas en la mano
            if (partida.getMano().isEmpty()) {
                if (comodinesDisponibles > 0) {
                    usarComodin();
                } else {
                    partida.setPartidaTerminada(true);
                    mostrarResultadoPartida();
                    return;
                }
            }

            if (partida.isPartidaTerminada()) {
                mostrarResultadoPartida();
            } else if (partida.getVidaEnemigo() > 0) {
                //El enemigo sigue vivo, mostrar mensaje y entrar en modo defensa
                mostrarDialogoDefensa();
                activarModoDefensa();
            }

        } else {
            //Modo defensa
            defensaTotal += cartaSeleccionada.getNumero();
            cartasDefensa.add(cartaSeleccionada);

            //Quitar carta de la mano y agregarla al mazo de jugadas
            partida.getMano().remove(indiceCarta);
            partida.getMazoCartasJugadas().add(cartaSeleccionada);

            //Actualizar defensa en pantalla
            lblContDefensa.setText(String.valueOf(defensaTotal));
            actualizarInterfaz();

            if (defensaTotal >= partida.getDanioEnemigo()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Defensa exitosa!");
                alert.showAndWait();
                desactivarModoDefensa();

                if (partida.getMano().isEmpty()) {
                    if (comodinesDisponibles > 0) {
                        usarComodin();
                    } else {
                        Alert sinCartas = new Alert(Alert.AlertType.INFORMATION, "Te has defendido con éxito, pero ya no te quedan cartas para continuar. Has perdido.");
                        sinCartas.showAndWait();
                        partida.setPartidaTerminada(true);
                        partida.registrarEstadisticas();
                        mostrarResultadoPartida();
                    }
                }

            } else if (partida.getMano().isEmpty()) {
                if (comodinesDisponibles > 0) {
                    usarComodin();
                } else {
                    //Si no hay cartas para defender
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No tienes cartas suficientes para defenderte. Has perdido.");
                    alert.showAndWait();
                    partida.setPartidaTerminada(true);
                    partida.registrarEstadisticas();
                    mostrarResultadoPartida();
                }
            }
        }
    }

    /**
     * Muestra un diálogo informativo alertando al jugador que debe defenderse.
     */
    private void mostrarDialogoDefensa() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Defensa");
        alert.setHeaderText("¡El enemigo contraataca!");
        alert.setContentText("Necesitas defenderte de " + partida.getDanioEnemigo() + " de daño.");
        alert.showAndWait();
    }

    /**
     * Activa el modo defensa, inicializando variables de control de defensa.
     */
    private void activarModoDefensa() {
        enModoDefensa = true;
        defensaTotal = 0;
        cartasDefensa.clear();
        lblContDefensa.setText("0");
    }

    /**
     * Desactiva el modo defensa y resetea variables relacionadas.
     */
    private void desactivarModoDefensa() {
        enModoDefensa = false;
        defensaTotal = 0;
        cartasDefensa.clear();
        lblContDefensa.setText("0");
    }

    /**
     * Muestra un diálogo con el resultado final de la partida (victoria o derrota).
     * Luego, carga la pantalla principal del juego.
     */
    private void mostrarResultadoPartida() {
        String mensaje = partida.isVictoria() ? "¡Felicidades! Has ganado la partida." : "¡Has perdido la partida!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partida");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) btnSalir.getScene().getWindow();

            if (stage != null) {
                stage.setScene(new Scene(fxmlLoader.load()));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el menú principal: " + e);
            e.printStackTrace();
        }
    }

    private void usarComodin(){
        if (comodinesDisponibles <= 0) return;

        //Mover cartas mano a descartes
        ArrayList<Carta> manoActual = partida.getMano();
        partida.getMazoCartasDescartadas().addAll(manoActual);
        manoActual.clear();

        //Mover carta jugada a descartes
        ArrayList<Carta> mazoJugadas = partida.getMazoCartasJugadas();
        if (!mazoJugadas.isEmpty()) {
            partida.getMazoCartasDescartadas().addAll(mazoJugadas);
            mazoJugadas.clear();
        }

        //Robar cartas del mazo posada
        ArrayList<Carta> mazoPosada = partida.getMazoPosada();
        for (int i = 0; i < 8 && !mazoPosada.isEmpty(); i++) {
            manoActual.add(mazoPosada.remove(0));
        }

        //Reducir contador
        comodinesDisponibles--;
        lblComodines.setText("Comodines : " + comodinesDisponibles);

        //Desactivar imagen si no quedan comodines
        if (comodinesDisponibles <= 0) {
            imgvComodin.setDisable(true);
            imgvComodin.setOpacity(0.5);
        }

        //Reiniciar contador de defensa
        defensaTotal = 0;
        cartasDefensa.clear();
        lblContDefensa.setText("0");

        actualizarInterfaz();
    }

    @FXML
    private void onComodinClicked() {
        usarComodin();
    }

    /**
     * Acción para rendirse en la partida.
     * Marca la partida como terminada, registra estadísticas y vuelve al menú principal.
     */
    @FXML
    private void rendirse() {
        partida.setPartidaTerminada(true);
        partida.registrarEstadisticas();
        mostrarResultadoPartida();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) btnRendirse.getScene().getWindow();

            if (stage != null) {
                stage.setScene(new Scene(fxmlLoader.load()));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el menú principal: " + e);
            e.printStackTrace();
        }

    }

    /**
     * Guarda la partida actual y sale al menú principal.
     */
    @FXML
    private void salir() {
        partida.guardarPartida();
        partida.setPartidaTerminada(true);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) btnSalir.getScene().getWindow();

            if (stage != null) {
                stage.setScene(new Scene(fxmlLoader.load()));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el menú principal: " + e);
            e.printStackTrace();
        }

    }

    /**
     * Abre el archivo PDF con las reglas del juego en el programa predeterminado del sistema.
     * Muestra un error si no se puede abrir el archivo.
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

    private void manejarSeleccionCarta(int indiceCarta) {
        if (enModoDefensa) {
            //En modo defensa, jugar la carta directamente
            jugarCarta(indiceCarta);
            return;
        }

        //Alternar seleccion de la carta
        if (cartasSeleccionadas.contains(indiceCarta)) {
            cartasSeleccionadas.remove(Integer.valueOf(indiceCarta));
            getStackPaneCarta(cartasMano.get(indiceCarta)).setStyle("");
        } else {
            cartasSeleccionadas.add(indiceCarta);
            getStackPaneCarta(cartasMano.get(indiceCarta)).setStyle("-fx-border-color: yellow; -fx-border-width: 3;");
        }

        //Si hay cartas seleccionadas, mostrar opcion para jugarlas
        if (!cartasSeleccionadas.isEmpty()) {
            ButtonType jugar = new ButtonType("Jugar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"¿Jugar estas " + cartasSeleccionadas.size() + " cartas?", jugar, cancelar);

            alert.showAndWait().ifPresent(response -> {
                if (response == jugar) {
                    //Comprobar si la combinación es válida
                    ArrayList<Carta> cartasParaJugar = new ArrayList<>();
                    for (int idx : cartasSeleccionadas) {
                        cartasParaJugar.add(partida.getMano().get(idx));
                    }

                    if (partida.jugadaValida(cartasParaJugar)) {
                        //Ordenar cartas seleccionadas de mayor a menor para evitar problemas al eliminar
                        cartasSeleccionadas.sort(Collections.reverseOrder());

                        ArrayList<Carta> cartasAJugar = new ArrayList<>();
                        for (int idx : cartasSeleccionadas) {
                            cartasAJugar.add(partida.getMano().get(idx));
                        }
                        partida.jugarCarta(cartasSeleccionadas);
                        for (int idx : cartasSeleccionadas) {
                            getStackPaneCarta(cartasMano.get(idx)).setStyle("-fx-border-color: black;");
                        }

                        cartasSeleccionadas.clear();
                        actualizarInterfaz();

                        //Comprobar si la partida ha terminado
                        if (partida.isPartidaTerminada()) {
                            mostrarResultadoPartida();
                        } else if (partida.getVidaEnemigo() > 0) {
                            mostrarDialogoDefensa();
                            activarModoDefensa();
                        }

                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR, "¡Combinación no válida!");
                        error.showAndWait();
                    }

                    for (int idx : cartasSeleccionadas) {
                        getStackPaneCarta(cartasMano.get(idx)).setStyle("-fx-border-color: black;");
                    }

                    cartasSeleccionadas.clear();
                    actualizarInterfaz();
                }
            });
        }
    }

    //Clic cartas
    /** Invoca jugarCarta con índice 0 al hacer click en la primera carta */
    @FXML
    private void onCarta1Clicked() { manejarSeleccionCarta(0); }

    /** Invoca jugarCarta con índice 1 al hacer click en la segunda carta */
    @FXML
    private void onCarta2Clicked() { manejarSeleccionCarta(1); }

    /** Invoca jugarCarta con índice 2 al hacer click en la tercera carta */
    @FXML
    private void onCarta3Clicked() { manejarSeleccionCarta(2); }

    /** Invoca jugarCarta con índice 3 al hacer click en la cuarta carta */
    @FXML
    private void onCarta4Clicked() { manejarSeleccionCarta(3); }

    /** Invoca jugarCarta con índice 4 al hacer click en la quinta carta */
    @FXML
    private void onCarta5Clicked() { manejarSeleccionCarta(4); }

    /** Invoca jugarCarta con índice 5 al hacer click en la sexta carta */
    @FXML
    private void onCarta6Clicked() { manejarSeleccionCarta(5); }

    /** Invoca jugarCarta con índice 6 al hacer click en la séptima carta */
    @FXML
    private void onCarta7Clicked() { manejarSeleccionCarta(6); }

    /** Invoca jugarCarta con índice 7 al hacer click en la octava carta */
    @FXML
    private void onCarta8Clicked() { manejarSeleccionCarta(7); }
}