package graphviz;

import java.util.ArrayList;
import java.util.List;

public class Link {
    public Node from;
    public Node to;

    private List<Attribute> attributes;

    public Link(Node from, Node to) {
        this.from = from;
        this.to   = to;
        attributes = new ArrayList<>();
    }

    public void addAtrribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void addAtrribute(String name, String value) {
        attributes.add(new Attribute(name, value));
    }

    public void setLabel(String label) {
        addAtrribute("label", label);
    }

    public void setColor(String color) {
        addAtrribute("color", color);
    }

    public void setPenWidth(double penwidth) {
        setPenWidth(String.format("%.2f", penwidth));
    }

    public void setPenWidth(String penwidth) {
        addAtrribute("penwidth", penwidth);
    }

    public void setWeight(double weight) {
        setWeight(String.format("%.2f", weight));
    }

    public void setWeight(String weight) {
        addAtrribute("weight", weight);
    }

    public String toString() {
        return toString(true, false);
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
