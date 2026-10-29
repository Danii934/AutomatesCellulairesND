package voisinages;
import java.util.Iterator;

import Exceptions.DimensionIncompatibleException;
import tab.Cellule;
import tab.Grille;
public class G6 extends Voisinage {
	
	int[][] deplacements = {{0,0,0},{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};

	public G6(){}

	public G6(Cellule c, Grille g) throws DimensionIncompatibleException {
		if(c.getCoords().length != 3)
			throw new DimensionIncompatibleException();
		this.cellule = c;
		grille = g;
		index = 0;
		coordonnes = c.getCoords();
		dimensions = g.getDimensions();
	}
	@Override
	public Iterator<Cellule> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		while (index < deplacements.length) {
			boolean dedans = true;
			for (int k = 0; k < 3; k++) {
				int x = coordonnes[k] + deplacements[index][k];
				if (x < 0 || x >= dimensions[k]) {
					dedans = false;
					break;
				}
			}
			if (dedans) return true;
			index++;
		}
		return false;

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
		return "G6";
	}
	
	public void reset(Cellule cell)
	{
		cellule = cell;
		index=0;
	}
}
