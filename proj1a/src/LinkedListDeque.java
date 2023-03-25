public class LinkedListDeque<T> {
    public Node sentinel;
    public int size;


    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node first = new Node(sentinel.next, sentinel, item);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }

    public void addLast(T item) {
        Node last = new Node(sentinel, sentinel.prev, item);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque() {
        for (Node p=sentinel.next; !p.equals(sentinel); p=p.next) {
            System.out.print(p.item + " ");
        }
        System.out.println(" ");
    }

    public T removeFirst() {
        Node first = sentinel.next;
        sentinel.next = first.next;
        first.next.prev = sentinel;
        size--;
        return first.item;
    }

    public T removeLast() {
        Node last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        size--;
        return last.item;
    }

    public T get(int index) {
        if (index >= size) return null;
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        if (index >= size) return null;
        return getRecursive(index, sentinel.next);
    }

    public T getRecursive(int index, Node p) {
        if (index == 0) return p.item;
        return getRecursive(index-1, p.next);
    }

    public class Node {
        public Node next, prev;
        public T item;

        public Node(Node next, Node prev, T item) {
            this.next = next;
            this.prev = prev;
            this.item = item;
        }
    }
}
