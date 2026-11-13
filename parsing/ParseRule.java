package parsing;

import operateurs.*;
import voisinages.*;

import javax.swing.*;
import java.util.Map;
import java.util.StringTokenizer;

public class ParseRule {
    String regle;
    Map<String, Voisinage> voisinageMap;
    private int dimensionalite;
    public void setDimensionalite(int d) { this.dimensionalite = d; }
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
        Voisinage v = voisinageMap.get(s);     // déjà créé, ou personnalisé
        if (v != null) return v;

        switch (s) {
            case "G0":  v = new G0();  break;
            case "G2":  exigerDimension(s,1) ; v = new G2();  break;
            case "G4":  exigerDimension(s,2) ; v = new G4();  break;
            case "G6":  exigerDimension(s,3) ; v = new G6();  break;
            case "G8":  exigerDimension(s,2) ; v = new G8();  break;
            case "G26": exigerDimension(s,3) ; v = new G26(); break;
            case "G2*": exigerDimension(s, 1); v = generique(GGenerique.voisinsEnCroix(1, false)); break;
            case "G4*": exigerDimension(s, 2); v = generique(GGenerique.voisinsEnCroix(2, false)); break;
            case "G8*": exigerDimension(s, 2); v = generique(GGenerique.voisinsEnBloc(2, false));  break;
            case "G6*": exigerDimension(s, 3); v = generique(GGenerique.voisinsEnCroix(3, false)); break;
            case "G26*": exigerDimension(s, 3); v = generique(GGenerique.voisinsEnBloc(3, false));  break;
            default:
                throw new IllegalArgumentException("Voisinage non reconnu : " + s);
        }
        voisinageMap.put(s, v);
        return v;

    }
    private Voisinage generique(int[][] deplacements) {
        GGenerique g = new GGenerique();
        g.setDeplacements(deplacements);
        return g;
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

    public Map<String, Voisinage> getVoisinageMap() {
        return voisinageMap;
    }

    public void setVoisinageMap(Map<String, Voisinage> voisinageMap) {
        this.voisinageMap = voisinageMap;
    }

    private void exigerDimension(String nom, int d) {
        if (dimensionalite != d)
            throw new IllegalArgumentException(nom + " n'est valable qu'en dimension " + d
                    + " (grille de dimension " + dimensionalite + ")");
    }
}
