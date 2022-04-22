package graphviz;

import java.util.ArrayList;
import java.util.List;

public class Link {
    Node from;
    Node to;
    List<Attribute> attributes;

    public Link(Node from, Node to) {
        this.from = from;
        this.to = to;
        attributes = new ArrayList<>();
    }

    public void addAtrribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void addAtrribute(String name, String value) {
        attributes.add(new Attribute(name, value));
    }

    public String toString(boolean directed) {
        String arrow = directed ? "->" : "--";
        String link = from.name + arrow + to.name;    
        return attributes.size() != 0 ? link + " " + attributes.toString() : link;
    }

    public String toString(boolean directed, boolean attributes) {
        String arrow = directed ? "->" : "--";
        String link = from.name + arrow + to.name;  
        return attributes ? toString(directed) : link;
    }
}
