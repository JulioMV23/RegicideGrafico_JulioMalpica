package org.example.regicidegrafico_juliomalpica;

/**
 * Clase Carta que nos permite representar una carta de una baraja compuesta por 52 cartas.
 * Cada carta tiene un número (valor del 1 al 13) y un palo (Picas, Corazones, Tréboles, Diamantes).
 */
public class Carta {
    private int numero;
    private String palo;

    /**
     * Constructor para crear una nueva carta
     * @param numero Número de la carta (1-13)
     * @param palo  Palo de la carta (Picas, Corazones, Tréboles, Diamantes)
     */
    public Carta(int numero, String palo) {
        this.numero = numero;
        this.palo = palo;
    }

    /**
     * Obtener número(valor) de la carta
     * @return número de la carta
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Obtener palo de la carta
     * @return palo de la carta
     */
    public String getPalo() {
        return palo;
    }

    /**
     * Metodo toString que devuelve la carta (número + palo)
     * @return cadena que representa la carta (número + palo)
     */
    @Override
    public String toString(){
        String[] nombres = {"","A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        return nombres[numero] + " de " + palo;
    }
}
