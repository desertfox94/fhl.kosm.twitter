package fhl.kosm.bubblebuster.model;

public class Relation<T> {

    T from;

    T to;

    long count;

    public Relation(T from, T to) {
        this.from = from;
        this.to = to;
    }

    public void inc() {
        count++;
    }

    @Override
    public String toString() {
        return from.toString() + " - " + to.toString();
    }
}
