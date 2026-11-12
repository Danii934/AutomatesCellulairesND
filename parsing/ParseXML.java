package parsing;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Exceptions.DimensionIncompatibleException;
import Exceptions.EntreeIncorrecteException;
import operateurs.Operateur;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import voisinages.GGenerique;
import voisinages.Voisinage;

public class ParseXML 
{

    String filename;
    int dimensionalite;
    int[] dimensionsGrille;
    int[][] coordonnesVoisinage;
    int[] plan;
    boolean randomInitialisation;
    boolean assignedCells;
    int randomConst;
    int[][] coordonneesCellulesVivantes;
    // Si plusieurs voisinages sont utilisés pour la règle d'évolution : NomVoisinage -> VoisinageGenerique
    Map<String, Voisinage> voisinages;
    Operateur regleEvolution;

    public  ParseXML()
    {

    }
    public ParseXML(String filename){
        this.filename = filename;
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

    public Map<String, Voisinage> getVoisinages() {
        return voisinages;
    }

    public void setVoisinages(Map<String, Voisinage> voisinages) {
        this.voisinages = voisinages;
    }

    public boolean isAssignedCells() {
        return assignedCells;
    }

    public int getRandomConst() {
        return randomConst;
    }

    public Operateur getRegleEvolution() {
        return regleEvolution;
    }

    public void readFile() throws EntreeIncorrecteException, DimensionIncompatibleException
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

            // Voisinage supplementaires
            NodeList noeudVoisinages = document.getElementsByTagName("voisinages");
            if(noeudVoisinages.getLength() > 1)
            {
                throw new EntreeIncorrecteException("Une seule balise voisinage doit être présente dans le fichier");
            }
            // On extrait les voisinages supplementaires
            if(noeudVoisinages.getLength() > 0)
            {
                System.out.println("Enfants de la balise voisinage");
                NodeList voisinagesEnfants = noeudVoisinages.item(0).getChildNodes();
                for (int i = 0; i < voisinagesEnfants.getLength(); i++) {
                    Node node = voisinagesEnfants.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("voisinage")) {
                        ExtraireVoisinage((Element) node);

                    }
                }
                System.out.println(voisinages);
            }

            // RÈGLE d'évoulution
            // Traiter les eventuels \n pour la regle d'évolution

            NodeList nodeRegle = document.getElementsByTagName("regle");
            if(nodeRegle.getLength() > 1 || nodeRegle.getLength() <=0)
            {
                throw new EntreeIncorrecteException("Une seule et unique regle doit etre mentionee dans votre fichier de configuration ");
            }

            String regleFinale = nodeRegle.item(0).getTextContent().replaceAll("\\s+", "");
            ParseRule parseRegle = new ParseRule(regleFinale);
            parseRegle.setVoisinageMap(voisinages);
            parseRegle.setDimensionalite(dimensionalite);
            regleEvolution = parseRegle.traiterRegle();

            System.out.println(regleEvolution);

            // Celulles vivantes a l'intialisation
            TreeSet<String> cellulesVivantes = new TreeSet<>();

            NodeList nodeCellulesVivantes = document.getElementsByTagName("initialisation");
            if(nodeCellulesVivantes.getLength() != 1)
            {
                throw new EntreeIncorrecteException(
                        "Le fichier doit contenir exactement une balise <initialisation>."
                );
            }
            NodeList enfantsCellulesVivantes =  nodeCellulesVivantes.item(0).getChildNodes();




            verfierInitialisationCellules(enfantsCellulesVivantes);



            if(randomInitialisation)
            {
                Node randomK = recupererElementEnfant(enfantsCellulesVivantes,"random");
                randomConst = Integer.parseInt(nettoyer(randomK.getTextContent()));

                if (randomConst < 0 || randomConst > 100) {
                    throw new EntreeIncorrecteException(
                            "La probabilité doit être comprise entre 0 et 100."
                    );
                }
                System.out.println("Random initialisation : "+ randomConst);
            }
            if(assignedCells)
            {
                Node listeCells = recupererElementEnfant(enfantsCellulesVivantes,"cellules");
                NodeList listeCellules = listeCells.getChildNodes();
                for (int i = 0; i < listeCellules.getLength(); i++) {
                    Node node = listeCellules.item(i);
                    if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("cellule"))
                    {
                        String txt = nettoyer(node.getTextContent());
                        verifierCoordonnees(txt);
                        cellulesVivantes.add(txt);

                    }
                }
                coordonneesCellulesVivantes = new int[cellulesVivantes.size()][dimensionalite];
                int idx = 0;
                for (String coords : cellulesVivantes)
                {
                    ExtraireCoordonnees(coords,coordonneesCellulesVivantes,idx);
                    idx++;
                }


                /*System.out.println("Celulles vivantes ");
                for (int i = 0; i < coordonneesCellulesVivantes.length; i++) {
                    verifierCoordonneesCellule(coordonneesCellulesVivantes[i]);
                    System.out.print("(");
                    for (int j = 0; j < dimensionalite; j++) {
                        if(j != dimensionalite -1) {
                            System.out.print(coordonneesCellulesVivantes[i][j] + ",");
                        }
                        else
                        {
                            System.out.print(coordonneesCellulesVivantes[i][j]);
                        }

                    }
                    System.out.println(")");
                }*/
            }
            System.out.print("Fin traitement du fichier d'entree\n");

        }
        catch (NumberFormatException e) {
            throw new EntreeIncorrecteException("Entier invalide : " + e.getMessage());
        }
        catch (IllegalArgumentException e) {   // erreurs de ParseRule
            throw new EntreeIncorrecteException("Règle invalide : " + e.getMessage());
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new EntreeIncorrecteException("Fichier illisible ou XML mal formé : " + e.getMessage());
        }


    }

    private Node recupererElementEnfant(NodeList list, String tagName) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tagName))
            {
                return node;
            }
        }
        return null;
    }

    private void verfierInitialisationCellules(NodeList enfantsCellulesVivantes) throws EntreeIncorrecteException {
        int nbListeCells = 0;
        int nbRandomConsts = 0;
        for (int i = 0; i < enfantsCellulesVivantes.getLength(); i++) {
            Node node = enfantsCellulesVivantes.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                if(node.getNodeName().equals("random"))
                {
                    nbRandomConsts++;
                    if(nbRandomConsts > 1)
                    {
                        throw new EntreeIncorrecteException("Une seule valeur pour l'initialisation aleatoire doit etre presente dans le ficher de configuration");
                    }
                    randomInitialisation = true;

                }
                if(node.getNodeName().equals("cellules"))
                {
                    nbListeCells++;
                    if(nbListeCells > 1)
                    {
                        throw new EntreeIncorrecteException("Une seule liste de cellules vivantes doit etre presenete dans le fichier de configuration");
                    }
                    assignedCells = true;

                }
            }
        }
        if(randomInitialisation && assignedCells)
        {
            throw new EntreeIncorrecteException("Pour l'initialisation des cellules vivantes, soit une liste de coordonnees, " +
                    "soit random(k) avec k la probabilite en % qu'une cellule soit vivante mais pas les deux a la fois");
        }
        if (!randomInitialisation && !assignedCells) {
            throw new EntreeIncorrecteException(
                    "L'initialisation doit contenir une balise <random> "
                            + "ou une liste <cellules>."
            );
        }

    }

    private void recupererCoupe(Document doc) throws EntreeIncorrecteException {
        NodeList noeudCoupe = doc.getElementsByTagName("coupe");
        if(noeudCoupe.getLength()  > 1 || (dimensionalite > 2 && noeudCoupe.getLength() <=0))
        {
            throw new EntreeIncorrecteException("Une seule coupe 2D doit être mentionnee dans le cas ou la dimensionalite de la grille est supérieur 2 \n" +
                    "Format attentdu dans le fichier : "+"(x,:,:,) avec ':' les 2 axes consideres");

        }

        if(dimensionalite > 2 && noeudCoupe.getLength() == 1) {
            Node coupe = noeudCoupe.item(0);
            if (coupe.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Coupe 2D : " + ((Element) coupe).getTextContent());
            }
            StringTokenizer tokenizer = new StringTokenizer(nettoyer(((Element) coupe).getTextContent()), "(),");
            plan = new int[dimensionalite];
            if(tokenizer.countTokens() != dimensionalite)
            {
                throw new EntreeIncorrecteException(
                        "La coupe doit contenir exactement "
                                + dimensionalite + " coordonnées."
                );
            }
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
                    else
                    {
                        throw new EntreeIncorrecteException(
                                "Coordonnée invalide dans la définition de la coupe : "
                                        + token
                        );
                    }
                }
                i++;
            }
            int nbAxesVariables = 0;

            for (int coordonnee : plan) {
                if (coordonnee == -1) {
                    nbAxesVariables++;
                }
            }

            if (nbAxesVariables != 2) {
                throw new EntreeIncorrecteException(
                        "La coupe doit contenir exactement deux axes variables (:)."
                );
            }
            for (int k = 0; k < dimensionalite; k++) {
                if (plan[k] != -1 && (plan[k] < 0 || plan[k] >= dimensionsGrille[k])) {
                    throw new EntreeIncorrecteException(
                            "Coordonnée de coupe " + plan[k] + " hors de la grille sur l'axe " + k
                                    + " (taille " + dimensionsGrille[k] + ").");
                }
            }
        }
    }

    void recupererDimensionsGrille(Document doc) throws DimensionIncompatibleException, EntreeIncorrecteException {
        NodeList dimensionGrilleNode = doc.getElementsByTagName("dimensionGrille");
        if(dimensionGrilleNode.getLength() > 1 || dimensionGrilleNode.getLength() <= 0)
        {
            throw new EntreeIncorrecteException("Il doit avoir une seule dimensionalite de la grille qui doit être mentionnée dans le fichier de configuration ni plus ni moins :)");
        }
        try {

            this.dimensionalite = Integer.parseInt(dimensionGrilleNode.item(0).getTextContent().trim());
            if(dimensionalite <= 0)
            {
                throw new EntreeIncorrecteException(
                        "La dimension de la grille doit être strictement positive."
                );
            }
            NodeList tailleGrille = doc.getElementsByTagName("tailleGrille");
            if (tailleGrille.getLength() != 1) {
                throw new EntreeIncorrecteException(
                        "Le fichier doit contenir exactement une balise <tailleGrille>."
                );
            }
            ArrayList<Integer> dimensions = new ArrayList<>();

            for (int i = 0; i < tailleGrille.item(0).getChildNodes().getLength(); i++) {
                Node node = tailleGrille.item(0).getChildNodes().item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element dimension = (Element) node;
                    try {
                        int taille = Integer.parseInt(nettoyer(dimension.getTextContent()));
                        if (taille <= 0) {
                            throw new EntreeIncorrecteException(
                                    "Les tailles de la grille doivent être strictement positives."
                            );
                        }
                        dimensions.add(taille);
                    }
                    catch (NumberFormatException e)
                    {
                        throw new EntreeIncorrecteException(
                                "Une taille de la grille doit être un entier valide."
                        );
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
            throw  new EntreeIncorrecteException("Une tentative de conversion de chaine de caractères en entier a echouee. Merci de verifier le fichier de configuration");
        }
    }

    void ExtraireVoisinage(Element voisinage) throws EntreeIncorrecteException {
        String nomVosinage = voisinage.getAttribute("nom");
        if (nomVosinage == null || nomVosinage.trim().isEmpty()) {
            throw new EntreeIncorrecteException(
                    "Chaque voisinage personnalisé doit avoir un nom."
            );
        }

        if (voisinages.containsKey(nomVosinage)) {
            throw new EntreeIncorrecteException(
                    "Le voisinage '" + nomVosinage + "' est défini plusieurs fois."
            );
        }
        NodeList voisinageChilds = voisinage.getChildNodes();
        ArrayList<String> coords = new ArrayList<String>();

        int nbListesCoords = 0;
        for (int j = 0; j < voisinageChilds.getLength(); j++) {
            Node node1 = voisinageChilds.item(j);
            if(node1.getNodeType() == Node.ELEMENT_NODE && node1.getNodeName().equals("coords"))
            {


                NodeList coordsList = node1.getChildNodes();
                for (int k = 0; k < coordsList.getLength() ; k++) {
                    Node node2 = coordsList.item(k);

                    if(node2.getNodeType() == Node.ELEMENT_NODE && node2.getNodeName().equals("coord")) {
                        String txt = nettoyer(node2.getTextContent());
                        verifierCoordonnees(txt);
                        coords.add(txt);
                    }
                }
                nbListesCoords++;
                if(nbListesCoords > 1)
                {
                    throw new EntreeIncorrecteException(
                            "Un seul élément <coords> est autorisé par voisinage."
                    );
                }

            }
        }
        if (nbListesCoords != 1 || coords.isEmpty()) {
            throw new EntreeIncorrecteException(
                    "Chaque voisinage personnalisé doit contenir "
                            + "une liste <coords> non vide."
            );
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

    private void verifierCoordonnees(String coordonnes) throws EntreeIncorrecteException {
        StringTokenizer tokenizer = new StringTokenizer(coordonnes, "(),");
        if(tokenizer.countTokens() == dimensionalite)
        {
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                try {
                    Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    throw new EntreeIncorrecteException( "Erreur dans la saisie des coordonees du voisinage, Verifier si les coordooness saisies sont tous des entiers");
                }
            }
            /*System.out.println("OK "+coordonnes);*/
        }
        else
        {
            throw  new EntreeIncorrecteException("Cordonnes pour le voisinage incorrectes verifier la liste de coordonnes dans le fichier d'entree \n" +
           "Dimensionalite attendue : "+ dimensionalite +"\n"+ "Dimensionalite trouvee "+ tokenizer.countTokens());
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
    private void verifierCoordonneesCellule(int[] coordonnees) throws EntreeIncorrecteException {

        if (coordonnees.length != dimensionalite) {
            throw new EntreeIncorrecteException(
                    "Le nombre de coordonnées est incorrect."
            );
        }

        for (int i = 0; i < dimensionalite; i++) {
            if (coordonnees[i] < 0
                    || coordonnees[i] >= dimensionsGrille[i]) {

                throw new EntreeIncorrecteException(
                        "La coordonnée " + coordonnees[i]
                                + " est invalide pour la dimension " + i
                                + ". Taille de cette dimension : "
                                + dimensionsGrille[i] + "."
                );
            }
        }
    }
    private static String nettoyer(String s) {
        return s.replaceAll("\\s+", "");
    }
}
