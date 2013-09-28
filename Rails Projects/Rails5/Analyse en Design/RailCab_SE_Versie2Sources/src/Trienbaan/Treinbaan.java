package Trienbaan;

import Model.WisselNode;
import Model.RailCab;
import Model.Node;
import Model.Station;
import Model.Groep;
import View.ActionListener;


public class Treinbaan {
    private ArrayList<ActionListener> listeners;

    public Treinbaan() {
    }

    public void switchWissel(WisselNode target) {
    }

    public void startRailCab(RailCab target) {
    }

    public void stopRailCab(RailCab target) {
    }

    public void setSpeed(RailCab target) {
    }

    public RailCab getIdleRailCab() {
        return null;
    }

    public RailCab getIdleRailCab(Node nearto) {
        return null;
    }

    public void setBestemming(RailCab target, Station bestemming) {
    }

    public void addGroep(Station target, Groep groep) {
    }

    public Node getBaan() {
        return null;
    }

    public ArrayList<ActionListener> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<ActionListener> val) {
        this.listeners = val;
    }

    public void addListener(ActionListener listener) {
    }

    public void removeListener(ActionListener listener) {
    }
}
