/**
 * Point-luokan tilalle itsetehty int-arvoilla toimvia Pnt-luokka
 * Yksinkertaisesti vain x- ja y-koordinaattien s�il�miseen ja k�ytt��n.
 * Sek� x ett� y on public, joten gettereit� ja settereit� ei tarvita,
 * "N�in koodi lyhenee ja selkeytyy kivasti."
 */
public class Pnt {
    public int x;
    public int y;
    
    public Pnt(int x, int y){
        this.x = x;
        this.y = y;
    }
}
