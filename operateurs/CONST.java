package operateurs;

public class CONST extends Operateur{
    int val;
    public CONST(int v)
    {
        val = v;
    }
    public int getValue()
    {
        return val;
    }
    public String toString()
    {
        return String.valueOf(val);
    }
}
