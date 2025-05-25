package org.example.regicidegrafico_juliomalpica;

public class CartaNormal extends Carta{
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
