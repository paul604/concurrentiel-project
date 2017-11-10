package prog2.concur.exercice2;

/**
 * haut niveau
 */
public class File2<T> extends AbstractFileBloquanteBornee<T> {

    public File2(int n) throws IllegalArgumentException {
        super(n);
    }

    @Override
    public void deposer(T s) throws InterruptedException {

    }

    @Override
    public T prendre() throws InterruptedException {
        return null;
    }
}
