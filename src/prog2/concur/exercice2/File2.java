package prog2.concur.exercice2;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * haut niveau
 */
public class File2<T> extends AbstractFileBloquanteBornee<T> {

    private ReadWriteLock readWriteLock;

    public File2(int n) throws IllegalArgumentException {
        super(n);
        readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public synchronized void deposer(T s) throws InterruptedException {
        while ( super.estPleine){// si le tableau est plein on attend
            wait();
        }
        readWriteLock.writeLock().lock();//on demande l'écriture

        super.tableau[super.queue]=s;
        super.queue++;// add 1 a la queue du tableau

        if (super.queue>=super.tableau.length) {
            super.estPleine=true;
        }
        super.estVide=false;

        notify();
        readWriteLock.writeLock().unlock();
    }

    @Override
    public synchronized T prendre() throws InterruptedException {
        while (super.estVide){// si le tableau est vide on attend
            wait();
        }
        readWriteLock.readLock().lock();

        T out = super.tableau[super.tete];

        super.queue--;// sup 1 a la queue du tableau
        for (int i = 0; i < super.queue; i++) {
            super.tableau[i]=super.tableau[i+1];//décalage à gauche
        }

        if (super.queue<=0) {
            super.estVide=true;
        }
        super.estPleine=false;

        notify();
        readWriteLock.readLock().unlock();
        return out;
    }
}
