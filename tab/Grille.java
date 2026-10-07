package tab;

public class Grille extends TableauDynamiqueND{
	TableauDynamiqueND[] tableau;
	int dim;
	private int[] dimensions;


	// @param dimensions : dimensions de la grille : 5 x 6 x 3 x 2 X 1 <=>
	// @param dim : Dimension de la grille ex : grille de dimension 5
	// tableau : grille de taille de la première dimension
	public Grille(int[] dimensions,int dim,int index,int[] coords)
	{
		this.dimensions = dimensions;
		this.dim = dim;

			// Initialisation du tableau de cellules dans la derniere dimension
			if(dim == 1)
			{
				tableau = new Cellule[dimensions[index]];
				for(int i = 0; i < dimensions[index]; i++ )
				{
					coords[index] = i;
					tableau[i] = new Cellule(coords);
				}
			}
			// Initialisation récursif
			else
			{
				tableau = new Grille[dimensions[index]];
				for(int i = 0; i < dimensions[index];i++)
				{ 
					coords[index] = i;
					tableau[i] = new Grille(dimensions,dim-1,index+1,coords);
				}
			}
		
		
	}
	public int getDimension()
	{
		return dim;
	}
	public void afficher()
	{
		
		afficher(dimensions,dim,0);
					
	}
	public void afficher(int[] dimensions,int dim,int index)
	{
		
		if(dim == 1)
		{

			for(int i = 0; i < dimensions[index];i++ )
			{
				tableau[i].afficher();
			}
			System.out.println();
		}
		else 
		{
			System.out.println("^^^^^^^^^^^^^^^^");
			for(int i = 0; i < dimensions[index];i++)
			{
				tableau[i].afficher(dimensions,dim-1,index+1);

			}
		
		}

	}
	public Cellule get(int coords[],int index, int dim)
	{
		if(dim == 1) {
			return tableau[coords[index]].get();
		}
		return tableau[coords[index]].get(coords,index+1,dim-1);
	}

	public int[] getDimensions()
	{
		return dimensions;
	}
	public int getDimensionGrille()
	{
		return dim;
	}

	// Coupe 2D pour l'affichage de la grille sur l'interface graphique
	// indices du tableau dimensios


	// Attribuer des valeurs aux cellules
	public void setCellule(int coords[],int index, int dim,int val)
	{
		if(dim == 1) {
			tableau[coords[index]].get().set(val);
		}
		else {
			tableau[coords[index]].setCellule(coords, index + 1, dim - 1, val);
		}
	}
}
