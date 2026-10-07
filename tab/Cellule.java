package tab;
public class Cellule extends TableauDynamiqueND{
	private int valeur;
	private int[] coordonnes;
	public Cellule(int[] tab)
	{

		coordonnes = new int[tab.length];
		for(int i =0; i < tab.length;i++)
		{
			coordonnes[i] = tab[i];
		}
	}
	public  String toString()
	{
		return ""+valeur+"";
	}
	public void afficher()
	{
		System.out.print(" (");
		for(int i = 0; i < coordonnes.length -1; i++)
		{
			System.out.print(coordonnes[i]+",");
		}
		System.out.print(coordonnes[coordonnes.length -1]+") ");
	}
	 public int getValeur()
	 {
		 return valeur;
	 }
	 public Cellule get()
	 {
		 return this;
	 }
	 public int[] getCoords()
	 {
		return coordonnes;
	 }
	 public void setCellule(int nb)
	 {
		this.valeur = nb;
	 }
	 public void set(int val)
	 {
		 valeur = val;
	 }
}
