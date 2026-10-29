package voisinages;

import Exceptions.DimensionIncompatibleException;
import tab.Cellule;
import tab.Grille;

import java.util.Iterator;

public class GGenerique extends Voisinage {


    // deplacements un tableau 2D de la taille n x d ou d est la dimension de la grille
    int[][] deplacements;

    public GGenerique(){}
    public GGenerique(Grille g, Cellule c, int[][] deplacements) throws DimensionIncompatibleException {
        for (int i = 0; i < deplacements.length; i++) {
            if(deplacements[0].length != g.getDimension())
            {
                throw new DimensionIncompatibleException();
            }
        }
        this.grille = g;
        this.cellule = c;
        this.deplacements = deplacements;
        index = 0;
        coordonnes = c.getCoords();
        dimensions = g.getDimensions();
    }

    @Override
    public Iterator<Cellule> iterator() {
        return this;
    }

    public boolean hasNext() {

        if(index < deplacements.length)
        {
            int[] newCoords = new int[grille.getDimension()];

            while(index < deplacements.length)
            {
                boolean correct = true;

                for (int i = 0; i < grille.getDimension(); i++)
                {
                    newCoords[i] = coordonnes[i] + deplacements[index][i];
                    if(newCoords[i] < 0 || newCoords[i] >= dimensions[i])
                    {
                        correct = false;
                        break;
                    }
                }
                if (correct)
                    break;

                index++;
            }
        }

        return (index <= deplacements.length -1);
    }

    public Cellule next() {
        int[] ind = new int[grille.getDimension()];
        for (int i = 0; i < grille.getDimension(); i++) {
            ind[i] = coordonnes[i] + deplacements[index][i];
        }
        index++;
        return grille.get(ind, 0, grille.getDimension());
    }
    public void reset(Cellule cell)
    {
        cellule = cell;
        index=0;
    }

    public int[][] getDeplacements() {
        return deplacements;
    }

    public void setDeplacements(int[][] deplacements) {
        this.deplacements = deplacements;
    }

    @Override
    public  String toString()
    {
        String s = "{";
        for (int i = 0; i < deplacements.length  ; i++) {
            s+="(";
            for (int j = 0; j < deplacements[i].length; j++) {
                s+=deplacements[i][j];
                if(j != deplacements[i].length -1)
                {
                    s+=",";
                }
            }
            if(i != deplacements.length -1)
            {
                s+="),";
            }
            else
            {
                s+=")";
            }

        }
        return s+="}";
    }
}
