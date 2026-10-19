package parsing;

import operateurs.*;
import voisinages.*;

import javax.swing.*;
import java.util.Map;
import java.util.StringTokenizer;

public class ParseRule {
    String regle;
    Operateur op;
    Map<String, Voisinage> voisinageMap;
    public ParseRule(String regle)
    {
        this.regle = regle;
    }

    public Operateur traiterRegle()  {
        return reconnaitreOperateur(new StringTokenizer(regle, "(),",true));
    }
    public Operateur reconnaitreOperateur(StringTokenizer tokenizer) throws IllegalArgumentException {
        if(tokenizer.hasMoreTokens())
        {
            Operateur res =  reconnaitreOperateurBis(tokenizer);
            if(tokenizer.hasMoreTokens())
            {
                throw  new IllegalArgumentException("La regle contient des caracteres superflus");
            }
            return  res;
        }
        else {
        throw  new IllegalArgumentException("La regle est vide");
        }
    }
    public Operateur reconnaitreOperateurBis(StringTokenizer tokenizer) throws IllegalArgumentException {

        String token = tokenizer.nextToken();
        Operateur op1, op2,op3;
            switch (token) {
                case "ADD":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");
                    return new ADD(op1, op2);
                case "COMPTER":
                        verifierToken(tokenizer,"(");
                    if (!tokenizer.hasMoreTokens()) {
                        throw new IllegalArgumentException(
                                "Le nom du voisinage est manquant dans COMPTER."
                        );
                    }
                        String voisinage = tokenizer.nextToken();
                        verifierToken(tokenizer,")");
                        return new COMPTER(reconnaitreVoisinage(voisinage));
                case "EQ":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");
                    return new EQ(op1, op2);
                case "ET":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");
                    return new ET(op1, op2);
                case "MUL":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new MUL(op1, op2);
                case "NON":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new NON(op1);
                case "OU":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new OU(op1, op2);
                case "SI":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");
                    op3 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new SI(op1, op2, op3);
                case "SUB":
                    verifierToken(tokenizer,"(");

                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");

                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new SUB(op1, op2);
                case "SUP":
                    verifierToken(tokenizer,"(");
                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");

                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new SUP(op1, op2);
                case "SUPEQ":
                    verifierToken(tokenizer,"(");

                    op1 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,",");

                    op2 = reconnaitreOperateurBis(tokenizer);
                    verifierToken(tokenizer,")");

                    return new SUPEQ(op1, op2);
                default:
                    /*System.out.println("Cas de base " + token);*/
                    try {
                        return new CONST(Integer.parseInt(token));
                    } catch (NumberFormatException e) {
                        throw  new IllegalArgumentException("Opérateur ou constante non reconnu : " + token);
                    }
            }
    }

    private Voisinage reconnaitreVoisinage(String s) {
        System.out.println(s);
        switch (s)
        {
            case "G0" :
                return new G0();
            case "G2" :
                return new G2();
            case "G4":
                return new G4();
            case "G6":
                return new G6();
            case "G8" :
                return new G8();
            case "G26" :
                return new G26();
            default :
                if (voisinageMap != null
                        && voisinageMap.containsKey(s)) {
                    return voisinageMap.get(s);
                }

                throw new IllegalArgumentException(
                        "Voisinage non reconnu : " + s
                );


        }

    }
    private void verifierToken(StringTokenizer tokenizer, String token)
    {
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException(
                    "La règle est incomplète : le token '" + token
                            + "' était attendu."
            );
        }
        if(!tokenizer.nextToken().equals(token))
        {
            throw new IllegalArgumentException("Le caractere suivant etait attendu dans la regle :"  + "'" +token +"'");
        }
    }



}
