/**
 * ULTIMA LINEA
 * @author Luis Juan Sanchez A01183634
 * @author Alfredo Hinojosa Huerta A01036053
 * @version 1.00 02/24/2014
 */
package tiroparabolico;

import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;

public class TiroParabolico extends JFrame {

    private Image background; // Imagen de fondo de JFrame <-- Agregar Imagen
    
    /**
     * Constructor
     * Se inicializan las variables
     */
    public TiroParabolico() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1008, 758);
        setTitle("NBA Series!");
        
        background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/nba.jpg"));
        
    }
    
    /**
     * ESTE METODO DEBE SER CAMBIADO POR PAINT1
     * Este metodo
     * @param g objeto grafico
     */
    public void paint(Graphics g){
        g.drawImage(background, 0, 0, this);
    }
    
    public static void main(String[] args) {
        TiroParabolico tiro = new TiroParabolico();
        tiro.setVisible(true);
    }
    
}
