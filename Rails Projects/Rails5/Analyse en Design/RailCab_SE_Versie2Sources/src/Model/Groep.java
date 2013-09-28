package Model;

import Model.Station;


public class Groep {
    private ArrayList<Reiziger> reizigers;

    private Station bestemming;

    public Groep() {
    }

    public ArrayList<Reiziger> getReizigers() {
        return reizigers;
    }

    public void setReizigers(ArrayList<Reiziger> val) {
        this.reizigers = val;
    }

    public Station getBestemming() {
        return bestemming;
    }

    public void setBestemming(Station val) {
        this.bestemming = val;
    }
}
