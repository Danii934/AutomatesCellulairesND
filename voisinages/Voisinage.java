package voisinages;
import tab.Cellule;
import tab.Grille;

import java.util.Iterator;

public abstract class Voisinage implements Iterator<Cellule>, Iterable<Cellule> {

    int index;
    int[] coordonnes;
    int[] dimensions;
    Grille grille;
    Cellule cellule;

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

    public Grille getGrille() {
        return grille;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }

    public void bind(Grille g, Cellule c) {
        this.grille = g;
        this.dimensions = g.getDimensions();
        this.cellule = c;
        this.coordonnes = c.getCoords();
        this.index = 0;
    }
    public void rewind() { index = 0; }
}
