/**
 * @author Luis Juan Sanchez A01183634
 * @author Alfredo Hinojosa Huerta A01036053
 * @version 1.00 02/24/2014
 */
package tiroparabolico;

import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;

public class TiroParabolico extends JFrame implements Runnable {

    private Animacion animBalon; // Animacion del balon
    private Balon balon; // Objeto de la clase balon
    private long tiempoActual;  // tiempo actual
    private long tiempoInicial; // tiempo inicial
    private Image background; // Imagen de fondo de JFrame <-- Agregar Imagen
    private Image dbImage; // Imagen
    private Graphics dbg; // Objeto Grafico
    private int bVelx; // Posicion en X del balon
    private int bVely; // Posicion en Y del balon

    /**
     * Constructor Se inicializan las variables
     */
    public TiroParabolico() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1008, 758);
        setTitle("NBA Series!");
        bVelx = (int) (Math.random() * 9 + 1);
        bVely = (int) (Math.random() * 13 + 10);
        bVely *= -1;
        background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/nba.jpg"));

        // Carga las imagenes de la animacion del balon
        Image b0 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b0.png"));
        Image b1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b1.png"));
        Image b2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b2.png"));
        Image b3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b3.png"));
        Image b4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b4.png"));
        Image b5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b5.png"));

        // Se crea la animacion del balon
        animBalon = new Animacion();
        animBalon.sumaCuadro(b5, 200);
        animBalon.sumaCuadro(b4, 200);
        animBalon.sumaCuadro(b3, 200);
        animBalon.sumaCuadro(b2, 200);
        animBalon.sumaCuadro(b1, 200);
        animBalon.sumaCuadro(b0, 200);

        // Balon
        balon = new Balon(100, 300, animBalon);

        Thread th = new Thread(this);
        th.start();
    }

    /**
     * Se ejecuta el Thread
     */
    public void run() {

        // Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();
        while (true) {
            //checaColision();
            actualiza();
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }
        }
    }

    /**
     * En este metodo se actualiza..
     */
    public void actualiza() {
        balon.setPosY(balon.getPosY() + bVely);
        bVely++;
        balon.setPosX(balon.getPosX() + bVelx);
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
        tiempoActual += tiempoTranscurrido;
        balon.getAnimacion().actualiza(tiempoTranscurrido);
    }

    
    checaColision(){
        
    }
    /**
     * Metodo que actualiza las animaciones
     *
     * @param g es la imagen del objeto
     */
    public void paint(Graphics g) {
        // Inicializa el DoubleBuffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Este metodo..
     *
     * @param g objeto grafico
     */
    public void paint1(Graphics g) {
        g.drawImage(background, 0, 0, this);
        if (balon.getAnimacion() != null) {
            g.drawImage(balon.animacion.getImagen(), balon.getPosX(), balon.getPosY(), this);
        }
    }

    public static void main(String[] args) {
        TiroParabolico tiro = new TiroParabolico();
        tiro.setVisible(true);
    }

}
