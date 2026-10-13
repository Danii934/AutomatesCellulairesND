package parsing;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Exceptions.DimensionIncompatibleException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import voisinages.GGenerique;
import voisinages.Voisinage;

public class ParseXML 
{

    String filename;
    int dimensionalite;
    int[] dimensionsGrille;
    int[][] coordonnesVoisinage;
    int[] plan;
    String regle;
    boolean randomInitialisation;
    boolean coupe2D;
    int[][] coordonneesCellulesVivantes;
    // Si plusieurs voisinages sont utilisés pour la règle d'évolution : NomVoisinage -> VoisinageGenerique
    Map<String, Voisinage> voisinages;

    public  ParseXML()
    {

    }
    public ParseXML(String filename){
        this.filename = filename;
        coupe2D = false;
        voisinages = new HashMap<>();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getDimensionalite() {
        return dimensionalite;
    }

    public void setDimensionalite(int dimensionalite) {
        this.dimensionalite = dimensionalite;
    }

    public int[] getDimensionsGrille() {
        return dimensionsGrille;
    }

    public void setDimensionsGrille(int[] dimensionsGrille) {
        this.dimensionsGrille = dimensionsGrille;
    }

    public int[][] getCoordonnesVoisinage() {
        return coordonnesVoisinage;
    }

    public void setCoordonnesVoisinage(int[][] coordonnesVoisinage) {
        this.coordonnesVoisinage = coordonnesVoisinage;
    }

    public int[] getPlan() {
        return plan;
    }

    public void setPlan(int[] plan) {
        this.plan = plan;
    }

    public String getRegle() {
        return regle;
    }

    public void setRegle(String regle) {
        this.regle = regle;
    }

    public boolean isRandomInitialisation() {
        return randomInitialisation;
    }

    public void setRandomInitialisation(boolean randomInitialisation) {
        this.randomInitialisation = randomInitialisation;
    }

    public int[][] getCoordonneesCellulesVivantes() {
        return coordonneesCellulesVivantes;
    }

    public void setCoordonneesCellulesVivantes(int[][] coordonneesCellulesVivantes) {
        this.coordonneesCellulesVivantes = coordonneesCellulesVivantes;
    }

    public void readFile()
    {
        try {
            File file = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document =  builder.parse(file);

            document.getDocumentElement().normalize();

            // Récupèration de la dimension de la grille
            recupererDimensionsGrille(document);
            System.out.println("Dimensionalité de la grille "+ this.dimensionalite);
            for (int i = 0; i < dimensionsGrille.length; i++) {
                System.out.println("Dimension "+(i+1)+" "+dimensionsGrille[i]);
            }

            // Si la dimension est supérieure à 2 on extrait la coupe 2D qui sera affichée sur le GUI
            if(dimensionalite > 2)
            {
                recupererCoupe(document);
            }

            // Voisinage
            NodeList noeudVoisinages = document.getElementsByTagName("voisinages");
            if(noeudVoisinages.getLength() > 1 || noeudVoisinages.getLength() <= 0)
            {
                System.out.println("Une seule balise voisinage doit être présente dans le fichier");
                System.exit(1);
            }
            System.out.println("Enfants de la balise voisinage");
            NodeList voisinagesEnfants = noeudVoisinages.item(0).getChildNodes();
            for (int i = 0; i < voisinagesEnfants.getLength(); i++) {
                Node node = voisinagesEnfants.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("voisinage")) {
                    ExtraireVoisinage((Element) node);

                }
            }
            System.out.println(voisinages);





           /* // Contrôle des coordonnées à faire si la la dimension matche avec les coordonnées
            System.out.println("Taille grille" + voisinage.getLength());*//*
            // RÈGLE d'évoulution
            // Traiter les eventuels \n pour la regle d'évolution

            NodeList regle = document.getElementsByTagName("regle");
            String regleFinale = regle.item(0).getTextContent().replaceAll("\\s+", "");
            System.out.println(regleFinale);

            // Celulles vivantes
            NodeList cellulesVivantes = document.getElementsByTagName("initialisation");
            NodeList enfantsCellulesVivantes =  cellulesVivantes.item(0).getChildNodes();
            System.out.println("Nombre de balises Initialisation : " + cellulesVivantes.getLength());
            System.out.println("Nombre d'enfants de la balise Initialisation : " + enfantsCellulesVivantes.getLength());
            for (int i = 0; i < enfantsCellulesVivantes.getLength(); i++) {
                Node node = enfantsCellulesVivantes.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    System.out.println("<"+node.getTextContent()+">");
                }
            }*/

        }
        catch (DimensionIncompatibleException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }


    }

    private void recupererCoupe(Document doc)
    {
        NodeList noeudCoupe = doc.getElementsByTagName("coupe");
        if(noeudCoupe.getLength()  > 1 || (dimensionalite > 2 && noeudCoupe.getLength() <=0))
        {
            System.out.println("Une seule coupe 2D doit être mentionnee dans le cas ou la dimensionalite de la grille est supérieur 2");
            System.out.println("Format attentdu dans le fichier : "+"(x,:,:,) avec ':' les 2 axes consideres");

            System.exit(1);
        }

        if(dimensionalite > 2 && noeudCoupe.getLength() == 1) {
            Node coupe = noeudCoupe.item(0);
            if (coupe.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Coupe 2D : " + ((Element) coupe).getTextContent());
            }
            StringTokenizer tokenizer = new StringTokenizer(((Element) coupe).getTextContent(), "(),");
            plan = new int[dimensionalite];
            int i = 0;
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                try
                {
                    plan[i] = Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    if(token.equals(":"))
                    {
                        plan[i] = -1;
                    }
                }
                i++;
            }
        }
    }

    void recupererDimensionsGrille(Document doc) throws DimensionIncompatibleException {
        NodeList dimensionGrilleNode = doc.getElementsByTagName("dimensionGrille");
        if(dimensionGrilleNode.getLength() > 1 || dimensionGrilleNode.getLength() <= 0)
        {
            throw new DimensionIncompatibleException("Il doit avoir une seule dimension de la grille qui doit être mentionnée dans le fichier de configuration ni plus ni moins :)");
        }
        try {

            this.dimensionalite = Integer.parseInt(dimensionGrilleNode.item(0).getTextContent());

            NodeList tailleGrille = doc.getElementsByTagName("tailleGrille");
            ArrayList<Integer> dimensions = new ArrayList<>();

            for (int i = 0; i < tailleGrille.item(0).getChildNodes().getLength(); i++) {
                Node node = tailleGrille.item(0).getChildNodes().item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element dimension = (Element) node;
                    try {
                        dimensions.add(Integer.parseInt(dimension.getTextContent()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            if(dimensions.size() != this.dimensionalite)
            {
                throw new DimensionIncompatibleException("La dimension de la grille et le nombre de dimensions fournies pour la grille ne sont pas egaux");
            }
            System.out.println("Dimensions de la grille : "+ this.dimensionalite);
            System.out.println("Nombre de dimensions fournies : " + dimensions.size());
            this.dimensionsGrille = new int[this.dimensionalite];
            int i = 0;
            for(Integer dimension : dimensions)
            {
                dimensionsGrille[i++] = dimension;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Une tentative de conversion de chaine de caractères en entier a echouee. Merci de verifier le fichier de configuration");
            e.printStackTrace();
        }
    }

    void ExtraireVoisinage(Element voisinage)
    {
        String nomVosinage = voisinage.getAttribute("nom");
        NodeList voisinageChilds = voisinage.getChildNodes();
        ArrayList<String> coords = new ArrayList<String>();

        int nbListesCoords = 0;
        for (int j = 0; j < voisinageChilds.getLength(); j++) {
            Node node1 = voisinageChilds.item(j);
            if(node1.getNodeType() == Node.ELEMENT_NODE && node1.getNodeName().equals("coords"))
            {

                if(nbListesCoords > 1)
                {
                    System.out.println("Une seule liste de coordonnes par voisinage doit etre ecrite");
                    System.exit(1);
                }
                NodeList coordsList = node1.getChildNodes();
                for (int k = 0; k < coordsList.getLength() ; k++) {
                    Node node2 = coordsList.item(k);

                    if(node2.getNodeType() == Node.ELEMENT_NODE && node2.getNodeName().equals("coord")) {
                        verifierCoordonnees(node2.getTextContent());
                        coords.add(node2.getTextContent());
                    }
                }
                nbListesCoords++;

            }
        }
        int[][] deplacements = new int[coords.size()][dimensionalite];
        for (int i = 0; i < coords.size(); i++) {
            ExtraireCoordonnees(coords.get(i),deplacements,i);
        }
        System.out.println(nomVosinage);
        GGenerique v = new GGenerique();
        v.setDeplacements(deplacements);
        voisinages.put(nomVosinage,v);
        System.out.println(v);

    }

    private void verifierCoordonnees(String coordonnes) {
        StringTokenizer tokenizer = new StringTokenizer(coordonnes, "(),");
        if(tokenizer.countTokens() == dimensionalite)
        {
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                try {
                    Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur dans la saisie des coordonees du voisinage, Verifier si les coordooness saisies sont tous des entiers");
                    System.exit(1);
                }
            }
            System.out.println(coordonnes);
        }
        else
        {
            System.out.println("Cordonnes pour le voisinage incorrectes verifier la liste de coordonnes dans le fichier d'entree");
            System.out.println("Dimensionalite attendue : "+ dimensionalite +"\n"+ "Dimensionalite trouvee "+ tokenizer.countTokens());

            System.exit(1);
        }
    }

    private void ExtraireCoordonnees(String s, int[][] deplacements, int index) {
        StringTokenizer tokenizer = new StringTokenizer(s, "(),");
        int i = 0;
        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();
            deplacements[index][i++] = Integer.parseInt(token);
        }

    }
}
