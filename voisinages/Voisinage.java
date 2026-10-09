package voisinages;
import tab.Cellule;

import java.util.Iterator;

public abstract class Voisinage implements Iterator<Cellule>, Iterable<Cellule> {

    int index;
    int[] coordonnes;
    int[] dimensions;


    public int[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public int[] getCoords() {
        return coordonnes;
    }

    public void setCoords(int[] coords) {
        this.coordonnes = coords;
    }
    public  abstract  void reset(Cellule cell) ;

}
