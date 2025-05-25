package org.example.regicidegrafico_juliomalpica;

import com.google.gson.Gson;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Clase Partida que permite gestionar la lógica del juego.
 * <ul>
 *     <li>Baraja de la partida</li>
 *     <li>Mano del jugador</li>
 *     <li>Enemigos castillo</li>
 *     <li>Mazo cartas jugadas y descartadas</li>
 * </ul>
 */
public class Partida {
    private ArrayList<Carta> baraja = new ArrayList<>();
    private ArrayList<Carta> mano = new ArrayList<>();
    private ArrayList<Carta> castillo = new ArrayList<>();
    private ArrayList<Carta> mazoPosada = new ArrayList<>();
    private ArrayList<Carta> mazoCartasJugadas = new ArrayList<>();
    private ArrayList<Carta> mazoCartasDescartadas = new ArrayList<>();
    private int vidaEnemigo;
    private int danioEnemigo;
    private int reduccionDanioEnemigo;
    private boolean partidaTerminada = false;
    protected boolean victoria = false;
    private int cartasJugadas = 0;
    private static final String ARCHIVO_PARTIDA = "src/main/resources/partida.json";
    private static final String ARCHIVO_ESTADISTICAS = "src/main/resources/estadisticas.csv";
    private static Gson gson = new Gson();
    private String nombreJugador;

    /**
     * Obtiene la mano del jugador.
     * @return Lista de cartas en la mano del jugador.
     */
    public ArrayList<Carta> getMano() {
        return mano;
    }

    /**
     * Obtiene la vida actual del enemigo.
     * @return Vida del enemigo.
     */
    public int getVidaEnemigo() {
        return vidaEnemigo;
    }

    public ArrayList<Carta> getBaraja() {
        return baraja;
    }

    public ArrayList<Carta> getCastillo() {
        return castillo;
    }

    public ArrayList<Carta> getMazoPosada() {
        return mazoPosada;
    }

    public ArrayList<Carta> getMazoCartasJugadas() {
        return mazoCartasJugadas;
    }

    public ArrayList<Carta> getMazoCartasDescartadas() {
        return mazoCartasDescartadas;
    }

    public boolean isVictoria() {
        return victoria;
    }

    /**
     * Obtiene el daño del enemigo después de aplicar la reducción de daño.
     * @return Daño del enemigo, no menor que 0.
     */
    public int getDanioEnemigo() {
        return Math.max(0, danioEnemigo - reduccionDanioEnemigo);
    }

    /**
     * Verifica si la partida ha terminado.
     * @return Verdadero si la partida ha terminado, falso si no.
     */
    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    /**
     * Establece si la partida ha terminado.
     * @param partidaTerminada Indica si la partida ha terminado.
     */
    public void setPartidaTerminada(boolean partidaTerminada) {
        this.partidaTerminada = partidaTerminada;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Partida(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Partida(){
    }

    //Saber si juego un AS
    public boolean esCompaneroAnimal(Carta carta) {
        return carta.getNumero() == 1;
    }


    /**
     * Generar una baraja completa con 52 cartas - 13 cartas de cada palo.
     */
    public void generarBaraja(){
        String[] palos = {"Picas", "Corazones", "Treboles", "Diamantes"};
        for (String palo : palos){
            for (int i=1; i<=13; i++){
                baraja.add(new CartaNormal(i,palo));
            }
        }
    }

    /**
     * Configura las estadísticas de un enemigo basado en su carta
     * @param enemigo Carta que representa al enemigo (J, Q o K)
     */
    private void configurarEnemigo(Carta enemigo) {
        switch(enemigo.getNumero()) {
            case 11: //Jota
                vidaEnemigo = 20;
                danioEnemigo = 10;
                break;
            case 12: //Reina
                vidaEnemigo = 30;
                danioEnemigo = 15;
                break;
            case 13: //Rey
                vidaEnemigo = 40;
                danioEnemigo = 20;
                break;
        }
        //Resetear reduccion danio cada vez que se genera uno nuevo
        reduccionDanioEnemigo = 0;
    }

    /**
     * Metodo para preparar las colecciones antes de iniciar la partida.
     * <ul>
     *     <li>Permite separar las cartas para el castillo (11-13) y para el mazoPosada (1-10)</li>
     *     <li>Ordena las cartas del castillo (J (Jotas) - Q (Reinas) - K (Reyes))</li>
     *     <li>Robar 8 cartas del mazoPosada para tener en mano</li>
     *     <li>Establecer la vida del enemigo según la carta (J = 20; Q = 30; K = 40),
     *     ademas del ataque por defecto correspondiente a cada enemigo (J = 10; Q = 15; K = 20)</li>
     * </ul>
     */
    public void prepararColecciones(){
        //Mezclar cartas baraja
        Collections.shuffle(baraja);

        //Separar figuras para el castillo
        for(Carta carta : baraja){
            if(carta.getNumero() >= 11){
                castillo.add(carta);
            } else {
                mazoPosada.add(carta);
            }
        }

        //Ordenar las cartas para el castillo
        castillo.sort((c1, c2) -> Integer.compare(c1.getNumero(), c2.getNumero()));

        //Robar 8 cartas para la mano de inicio
        for (int i = 0; i < 8; i++){
            mano.add(mazoPosada.remove(0)); //Agregar primera carta(0) a la mano y quitarla del mazoPosada
        }

        //Configurar primer enemigo
        if(!castillo.isEmpty()){
            configurarEnemigo(castillo.get(0));
        }
    }

    /**
     * Muestra el estado actual de todas las colecciones de cartas
     */
    private void mostrarEstadoColecciones() {
        System.out.println("\nEstado colecciones: ");
        System.out.println("\t- Castillo: " + castillo.size() + " enemigos");
        System.out.println("\t- Mazo de Posada: " + mazoPosada.size() + " cartas");
        System.out.println("\t- Mazo de Cartas Jugadas: " + mazoCartasJugadas.size() + " cartas");
        System.out.println("\t- Mazo de Descartes: " + mazoCartasDescartadas.size() + " cartas");
    }

    /**
     * Mostrar el estado de la partida antes de iniciar el juego, mostrando todas las colecciones y
     * la vida del enemigo.
     */
    public void verEstadoInicial(){
        System.out.println("****************************************************************");
        System.out.println("*                     BIENVENIDOS A REGICIDE                   *");
        System.out.println("*--------------------------------------------------------------*");
        System.out.println("*                          MODO SOLITARIO                      *");
        System.out.println("****************************************************************");
        System.out.println("\nTu mano actual: ");
        //mostrar las 8 cartas de la mano
        mostrarMano();

        System.out.println("\nEnemigo actual en el castillo: ");
        //mostrar enemigo actual en el castillo
        if (!castillo.isEmpty()) {
            System.out.println(castillo.get(0) + " (Vida: " + vidaEnemigo + ")");
        }

        mostrarEstadoColecciones();
        System.out.println("\n****************************************************************");
        System.out.println("\n¡¡COMIENZA LA PELEA!!");
    }

    /**
     * Muestra las cartas en la mano del jugador.
     */
    public void mostrarMano() {
        for (int i = 0; i < mano.size(); i++) {
            System.out.println("\t[" + (i + 1) + "] " + mano.get(i));
        }
    }

    /**
     * Guarda el estado actual de la partida en un archivo JSON.
     */
    public void guardarPartida()  {
        try (FileWriter writer = new FileWriter(ARCHIVO_PARTIDA)) {
            gson.toJson(this, writer);
            System.out.println("Partida guardada en " + ARCHIVO_PARTIDA);
        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    /**
     * Carga una partida previamente guardada desde un archivo JSON.
     * @return Una instancia de la partida cargada, o null si no se pudo cargar.
     */
    public static Partida cargarPartida() {
        File file = new File(ARCHIVO_PARTIDA);
        if (!file.exists() || file.length() == 0){
            System.out.println("No hay partida guardada");
            return null;
        }
        try (FileReader reader = new FileReader(ARCHIVO_PARTIDA)){
            return gson.fromJson(reader, Partida.class);
        } catch (IOException e){
            System.err.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registra las estadísticas de la partida (fecha, cartas jugadas, vida del enemigo, estado de la mano y resultado)
     * en un archivo CSV.
     * Cada campo está separado por un delimitador (||)
     */
    public void registrarEstadisticas() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String numeroCartasJugadas = String.valueOf(cartasJugadas);
        String vidaEnemigoRest = String.valueOf(vidaEnemigo);
        String estadoMano = gson.toJson(mano);
        String resultado = victoria ? "Victoria" : "Derrota";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_ESTADISTICAS, true))) {
            File archivo = new File(ARCHIVO_ESTADISTICAS);
            if (archivo.length() == 0) {
                //Si el archivo está vacío, escribir la cabecera
                writer.write("Fecha||Cartas Jugadas||Vida Enemigo||Estado Mano||Resultado\n");
            }
            writer.write(String.join("||",fecha,numeroCartasJugadas,vidaEnemigoRest,estadoMano,resultado)+ "\n");
        }  catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Muestra las estadísticas de las partidas guardadas en el archivo CSV.
     */
    public static void mostrarEstadisticas() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_ESTADISTICAS));
            System.out.println("\n------------- ESTADISTICAS DE PARTIDAS -------------");
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] partes = line.split("\\|\\|");

                String fecha = partes[0];
                int cartasJugadas = Integer.parseInt(partes[1].trim());
                int vidaEnemigoRest = Integer.parseInt(partes[2].trim());
                String estadoMano = partes[3];
                String resultado = partes[4];

                System.out.println("Fecha             : " + fecha);
                System.out.println("Cartas Jugadas    : " + cartasJugadas);
                System.out.println("Vida Enemigo      : " + vidaEnemigoRest);
                System.out.println("Estado Mano       : " + estadoMano);
                System.out.println("Resultado         : " + resultado);
                System.out.println("---------------------------------------------------");
            }
            br.close();
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean jugadaValida(List<Carta> cartasSeleccionadas) {
        List<String> palosVistos = new ArrayList<>();
        for (Carta c : cartasSeleccionadas) {
            String palo = c.getPalo();
            if (palosVistos.contains(palo)) {
                return false; //Si juntas dos del mismo palo salgo
            }
            palosVistos.add(palo);
        }


        if (cartasSeleccionadas.size() == 1) {
            return true;
        }

        //Compañeros animales
        int numCompanieros = 0;
        for (Carta carta : cartasSeleccionadas) {
            if (carta.esCompanieroAnimal()) {
                numCompanieros++;
            }
        }

        //AS emparejado con UNA carta
        if (cartasSeleccionadas.size() == 2 && numCompanieros == 1) {
            return true;
        }

        //Combinaciones de 2-4 cartas del mismo número, total de puntos <= 10 y sin compañeros
        if (cartasSeleccionadas.size() >= 2 && cartasSeleccionadas.size() <= 4 && numCompanieros == 0) {
            int numero = cartasSeleccionadas.get(0).getNumero();
            int total = 0;

            for (Carta carta : cartasSeleccionadas) {
                if (carta.getNumero() != numero) {
                    return false; //No son el mismo numero
                }
                total += carta.getNumero();
            }
            return total <= 10;
        }
        return false;
    }


    /**
     * Metodo para jugar una carta de la mano del jugador contra el enemigo actual del castillo.
     * Se muestra el daño que se ha causado al enemigo además de la vida restante y nuevamente se muestra las
     * colecciones de las cartas restantes de la partida.
     * @param indicesCartas índice de las cartas escogidas de la mano del jugador para enfrentar al enemigo
     */
    public void jugarCarta (List<Integer> indicesCartas){
        //Si no hay cartas en la mano
        if (mano.isEmpty()) {
            //System.out.println("¡No tienes cartas para atacar! Has perdido.");
            partidaTerminada = true;
            victoria = false;
            registrarEstadisticas();
            return;
        }

        if (indicesCartas.isEmpty()) {
            //System.out.println("¡No has seleccionado ninguna carta!");
            return;
        }

        //Verificar si la jugada es valida
        List<Carta> cartasSeleccionadas = new ArrayList<>();
        for (int idx : indicesCartas) {
            cartasSeleccionadas.add(mano.get(idx));
        }

        if (!jugadaValida(cartasSeleccionadas)) {
            //System.out.println("¡Combinación de cartas no válida!");
            return;
        }

        //Daño total y aplicar efectos
        int danioTotal = 0;
        Map<String, Integer> efectosPorPalo = new HashMap<>();
        efectosPorPalo.put("Corazones", 0);
        efectosPorPalo.put("Diamantes", 0);
        efectosPorPalo.put("Picas", 0);
        efectosPorPalo.put("Treboles", 0);

        for (int idx : indicesCartas) {
            Carta carta = mano.get(idx);
            danioTotal += carta.getNumero();
            mazoCartasJugadas.add(carta);

            //Acumular efectos por palo (si no es inmune)
            if (castillo.isEmpty() || !carta.getPalo().equals(castillo.get(0).getPalo())) {
                efectosPorPalo.put(carta.getPalo(), efectosPorPalo.get(carta.getPalo()) + carta.getNumero());
            }
        }

        //Efectos por orden correcto
        //CORAZONES
        if (efectosPorPalo.get("Corazones") > 0) {
            int cartasAMover = Math.min(efectosPorPalo.get("Corazones"), mazoCartasDescartadas.size());
            for (int i = 0; i < cartasAMover; i++) {
                mazoPosada.add(mazoCartasDescartadas.remove(0));
            }
            System.out.println("¡Efecto Corazones! Movidas " + cartasAMover + " cartas de descartes a posada");
        }

        //DIAMANTES
        if (efectosPorPalo.get("Diamantes") > 0) {
            int cartasARobar = Math.min(efectosPorPalo.get("Diamantes"), 8 - mano.size());
            for (int i = 0; i < cartasARobar && !mazoPosada.isEmpty(); i++) {
                mano.add(mazoPosada.remove(0));
            }
            System.out.println("¡Efecto Diamantes! Robadas " + cartasARobar + " cartas");
        }

        //PICAS
        if (efectosPorPalo.get("Picas") > 0) {
            reduccionDanioEnemigo += efectosPorPalo.get("Picas");
            System.out.println("¡Efecto Picas! Reducción de daño aumentada en " + efectosPorPalo.get("Picas"));
        }

        //TREBOLES
        if (efectosPorPalo.get("Treboles") > 0) {
            danioTotal *= 2;
            System.out.println("¡Efecto Tréboles! Daño duplicado a " + danioTotal);
        }

        //Restar vida al enemigo
        vidaEnemigo -= danioTotal;
        cartasJugadas += cartasSeleccionadas.size();

        //Eliminar cartas jugadas de la mano
        indicesCartas.sort(Collections.reverseOrder());
        for (int idx : indicesCartas) {
            mano.remove(idx);
        }

        System.out.println("Daño causado al enemigo: " + danioTotal);
        System.out.println("Vida restante enemigo: " + vidaEnemigo);

        //Si enemigo ha sido derrotado
        if (vidaEnemigo <= 0) {
            Carta enemigoDerrotado = castillo.remove(0);

            if (vidaEnemigo == 0) {
                System.out.println("Carta enemiga añadida al mazo Posada: " + enemigoDerrotado);
                mazoPosada.add(enemigoDerrotado);
            } else {
                System.out.println("Carta enemiga descartada: " + enemigoDerrotado);
                mazoCartasDescartadas.add(enemigoDerrotado);
            }

            for (Carta carta : mazoCartasJugadas){
                mazoCartasDescartadas.add(carta);
            }
            mazoCartasJugadas.clear();//Limpiar mazo temporal después de eliminar al enemigo

            //Si el enemigo ha muerto, preparar uno nuevo o si no quedan mas enemigos terminar partida
            if (!castillo.isEmpty()) {
                prepararNuevoEnemigo();
            } else {
                partidaTerminada = true;
                victoria = true;
                registrarEstadisticas();
            }
        }

        //volver a mostrar las cartas restantes
        mostrarEstadoColecciones();
    }

    /**
     * Prepara un nuevo enemigo en el castillo, con sus respectivas características.
     */
    private void prepararNuevoEnemigo() {
        if (!castillo.isEmpty()) {
            configurarEnemigo(castillo.get(0));
            System.out.println("\n¡Nuevo enemigo aparece! " + castillo.get(0) + " (Vida: " + vidaEnemigo + ")");
        }
    }

    /**
     * Maneja la defensa contra el ataque del enemigo utilizando las cartas de defensa seleccionadas.
     * Calcula el total de defensa, aplica la reducción del daño del enemigo, y determina si la defensa es exitosa
     * o si la partida termina debido a una defensa fallida.
     *
     * @param cartasDefensa Lista de índices de las cartas de la mano que se usarán para defenderse.
     * @return {@code true} si la defensa fue exitosa, {@code false} si la defensa falló y termina la partida.
     */
    public boolean manejarDefensa (ArrayList<Integer> cartasDefensa, Scanner sc) {
        //Si no tienes ninguna carta en mano
        if (mano.isEmpty()) {
            System.out.println("¡No tienes cartas para defenderte! Has perdido.");
            partidaTerminada = true;
            victoria = false;
            registrarEstadisticas();
            return false;
        }

        int defensaTotal = 0;
        //Aplicar reduccion en caso de PICAS
        int danioReal = getDanioEnemigo();

        //Calcular puntos de defensaTotal inicial
        for (int contador : cartasDefensa) {
            defensaTotal += mano.get(contador).getNumero();
        }

        while (defensaTotal < danioReal) {
            if (mano.size() == cartasDefensa.size()) {
                //No quedan más cartas para defender
                partidaTerminada = true;
                victoria = false;
                registrarEstadisticas();
                return false;
            }

            if (sc != null) {
                //Solo interactuar si hay Scanner disponible
                System.out.println("\nDefensa insuficiente (" + defensaTotal + "/" + danioReal + ")");
                System.out.println("Necesitas " + (danioReal - defensaTotal) + " puntos más");
                System.out.println("Cartas disponibles:");

                //Mostrar solo cartas no seleccionadas
                for (int i = 0; i < mano.size(); i++) {
                    if (!cartasDefensa.contains(i)) {
                        System.out.println("\t[" + (i + 1) + "] " + mano.get(i));
                    }
                }

                System.out.println("Selecciona cartas adicionales (0 para cancelar)\nSeparar cada carta elegida [Ej: 5,1,2]:");
                try {
                    String entrada = sc.nextLine().trim();
                    if (entrada.equals("0")) {
                        partidaTerminada = true;
                        victoria = false;
                        registrarEstadisticas();
                        return false;
                    }

                    //Procesar cartas adicionales
                    for (String s : entrada.split(",")) {
                        int idx = Integer.parseInt(s.trim()) - 1;
                        if (idx >= 0 && idx < mano.size() && !cartasDefensa.contains(idx)) {
                            cartasDefensa.add(idx);
                            defensaTotal += mano.get(idx).getNumero();
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Introduce números separados por comas.");
                }
            } else {
                //No hay interfaz interactiva, terminar con defensa fallida
                partidaTerminada = true;
                victoria = false;
                registrarEstadisticas();
                return false;
            }
        }

        //Defensa exitosa - descartar cartas usadas
        cartasDefensa.sort(Collections.reverseOrder());
        for (int contador : cartasDefensa) {
            mazoCartasDescartadas.add(mano.remove(contador));
        }
        return true;
    }
}

