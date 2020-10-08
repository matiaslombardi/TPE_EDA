package model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    public List<Node> nodes;
    public Grid(){
        nodes = new ArrayList<>();
    }

    public void add(Node node){
        nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
