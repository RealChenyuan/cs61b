package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T>{
    protected int fillCount;
    protected int capacity;
    public int capacity() {
        return capacity;
    }
    public int fillCount() {
        return fillCount;

    }
//    public boolean isEmpty() {// is the buffer empty (fillCount equals zero)?
//        return fillCount() == 0;
//    }
//    public boolean isFull() { // is the buffer full (fillCount is same as capacity)?
//        return fillCount() == capacity();
//    }
}
