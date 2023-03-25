public class ArrayDeque<T> {
    public T[] items;
    public int size;
    public int nextFirst, nextLast;


    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
        size = 0;
    }

    public void addFirst(T item) {
        if (nextFirst == -1) reSizeLarge();
        items[nextFirst] = item;
        nextFirst--;
        size++;
    }

    public void addLast(T item) {
        if (nextLast == items.length) reSizeLarge();
        items[nextLast] = item;
        nextLast++;
        size++;
    }

    private void reSizeLarge() {
        T[] temp = (T[]) new Object[items.length * 10];
        System.arraycopy(items, 0, temp, items.length * 5, items.length);
        items = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque() {
        for (int i = nextFirst+1; i<nextLast; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println(" ");
    }

    public T removeFirst() {
        if (nextFirst == 4) return null;
        T first = items[nextFirst+1];
        items[nextFirst+1] = null;
        nextFirst++;
        size--;
        if ((double) (size/items.length) < 0.25) reSizeSmall();
        return first;
    }

    public T removeLast() {
        if (nextLast == 5) return null;
        T last = items[nextLast-1];
        items[nextLast-1] = null;
        nextLast--;
        size--;
        if ((double) (size/items.length) < 0.25) reSizeSmall();
        return last;
    }

    private void reSizeSmall() {
        T[] temp = (T[]) new Object[items.length/2];
        System.arraycopy(items, nextFirst+1, temp, nextFirst-items.length/4, items.length/2);
        items = temp;
    }

    public T get(int index) {
        if (index >= nextLast || index <= nextFirst) return null;
        return items[index];
    }

}
