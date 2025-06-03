package org.example.regicidegrafico_juliomalpica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase Main que gestiona la interacción con el usuario y controla el flujo del juego.
 * Proporciona las opciones del menu principal, gestiona el inicio de nuevas partidas, carga partidas guardadas y
 * visualiza las estadisticas de partidas anteriores.
 *
 * @author Julio Malpica
 * @version 1.0
 */
public class Main {
    /**
     * Metodo principal que inicia el juego.
     * Muestra un menú con opciones al usuario y maneja la selección de cada opción.
     * Permite iniciar una nueva partida, cargar una partida guardada, ver estadísticas o salir del programa.
     *
     * @param args Argumentos de linea de comandos(no se utilizan)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        do {
            System.out.println("\nMENÚ PRINCIPAL - REGICIDE");
            System.out.println("\t1. Nueva partida");
            System.out.println("\t2. Cargar partida");
            System.out.println("\t3. Ver estadísticas");
            System.out.println("\t4. Salir");
            System.out.print("Selecciona una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.println("\nPartida nueva iniciada.\n");
                        nuevaPartida(sc);
                        break;
                    case 2:
                        continuarPartida(sc);
                        break;
                    case 3:
                        Partida.mostrarEstadisticas();
                        break;
                    case 4:
                        System.out.println("SALIENDO...");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error " + e.getMessage());
            }
        } while (opcion != 4);
        sc.close();
        System.out.println("Programa finalizado");
    }

    /**
     * Inicia una nueva partida del juego.
     * Crea una nueva instancia de la clase Partida, genera la baraja, prepara las colecciones necesarias
     * y muestra el estado inicial antes de comenzar a jugar.
     *
     * @param sc El objeto Scanner utilizado para la entrada del usuario.
     */
    private static void nuevaPartida(Scanner sc) {
        Partida partida = new Partida();

        //metodos antes de empezar a jugar
        partida.generarBaraja();
        partida.prepararColecciones();
        partida.verEstadoInicial();
        jugarPartida(sc, partida);
    }

    /**
     * Continúa una partida guardada.
     * Intenta cargar una partida previamente guardada y, si existe, muestra el estado inicial y continúa el juego.
     * Si no hay partida guardada, inicia una nueva partida.
     *
     * @param sc El objeto Scanner utilizado para la entrada del usuario.
     */
    private static void continuarPartida(Scanner sc) {
        Partida partida = Partida.cargarPartida();
        if (partida != null) {
            partida.verEstadoInicial();
            jugarPartida(sc, partida);
        } else {
            System.out.println("No hay partida guardada, creando una nueva...");
            nuevaPartida(sc);
        }
    }

    /**
     * Controla el flujo principal del juego una vez que la partida ha comenzado.
     * Gestiona la elección de cartas, la defensa contra los ataques enemigos y el final del juego.
     * Permite al usuario elegir cartas para jugar o defenderse, y termina la partida cuando es necesario.
     *
     * @param sc El objeto Scanner utilizado para la entrada del usuario.
     * @param partida La instancia de la partida actual.
     */
    private static void jugarPartida(Scanner sc, Partida partida) {
        while (partida.getVidaEnemigo() > 0 && !partida.getMano().isEmpty()) {
            int eleccionCarta = -1;

            while (eleccionCarta < 0 || eleccionCarta >= partida.getMano().size()) {
                try {
                    System.out.print("\nElige una carta (posición en mano) // salir --> GUARDAR Y SALIR: ");
                    String entrada = sc.nextLine().trim();

                    if (entrada.equalsIgnoreCase("salir")) {
                        partida.guardarPartida();
                        partida.setPartidaTerminada(true);
                        return;
                    }

                    if (!entrada.matches("\\d+")) {
                        System.out.println("Entrada inválida. Debes introducir un número válido.");
                        continue;
                    }

                    eleccionCarta = Integer.parseInt(entrada) - 1;

                    if (eleccionCarta >= 0 && eleccionCarta < partida.getMano().size()) {
                        //Cartas a jugar
                        ArrayList<Integer> cartasAJugar = new ArrayList<>();
                        cartasAJugar.add(eleccionCarta);
                        partida.jugarCarta(cartasAJugar);
                        //Verificar si el jugador se quedó sin cartas
                        if (partida.getMano().isEmpty()) {
                            System.out.println("¡No tienes cartas para jugar! Has perdido.");
                            partida.setPartidaTerminada(true);
                            partida.registrarEstadisticas();
                            return; //Terminar metodo
                        }
                    } else {
                        System.out.println("Opción no válida, debes elegir una carta de tu mano");
                        continue;
                    }

                    //Defender si es necesario
                    while (partida.getVidaEnemigo() > 0 && !partida.getMano().isEmpty()) {
                        System.out.println("¡El enemigo contraataca! Necesitas defenderte de " + partida.getDanioEnemigo() + " de daño.");
                        System.out.println("\nCartas en mano: ");
                        partida.mostrarMano();

                        //Verificar si tienes cartas para defender
                        if (partida.getMano().isEmpty()) {
                            System.out.println("¡No tienes cartas para defenderte! Has perdido.");
                            partida.setPartidaTerminada(true);
                            partida.registrarEstadisticas();
                            return;
                        }

                        System.out.println("Elige cartas para defender (0 para cancelar)\nSeparar cada carta elegida [Ej: 5,1,2]: ");
                        String cartasElegidasDefensa = sc.nextLine().trim();

                        if (cartasElegidasDefensa.equals("0")) {
                            System.out.println("¡Has decidido rendirte! Partida registrada.");
                            partida.setPartidaTerminada(true);
                            partida.registrarEstadisticas();
                            return;
                        }

                        if (!cartasElegidasDefensa.matches("^(\\d+)(,\\d+)*$")) {
                            System.out.println("Entrada inválida. Asegúrate de ingresar solo números separados por coma.");
                            continue;
                        }

                        try {
                            ArrayList<Integer> defensa = new ArrayList<>();
                            boolean indicesValidos = true;

                            for (String s : cartasElegidasDefensa.split(",")) {
                                int idx = Integer.parseInt(s.trim()) - 1;

                                if (idx < 0 || idx >= partida.getMano().size()) {
                                    System.out.println("El número " + (idx + 1) + " no es una posición válida en tu mano.");
                                    indicesValidos = false;
                                    break;
                                }
                                defensa.add(idx);
                            }

                            if (!indicesValidos) {
                                System.out.println("Por favor, introduce solo posiciones válidas");
                                continue;
                            }

                            if (!partida.manejarDefensa(defensa, sc)) {
                                System.out.println("¡Defensa fallida! Has perdido.");
                                partida.setPartidaTerminada(true);
                                partida.registrarEstadisticas();
                                return;
                            }

                            //Verificar si tienes cartas para jugar
                            if (partida.getMano().isEmpty()) {
                                System.out.println("¡No tienes cartas para seguir jugando! Has perdido.");
                                partida.setPartidaTerminada(true);
                                partida.registrarEstadisticas();
                                return;
                            }

                            System.out.println("¡Enhorabuena te has defendido! Ataque de nuevo");
                            System.out.println("\nVida enemigo: " + partida.getVidaEnemigo());
                            System.out.println("\nCartas restantes en mano:");
                            partida.mostrarMano();
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. Asegúrate de ingresar solo números separados por coma.");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Debes introducir un número válido");
                    sc.next();
                }
            }
        }
    }
}
