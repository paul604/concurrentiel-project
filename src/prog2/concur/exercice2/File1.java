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
        while (super.estPleine){
            wait();
        }
        SemaphoreWrite.lock();

        super.tableau[super.queue]=s;
        super.queue++;

        if (super.queue>=super.tableau.length) {
            super.estPleine=true;
        }
        super.estVide=false;

        SemaphoreWrite.unLock();
        notify();
    }

    @Override
    public synchronized T prendre() throws InterruptedException {
        while (super.estVide){
            wait();
        }
        SemaphoreRead.lock();


        T out = super.tableau[super.tete];

        super.queue--;
        for (int i = 0; i < super.queue; i++) {
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
