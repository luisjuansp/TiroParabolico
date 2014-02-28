/**
 * @author Luis Juan Sanchez A01183634
 * @author Alfredo Hinojosa Huerta A01036053
 * @version 1.00 02/24/2014
 */
package tiroparabolico;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;

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
    private double bAng; // Angulo de la velocidad del balon
    private boolean click; // Booleano de click
    private boolean pausa; // Booleano de pausa
    private boolean mute; // Control de sonidos
    private int score; // Puntaje del juego
    private int lives; // Vidas del jugador
    private int fouls; // Errores del jugador
    private Font myFont;
    private SoundClip fail;
    private SoundClip goal;
    private SoundClip over;

    /**
     * Constructor Se inicializan las variables
     */
    public TiroParabolico() {
        myFont = new Font("Serif", Font.BOLD, 30); // Estilo de fuente
        pausa = false;
        mute = false;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1008, 758);
        click = false;
        setTitle("NBA Series!");
        score = 0;
        lives = 5;
        fouls = 3;
        bVelx = 0;
        bVely = 0;
        grav = 1;
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

        // Se crea la animacion de la canasta
        cuadroCanasta = new Animacion();
        cuadroCanasta.sumaCuadro(c, 200);

        // Balon
        balon = new Balon(100, 300, animBalon);

        //Canasta
        canasta = new Canasta(900, 680, cuadroCanasta);
        
        // Se cargan los sonidos
        fail = new SoundClip("sounds/boing2.wav");
        goal = new SoundClip("sounds/bloop_x.wav");
        over = new SoundClip("sounds/buzzer_x.wav");
        
        addMouseListener(this);
        addKeyListener(this);
        Thread th = new Thread(this);
        th.start();
    }

    /**
     * Se ejecuta el Thread, el juego no continua si la pausa esta
     * activada.
     */
    public void run() {

        // Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();
        while (true) {
            if(!pausa){
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
     * En este metodo se actualiza las posiciones del balon y de la
     * canasta.
     */
    public void actualiza() {
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
     * Este metodo se encarga de cambiar las posiciones de lso objetos balon
     * y canasta cuando colisionan entre si. Tambien se encarga de obstruir la
     * salida de la canasta del JFrame.
     */
    public void checaColision() {
        
        // BALON VS JFRAME
        Rectangle cuadro = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        if (!cuadro.intersects(balon.getPerimetro())) {
            bVelx = 0;
            bVely = 0;
            balon.setPosX(100);
            balon.setPosY(300);
            fouls--;
            click = false;
            if (!mute) {
                fail.play();
            }
        }
        
        // CANASTA VS BALON
        if (canasta.getPerimetro().intersects(balon.getPerimetro())) {
            bVelx = 0;
            bVely = 0;
            balon.setPosX(100);
            balon.setPosY(300);
            score+=2;
            click = false;
            if (!mute) {
                goal.play();
            }
        }
        
        // CANASTA VS JFRAME
        if(canasta.getPosX() < 5 ) {
            cMovx = 0;
        }
    }

    /**
     * Metodo que actualiza las animaciones.
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
        
        //-----IMPRESION DEL TABLERO
        g.setFont(myFont); // Aplica el estilo fuente a las string
        g.setColor(Color.yellow);
        g.drawString("" + score, 930, 97);
        g.setColor(Color.red);
        g.drawString("" + lives, 754, 98);
        g.drawString("" + fouls, 756, 178);
        if(pausa){
            g.drawString("P", 943, 178);
        }
        
    }

    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Evento que inicia el movimiento random del balon
     * usando la bandera click.
     * @param e Evento
     */
    public void mouseClicked(MouseEvent e) {
        if (!click) {
            if (balon.getPerimetro().contains(e.getPoint())) {
                click = true;
                bAng = (Math.random() * 60) + 15;
                bVely = (int) (Math.sqrt(250 * 2 * grav));
                bVelx = (int) ((650 * grav) / (bVely * 2));
            }
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    /**
     * Este evento se carga de activar la pausa al presionar
     * la tecla P. Las flechas de izquierda y derecha funcionan
     * para darle la direccion correta a la canasta.
     * @param e Evento
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
        
        if (e.getKeyCode() == KeyEvent.VK_S) {
            mute = !mute;
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        TiroParabolico tiro = new TiroParabolico();
        tiro.setVisible(true);
    }

}