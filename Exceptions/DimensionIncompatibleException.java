package Exceptions;

public class DimensionIncompatibleException extends Exception{
    public DimensionIncompatibleException()
    {
        super("La dimension de la grille est différente de la dimension du voisinage");
    }
    public DimensionIncompatibleException(String message)
    {
        super(message);
    }

}
