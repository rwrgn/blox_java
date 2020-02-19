/**
 * Point-luokan tilalle itsetehty int-arvoilla toimvia Pnt-luokka
 * Yksinkertaisesti vain x- ja y-koordinaattien säilömiseen ja käyttöön.
 * Sekä x että y on public, joten gettereitä ja settereitä ei tarvita,
 * "Näin koodi lyhenee ja selkeytyy kivasti."
 */
public class Pnt {
    public int x;
    public int y;
    
    public Pnt(int x, int y){
        this.x = x;
        this.y = y;
    }
}
