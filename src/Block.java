public class Block {
    
    // x- ja y-muuttujia ei ole, sillä palikoiden ei tarvitse tietää omia
    // koordinaattejaan, ehkä.
    
    public static final int COLOR_MAX = 5;
    public static final int DESTRUCTION_COUNTER_MAX = 16;
    public int color;
    public boolean destroyer;
    public boolean toBeDestroyed;
    public boolean destructionInProgress;
    public int destructionCounter;
    
    // Tuhoajalle
    public static final int DESTROYER_BLINK_COUNTER_MAX = 16;
    public int destroyerBlinkCounter;
    
    // Kivettymiseen
    public static int PETRIFICATION_COUNTER_MAX = 200;
    public int petrificationCounter;
    public boolean petrified;
    public boolean petrifiable;
    
    public Block(){
        
    }
    
    /**
     * @param color: palikan väri; positiiviset luvut ovat tavallisia palikoita,
     * negatiiviset luvut antipalikoita
     * @param destroyer: true, jos kysymyksessä universaali tuhoajapalikka
     * @param petrifiable: true, jos ollaan vaiheessa, jossa palikat kivettyvät
     */
    
    public Block(int color, boolean destroyer, boolean petrifiable){
        this.color = color;
        this.destroyer = destroyer;
        if (destroyer) {this.color = 100;}
        this.petrifiable = petrifiable;
        toBeDestroyed = false;
        destructionCounter = DESTRUCTION_COUNTER_MAX;
        destructionInProgress = false;
        petrificationCounter = PETRIFICATION_COUNTER_MAX;
        petrified = false;
        destroyerBlinkCounter = DESTROYER_BLINK_COUNTER_MAX;
    }
    
    public void cycleColor(){
        if (color > 0 && !petrified){
            color += 1;
            if (color > COLOR_MAX) {color -= COLOR_MAX;}
        }
        if (color < 0 && !petrified)
            color -= 1;
            if (color < -COLOR_MAX) {color += COLOR_MAX;}
    }
    
    public void destroyBlock(){
        // Frame framelta kohti tuhoa.
        if (destructionCounter >= 0) {destructionCounter -= 1;}
    }
    
    public void petrifyBlock(){
        // Askel askeleelta kohti kivettymistä
        if (!petrified && petrifiable){
            if (petrificationCounter > 0) {petrificationCounter -= 1;}
            else {petrified = true;}
        }
    }
    
    public void destroyerBlink(){
        // Tuhoajapalikan vilkutus
        if (destroyerBlinkCounter > 0) {destroyerBlinkCounter -= 1;}
        else {destroyerBlinkCounter = DESTROYER_BLINK_COUNTER_MAX;}
    }
    
}
