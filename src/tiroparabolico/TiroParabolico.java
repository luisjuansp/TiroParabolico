/**
 * @author Luis Juan Sanchez A01183634
 * @author Alfredo Hinojosa Huerta A01036053
 * @version 1.00 02/24/2014
 */
package tiroparabolico;

import javax.swing.JFrame;
import java.awt.Image;

public class TiroParabolico extends JFrame {

    private Image background; // Imagen de fondo de JFrame
    
    /**
     * Constructor
     * Se inicializan las variables
     */
    public TiroParabolico() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setTitle("NBA Series!");
        
    }
    
    public static void main(String[] args) {
        TiroParabolico tiro = new TiroParabolico();
        tiro.setVisible(true);
    }
    
}
