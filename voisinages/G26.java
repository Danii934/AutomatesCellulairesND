package voisinages;
import java.util.Iterator;

import Exceptions.DimensionIncompatibleException;
import tab.Cellule;
import tab.Grille;
public class G26 extends Voisinage {
	
	Grille grille;
	Cellule c;
	int[][] deplacements;
	int index;
	int dim;
	int[] coordonnes;
	int[] dimensions;
	
	public G26(){}

	public G26(Cellule c, Grille g) throws DimensionIncompatibleException {
		if(c.getCoords().length != 3)
			throw new DimensionIncompatibleException();
		this.c = c;
		grille = g;
		index = 0;
		coordonnes = c.getCoords();
		dimensions = g.getDimensions();


        deplacements = new int[27][3];
        int pos = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                        deplacements[pos][0] = i;
                        deplacements[pos][1] = j;
                        deplacements[pos][2] = k;
                        pos++;
                }
            }
        }
	}
	@Override
	public Iterator<Cellule> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		int len1 = dimensions[0];
		int len2 = dimensions[1];
		int len3 = dimensions[2];
		int[] newCoords = new int[3];
		if(index < deplacements.length)
		{
			//X
			newCoords[0] = coordonnes[0] + deplacements[index][0];
			//Y
			newCoords[1] = coordonnes[1] + deplacements[index][1];
			// Z
			newCoords[2] = coordonnes[2] + deplacements[index][2];

			while(index < deplacements.length )
			{
				if(newCoords[0] < 0 || newCoords[0] >= len1 || newCoords[1] < 0 || newCoords[1] >= len2 || newCoords[2] < 0 || newCoords[2] >= len3)
					index++;
				else
					break;
			}	
		}
		
		return (index <= deplacements.length - 1);
	}

	@Override
	public Cellule next() {
		int[] ind = new int[3];
		ind[0] = coordonnes[0] + deplacements[index][0];
		ind[1] = coordonnes[1] + deplacements[index][1];
		ind[2] = coordonnes[2] + deplacements[index][2];
		index++;
		return grille.get(ind, 0, 3);
	}

	public String toString()
	{
		return "G26";
	}
	public void reset(Cellule cell)
	{
		c = cell;
		index=0;
	}
}
