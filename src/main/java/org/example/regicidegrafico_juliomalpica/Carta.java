package org.example.regicidegrafico_juliomalpica;

/**
 * Clase abstracta Carta que nos permite representar una carta de una baraja compuesta por 52 cartas.
 * Cada carta tiene un número (valor del 1 al 13) y un palo (Picas, Corazones, Tréboles, Diamantes).
 * Los As de cada palo seran considerados compañeros animales.
 */
public abstract class Carta {
    protected int numero;
    protected String palo;
    protected boolean esCompanieroAnimal;

    /**
     * Constructor para crear una nueva carta
     * @param numero Número de la carta (1-13)
     * @param palo  Palo de la carta (Picas, Corazones, Tréboles, Diamantes)
     */
    public Carta(int numero, String palo) {
        this.numero = numero;
        this.palo = palo;
        this.esCompanieroAnimal = (numero == 1);
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
     * Determina si esta carta es un compañero animal (As).
     * @return TRUE = si la carta es un As (número 1), FALSE = en caso contrario
     */
    public boolean esCompanieroAnimal() {
        return esCompanieroAnimal;
    }

    /**
     * Representación en cadena de la carta.
     * @return Cadena que representa la carta
     */
    public abstract String toString();

}
