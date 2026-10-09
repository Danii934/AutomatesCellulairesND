package parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Exceptions.DimensionIncompatibleException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    int[][] coordonneesCellulesVivantes;
    // Si plusieurs voisinages sont utilisés pour la règle d'évolution : NomVoisinage -> VoisinageGenerique
    Map<String, Voisinage> voisinages;
    public ParseXML(String filename){
        this.filename = filename;
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

            // Si la dimension est supérieure à 2 on extrait la coupe 2D qui sera affichée sur le GUI
            NodeList cp = document.getElementsByTagName("coupe");
            Node coupe = cp.item(0);
            if(coupe.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Coupe 2D : " + ((Element) coupe).getTextContent());
            }

            // Voisinage
            NodeList voisinage = document.getElementsByTagName("voisinages");
            System.out.println("Nombre de balise voisinage : " + voisinage.getLength());
            System.out.println("Nombre d'enfants de la balise voisinage : " + voisinage.item(0).getChildNodes().getLength());
            System.out.println("Enfants de la balise voisinage");
            NodeList voisinageEnfants = voisinage.item(0).getChildNodes();
            for (int i = 0; i < voisinageEnfants.getLength(); i++) {
                Node node = voisinageEnfants.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("voisinage")) {
                    System.out.println(((Element) node).getAttribute("nom"));
                    NodeList voisinageChilds = node.getChildNodes();
                    /*NodeList coordsChilds = node.getChildNodes();*/

                    for (int j = 0; j < voisinageChilds.getLength(); j++) {
                        Node node1 = voisinageChilds.item(j);
                        if(node1.getNodeType() == Node.ELEMENT_NODE && node1.getNodeName().equals("coords"))
                        {
                            NodeList coordsList = node1.getChildNodes();
                            for (int k = 0; k < coordsList.getLength() ; k++) {
                                Node node2 = coordsList.item(k);
                                if(node2.getNodeType() == Node.ELEMENT_NODE && node2.getNodeName().equals("coord")) {
                                    System.out.println("coord " + node2.getTextContent());
                                }
                            }
                        }
                    }
                }
            }





           /* // Contrôle des coordonnées à faire si la la dimension matche avec les coordonnées
            System.out.println("Taille grille" + voisinage.getLength());*/
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
            }

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
    void recupererDimensionsGrille(Document doc) throws DimensionIncompatibleException {
        NodeList dimensionGrilleNode = doc.getElementsByTagName("dimensionGrille");
        if(dimensionGrilleNode.getLength() > 1 || dimensionGrilleNode.getLength() <= 0)
        {
            throw new DimensionIncompatibleException("Il doit avoir une seule dimension de la grille qui doit être mentionnée dans le fichier de configuration ni plus ni moins :)");
        }
        try {

            this.dimensionalite = Integer.parseInt(dimensionGrilleNode.item(0).getTextContent());

            NodeList tailleGrille = doc.getElementsByTagName("tailleGrille");
            ArrayList<Integer> dimensions = new ArrayList<Integer>();

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


}
