package prog2.concur.exercice2;

/**
 * bas niveau
 */
public class File1<T> extends AbstractFileBloquanteBornee<T> {

    private boolean SemaphoreRead;
    private boolean SemaphoreWrite;

    public File1(int n) throws IllegalArgumentException {
        super(n);
        SemaphoreRead = true;
        SemaphoreWrite = true;
    }

    @Override
    public synchronized void deposer(T s) throws InterruptedException {
        System.out.println("deposer");
        while (!SemaphoreWrite || super.estPleine){
            wait();
        }
        SemaphoreWrite=false;

        super.tableau[super.queue]=s;
        super.queue++;

        if (super.queue>=super.tableau.length) {
            super.estPleine=true;
        }
        super.estVide=false;

        SemaphoreWrite=true;
        notify();
    }

    @Override
    public synchronized T prendre() throws InterruptedException {
        System.out.println("prendre");
        while (!SemaphoreRead || super.estVide){
            wait();
        }
        SemaphoreRead=false;

        T out = super.tableau[super.tete];

        super.queue--;
        for (int i = 0; i < super.queue; i++) {
            super.tableau[i]=super.tableau[i+1];
        }

        if (super.queue<=0) {
            super.estVide=true;
        }
        super.estPleine=false;

        SemaphoreRead=true;
        notify();
        return out;
    }
}
