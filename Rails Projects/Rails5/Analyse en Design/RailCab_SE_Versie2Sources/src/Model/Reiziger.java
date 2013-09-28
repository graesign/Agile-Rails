package Model;

public class Reiziger {
    private date starttijd;

    private date vertrek;

    private date aankomst;

    public Reiziger() {
    }

    public date getStarttijd() {
        return starttijd;
    }

    public void setStarttijd(date val) {
        this.starttijd = val;
    }

    public date getVertrek() {
        return vertrek;
    }

    public void setVertrek(date val) {
        this.vertrek = val;
    }

    public date getAankomst() {
        return aankomst;
    }

    public void setAankomst(date val) {
        this.aankomst = val;
    }
}
