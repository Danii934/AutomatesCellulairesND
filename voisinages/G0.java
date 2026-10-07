package voisinages;

import tab.Cellule;
import tab.Grille;

import java.util.Iterator;

// Vosinage comortnant la cellule elle même
public class G0 extends Voisinage {

	Grille grille;
	Cellule c;
	int index; // nécessaire pour parcourir les cellules voisines
	int[] coordonnes;
	public G0(){}
	public G0(Cellule c, Grille g)
	{
		this.c = c;
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
		return  false;
	}

	@Override
	public Cellule next() {
		return c;
	}
	public String toString()
	{
		return "G0";
	}

	// Revenir à un point donné
	public void reset(Cellule cell)
	{
		c = cell;
		index=0;
	}

}
