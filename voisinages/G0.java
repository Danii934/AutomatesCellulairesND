package voisinages;

import tab.Cellule;
import tab.Grille;

import java.util.Iterator;

// Vosinage comportnant la cellule elle même
public class G0 extends Voisinage {

	public G0(){}
	public G0(Cellule c, Grille g)
	{
		this.cellule = c;
		grille = g;
		coordonnes = c.getCoords();
		index = 0;
		dimensions = g.getDimensions();
	}
	@Override
	public Iterator<Cellule> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		// 1 seul dimension
		return index < 1;
	}

	@Override
	public Cellule next() {
		index++;
		return cellule;
	}
	public String toString()
	{
		return "G0";
	}

	// Revenir à un point donné
	public void reset(Cellule cell)
	{
		cellule = cell;
		index=0;
	}

}
