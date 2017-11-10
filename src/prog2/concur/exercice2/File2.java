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
        System.out.println("deposer");
        while ( super.estPleine){
            wait();
        }
        readWriteLock.writeLock().lock();

        super.tableau[super.queue]=s;
        super.queue++;

        if (super.queue>=super.tableau.length) {
            super.estPleine=true;
        }
        super.estVide=false;

        notify();
        readWriteLock.writeLock().unlock();
    }

    @Override
    public synchronized T prendre() throws InterruptedException {
        System.out.println("prendre");
        while (super.estVide){
            wait();
        }
        readWriteLock.readLock().lock();

        T out = super.tableau[super.tete];

        super.queue--;
        for (int i = 0; i < super.queue; i++) {
            super.tableau[i]=super.tableau[i+1];
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
