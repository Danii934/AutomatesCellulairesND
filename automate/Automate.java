package automate;
import java.awt.Point;
import java.util.*;
import operateurs.Operateur;
import parsing.ParseXML;
import tab.Cellule;
import tab.Grille;
import voisinages.Voisinage;

public class Automate {
    private Grille grille;
    private final int[] dimensions;
    private final int dim;
    private final Operateur regle;
    private final Collection<Voisinage> voisinages;
    private final List<int[]> positions = new ArrayList<>();

    public Automate(ParseXML cfg) {
        dimensions = cfg.getDimensionsGrille();
        dim = dimensions.length;
        regle = cfg.getRegleEvolution();
        voisinages = cfg.getVoisinages().values();
        calculerPositions(new int[dim], 0);
        grille = nouvelleGrille();
        initialiser(cfg);
    }

    private Grille nouvelleGrille() { return new Grille(dimensions, dim, 0, new int[dim]); }

    private void calculerPositions(int[] cur, int k) {
        if (k == dim) { positions.add(cur.clone()); return; }
        for (int i = 0; i < dimensions[k]; i++) { cur[k] = i; calculerPositions(cur, k + 1); }
    }

    private void initialiser(ParseXML cfg) {
        if (cfg.isRandomInitialisation()) {
            Random r = new Random();
            for (int[] c : positions)
                if (r.nextInt(100) < cfg.getRandomConst())
                    grille.setCellule(c, 0, dim, 1);
        } else {
            for (int[] c : cfg.getCoordonneesCellulesVivantes())
                grille.setCellule(c, 0, dim, 1);
        }
    }

    public boolean pas() {
        Grille suivante = nouvelleGrille();
        boolean change = false;
        for (int[] c : positions) {
            Cellule cell = grille.get(c, 0, dim);
            for (Voisinage v : voisinages) v.bind(grille, cell);
            int val = regle.getValue();
            if (val != cell.getValeur()) change = true;
            suivante.setCellule(c, 0, dim, val);
        }
        grille = suivante;
        return change;
    }

    public Grille getGrille() { return grille; }

    public void afficher2D() {   // temporaire, pour tester en console
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++)
                sb.append(grille.get(new int[]{i, j}, 0, 2).getValeur() > 0 ? '#' : '.');
            sb.append('\n');
        }
        System.out.println(sb);
    }

    public int getNbDimensions() { return dim; }

    public void afficher1D() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimensions[0]; i++)
            sb.append(grille.get(new int[]{i}, 0, 1).getValeur() > 0 ? '#' : '.');
        System.out.println(sb);
    }

    public int[] getDimensions() { return dimensions.clone(); }

    /** Cases vivantes de la coupe 2D définie par plan (-1 = axe affiché). */
    public List<Point> casesVivantes(int[] plan) {
        int axeLigne = -1, axeColonne = -1;
        for (int k = 0; k < dim; k++) {
            if (plan[k] == -1) {
                if (axeLigne == -1) axeLigne = k; else axeColonne = k;
            }
        }
        List<Point> res = new ArrayList<>();
        for (int[] pos : positions) {
            boolean dansLaCoupe = true;
            for (int k = 0; k < dim; k++) {
                if (plan[k] != -1 && pos[k] != plan[k]) { dansLaCoupe = false; break; }
            }
            if (dansLaCoupe && grille.get(pos, 0, dim).getValeur() > 0)
                res.add(new Point(pos[axeColonne], pos[axeLigne]));
        }
        return res;
    }

    /** Recupère les dimension de la coupe : {nombre de lignes, nombre de colonnes} de la coupe : les deux axes à -1, dans l'ordre. */
    public int[] tailleCoupe(int[] plan) {
        int[] res = new int[2];
        int n = 0;
        for (int k = 0; k < dim; k++)
            if (plan[k] == -1) res[n++] = dimensions[k];
        return res;
    }
}