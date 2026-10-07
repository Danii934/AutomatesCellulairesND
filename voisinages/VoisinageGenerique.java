package voisinages;

import Exceptions.DimensionIncompatibleException;
import tab.Cellule;
import tab.Grille;

import java.util.Iterator;

public class VoisinageGenerique extends Voisinage {
    Grille grille;
    Cellule c;
    // deplacements un tableau 2D de la taille n x d ou d est la dimension de la grille
    int[][] deplacements;

    public VoisinageGenerique(Grille g, Cellule c, int[][] deplacements) throws DimensionIncompatibleException {
        if(g.getDimensions().length != deplacements[0].length || c.getCoords().length != g.getDimension() )
            throw new DimensionIncompatibleException();
        this.grille = g;
        this.c = c;
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
}
