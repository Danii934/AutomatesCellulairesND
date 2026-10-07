package operateurs;

import java.util.Iterator;

import tab.Cellule;
import voisinages.Voisinage;

public class COMPTER extends Operateur {
    Voisinage v;

    public COMPTER(Voisinage voi)
    {
        v = voi;
    }

    public int getValue()
    {
        Iterator<Cellule> it = v.iterator();
        int val = 0;
        while(it.hasNext()) {
			if(it.next().getValeur() > 0)
                val++;
		}
        return val;
        
    }
    public String toString()
    {
        return "COMPTER("+v+")";
    }
}
