package Model;

import Model.RailCab;
import Model.Node;
import Model.Station;


public class RailCab {
    private RailCab next;

    private Node currentPosition;

    private ArrayList<Cummuter> cummuter;

    private Station destination;

    public RailCab() {
    }

    public RailCab getNext() {
        return next;
    }

    public void setNext(RailCab val) {
        this.next = val;
    }

    public Node getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Node val) {
        this.currentPosition = val;
    }

    public ArrayList<Cummuter> getCummuter() {
        return cummuter;
    }

    public void setCummuter(ArrayList<Cummuter> val) {
        this.cummuter = val;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station val) {
        this.destination = val;
    }
}
