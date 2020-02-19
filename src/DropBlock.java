public class DropBlock {
    public Block[] blocks;
    public int x;
    public int y;
    public int[] dropCounter;
    
    public DropBlock(int x, Block[] blocks, int[] dropCounter){
        this.x = x;
        this.blocks = blocks;
        this.dropCounter = dropCounter;
    }
        
    // Siirret‰‰n pudottamatonta dropBlockia vasemmalle tai oikealle
    public void move(int d){
        if (d == 1) {
           if (x < 9) {x += 1;} 
        }
        
        if (d == -1) {
           if (x > 0) {x -= 1;}
        }
    }
    
    public void rotate(){
        // Kierret‰‰n palikoita ylˆsp‰in
        Block b = new Block(blocks[0].color,blocks[0].destroyer,blocks[0].petrifiable);
        blocks[0] = new Block(blocks[1].color,blocks[1].destroyer,blocks[1].petrifiable);
        blocks[1] = new Block(blocks[2].color,blocks[2].destroyer,blocks[2].petrifiable);
        blocks[2] = new Block(b.color,b.destroyer,b.petrifiable);
    }
    
    public void decreaseCounter(){
        if (dropCounter[0] > 0) {dropCounter[0] -= 1;}
        else if (dropCounter[1] > 0) {dropCounter[1] -= 1;}
        else if (dropCounter[2] > 0) {dropCounter[2] -= 1;}
        else if (dropCounter[3] > 0) {dropCounter[3] -= 1;}
    }
}
