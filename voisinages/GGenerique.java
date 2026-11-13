package voisinages;

import Exceptions.DimensionIncompatibleException;
import tab.Cellule;
import tab.Grille;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    /**
     * Voisinage « en croix » : un pas le long de chaque axe (dit de von Neumann).
     * G2 en 1D, G4 en 2D, G6 en 3D.
     * @param d           dimension de la grille
     * @param avecCentre  true pour G2/G4/G6, false pour les variantes G2* G4* G6*
     */
    public static int[][] voisinsEnCroix(int d, boolean avecCentre) {
        List<int[]> res = new ArrayList<>();
        if (avecCentre) res.add(new int[d]);
        for (int k = 0; k < d; k++)
            for (int s = -1; s <= 1; s += 2) {
                int[] o = new int[d];
                o[k] = s;
                res.add(o);
            }
        return res.toArray(new int[0][]);
    }

    /**
     * Voisinage « en bloc » : tous les décalages de {-1,0,1}^d, qu'ils touchent
     * par une face, une arête ou un coin (dit de Moore). G8 en 2D, G26 en 3D.
     * @param d           dimension de la grille
     * @param avecCentre  true pour G8/G26, false pour les variantes G8* /G26*
     */
    public static int[][] voisinsEnBloc(int d, boolean avecCentre) {
        List<int[]> res = new ArrayList<>();
        int total = (int) Math.pow(3, d);
        for (int n = 0; n < total; n++) {
            int[] o = new int[d];
            int m = n;
            boolean nul = true;
            for (int k = 0; k < d; k++) {
                o[k] = (m % 3) - 1;
                m /= 3;
                if (o[k] != 0) nul = false;
            }
            if (nul && !avecCentre) continue;
            res.add(o);
        }
        return res.toArray(new int[0][]);
    }
}
