package Model;

import Model.Node;


public class WisselNode extends Node {
    private Node other;

    public WisselNode() {
    }

    public Node getOther() {
        return other;
    }

    public void setOther(Node val) {
        this.other = val;
    }

    public void doSwitch() {
    }
}
