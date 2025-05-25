package org.example.regicidegrafico_juliomalpica;

/**
 * Clase Carta que nos permite representar una carta de una baraja compuesta por 52 cartas.
 * Cada carta tiene un número (valor del 1 al 13) y un palo (Picas, Corazones, Tréboles, Diamantes).
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

    public boolean esCompanieroAnimal() {
        return esCompanieroAnimal;
    }

    public abstract String toString();

}
