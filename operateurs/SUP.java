package operateurs;

public class SUP extends Operateur {
    Operateur op1;
    Operateur op2;
    public SUP(Operateur o1, Operateur o2)
    {
        op1 = o1;
        op2 = o2;
    }
    public int getValue()
    {
        
        if(op1.getValue() > op2.getValue())
            return 1;
        else 
            return 0;    
    }
    public String toString()
    {
        return "SUP("+op1.toString()+","+op2.toString()+")";
    }
}
