package prog2.concur.exercice1;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Baguette {
    private boolean _prise = false;

    final synchronized boolean prendreBool() {
        if (_prise) {
            return false;
        }
        _prise = true;
        return true;
    }

    final synchronized void relacher() {
        _prise = false;
        notifyAll();
    }
}

class GestionBaguettes{

    public synchronized void prendre(Baguette baguette1, Baguette baguette2){
        if (! (baguette1.prendreBool() || baguette2.prendreBool())){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            //évite les problèmes de deadLock si le philosophe a pris une des deux baguettes
            relacher(baguette1, baguette2);
        }
    }

    public synchronized void relacher(Baguette baguette1, Baguette baguette2){
        baguette1.relacher();
        baguette2.relacher();
    }
}

public class Philosophe implements Runnable {
    private String _nom;
    private Baguette _bGauche, _bDroite;
    private static GestionBaguettes gestionBaguettes = new GestionBaguettes();
    private int nbPenser;
    private int nbManger;

    public Philosophe(String n, Baguette g, Baguette d) {
        _nom = n;
        _bGauche = g;
        _bDroite = d;
        nbPenser = 0;
        nbManger = 0;
    }

    public void run() {
        while (true) {
            penser();
            gestionBaguettes.prendre(_bDroite, _bGauche);
            manger();
            gestionBaguettes.relacher(_bDroite, _bGauche);
        }
    }

    final void manger() {
//        System.out.println(_nom + " mange.");
        nbManger++;
    }

    final void penser() {
//        System.out.println(_nom + " pense.");
        nbPenser++;
    }

    public static void main(String args[]) {
        final String[] noms = {"Platon", "Socrate", "Aristote", "Diogène", "Sénèque"};
        final Baguette[] baguettes = {new Baguette(), new Baguette(), new Baguette(), new Baguette(), new Baguette()};
        Philosophe[] table;

        table = new Philosophe[5];
        //utilisation d'un ExecutorService pour pouvoir stopper et attendre tout les threads
        ExecutorService es = Executors.newCachedThreadPool();
        for (char cpt = 0; cpt < table.length; ++cpt) {
            table[cpt] = new Philosophe(noms[cpt], baguettes[cpt], baguettes[(cpt + 1) % table.length]);
            es.execute(new Thread(table[cpt]));//run thread
        }
        try {
            System.out.println("Pour finir appuyé sur une touche");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
            es.shutdown();//stop les threads

            //attente de tous les threads
            es.awaitTermination(1, TimeUnit.SECONDS);
            //affiche les informations des philosophes
            System.out.println("stats");
            System.out.println("-----------------------------------");

            Long nbTotla=0L;
            for (Philosophe philosophe : table) {
                nbTotla += philosophe.nbPenser+philosophe.nbManger;
                System.out.println(philosophe._nom);
                System.out.println("    nbManger: " + philosophe.nbManger);
                System.out.println("    nbPenser: " + philosophe.nbPenser);
                System.out.println("    total: " + (philosophe.nbPenser+philosophe.nbManger));
                System.out.println("-----------------------------------");
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (Philosophe philosophe : table) {

                float nbMengerPourCent =((float)(philosophe.nbPenser+philosophe.nbManger)/nbTotla)*100;

                stringBuilder.append(philosophe._nom).append("\n");
                for (int i=1; i<=(int)nbMengerPourCent; i++){
                    stringBuilder.append("+");
                }
                for (int j=(int)nbMengerPourCent; j<100; j++){
                    stringBuilder.append("-");
                }
                stringBuilder.append("\n");
            }
            System.out.println(stringBuilder);


            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
