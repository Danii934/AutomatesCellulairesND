import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import automate.Automate;
import parsing.ParseXML;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import javax.swing.Timer;
import gui.GrilleGraphique;

public class Main {
    private static final int PAS_PAR_DEFAUT = 20;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            File[] configs = listerConfigs(new File("./inputs"));
            if (configs.length == 0) {
                System.out.println("Aucun fichier .xml dans " + new File(".").getAbsolutePath());
                return;
            }
            System.out.println("\n=== Automates cellulaires ===");
            for (int i = 0; i < configs.length; i++)
                System.out.println((i + 1) + ") " + configs[i].getName());
            System.out.println("0) Quitter");
            System.out.print("Votre choix : ");
            int choix;
            try { choix = Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Entrez un numéro."); continue; }

            if (choix == 0) return;
            if (choix < 1 || choix > configs.length) { System.out.println("Choix hors liste."); continue; }
            lancer(configs[choix - 1], sc);
        }
    }

    private static File[] listerConfigs(File dossier) {
        File[] res = dossier.listFiles((d, nom) -> nom.toLowerCase().endsWith(".xml"));
        if (res == null) return new File[0];
        Arrays.sort(res);
        return res;
    }

    private static void lancer(File f, Scanner sc) {
        Automate a;
        ParseXML p;
        try {
            p = new ParseXML(f.getPath());
            p.readFile();
            a = new Automate(p);
        } catch (Exception e) {
            System.out.println("Fichier invalide (" + f.getName() + ") : " + e.getMessage());
            return;
        }
        int d = a.getNbDimensions();
        boolean graphique = demanderGraphique(sc);
        if (graphique && d == 1)
            simulerGraphique1D(a, demanderPas(sc));
        else if (graphique)
            simulerGraphique(a, d == 2 ? new int[]{-1, -1} : p.getPlan());
        else
            simulerConsole(a, demanderPas(sc));
    }

    private static int demanderPas(Scanner sc) {
        System.out.print("Nombre de pas (Entrée = " + PAS_PAR_DEFAUT + ") : ");
        String s = sc.nextLine().trim();
        try {
            int n = Integer.parseInt(s);
            return n > 0 ? n : PAS_PAR_DEFAUT;
        } catch (NumberFormatException e) {
            return PAS_PAR_DEFAUT;
        }
    }

    private static void simulerConsole(Automate a, int pas) {
        int d = a.getNbDimensions();
        if (d > 2) {
            System.out.println("Affichage console limité aux dimensions 1 et 2 (la coupe 2D viendra avec la GUI).");
            return;
        }
        for (int t = 0; t <= pas; t++) {
            if (d == 1) a.afficher1D();
            else { System.out.println("t=" + t); a.afficher2D(); }
            if (!a.pas()) {
                System.out.println("Convergence : grille identique entre t=" + t + " et t=" + (t + 1));
                return;
            }
        }
    }

    private static boolean demanderGraphique(Scanner sc) {
        System.out.print("Affichage graphique ? (o/n, Entrée = o) : ");
        return !sc.nextLine().trim().toLowerCase().startsWith("n");
    }

    private static void simulerGraphique(Automate a, int[] plan)  {
        int[] dims = a.tailleCoupe(plan);
        int taille = Math.max(4, Math.min(30, 600 / Math.max(dims[0], dims[1])));
        GrilleGraphique g = new GrilleGraphique(dims[1], dims[0], taille);  // largeur = colonnes

        CountDownLatch fermeture = new CountDownLatch(1);
        int[] t = {0};
        Timer timer = new Timer(500, null);
        timer.addActionListener(e -> {
            g.mettreAJour(a.casesVivantes(plan));
            boolean change = a.pas();
            g.window.setTitle("t=" + t[0]++ + (change ? "" : " (convergé)"));
            if (!change) timer.stop();
        });
        g.window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { timer.stop(); fermeture.countDown(); }
        });
        timer.start();

        try { fermeture.await(); }                       // le menu attend la fermeture de la fenêtre
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void simulerGraphique1D(Automate a, int nbPas) {
        int largeur = a.getDimensions()[0];
        int hauteur = nbPas + 1;                                  // une ligne par génération, t = 0..nbPas
        int taille = Math.max(4, Math.min(30, 600 / Math.max(largeur, hauteur)));
        GrilleGraphique g = new GrilleGraphique(largeur, hauteur, taille);

        CountDownLatch fermeture = new CountDownLatch(1);
        int[] t = {0};
        Timer timer = new Timer(100, null);
        timer.addActionListener(e -> {
            for (int x : a.vivantes1D()) g.colorierCase(x, t[0]);   // ligne t = génération courante
            g.window.setTitle("t=" + t[0]);
            if (t[0] >= nbPas) { timer.stop(); return; }
            a.pas();
            t[0]++;
        });
        g.window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { timer.stop(); fermeture.countDown(); }
        });
        timer.start();

        try { fermeture.await(); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}