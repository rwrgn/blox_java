import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

// javafx ‰‰nentoistoon
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;

public class Blox extends JPanel implements KeyListener{
    
    public static int FRAME_WIDTH;// = 540;
    public static int FRAME_HEIGHT;// = (int)(FRAME_WIDTH*1.8);
    public static int PIXEL_SIZE;// = FRAME_WIDTH/133;
    public static int BLOCK_SIZE;// = PIXEL_SIZE*8;
    public static BufferedImage screenBuffer;//

    // Palikoiden muototeema
    public static final int BLOCK_THEME = 0;
    
    // N‰pp‰inten lukuun
    public static KeyCommand keyBuffer = KeyCommand.NONE;
    
    // Pelin tila
    public static boolean gameRunning = false;
    public static boolean titleScreenActive = true;
    public static boolean infoScreenActive = true;
    
    public static Level l;
    
    public static final int TITLE_SCREEN_COUNTER_MAX = 60;
    public static final int TITLE_SCREEN_COUNTER_THRESHOLD = TITLE_SCREEN_COUNTER_MAX/2;
    public static int titleScreenCounter  = TITLE_SCREEN_COUNTER_MAX;
    
    // Tuhoanimaatioihin
    public static boolean anyDestroyed = false;
    
    // Viiveet
    public static final int FPS_DELAY = 16;
    public static final int ANIM_DELAY_MAX = 1;
    public static final int BLINK_DELAY_MAX = 2;
    public static final int AI_DELAY_MAX = 10;
    
    public static int animDelay = ANIM_DELAY_MAX;
    public static int blinkDelay = BLINK_DELAY_MAX;
    public static int aiDelay = AI_DELAY_MAX;
    public static int cycleEventDelay = 200;
    
    // Musiikin ja ‰‰niefektien toistoon.
    // JFXPanel on luotava, vaikka sit‰ ei mihink‰‰n k‰ytet‰k‰‰n,
    // jotta JavaFX:‰‰ voi k‰ytt‰‰.
    public static final JFXPanel fxPanel = new JFXPanel();
    public static MediaPlayer mediaPlayer;
    public static MediaPlayer fxPlayer;
    
    public static boolean musicAvailable = false;
    public static boolean fxAvailable = false;
    
    public Blox(){
        // N‰pp‰insyˆtteen lukuun.
        addKeyListener(this);
    }
    
    public void addNotify(){
        super.addNotify();
        requestFocus();
    }
    
    public static void main(String[] args) {
        
        // Katsotaan n‰ytˆn resoluutio ja s‰‰det‰‰n ikkunan koko
        adjustWindowSize();
        
        // Luodaan JFrame grafiikkaa varten
        String appName = "blox";
        
        JFrame frame = new JFrame(appName);
        frame.getContentPane().add(new Blox());
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setResizable(false);
        
        runGame(frame);
               
    }
        
    // paintComponent vaaditaan JFramen/JPanelin extendaamiseen, jos
    // ruudulle haluaa piirt‰‰ jotain.   
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setDoubleBuffered(true);
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawImage(screenBuffer,0,0,this);
    }
    
    // keyReleased ja keyPressed on uudelleentoteutettava KeyListener-
    // rajapinnan toteutusta varten
    @Override
    public void keyPressed(KeyEvent e){
        
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT){
            keyBuffer = KeyCommand.LEFT;
            //System.out.println("VASEN!");
        }
        
        if (key == KeyEvent.VK_RIGHT){
            keyBuffer = KeyCommand.RIGHT;
            //System.out.println("OIKEA!");
        }
        
        if (key == KeyEvent.VK_DOWN){
            keyBuffer = KeyCommand.DOWN;
            //System.out.println("ALAS!");
        }
        
        if (key == KeyEvent.VK_UP){
            keyBuffer = KeyCommand.ROTATE;
            //System.out.println("KIERTO!");
        }
        
        if (key == KeyEvent.VK_ENTER){
            keyBuffer = KeyCommand.ENTER;
        }
        
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        ;
    }
    
    @Override
    public void keyTyped(KeyEvent e){
        ;
    }
    
    public static BufferedImage drawScreen(Level l){
        Graphics gg = screenBuffer.getGraphics();
        
        if (gameRunning){
        
            // Koko ruutu mustaksi
            gg.setColor(Color.BLACK);
            gg.fillRect(0,0, FRAME_WIDTH,FRAME_HEIGHT);

            // Piirret‰‰n kaivo
            Gfx.drawWell(gg,l.phase);
            
            // Piiret‰‰n pistelaskuri ja vaikeutuselementti-indikaattorit
            Gfx.drawScoreBar(gg, l.score, l.scoreLimit.get(l.phase),l.phase);
            
            Gfx.drawColorCycleBar(gg, l);
            Gfx.drawSurfaceRiseBar(gg, l);

            // Droppipalikan piirto
            if (l.db != null){
                Gfx.drawDropBlock(gg,l.db);
                
                // Vilkkuosoittimen piirto
                Gfx.drawBlinkerBar(gg, l.db, l.dropCounterMax, l.surfaceYAtX(l.db.x), l.phase);
            }

            for (int i = 0; i < l.blocks.length; i++){
                for (int j = 0; j < l.blocks[0].length; j++){
                    if (l.blocks[i][j] != null){
                        Gfx.drawBlock(gg,i,j,l.blocks[i][j],false);
                    }
                }
            }
        }
        
        else if (titleScreenActive){
            gg.setColor(Color.BLACK);
            gg.fillRect(0,0, FRAME_WIDTH,FRAME_HEIGHT);
            Gfx.drawIntroScreen(gg);
        }
        
        else if (infoScreenActive){
            gg.setColor(Color.BLACK);
            gg.fillRect(0,0, FRAME_WIDTH,FRAME_HEIGHT);
            Gfx.drawInfoScreen(gg);
        }
        
        else {
            Gfx.drawGameOverScreen(gg,l);
        }
        
        return screenBuffer;
    }
    
    public static void runGame(JFrame frame){
        Random rnd = new Random();
        
        // Taustamusiikin ja ‰‰niefektien alustus
        try {
            Media music = new Media(new File("bgm.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(music);
            musicAvailable = true;
        }
        
        catch (Exception e){
            musicAvailable = false;
        }
        
        try {
            Media destroyFx = new Media(new File("destroy.wav").toURI().toString());
            fxPlayer = new MediaPlayer(destroyFx);
            fxAvailable = true;
        }
        
        catch (Exception e){
            fxAvailable = false;
        }
        
        
        keyBuffer = KeyCommand.NONE;
        
        screenBuffer = drawScreen(l);
        
        // Ajastin
        long startTime = System.currentTimeMillis();
        long elapsedTime;
        long drift = 0;
        
        // P‰‰looppi
        while (true){
            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= FPS_DELAY){
                if (gameRunning) {runLevel(l);}
                else if (titleScreenActive) {showTitleScreen();}
                else if (infoScreenActive) {showInfoScreen();}
                else {showGameOverScreen();}
                screenBuffer = drawScreen(l);
                frame.repaint();
                
                startTime = System.currentTimeMillis();
                // Yritet‰‰n korjata mahdollinen ajastimen j‰ttˆ
                // Ei varmuutta, onko fiksu ratkaisu
                drift = elapsedTime-FPS_DELAY;
                if (drift > 0){
                    startTime -= drift;
                }
            }
            // Kˆkkˆ ajastinimplementaatio; silti parempi kuin
            // swingin oma Timer
            try {
                Thread.sleep(2);
            }
            catch (Exception e){;}
        }
        
    }
    
    public static void showTitleScreen(){
        
        if (keyBuffer != KeyCommand.NONE){
            if (keyBuffer == KeyCommand.ENTER){
                // Luodaan leveli ja siirryt‰‰n infon‰yttˆˆn
                l = new Level();
                l.db = l.createNewDropBlock(false);
                titleScreenActive = false;
                infoScreenActive = true;
                
            }
        }
        
        if (titleScreenCounter > 0) {titleScreenCounter -= 1;}
        else {titleScreenCounter = TITLE_SCREEN_COUNTER_MAX;}
        
        keyBuffer = KeyCommand.NONE;
        
    }
    
    public static void showInfoScreen(){
        if (keyBuffer != KeyCommand.NONE){
            if (keyBuffer == KeyCommand.ENTER){
                // K‰ynnistet‰‰n peli
                infoScreenActive = false;
                gameRunning = true;
                
                // Musiikit soimaan loputtomalle loopille
                if (musicAvailable){
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayer.play();
                }                
            }
        }
        
        keyBuffer = KeyCommand.NONE;
        
    }
    
    public static void showGameOverScreen(){
        
        if (keyBuffer != KeyCommand.NONE){
            if (keyBuffer == KeyCommand.ENTER){
                l = null;
                titleScreenActive = true;
            }
        }
        
        keyBuffer = KeyCommand.NONE;
    }
    
    public static void runLevel(Level l){
        if (!l.gameOver){
                        
            l.doPhaseEvents();
            
            if (l.breatheCounter > 0){
                l.decreaseBreatheCounter();
            }
            
            else{
                // Annetaan palikan pudotuskomento, jos pudotuslaskuri on kulunut
                // loppuun
                if (l.db != null && l.db.dropCounter[l.db.dropCounter.length-1] == 0){
                    keyBuffer = KeyCommand.DOWN;
                }

                // Luetaan komennot.
                if (keyBuffer != KeyCommand.NONE && l.db != null){    
                    if (keyBuffer == KeyCommand.LEFT){l.db.move(-1);}
                    if (keyBuffer == KeyCommand.RIGHT){l.db.move(1);}
                    if (keyBuffer == KeyCommand.ROTATE){l.db.rotate();}
                    if (keyBuffer == KeyCommand.DOWN){
                        // Pudotuksesta seuraavat seikat:
                        l.dropDropBlock(l.db);
                        
                        l.dbx = l.db.x;
                        l.db = null;

                        anyDestroyed = false;

                        l.checkAndInfectSeedBlocks();

                        anyDestroyed = l.destroyBlocks();

                    }
                }

                // Tarkistetaan, onko tuho k‰ynniss‰ ja edistet‰‰n tuhoanimaatiota
                l.checkIfDestructionInProgress();
                if (l.destructionInProgress){
                    anyDestroyed = l.destroyBlocks();
                    //if (!fxPlaying){
                    if (fxAvailable && fxPlayer.getStatus() != MediaPlayer.Status.PLAYING){
                        fxPlayer.play();
                    }
                    //}
                }

                // Jos tapahtui droppaus, tarkistetaan pit‰‰kˆ tuhota lis‰‰ kamaa
                if (anyDestroyed){
                    if (fxAvailable && fxPlayer.getStatus() != MediaPlayer.Status.STOPPED){
                        fxPlayer.stop();
                    }
                    l.checkAndInfectSeedBlocks();
                }

                if (l.db == null && !l.destructionInProgress){
                    l.db = l.createNewDropBlock(true);
                }

                keyBuffer = KeyCommand.NONE;

                // Pienenennet‰‰n Levelin dropBlockin droppilaskuria
                if (l.db != null){
                    l.db.decreaseCounter();
                }
            }
        }
        
        // Gameover:
        else{
            gameRunning = false;
            if (musicAvailable) {mediaPlayer.stop();}
        }
    }
    
    public static void adjustWindowSize(){
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        double h = s.getHeight();
        
        if (h <= 720) {
            FRAME_WIDTH = 280;
        }
        
        if (h <= 960){
            FRAME_WIDTH = 400;
        }
        
        if (h > 960) {
            FRAME_WIDTH = 540;
        }  
        
        FRAME_HEIGHT = (int)(FRAME_WIDTH*1.8);
        PIXEL_SIZE = FRAME_WIDTH/133;
        BLOCK_SIZE = PIXEL_SIZE*8;
        
        screenBuffer = new BufferedImage
                      (FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
        
    }
    
}