package Model;

import Model.Node;
import Model.RailCab;

public class Node {
    protected Node next;

    protected RailCab railcab;

    public Node() {
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node val) {
        this.next = val;
    }

    public RailCab getRailcab() {
        return railcab;
    }

    public void setRailcab(RailCab val) {
        this.railcab = val;
    }
}
