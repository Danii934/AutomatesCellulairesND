package operateurs;

public class ADD extends Operateur {
    Operateur op1;
    Operateur op2;
    public ADD(Operateur o1, Operateur o2)
    {
        op1 = o1;
        op2 = o2;
    }
    public int getValue()
    {
        return op1.getValue() + op2.getValue();   
    }
    public String toString()
    {
        return "ADD("+op1.toString()+","+op2.toString()+")";
    }
}
