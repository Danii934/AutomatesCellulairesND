package voisinages;
import java.util.Iterator;
import tab.Cellule;
import tab.Grille;
// Vosinage 1D
public class G2 extends Voisinage {

	int[] deplacements = {0,1,-1}; // Emplacement des voisins

	public G2(){}
	public G2(Cellule c, Grille g)
	{
		this.cellule = c;
		grille = g;
		index = 0;
		coordonnes = c.getCoords();
		dimensions = g.getDimensions(); // Taille de la grille
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
				// Si on est en dehors de la grile
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
		// On se déplace vers la Case suivante : On calcule les nouvelles coordonnées a partir de la case courante
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
		cellule = cell;
		index=0;
	}

}
