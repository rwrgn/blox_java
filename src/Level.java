import java.util.*;

public class Level {
    
    public boolean gameOver;
    public Block[][] blocks;
    public int bpm;
    public int phase;
    public DropBlock db;
    public boolean destructionInProgress;
    
    // Dropblockin x-koordinaatti
    public int dbx;
    public int dropCounterMax;
    // breatheCounter on tapahtumien v‰liss‰ tapahtuva hengityspaussi
    public int breatheCounter;
    
    // Vaikeutuselementit:
    public boolean colorCycling;
    public boolean petrification;
    public boolean surfaceRising;
    public static int PETRIFICATION_COUNTER_MAX = 400;
    public int petrificationCounter;
    
    public int colorCycleCounter;
    public static int COLOR_CYCLE_COUNTER_MAX = 240;
    public int surfaceRiseCounter;
    public static int SURFACE_RISE_COUNTER_MAX = 1000;
    
    // Pisteet
    public int score;
    public ArrayList<Integer> scoreLimit;
    public static final int POINT_VALUE = 1;
    
    public Level(){
        gameOver = false;
        bpm = 120;
        phase = 0;
        blocks = new Block[10][20];
        destructionInProgress = false;
        dbx = 5;
        score = 0;
        dropCounterMax = 60;
        breatheCounter = 0;
        
        colorCycling = false;
        petrification = false;
        surfaceRising = false;
        
        scoreLimit = new ArrayList<>();
        scoreLimit.add(50);
        
        surfaceRiseCounter = SURFACE_RISE_COUNTER_MAX;
        colorCycleCounter = COLOR_CYCLE_COUNTER_MAX;
        
        petrificationCounter = PETRIFICATION_COUNTER_MAX;
    }
       
    public void nextPhase(){
        // Siirryt‰‰n seuraavaan vaiheeseen
        score = 0;
        scoreLimit.add((int)((scoreLimit.get(phase))*1.2));
        
        phase += 1;
        
        // Phasekohtaiset vaikeutuselementit:
        if (phase == 1) {
            colorCycling = true;
        }
        if (phase == 2) {
            petrification = true;
        }
        if (phase == 3) {
            dropCounterMax = 50;
        }
        if (phase == 4) {
            surfaceRising = true;
            PETRIFICATION_COUNTER_MAX -= 100;
        }
        if (phase == 5) {
            dropCounterMax = 40;
        }
        if (phase == 6) {
            dropCounterMax = 36;
            PETRIFICATION_COUNTER_MAX -= 100;
        }
        if (phase == 7) {
            dropCounterMax = 32;
        }
        if (phase > 7) {
            if (dropCounterMax > 16) {dropCounterMax -= 2;}
            if (PETRIFICATION_COUNTER_MAX > 60) {PETRIFICATION_COUNTER_MAX -= 10;}
        }
    }
    
    public void cycleColors(){
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++){
                if (blocks[i][j] != null && !blocks[i][j].toBeDestroyed){
                    blocks[i][j].cycleColor();
                }
            }
        }
    }
    
    public void dropDropBlock(DropBlock db){
        // Tarkistetaan, miss‰ kohdassa palikka tˆrm‰‰ jo laudalla oleviin
        // palikoihin.
        int dropY = blocks[0].length - 1;
        int dropX = db.x;
        boolean foundStop = false;
        
        for (int i = 0; i < blocks[0].length; i++){
            if (foundStop == false){
                if (blocks[dropX][i] != null){
                    dropY = i-1;
                    foundStop = true;
                }
            }
        }
        
        // Luodaan pudotettavaa palikkaa vastaavat palikat laudalle
        for (int i = 0; i < db.blocks.length; i++){
            // Tarkistetaan, mahtuuko palikka
            if (dropY-i > -1){
                blocks[dropX][dropY-i] = 
                    new Block(db.blocks[db.blocks.length-i-1].color,
                              db.blocks[db.blocks.length-i-1].destroyer,
                              db.blocks[db.blocks.length-i-1].petrifiable);
            }
            else {
                // Jos ei mahdu, gameOver.
                gameOver = true; break;
            }
        }
        
        // Nollataan k‰sitelty dropBlock
        // db = null;
    }
    
    public void checkIfDestructionInProgress(){
        // Tarkistaa, onko tuhoanimaatio menossa ja muuttaa levelin
        // destructionInProgress-booleanin vastaavasti.
        boolean dip = false;
        
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++){
                if (blocks[i][j] != null && blocks[i][j].toBeDestroyed){
                    dip = true;
                }
            }
        }
        
        destructionInProgress = dip;
    }
    
    public void checkAndInfectSeedBlocks(){
        // Tarkistaa koko laudan palikat, ja katsoo, onko palikoilla ja anti-
        // palikoilla vieress‰‰n tuhon aiheuttavia palikoita (eli vastaavia
        // antipalikoita tai palikoita)
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++){
                // Ensin varmistetaan, ett‰ palikka ei ole tyhj‰ ja ett‰ sit‰
                // ei ole jo merkattu tuhottavaksi.
                if (blocks[i][j] != null && !blocks[i][j].toBeDestroyed &&
                    !blocks[i][j].petrified){
                    // Katsotaan ensin, onko kysymyksess‰ tuhoajapalikka
                    if (blocks[i][j].destroyer){
                        destroyerInfectBlocks(i,j,0);
                    }
                    
                    // Sitten tarkistetaan naapurit ja infektoidaan t‰m‰
                    // palikka, mik‰li aiheellista. Pitk‰ or-ketju.
                    else{
                    
                        if ((i-1 >= 0 && blocks[i-1][j] != null && 
                            (blocks[i-1][j].color == -blocks[i][j].color) &&
                             !blocks[i-1][j].petrified) ||
                            (i+1 <= 9 && blocks[i+1][j] != null &&
                            (blocks[i+1][j].color == -blocks[i][j].color) &&
                             !blocks[i+1][j].petrified) ||
                            (j-1 >= 0 && blocks[i][j-1] != null && 
                            (blocks[i][j-1].color == -blocks[i][j].color) &&
                             !blocks[i][j-1].petrified) ||
                            (j+1 <= 19 && blocks[i][j+1] != null &&
                            (blocks[i][j+1].color == -blocks[i][j].color) &&
                             !blocks[i][j+1].petrified)
                                ) {
                            infectBlocks(i,j);
                        }
                    }
                }
            }
        }
    }
    
    public void destroyerInfectBlocks(int x, int y, int dir){
        // Tuhoajapalikka tuhoaa kaikki sen kanssa samalla jatkuvalla x- ja y-
        // palikka-akselilla olevat palikat.
        // dir on suunta:
        // 4 = vasen
        // 8 = ylˆs
        // 6 = oikea
        // 2 = alas
        if (blocks[x][y].destroyer){
            blocks[x][y].toBeDestroyed = true;
            
            if (x-1 >= 0 && blocks[x-1][y] != null && !blocks[x-1][y].destroyer) {destroyerInfectBlocks(x-1,y,4);}
            if (x+1 <= 9 && blocks[x+1][y] != null && !blocks[x+1][y].destroyer) {destroyerInfectBlocks(x+1,y,6);}
            if (y-1 >= 0 && blocks[x][y-1] != null && !blocks[x][y-1].destroyer) {destroyerInfectBlocks(x,y-1,8);}
            if (y+1 <= 19 && blocks[x][y+1] != null && !blocks[x][y+1].destroyer) {destroyerInfectBlocks(x,y+1,2);}
        }
        else {
            blocks[x][y].toBeDestroyed = true;
            
            if (dir == 4) {
                if (x-1 >= 0 && blocks[x-1][y] != null && !blocks[x-1][y].destroyer) {destroyerInfectBlocks(x-1,y,4);}
            }
            else if (dir == 6){
                if (x+1 <= 9 && blocks[x+1][y] != null && !blocks[x+1][y].destroyer) {destroyerInfectBlocks(x+1,y,6);}
            }
            else if (dir == 8){
                if (y-1 >= 0 && blocks[x][y-1] != null && !blocks[x][y-1].destroyer) {destroyerInfectBlocks(x,y-1,8);}
            }
            else{
                if (y+1 <= 19 && blocks[x][y+1] != null && !blocks[x][y+1].destroyer) {destroyerInfectBlocks(x,y+1,2);}
            }
        }
        
    }
    
    public void infectBlocks(int x, int y){
        
        // Nyt toimii, tosiaan oli fiksua huomioida, ett‰ antipalikoiden v‰riarvohan
        // on negatiivinen luku, joten vertailu abs(vieripalikka.c) == this.color
        // ei tietenk‰‰n palauta true, jos jompikumpi palikoista on antipalikka
        
        // Tuhoaja tuhoaa kaikki viereiset ja saa ne edelleen tartuttaviksi
        if (blocks[x][y].destroyer){
            if (blocks[x][y].toBeDestroyed == false){
                blocks[x][y].toBeDestroyed = true;
                
                if (x-1 >= 0 && blocks[x-1][y] != null &&
                    blocks[x-1][y].toBeDestroyed == false &&
                    !blocks[x-1][y].petrified){
                    infectBlocks(x-1,y);
                }
                
                if (x+1 <= 9 && blocks[x+1][y] != null &&
                    blocks[x+1][y].toBeDestroyed == false &&
                    !blocks[x+1][y].petrified){
                    infectBlocks(x+1,y);
                }
                
                if (y-1 >= 0 && blocks[x][y-1] != null &&
                    blocks[x][y-1].toBeDestroyed == false &&
                    !blocks[x][y-1].petrified){
                    infectBlocks(x,y-1);
                }
                
                if (y+1 <= 19 && blocks[x][y+1] != null &&
                    blocks[x][y+1].toBeDestroyed == false &&
                    !blocks[x][y+1].petrified){
                    infectBlocks(x,y+1);
                }
                
            }
            
        }
        
        // Anti- ja tavalliset palikat
        else{
            if (blocks[x][y].toBeDestroyed == false){
                
                blocks[x][y].toBeDestroyed = true;
                
                if (x-1 >= 0 && blocks[x-1][y] != null &&
                    blocks[x-1][y].toBeDestroyed == false &&
                    !blocks[x-1][y].petrified &&
                    Math.abs(blocks[x-1][y].color) == Math.abs(blocks[x][y].color)){
                    infectBlocks(x-1,y);
                }
                
                if (x+1 <= 9 && blocks[x+1][y] != null &&
                    blocks[x+1][y].toBeDestroyed == false &&
                    !blocks[x+1][y].petrified &&
                    Math.abs(blocks[x+1][y].color) == Math.abs(blocks[x][y].color)){
                    infectBlocks(x+1,y);
                }
                
                if (y-1 >= 0 && blocks[x][y-1] != null &&
                    blocks[x][y-1].toBeDestroyed == false &&
                    !blocks[x][y-1].petrified &&
                    Math.abs(blocks[x][y-1].color) == Math.abs(blocks[x][y].color)){
                    infectBlocks(x,y-1);
                }
                
                if (y+1 <= 19 && blocks[x][y+1] != null &&
                    blocks[x][y+1].toBeDestroyed == false &&
                    !blocks[x][y+1].petrified &&
                    Math.abs(blocks[x][y+1].color) == Math.abs(blocks[x][y].color)){
                    infectBlocks(x,y+1);
                }
                
            }
        }
            
    }
    
    public boolean destroyBlocks(){
        // Tuhotaan tuhottavaksi merkityt palikat
        // ja kirjataan tuhottujen palikoiden koordinaatit Pnt-ArrayListiin.
        // Palauttaa booleanina, tuliko yht‰‰n palikkaa tuhottua
        ArrayList<Pnt> c = new ArrayList<>();
        boolean anyDestroyed = false;
        
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++){
                if (blocks[i][j] != null && blocks[i][j].toBeDestroyed){
                    if (blocks[i][j].destructionCounter >= 0){
                        blocks[i][j].destroyBlock();
                    }
                    
                    else{
                        blocks[i][j] = null;
                        c.add(new Pnt(i,j));
                        anyDestroyed = true;
                        // Annetaan pisteit‰ tuhotusta palikasta.
                        score += POINT_VALUE;
                        
                        if (score > scoreLimit.get(phase)) {nextPhase();}
                    }
                }
            }
        }
        
        // Heitet‰‰n tuhottujen palikoiden koordinaatit sis‰lt‰v‰ ArrayList
        // metodille, joka siirt‰‰ tuhottujen yl‰puolella olevia palikoita
        // alasp‰in.
        
        if (anyDestroyed) {moveBlocksDown(c);}
        return anyDestroyed;
    }
    
    public void moveBlocksDown(ArrayList<Pnt> c){
        // Palikoiden tuhoamisen j‰lkeen siirt‰‰ yl‰puolella olevia palikoita
        // alasp‰in tyhj‰ksi j‰‰neisiin kohtiin.
        for (Pnt p : c){
            for (int i = p.y; i >= 0; i--){
                if (blocks[p.x][i] == null && i-1 >= 0){
                    if (blocks[p.x][i - 1] != null){
                        blocks[p.x][i] = new Block(blocks[p.x][i-1].color,false,petrification);
                        blocks[p.x][i].petrified = blocks[p.x][i-1].petrified;
                        blocks[p.x][i].petrificationCounter = blocks[p.x][i-1].petrificationCounter;
                        blocks[p.x][i - 1] = null;
                    }
                    
                    else{
                        blocks[p.x][i] = null;
                    }
                }
            }
        }
        
    }
    
    public void fillWithRandomBlocks(){
        Random r = new Random();
        
        for (int i = 0; i < 20; i++){
            blocks[r.nextInt(10)][r.nextInt(20)] = new Block(r.nextInt(10)-5,false,petrification);
        }
    }
    
    public void riseSurface(){
        int[] bottomRowBlockColors = {100,100,100,100,100,100,100,100,100,100};
        for (int i = 0; i < blocks.length; i++){
            if (blocks[i][19] != null) {
                bottomRowBlockColors[i] = blocks[i][19].color;
            }
        }
        
        // Nostetaan kaikkia nykyisi‰ blokkeja yksi ylˆsp‰in.
        for (int i = 0; i < blocks.length; i++){
            for (int j = 1; j < blocks[0].length; j++){
                if (blocks[i][j] != null){
                    blocks[i][j-1] = new Block(blocks[i][j].color,false,petrification);
                    // "Perit‰‰n" kivettyminen edelliselt‰
                    blocks[i][j-1].petrificationCounter = blocks[i][j].petrificationCounter;
                    blocks[i][j-1].petrified = blocks[i][j].petrified;
                    blocks[i][j] = null;
                }  
            }
        }
        
        // Luodaan uusi satunnaisrivi pohjalle.
        Random r = new Random();
        for (int i = 0; i < blocks.length; i++){
            int rc = r.nextInt(4) + 1;
            
            while (rc*-1 == bottomRowBlockColors[i]){
                rc = r.nextInt(4) + 1;
            }
            blocks[i][19] = new Block(rc,false,petrification);
        }
    }
    
    public DropBlock createNewDropBlock(boolean allowSpecialBlocks){
        /**
         * @param allowSpecialBlocks: voiko luotava olla antipalikka tai tuhoaja
         * 
         * Tehty niin, ett‰ dropBlockissa ei voi olla itsens‰ tuhoavia elementtej‰
         * eli palikoita ja niiden samanv‰risi‰ antipalikoita samassa.
         * 
         */
        // Randomisoidaan uusi DropBlock
        Random rnd = new Random();
        Block[] b = new Block[3];
        // Edellisten arvottujen v‰rit
        int[] prevBlocks = {-100,-100,-100};
        
        for (int i = 0; i < 3; i++){
            // Antipalikoilla on todenn‰kˆisyys 15/48, tavallisilla 2/3,
            // tuhoajapalikoilla 1/64
            int antiOrNormal = rnd.nextInt(55);
            int r = -100;
            if (antiOrNormal == 0 && allowSpecialBlocks){
                // Tuhoaja
                b[i] = new Block(0, true, petrification);
            }
            else if (antiOrNormal > 39 && allowSpecialBlocks) {
                // Antipalikka
                r = (rnd.nextInt(Block.COLOR_MAX) - Block.COLOR_MAX);
                
                if (i > 0) {
                    for (int k = 0; k < i; k++){
                        while (prevBlocks[k]*-1 == r){
                            r = (rnd.nextInt(Block.COLOR_MAX) - Block.COLOR_MAX);
                        }
                    }
                }
                
                b[i] = new Block(r, false, petrification);
            }
            
            else{
                // Tavallinen
                r = (rnd.nextInt(Block.COLOR_MAX) + 1);
                
                if (i > 0) {
                    for (int k = 0; k < i; k++){
                        while (prevBlocks[k]*-1 == r){
                            r = (rnd.nextInt(Block.COLOR_MAX) + 1);
                        }
                    }
                }
                
                b[i] = new Block(r, false, petrification);
            }
            
            prevBlocks[i] = r;
               
        }
        return new DropBlock(dbx,b, new int[]
                            {dropCounterMax,dropCounterMax,dropCounterMax,dropCounterMax});
    }
    
    public int surfaceYAtX(int x){
        // Palauttaa pelilaudan x-koordinaatissa olevan pinnankorkeuden.
        int YAtX = 19;
        for (int i = 0; i < 20; i++){
            if (blocks[x][i] != null){
                YAtX = i-1;
                break;
            }
        }
        return YAtX;
    }
    
    public ArrayList<Pnt> blockCoords(){
        // Palauttaa koordinaatit, joissa on muuta kuin null.
        ArrayList<Pnt> p = new ArrayList<>();
        
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++)
                if (blocks[i][j] != null) {
                    p.add(new Pnt(i,j));
                }
        }
        
        return p;
    }
    
    // Vaikeutuselementtien tarkistus ja aktivointi joka framella.
    public void doPhaseEvents(){
        
        if (db != null){
            if (db.blocks[0].destroyer){db.blocks[0].destroyerBlink();}
            if (db.blocks[1].destroyer){db.blocks[1].destroyerBlink();}
            if (db.blocks[2].destroyer){db.blocks[2].destroyerBlink();}
        }
        
        if (colorCycling){
            
            if (colorCycleCounter <= 0) {
                cycleColors();   
                colorCycleCounter = COLOR_CYCLE_COUNTER_MAX;
            }
            
            // Laskuri
            else {colorCycleCounter -= 1;}
        
        }
        
        // Kivetys 
        if (petrification){
            
            if (petrificationCounter <= 0){
                boolean onePetrified = false;
                ArrayList<Pnt> pp = blockCoords();
                if (pp != null && !pp.isEmpty()){
                    Collections.shuffle(pp);
                    for (int i = 0; i < pp.size(); i++){
                        Pnt j = pp.get(i);
                        if (!blocks[j.x][j.y].petrified && !onePetrified){
                            blocks[j.x][j.y].petrifyBlock();
                            if (blocks[j.x][j.y].petrified){
                                petrificationCounter = PETRIFICATION_COUNTER_MAX;
                                onePetrified = true;
                                break;
                            }
                        }
                    }
                }
                
            }
            
            else {petrificationCounter -= 1;}
        }
        
        if (surfaceRising){
            if (surfaceRiseCounter <= 0) {
                riseSurface();
                surfaceRiseCounter = SURFACE_RISE_COUNTER_MAX;
            }
            
            // Laskuri
            else {surfaceRiseCounter -= 1;}
        }
        
    }
    
    public void decreaseBreatheCounter(){
        breatheCounter -= 1;
    }
    
    // Debuggaukseen:
    public void printLevel(){
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++){
                if (blocks[i][j] != null){
                    System.out.println("Palikka: " + i + "," + j + ":");
                    System.out.println("V‰ri:" + blocks[i][j].color);
                }
                else {
                    System.out.println("Palikka:" + i + "," + j + ": tyhj‰");
                }
            }
        }
    }
    
}
