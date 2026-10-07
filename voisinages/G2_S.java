package voisinages;

import tab.Cellule;
import tab.Grille;

import java.util.Iterator;

// Vosinage 1D
public class G2_S extends Voisinage {

	Grille grille;
	Cellule c;

	int[] deplacements = {1,-1};
	public G2_S(){}
	public G2_S(Cellule c, Grille g)
	{
		this.c = c;
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
		// 1 seul dimension
		int len = dimensions[0];
		if(index < deplacements.length)
		{
			while(index < deplacements.length)
			{
				if( (coordonnes[0] + deplacements[index] < 0 ) || (coordonnes[0] + deplacements[index] >= len) )
					index++;
				else
					break;
			}	
		}
		
		return (index <= deplacements.length -1);
	}

	@Override
	public Cellule next() {
		int[] ind = new int[1];
		// On se déplace vers la Case suivante
		ind[0] = coordonnes[0] + deplacements[index++];
		return grille.get(ind, 0, 1);
	}
	public String toString()
	{
		return "G2";
	}

	// Revenir à un point donné
	public void reset(Cellule cell)
	{
		c = cell;
		index=0;
	}

}
