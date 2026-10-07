package operateurs;

public class SI extends Operateur{
    Operateur op1;
    Operateur op2;
    Operateur op3;

    public SI(Operateur o1,Operateur o2,Operateur o3)
    {
        op1 = o1;
        op2 = o2;
        op3 = o3;
    }
    public int getValue()
    {
        int cond = op1.getValue();
        if(cond != 0)
            return op2.getValue();
        else
            return op3.getValue();    
    }
    public String toString()
    {
        return "SI("+op1.toString()+","+op2.toString()+","+op3.toString()+")";
    }
}
