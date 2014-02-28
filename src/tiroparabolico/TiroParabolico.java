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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TiroParabolico extends JFrame implements Runnable, MouseListener, KeyListener {

    private Animacion animBalon; // Animacion del balon
    private Animacion cuadroCanasta; // Animacion de la canasta
    private Balon balon; // Objeto de la clase balon
    private Canasta canasta; // Objeto de la clase Canasta
    private long tiempoActual;  // tiempo actual
    private long tiempoInicial; // tiempo inicial
    private Image background; // Imagen de fondo de JFrame <-- Agregar Imagen
    private Image dbImage; // Imagen
    private Graphics dbg; // Objeto Grafico
    private int bVelx; // Velocidad en X del balon
    private int bVely; // Velocidad en Y del balon
    private int cMovx; // Movimiento en X de la canasta
    private int grav; // Gravedad
    private int vidas; // Vidas del usuario
    private int score; // Score del usuario
    private boolean click; // Booleano de click
    private boolean pausa; // Booleano de pausa

    /**
     * Constructor Se inicializan las variables
     */
    public TiroParabolico() {
        pausa = false;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1008, 758);
        click = false;
        setTitle("NBA Series!");
        bVelx = 0;
        bVely = 0;
        grav = 1;
        score = 0;
        vidas = 14;
        background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/nba.jpg"));

        // Carga las imagenes de la animacion del balon
        Image b0 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b0.png"));
        Image b1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b1.png"));
        Image b2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b2.png"));
        Image b3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b3.png"));
        Image b4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b4.png"));
        Image b5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/b5.png"));
        Image c = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/canasta.png"));

        // Se crea la animacion del balon
        animBalon = new Animacion();
        animBalon.sumaCuadro(b5, 200);
        animBalon.sumaCuadro(b4, 200);
        animBalon.sumaCuadro(b3, 200);
        animBalon.sumaCuadro(b2, 200);
        animBalon.sumaCuadro(b1, 200);
        animBalon.sumaCuadro(b0, 200);

        cuadroCanasta = new Animacion();
        cuadroCanasta.sumaCuadro(c, 200);

        // Balon
        balon = new Balon(100, 300, animBalon);

        //Canasta
        canasta = new Canasta(900, 680, cuadroCanasta);
        addMouseListener(this);
        addKeyListener(this);
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
            if (vidas >= 0) {
                checaColision();
                actualiza();
            }
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }
        }
    }

    /**
     * En este metodo se actualiza..
     */
    public void actualiza() {
        grav = 6 - vidas / 3;
        balon.setPosY(balon.getPosY() - bVely);
        if (click) {
            bVely -= grav;
        }
        balon.setPosX(balon.getPosX() + bVelx);
        canasta.setPosX(canasta.getPosX() + cMovx);
        cMovx = 0;
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
        tiempoActual += tiempoTranscurrido;
        balon.getAnimacion().actualiza(tiempoTranscurrido);
    }

    /**
     *
     */
    public void checaColision() {
        Rectangle cuadro = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        if (!cuadro.intersects(balon.getPerimetro())) {
            bVelx = 0;
            bVely = 0;
            balon.setPosX(100);
            balon.setPosY(300);
            click = false;
            vidas--;
        }
        if (canasta.getPerimetro().intersects(balon.getPerimetro())) {
            bVelx = 0;
            bVely = 0;
            balon.setPosX(100);
            balon.setPosY(300);
            click = false;
            score += 2;
        }
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
        if (canasta.getAnimacion() != null) {
            g.drawImage(canasta.animacion.getImagen(), canasta.getPosX(), canasta.getPosY(), this);
        }
    }

    /**
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        if (!click) {
            if (balon.getPerimetro().contains(e.getPoint())) {
                click = true;
                bVely = (int) (Math.random() * (Math.sqrt(250 * 2 * grav) / 2)
                        + (Math.sqrt(250 * 2 * grav) / 2));

                bVelx = (int) ((((Math.random() * 500 / 2) + 250) * grav)
                        / (bVely * 2));
            }
        }
    }

    /**
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_P) {
            pausa = !pausa;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            cMovx = -15;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            cMovx = 15;
        }

    }

    /**
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {

    }

    /**
     *
     * @param e
     */
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        TiroParabolico tiro = new TiroParabolico();
        tiro.setVisible(true);
    }

}
