package org.example.regicidegrafico_juliomalpica;

/**
 * Representa una carta estandar del juego REGICIDE.
 * Extiende la clase Carta para cartas numericas (1-10) y las figuras enemigas (J, Q, K)
 */
public class CartaNormal extends Carta{
    /**
     * Crea una nueva carta.
     * @param numero Valor de la carta (1-13 donde 1=A, 11=J, 12=Q, 13=K)
     * @param palo Palo de la carta ("Picas", "Corazones", "Tréboles" o "Diamantes")
     */
    public CartaNormal(int numero, String palo) {
        super(numero, palo);
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
