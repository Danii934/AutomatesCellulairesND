package operateurs;

public class ET extends Operateur{

    Operateur op1;
    Operateur op2;
    public ET(Operateur o1, Operateur o2)
    {
        op1 = o1;
        op2 = o2;
    }
    @Override
    public int getValue() {
        int res1 = op1.getValue();
        int res2 = op2.getValue();
        if((res1 != 0) && (res2 != 0))
            return 1;
        return 0;
    }
    public String toString()
    {
        return "ET("+op1.toString()+","+op2.toString()+")";
    }
    
}
