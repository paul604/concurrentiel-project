package prog2.concur.exercice2;

/**
 * bas niveau
 */
public class File1<T> extends AbstractFileBloquanteBornee<T> {

    private Semaphore SemaphoreRead;
    private Semaphore SemaphoreWrite;

    public File1(int n) throws IllegalArgumentException {
        super(n);
        SemaphoreRead = new Semaphore();
        SemaphoreWrite = new Semaphore();
    }

    @Override
    public synchronized void deposer(T s) throws InterruptedException {
        while (super.estPleine){// si le tableau est plein on attend
            wait();
        }
        SemaphoreWrite.lock();//on demande l'écriture

        super.tableau[super.queue]=s;//add val
        super.queue++;// add 1 a la queue du tableau

        if (super.queue>=super.tableau.length) {
            super.estPleine=true;
        }
        super.estVide=false;

        SemaphoreWrite.unLock();
        notify();
    }

    @Override
    public synchronized T prendre() throws InterruptedException {
        while (super.estVide){// si le tableau est vide on attend
            wait();
        }
        SemaphoreRead.lock();//on demande la lecture


        T out = super.tableau[super.tete];

        super.queue--;// sup 1 a la queue du tableau
        for (int i = 0; i < super.queue; i++) {//décalage à gauche
            super.tableau[i]=super.tableau[i+1];
        }

        if (super.queue<=0) {
            super.estVide=true;
        }
        super.estPleine=false;

        SemaphoreRead.unLock();

        notify();
        return out;
    }
}

class Semaphore{
    boolean ok;

    public Semaphore() {
        this.ok = true;
    }

    public synchronized void lock() throws InterruptedException {
        while (!ok){
            wait();
        }
        ok=false;
    }

    public synchronized void unLock(){
        this.ok = true;
        notifyAll();
    }
}
