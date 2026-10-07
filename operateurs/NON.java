package operateurs;


public class NON extends Operateur {
    Operateur op;

    public NON(Operateur o)
    {
        op = o;
    }
    public int getValue()
    {
        
        if(op.getValue() == 0)
            return 1;
        else 
            return 0;    
    }
    public String toString()
    {
        return "NON("+op.toString()+")";
    }
}
