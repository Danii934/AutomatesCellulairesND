package operateurs;


import java.util.Random;

public class RANDOM extends Operateur {
    int k;
    Random rand;
    public RANDOM(int k)
    {
        this.k = k;
        rand = new Random();
    }
    public int getValue()
    {
        int rd = rand.nextInt(101);
        if(rd <= k)
            return 1;
        return 0;    
        
    }
    public String toString()
    {
        return "RANDOM("+k+")";
    }
}
