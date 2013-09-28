package Model;

import Model.StationNode;
import java.util.ArrayList;


public class Station {
    private Queue<Group> queue;

    private StationNode firstnode;

    private StationNode mStationNode;

    public Station() {
    }

    public Queue<Group> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Group> val) {
        this.queue = val;
    }

    public StationNode getFirstnode() {
        return firstnode;
    }

    public void setFirstnode(StationNode val) {
        this.firstnode = val;
    }

    public ArrayList<StationNode> getStationNode() {
        return mStationNode;
    }

    public void setStationNode(ArrayList<StationNode> val) {
        this.mStationNode = val;
    }
}
