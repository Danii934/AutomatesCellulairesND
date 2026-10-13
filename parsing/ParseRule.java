package parsing;

import operateurs.*;
import voisinages.*;

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

    public Operateur traiterRegle()
    {
        return reconnaitreOperateur(new StringTokenizer(regle, "(),",true));
    }
    public Operateur reconnaitreOperateur(StringTokenizer tokenizer)
    {
        if(tokenizer.hasMoreTokens())
        {
            return reconnaitreOperateurBis(tokenizer,null);
        }

        return null;
    }
    public Operateur reconnaitreOperateurBis(StringTokenizer tokenizer, Operateur acc)
    {
        if(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            Operateur op1, op2;
            switch (token) {
                case "ADD":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new ADD(op1, op2);
                case "COMPTER":
                    if (tokenizer.nextToken().equals("(")) {

                        String voisinage = tokenizer.nextToken();
                        return reconnaitreOperateurBis(tokenizer, new COMPTER(reconnaitreVoisinage(voisinage)));
                    } else {
                        System.out.println("Merci de verifier la saisie de la règle au niveau de l'operateru compter");
                        System.exit(1);
                    }

                    break;
                case "EQ":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new EQ(op1, op2);
                case "ET":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new ET(op1, op2);
                case "MUL":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new MUL(op1, op2);
                case "NON":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    return new NON(op1);
                case "OU":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new OU(op1, op2);
                case "SI":
                    Operateur fi = reconnaitreOperateurBis(tokenizer, acc);
                    Operateur th = reconnaitreOperateurBis(tokenizer, acc);
                    Operateur els = reconnaitreOperateurBis(tokenizer, acc);
                    return new SI(fi, th, els);
                case "SUB":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new SUB(op1, op2);
                case "SUP":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new SUP(op1, op2);
                case "SUPEQ":
                    op1 = reconnaitreOperateurBis(tokenizer, acc);
                    op2 = reconnaitreOperateurBis(tokenizer, acc);
                    return new SUPEQ(op1, op2);
                case ",":
                        return acc;
                case "(", ")":
                    return reconnaitreOperateurBis(tokenizer, acc);
                default:
                    /*System.out.println("Cas de base " + token);*/
                    try {
                        int cst = Integer.parseInt(token);
                        acc = new CONST(cst);
                        return reconnaitreOperateurBis(tokenizer, acc);
                    } catch (NumberFormatException e) {
                        System.out.println("Une tentative de conversion de caractère en valeur numérique a echouée");
                        System.exit(1);
                    }
            }
        }
        return acc;
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
            case "G8" :
                return new G8();
            case "G26" :
                return new G26();
            default :
                if(voisinageMap.containsKey(s))
                {
                    return voisinageMap.get(s);
                }
                else
                {
                    System.out.println("Voisinage non reconnu");
                    System.exit(1);
                }


        }
        return null;
    }


}
