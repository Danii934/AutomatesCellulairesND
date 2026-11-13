package Exceptions;

public class EntreeIncorrecteException extends Exception{
    public EntreeIncorrecteException()
    {
        super("L'entree correspondante dans le fichier de configuration est incorrecte");
    }
    public EntreeIncorrecteException(String message)
    {
        super(message);
    }
}
