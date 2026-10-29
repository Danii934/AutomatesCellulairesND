package voisinages;
import java.util.Iterator;
import Exceptions.DimensionIncompatibleException;
import tab.Cellule;
import tab.Grille;
public class G4 extends Voisinage {
	

	int[][] deplacements = {{0,0},{0,-1},{0,1},{1,0},{-1,0}};
	public G4(){}
	public G4(Cellule c, Grille g) throws DimensionIncompatibleException {

		if(c.getCoords().length != 2)
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
		int len1 = dimensions[0];
		int len2 = dimensions[1];
		int[] newCoords = new int[2];
		if(index < deplacements.length)
		{
			while(index < deplacements.length )
			{
				//Ligne
				newCoords[0] = coordonnes[0] + deplacements[index][0];
				//Colonne
				newCoords[1] = coordonnes[1] + deplacements[index][1];


				// Calcul des nouvelles coordonnées
				// Verifier si on est pas hors du tableau 2D ( ligne et colonne )
				if( (newCoords[0] < 0 || newCoords[0] >= len1)  || (newCoords[1] < 0 || newCoords[1] >= len2))
					index++;
				else
					break;
			}
		}
		
		return (index <= deplacements.length - 1);
	}

	@Override
	public Cellule next() {
		int[] ind = new int[2];
		ind[0] = coordonnes[0] + deplacements[index][0];
		ind[1] = coordonnes[1] + deplacements[index][1];
		index++;
		return grille.get(ind, 0, 2);
	}

	public String toString()
	{
		return "G4";
	}

	public void reset(Cellule cell)
	{
		cellule = cell;
		index=0;
	}
}
