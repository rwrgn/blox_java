import java.awt.*;
import java.util.*;

// Staattiset grafiikkametodit pelin grafiikan piirtoon

public class Gfx {
    
    // Kaivonpiirtometodi (sivuvaikutuksena)
    public static void drawWell(Graphics g, int phase){
        Color[] c = Gfx.blockColors(phase+1);
        
        int oX = Blox.FRAME_WIDTH/2 - Blox.BLOCK_SIZE*6 - Blox.PIXEL_SIZE;
        int oY = 0+Blox.BLOCK_SIZE*4;
        
        g.setColor(c[3]);
        // Vasen puolisko
        g.fillRect(oX,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*20+Blox.PIXEL_SIZE);
        
        // Oikea puolisko
        g.setColor(c[3]);
        g.fillRect(oX+Blox.BLOCK_SIZE*11+Blox.PIXEL_SIZE*2,oY,
                   Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*20+Blox.PIXEL_SIZE);
        
        // Pohja
        g.setColor(c[2]);
        g.fillRect(oX,oY+Blox.BLOCK_SIZE*20+Blox.PIXEL_SIZE,
                   Blox.BLOCK_SIZE*12+Blox.PIXEL_SIZE*2,Blox.BLOCK_SIZE);
        
    }
    
    // Palikan vilkkuvan osoittimen piirtometodi
    public static void drawBlinkerBar(Graphics g, DropBlock db, int dropCounterMax, int y, int phase){
        int oX = Blox.FRAME_WIDTH/2 - Blox.BLOCK_SIZE*5 + Blox.PIXEL_SIZE;
        int oY = 0+Blox.BLOCK_SIZE*4 + Blox.PIXEL_SIZE;
        
        // Kaivon oikean reunan laskuripalikoita varten:
        int rX = Blox.FRAME_WIDTH/2+Blox.BLOCK_SIZE*6+Blox.PIXEL_SIZE;
        int rY = 0+Blox.BLOCK_SIZE*4;
        
        int dc = 0;
        
        // Katsotaan, mikä droppilaskurin komponenteista valitaan
        if (db.dropCounter[0] >= 0 && db.dropCounter[1] == dropCounterMax){
            dc = db.dropCounter[0];
        }
        
        else if (db.dropCounter[1] >= 0 && db.dropCounter[2] == dropCounterMax){
            dc = db.dropCounter[1];
        }
        
        else if (db.dropCounter[2] >= 0 && db.dropCounter[3] == dropCounterMax){
            dc = db.dropCounter[2];
        }
        
        else {
            dc = db.dropCounter[3];
        }
        
        // Monipuolisempi, vilkun väri vaihtuu pudotettavan palikan mukaan
        Color[] c = {(Gfx.blockColors(Math.abs(db.blocks[0].color)))[1],
                     (Gfx.blockColors(Math.abs(db.blocks[1].color)))[1],
                     (Gfx.blockColors(Math.abs(db.blocks[2].color)))[1]};
        
        int[] newRed = {0,0,0}; int[] newGreen = {0,0,0}; int[] newBlue = {0,0,0};
        Color[] cc = new Color[3];
        
        for (int i = 0; i < 3; i++){
            newRed[i] = (c[i].getRed() - (c[i].getRed()/dropCounterMax)*(dropCounterMax-dc));
            newGreen[i] = (c[i].getGreen() - (c[i].getGreen()/dropCounterMax)*(dropCounterMax-dc));
            newBlue[i] = (c[i].getBlue() - (c[i].getBlue()/dropCounterMax)*(dropCounterMax-dc));
            if (newRed[i]<0){newRed[i]=0;} if (newGreen[i]<0){newGreen[i]=0;} if (newBlue[i]<0){newBlue[i]=0;}
            cc[i] = new Color(newRed[i],newGreen[i],newBlue[i]);
        }
        
        // Ei piirrettä palkkia gameoveria aiheuttaville palikoille:
        if (y > 1) {
        
            for (int i = 0; i < 3; i++){
                g.setColor(cc[2-i]);
                if (db.blocks[2-i].destroyer){
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE)+Blox.PIXEL_SIZE*5,
                                   Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x+Blox.PIXEL_SIZE*5,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*6);
                    
                    // Naama
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x+Blox.PIXEL_SIZE,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE)+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x+Blox.PIXEL_SIZE*4,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE)+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                    g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE)+Blox.PIXEL_SIZE*3,
                                   Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    
                    
                }
                else{
                    if (db.blocks[2-i].color >= 0){
                        g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*6);
                    }
                    else{
                        g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                        g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE)+Blox.PIXEL_SIZE*5,
                                   Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                        g.fillRect(oX+Blox.BLOCK_SIZE*db.x,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                        g.fillRect(oX+Blox.BLOCK_SIZE*db.x+Blox.PIXEL_SIZE*5,oY+y*Blox.BLOCK_SIZE-(i*Blox.BLOCK_SIZE),
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*6);
                    }
                }
            }
        }
        
        // Palikat kaivon oikeaan reunaan:
        g.setColor((Gfx.blockColors(phase+1))[1]);
        if (db.dropCounter[0] > 0) {
            g.fillRect(rX+Blox.PIXEL_SIZE,rY+Blox.BLOCK_SIZE*2+Blox.PIXEL_SIZE,
                       Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*6);
        }
        if (db.dropCounter[1] > 0) {
            g.fillRect(rX+Blox.PIXEL_SIZE,rY+Blox.BLOCK_SIZE+Blox.PIXEL_SIZE,
                       Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*6);
        }
        if (db.dropCounter[2] > 0) {
            g.fillRect(rX+Blox.PIXEL_SIZE,rY+Blox.PIXEL_SIZE,
                       Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*6);
        }
        
        
    }
    
    
    public static void drawGameOverScreen(Graphics g, Level l){
        int x = 0;
        int y = Blox.FRAME_HEIGHT/2-Blox.BLOCK_SIZE*4;
        
        g.setColor(Color.black);
        g.fillRect(x,y,Blox.FRAME_WIDTH,Blox.BLOCK_SIZE*8);
        g.setColor((Gfx.blockColors(l.phase+1))[1]);
        g.fillRect(x,y-Blox.PIXEL_SIZE*4,Blox.FRAME_WIDTH,Blox.PIXEL_SIZE*4);
        g.setColor((Gfx.blockColors(l.phase+1))[2]);
        y = Blox.FRAME_HEIGHT/2+Blox.BLOCK_SIZE*4;
        g.fillRect(x,y,Blox.FRAME_WIDTH,Blox.PIXEL_SIZE*4);
        
        // xoxo
        g.setColor((Gfx.blockColors(l.phase+1))[0]);
        int oX = Blox.FRAME_WIDTH/2-Blox.BLOCK_SIZE*8+Blox.PIXEL_SIZE*3;
        int oY = Blox.FRAME_HEIGHT/2-Blox.BLOCK_SIZE*2+Blox.PIXEL_SIZE*4;
        g.fillRect(oX, oY, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX, oY+Blox.BLOCK_SIZE*2, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY+Blox.BLOCK_SIZE*2, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        
        oX += Blox.BLOCK_SIZE*4;
        g.fillRect(oX,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX+Blox.BLOCK_SIZE*2,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        
        oX += Blox.BLOCK_SIZE*4;
        g.fillRect(oX, oY, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX, oY+Blox.BLOCK_SIZE*2, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY+Blox.BLOCK_SIZE*2, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        
        oX += Blox.BLOCK_SIZE*4;
        g.fillRect(oX,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX+Blox.BLOCK_SIZE*2,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        
        
    }  
    
    // Droppipalikan piirtometodi
    public static void drawDropBlock(Graphics g, DropBlock db){
        int oX = Blox.FRAME_WIDTH/2 - Blox.BLOCK_SIZE*5;
        int oY = Blox.BLOCK_SIZE - Blox.PIXEL_SIZE;
        
        int realX = oX+(db.x*Blox.BLOCK_SIZE);
        int realY = oY;
        
        Gfx.drawBlock(g, realX, realY, db.blocks[0], true);
        Gfx.drawBlock(g, realX, realY+Blox.BLOCK_SIZE, db.blocks[1], true);
        Gfx.drawBlock(g, realX, realY+(Blox.BLOCK_SIZE*2), db.blocks[2], true);
        
    }
    
    // Metodi, joka sivuvaikutuksena piirtää annettuun Graphicsiin
    // määritetynlaisen palikan.
    // Jos boolean absoluteCoord = true, piirtoon ei käytetä levelin
    // koordinaatteja, vaan oikeita
    public static void drawBlock(Graphics g, int x, int y, Block b,
            boolean absoluteCoord){
        
        int oX; int oY; int realX; int realY;
        Color[] c = new Color[5];
        
        if (absoluteCoord){
            realX = x; realY = y;
        }
        
        else{
            // Origo
            oX = Blox.FRAME_WIDTH/2 - Blox.BLOCK_SIZE*5;
            oY = 0+Blox.BLOCK_SIZE*4;

            // Todelliset piirrettävän palikan koordinaatit
            realX = oX+(x*Blox.BLOCK_SIZE);
            realY = oY+(y*Blox.BLOCK_SIZE);
        }
        
        // Haetaan värit sen perusteella, onko palikan tuhoutuminen aktivoitunut
        if (b.toBeDestroyed){
            Color[] orig = Gfx.blockColors(Math.abs(b.color));
            // Himmennetään valkoista sen mukaan, monesko tuhoframe on menossa.
            int redFactor = 127*Block.DESTRUCTION_COUNTER_MAX/orig[2].getRed();
            int greenFactor = 127*Block.DESTRUCTION_COUNTER_MAX/orig[2].getGreen();
            int blueFactor = 127*Block.DESTRUCTION_COUNTER_MAX/orig[2].getBlue();
            
            int newRed = 255 - redFactor*(Block.DESTRUCTION_COUNTER_MAX -
                        b.destructionCounter);
            int newGreen = 255 - greenFactor*(Block.DESTRUCTION_COUNTER_MAX -
                        b.destructionCounter);
            int newBlue = 255 - blueFactor*(Block.DESTRUCTION_COUNTER_MAX -
                        b.destructionCounter);
            
            if (newRed < 0) {newRed = 0;}
            if (newGreen < 0) {newGreen = 0;}
            if (newBlue < 0) {newBlue = 0;}
            
            Color ddColor = new Color(newRed,newGreen,newBlue);
            
            for (int i = 0; i < 5; i++){
                c[i] = ddColor;
            }
                    
            
        }
        
        else{
            // Haetaan normaali väritaulukko{
            if (b.destroyer) {
                c = Gfx.blockColors(100);
            }
            else{
                c = Gfx.blockColors(Math.abs(b.color));
            }
        }
        
        
        // Blox.BLOCK_THEME määrää palikoiden tyylin:
        if (Blox.BLOCK_THEME == 2) {
            
            if (b.destroyer){
                g.setColor(Color.black);
                    g.fillRect(realX, realY, Blox.PIXEL_SIZE*8,Blox.PIXEL_SIZE*8);
                    
                    g.setColor(c[0]);
                    g.fillRect(realX,realY,Blox.PIXEL_SIZE*7, Blox.PIXEL_SIZE);
                    g.fillRect(realX,realY+Blox.PIXEL_SIZE,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*6);
                    
                    g.setColor(c[3]);
                    g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                               Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE*2,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                    /*
                    g.setColor(c[3]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*3, realY+Blox.PIXEL_SIZE*3,
                               Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE*2);
                    g.setColor(c[1]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*4, realY+Blox.PIXEL_SIZE*4,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);*/
                    
                    g.setColor(c[1]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*6,
                               Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE*6,realY+Blox.PIXEL_SIZE,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                    
                    g.setColor(c[4]);
                    g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,
                               Blox.PIXEL_SIZE*8,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*8);
            }
            
            else{
                if (b.color >= 0){
                    // Tavalliset ja kivettyneet
                    g.setColor(c[2]);
                    g.fillRect(realX, realY, Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE*8);
                    
                    g.setColor(c[0]);
                    g.fillRect(realX,realY,Blox.PIXEL_SIZE*7, Blox.PIXEL_SIZE);
                    g.fillRect(realX,realY+Blox.PIXEL_SIZE,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*7);
                    
                    g.setColor(c[1]);
                    g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                               Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE*2,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*4);
                    
                    /*g.setColor(c[3]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*3, realY+Blox.PIXEL_SIZE*3,
                               Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE*2);
                    g.setColor(c[1]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*4, realY+Blox.PIXEL_SIZE*4,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);*/
                    
                    g.setColor(c[4]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*6,
                               Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE*6,realY+Blox.PIXEL_SIZE*2,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                    
                    g.setColor(c[4]);
                    g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,
                               Blox.PIXEL_SIZE*8,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*8);
                    
                }
                
                else{
                    // Antipalikat
                    g.setColor(Color.black);
                    g.fillRect(realX, realY, Blox.PIXEL_SIZE*8,Blox.PIXEL_SIZE*8);
                    
                    g.setColor(c[0]);
                    g.fillRect(realX,realY,Blox.PIXEL_SIZE*7, Blox.PIXEL_SIZE);
                    g.fillRect(realX,realY+Blox.PIXEL_SIZE,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*6);
                    
                    g.setColor(c[3]);
                    g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                               Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE*2,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                    
                    g.setColor(c[3]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*3, realY+Blox.PIXEL_SIZE*3,
                               Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE*2);
                    g.setColor(c[1]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*4, realY+Blox.PIXEL_SIZE*4,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                    
                    g.setColor(c[1]);
                    g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*6,
                               Blox.PIXEL_SIZE*5,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE*6,realY+Blox.PIXEL_SIZE,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*5);
                    
                    g.setColor(c[4]);
                    g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,
                               Blox.PIXEL_SIZE*8,Blox.PIXEL_SIZE);
                    g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,
                               Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*8);
                }
                
            }
            
        }
        
        // Default-theme
        else{
            // Kaikentuhoajapalikat
            if (b.destroyer){
                /*g.setColor(c[0]);
                g.fillRect(realX,realY,Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE*4);
                g.setColor(c[1]);
                g.fillRect(realX+Blox.PIXEL_SIZE*4,realY,
                           Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE*4);
                g.fillRect(realX,realY+Blox.PIXEL_SIZE*4,
                           Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE*4);
                g.setColor(c[2]);
                g.fillRect(realX+Blox.PIXEL_SIZE*4,realY+Blox.PIXEL_SIZE*4,
                           Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE*4);*/
                int factor = b.destroyerBlinkCounter*3/Block.DESTROYER_BLINK_COUNTER_MAX;
                
                g.setColor(c[3-factor]);
                g.fillRect(realX,realY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
                              
                //g.setColor(c[3-factor]);
                g.setColor(Color.black);
                g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                           Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*6);
                
                // Naama
                g.setColor(c[3-factor]);
                g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*2,
                           Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                g.fillRect(realX+Blox.PIXEL_SIZE*5,realY+Blox.PIXEL_SIZE*2,
                           Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*5,
                           Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE);
            }

            else{
                
                // Kivettyneet palikat
                if (b.petrified){
                    g.setColor(c[3]);
                        g.fillRect(realX,realY,
                                   Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE);
                        g.fillRect(realX,realY,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*7);

                        /*g.setColor(c[4]);
                        g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE*6);*/

                        g.setColor(Color.black);
                        g.fillRect(realX+Blox.PIXEL_SIZE*3,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE*6);

                        /*g.setColor(c[3]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*6,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*6);*/

                        g.setColor(c[4]);
                        g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,
                                   Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*8);

                        // Yksittäiset pikselit
                        g.setColor(c[3]);
                        g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*6,realY,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                        g.setColor(c[4]);
                        g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE*7,
                                   Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                    
                }
                
                else{
                    // Tavalliset palikat
                    if (b.color >= 0){
                        //g.setColor(c[0]);
                        //g.fillRect(realX,realY,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                        g.setColor(c[0]);
                        g.fillRect(realX,realY,
                                   Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE);
                        g.fillRect(realX,realY,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*7);

                        g.setColor(c[1]);
                        g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE*6);

                        g.setColor(c[2]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*3,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE*4,Blox.PIXEL_SIZE*6);

                        g.setColor(c[3]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*6,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*6);

                        g.setColor(c[4]);
                        g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,
                                   Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*8);

                        // Yksittäiset pikselit
                        g.setColor(c[2]);
                        g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*6,realY,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                        g.setColor(c[3]);
                        g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE*7,
                                   Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                    }

                    // Antipalikat
                    else {
                        g.setColor(c[0]);
                        g.fillRect(realX,realY,Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE);
                        g.fillRect(realX,realY,Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*7);

                        g.setColor(Color.black);
                        g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*6);

                        g.setColor(c[4]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*2,
                                   Blox.PIXEL_SIZE*2,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*2,realY+Blox.PIXEL_SIZE*2,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*4);

                        g.fillRect(realX+Blox.PIXEL_SIZE*7,realY+Blox.PIXEL_SIZE,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*7);
                        g.fillRect(realX+Blox.PIXEL_SIZE,realY+Blox.PIXEL_SIZE*7,
                                   Blox.PIXEL_SIZE*7,Blox.PIXEL_SIZE);

                        g.setColor(c[3]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*4,realY+Blox.PIXEL_SIZE*2,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*3,realY+Blox.PIXEL_SIZE*5,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                        g.setColor(c[2]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*7,realY,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                        g.fillRect(realX,realY+Blox.PIXEL_SIZE*7,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                        g.fillRect(realX+Blox.PIXEL_SIZE*5,realY+Blox.PIXEL_SIZE*2,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);
                        g.fillRect(realX+Blox.PIXEL_SIZE*4,realY+Blox.PIXEL_SIZE*5,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE);

                        g.setColor(c[1]);
                        g.fillRect(realX+Blox.PIXEL_SIZE*5,realY+Blox.PIXEL_SIZE*3,
                                   Blox.PIXEL_SIZE,Blox.PIXEL_SIZE*3);

                    }
                }
            }
        }
    }
    
    public static void drawColorCycleBar(Graphics g, Level l){
        if (l.colorCycling){
            Color c = (Gfx.blockColors(l.phase+1))[2];
            int factor = (l.colorCycleCounter-1)/(l.COLOR_CYCLE_COUNTER_MAX/8);
                        
            int x = (Blox.FRAME_WIDTH/2) - Blox.BLOCK_SIZE*7-Blox.PIXEL_SIZE*2;
            int y = (Blox.BLOCK_SIZE*25) - Blox.PIXEL_SIZE;
            
            g.setColor(c);
            g.fillRect(x,y+(8-(Blox.BLOCK_SIZE*factor)),
                       Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*factor);
            
            g.fillRect(x+Blox.PIXEL_SIZE*2,y+Blox.PIXEL_SIZE*3,
                       Blox.PIXEL_SIZE*3,Blox.PIXEL_SIZE*3);
            g.fillRect(x,y+Blox.PIXEL_SIZE*7,
                       Blox.PIXEL_SIZE*3,Blox.PIXEL_SIZE*3);
            g.fillRect(x+Blox.PIXEL_SIZE*4,y+Blox.PIXEL_SIZE*7,
                       Blox.PIXEL_SIZE*3,Blox.PIXEL_SIZE*3);
            
        }
    }
    
    public static void drawSurfaceRiseBar(Graphics g, Level l){
        if (l.surfaceRising){
            Color c = (Gfx.blockColors(l.phase+1))[4];
            int factor = (l.surfaceRiseCounter-1)/(l.SURFACE_RISE_COUNTER_MAX/8);
                        
            int x = (Blox.FRAME_WIDTH/2) + Blox.BLOCK_SIZE*6+Blox.PIXEL_SIZE*2;
            int y = (Blox.BLOCK_SIZE*25) - Blox.PIXEL_SIZE;
            
            g.setColor(c);
            g.fillRect(x,y+(8-(Blox.BLOCK_SIZE*factor)),
                       Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*factor);
            
            g.fillRect(x+Blox.PIXEL_SIZE,y+Blox.PIXEL_SIZE*3,
                       Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*2);
            g.fillRect(x+Blox.PIXEL_SIZE,y+Blox.PIXEL_SIZE*6,
                       Blox.PIXEL_SIZE*6,Blox.PIXEL_SIZE*2);
        }
        
    }
    
    public static void printNumbers(Graphics g, int n){
        
    }
    
    public static void drawScoreBar(Graphics g, int score, int scoreLimit, int phase){
        //Color c = Gfx.colorPalette(1);
        Color c = (Gfx.blockColors(phase+1))[0];
        int x = (Blox.FRAME_WIDTH/2) - Blox.BLOCK_SIZE*6-Blox.PIXEL_SIZE;
        int y = (Blox.BLOCK_SIZE*25) + Blox.PIXEL_SIZE*2; 
        int wellBottomWidth = (Blox.BLOCK_SIZE*12)+Blox.PIXEL_SIZE*2;
        int barWidth = ((1000*wellBottomWidth/scoreLimit)*score)/1000;
        
        Color cc = new Color(c.getRed(),c.getGreen(),c.getBlue());
        
        g.setColor(cc);
        g.fillRect(x,y, barWidth,Blox.BLOCK_SIZE);
    }
    
    public static void drawInfoScreen(Graphics g){
        // Piirretään ohjenäyttö
        int b = Blox.PIXEL_SIZE;
        int bs = Blox.BLOCK_SIZE;
        int x = Blox.FRAME_WIDTH/6+b*2;
        int y = Blox.FRAME_HEIGHT/5;
        
        Gfx.drawBlock(g,x,y,new Block(4,false,false),true);
        Color c = (Gfx.blockColors(1))[1];
        g.setColor(c);
        x += bs*2+b*2;
        y += b*3;
        g.fillRect(x,y,b*6,b*2);
        g.fillRect(x+b*2,y-b*2,b*2,b*6);
        
        x += bs*2;
        y -= b*3;
        Gfx.drawBlock(g,x,y,new Block(-4,false,false),true);
        
        x += bs*2+b*2;
        g.setColor(c);
        g.fillRect(x, y+b,b*6,b*2);
        g.fillRect(x, y+b*4,b*6,b*2);
        
        x += bs*2;
        g.setColor(Color.white);
        g.fillRect(x,y,bs,bs);
        g.fillRect(x+bs+b,y,bs,bs);
        
        
        x = Blox.FRAME_WIDTH/6+b*2;
        y = Blox.FRAME_HEIGHT/5*2;
        
        
        Block bbb = new Block(0,true,false);
        bbb.destroyerBlinkCounter = 5;
        
        Gfx.drawBlock(g,x,y,bbb,true);
        g.setColor(c);
        x += bs*2+b*2;
        y += b*3;
        g.fillRect(x,y,b*6,b*2);
        g.fillRect(x+b*2,y-b*2,b*2,b*6);
        
        x += bs*2;
        y -= b*3;
        Gfx.drawBlock(g,x-b*5,y-b*5,new Block(1,false,false),true);
        Gfx.drawBlock(g,x+bs-b*3,y-b*5,new Block(2,false,false),true);
        Gfx.drawBlock(g,x,y+b*5,new Block(3,false,false),true);
        
        x += bs*2+b*2;
        g.setColor(c);
        g.fillRect(x, y+b,b*6,b*2);
        g.fillRect(x, y+b*4,b*6,b*2);
        
        x += bs*2;
        g.setColor(Color.white);
        g.fillRect(x,y,bs,bs);
        g.fillRect(x+bs+b,y,bs,bs);
        
        
        x = Blox.FRAME_WIDTH/6+b*2;
        y = Blox.FRAME_HEIGHT/5*3;
        
        Gfx.drawBlock(g,x,y,new Block(5,false,false),true);
        g.setColor(c);
        x += bs*2+b*2;
        y += b*3;
        g.fillRect(x,y,b*6,b*2);
        g.fillRect(x+b*2,y-b*2,b*2,b*6);
        
        x += bs*2;
        y -= b*3;
        Gfx.drawBlock(g,x-b*4,y,new Block(5,false,false),true);
        Gfx.drawBlock(g,x+b*5,y,new Block(5,false,false),true);
        
        x += bs*2+b*2;
        g.setColor(c);
        g.fillRect(x, y+b,b*6,b*2);
        g.fillRect(x, y+b*4,b*6,b*2);
        
        x += bs*2;
        Gfx.drawBlock(g,x-b*4,y,new Block(5,false,false),true);
        Gfx.drawBlock(g,x+b*5,y,new Block(5,false,false),true);
        Gfx.drawBlock(g,x+b*14,y,new Block(5,false,false),true);
    }
    
    public static void drawIntroScreen(Graphics g){
        //Random r = new Random();
        Color[] c = Gfx.blockColors(1);
        
        int oX = Blox.FRAME_WIDTH/2-Blox.BLOCK_SIZE*6-Blox.PIXEL_SIZE*4;
        int oY = Blox.FRAME_HEIGHT/10;
        
        // Piirretään teksti
        
        g.setColor(c[2]);
        // b
        g.fillRect(oX,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*5);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX,oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE);
        g.fillRect(oX,oY+Blox.BLOCK_SIZE*4,Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE);
        
        // l
        oX = oX+Blox.BLOCK_SIZE*4;
        g.fillRect(oX,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*5);
        
        // ö
        oX = oX+Blox.BLOCK_SIZE*2;
        g.fillRect(oX,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2,oY,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        
        g.fillRect(oX,oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX+Blox.BLOCK_SIZE*2,oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE*3);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE*4,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
        
        // x
        oX = oX+Blox.BLOCK_SIZE*4;
        oY = oY+Blox.BLOCK_SIZE*2;
        g.fillRect(oX, oY, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE, oY+Blox.BLOCK_SIZE, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX, oY+Blox.BLOCK_SIZE*2, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        g.fillRect(oX+Blox.BLOCK_SIZE*2, oY+Blox.BLOCK_SIZE*2, Blox.BLOCK_SIZE, Blox.BLOCK_SIZE);
        
        // Vilkkuva Enter-palikka
        if (Blox.titleScreenCounter > Blox.TITLE_SCREEN_COUNTER_THRESHOLD){
            Color[] cc = Gfx.blockColors(101);
            oX = Blox.FRAME_WIDTH/2-Blox.BLOCK_SIZE*2;
            oY = Blox.FRAME_HEIGHT-Blox.BLOCK_SIZE*8;
            g.setColor(cc[4]);
            g.fillRect(oX+Blox.PIXEL_SIZE*2,oY+Blox.PIXEL_SIZE*2,Blox.BLOCK_SIZE*4,Blox.BLOCK_SIZE);
            g.fillRect(oX+Blox.BLOCK_SIZE*3+Blox.PIXEL_SIZE*2,oY-Blox.BLOCK_SIZE+Blox.PIXEL_SIZE*2,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
            g.setColor(cc[3]);
            g.fillRect(oX,oY,Blox.BLOCK_SIZE*4,Blox.BLOCK_SIZE);
            g.fillRect(oX+Blox.BLOCK_SIZE*3,oY-Blox.BLOCK_SIZE,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE);
            
            
        }
        
        // Tuplaukset
        oX = Blox.FRAME_WIDTH/2-Blox.BLOCK_SIZE*6-Blox.PIXEL_SIZE*4;
        oY = Blox.FRAME_HEIGHT/10;
        g.setColor(c[4]);
        int b = Blox.PIXEL_SIZE;
        // b
        g.fillRect(oX+b,oY+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE*5-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE*2+b, oY+Blox.BLOCK_SIZE*2+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE*3-b*2);
        g.fillRect(oX+b*2,oY+Blox.BLOCK_SIZE*2+b,Blox.BLOCK_SIZE*2-b,Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+b*2,oY+Blox.BLOCK_SIZE*4+b,Blox.BLOCK_SIZE*2-b,Blox.BLOCK_SIZE-b*2);
        
        // l
        oX = oX+Blox.BLOCK_SIZE*4;
        g.fillRect(oX+b,oY+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE*5-b*2);
        
        // ö
        oX = oX+Blox.BLOCK_SIZE*2;
        g.fillRect(oX+b,oY+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE*2+b,oY+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE-b*2);
        
        g.fillRect(oX+b,oY+Blox.BLOCK_SIZE*2+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE*3-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE*2+b,oY+Blox.BLOCK_SIZE*2+b,Blox.BLOCK_SIZE-b*2,Blox.BLOCK_SIZE*3-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE-b, oY+Blox.BLOCK_SIZE*2+b,Blox.BLOCK_SIZE+b*2,Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE-b, oY+Blox.BLOCK_SIZE*2+b,Blox.BLOCK_SIZE,Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE-b, oY+Blox.BLOCK_SIZE*4+b,Blox.BLOCK_SIZE+b*2,Blox.BLOCK_SIZE-b*2);
        
        // x
        oX = oX+Blox.BLOCK_SIZE*4;
        oY = oY+Blox.BLOCK_SIZE*2;
        g.fillRect(oX+b, oY+b, Blox.BLOCK_SIZE-b*2, Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE*2+b, oY+b, Blox.BLOCK_SIZE-b*2, Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE+b, oY+Blox.BLOCK_SIZE+b, Blox.BLOCK_SIZE-b*2, Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+b, oY+Blox.BLOCK_SIZE*2+b, Blox.BLOCK_SIZE-b*2, Blox.BLOCK_SIZE-b*2);
        g.fillRect(oX+Blox.BLOCK_SIZE*2+b, oY+Blox.BLOCK_SIZE*2+b, Blox.BLOCK_SIZE-b*2, Blox.BLOCK_SIZE-b*2);
        
        
    }
    
    public static Color colorPalette(int colNum){
        // Pelin erinäisiin piirtotoimintoihin käytettäviä värejä
        Color c;
        
        switch (colNum) {
            // Scorejen keltainen
            case 1: c = new Color(255,255,127);
                    break;
            // Defaultti
            default:    c = new Color(255,255,255);
                        break;
        }
        
        return c;
    }
    
    public static Color[] blockColors(int colNum){
        // Palautetaan viisi kirkkausastetta annetusta parametriväristä:
        // 1 = violetti
        // 2 = turkoosi
        // 3 = sininen
        // 4 = keltainen
        
        // 100 = tuhoajapalikka
        Color c1; Color c2; Color c3; Color c4; Color c5;
        
        switch (colNum) {
            // Violetti
            /*case 1: c1 = new Color(161,158,196);
                    c2 = new Color(142,137,183);
                    c3 = new Color(125,119,174);
                    c4 = new Color(92,87,155);
                    c5 = new Color(46,43,77);
                    break;*/
            case 1: c1 = new Color(196,185,224);
                    c2 = new Color(153,126,200);
                    c3 = new Color(126,74,192);
                    c4 = new Color(88,43,160);
                    c5 = new Color(47,32,77);
                    break;
                    
            // Turkoosi/vihreä
            /*case 2: c1 = new Color(153,212,192);
                    c2 = new Color(96,194,174);
                    c3 = new Color(68,173,166);
                    c4 = new Color(0,123,133);
                    c5 = new Color(0,61,66);
                    break;*/
            case 2: c1 = new Color(125,212,156);
                    c2 = new Color(58,195,119);
                    c3 = new Color(33,169,110);
                    c4 = new Color(0,107,72);
                    c5 = new Color(0,39,27);
                    break;
                    
            // Sininen
            /*case 3: c1 = new Color(225,244,253);
                    c2 = new Color(171,223,249);
                    c3 = new Color(116,206,225);
                    c4 = new Color(106,154,193);
                    c5 = new Color(53,77,96);
                    break;*/
            case 3: c1 = new Color(216,222,251);
                    c2 = new Color(150,175,235);
                    c3 = new Color(105,151,215);
                    c4 = new Color(80,113,204);
                    c5 = new Color(43,60,141);
                    break;
            
                    
            // Keltainen
            case 4: c1 = new Color(255,242,0);
                    c2 = new Color(255,203,5);
                    c3 = new Color(253,184,19);
                    c4 = new Color(247,147,40);
                    c5 = new Color(133,73,20);
                    break;
                    
            // Pinkki 
            // PLACEHOLDER, SÄÄDÄ
            /*case 5: c1 = new Color(255,220,242);
                    c2 = new Color(242,180,203);
                    c3 = new Color(203,140,184);
                    c4 = new Color(184,96,147);
                    c5 = new Color(92,48,73);
                    break;*/
            case 5: c1 = new Color(255,224,242);
                    c2 = new Color(243,183,199);
                    c3 = new Color(212,129,180);
                    c4 = new Color(193,76,133);
                    c5 = new Color(82,32,54);
                    break;
                    
            // Jännittävä violetti
            case 6: c1 = new Color(255,160,254);
                    c2 = new Color(242,117,240);
                    c3 = new Color(203,72,200);
                    c4 = new Color(184,44,163);
                    c5 = new Color(92,22,81);
                    break;
                    
            // Tuhoajapalikka
            /*case 100:   c1 = new Color(255,255,255);
                        c2 = new Color(223,223,223);
                        c3 = new Color(163,163,163);
                        c4 = new Color(127,127,127);
                        c5 = new Color(63,63,63);
                        break;*/
                   
            // Ekstralevelivärejä, jos joku vaikka pääsee näin pitkälle
            case 7: c1 = new Color(196,194,164);
                    c2 = new Color(170,167,115);
                    c3 = new Color(132,129,74);
                    c4 = new Color(85,83,54);
                    c5 = new Color(57,57,43);
                    break;
                        
            // Intropalikka
            case 100:
            case 101:   c1 = new Color(255,255,255);
                        c2 = new Color(255,255,223);
                        c3 = new Color(255,255,163);
                        c4 = new Color(255,255,127);
                        c5 = new Color(255,192,63);
                        break;
            
            // Harmaa defaulttina
            default:c1 = new Color(127,127,127);
                    c2 = new Color(96,96,96);
                    c3 = new Color(64,64,64);
                    c4 = new Color(48,48,48);
                    c5 = new Color(24,24,24);
                    break;
        }
        
        Color[] cc = {c1,c2,c3,c4,c5};
        return cc;
        
    }
    
}
