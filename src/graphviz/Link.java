package graphviz;

import java.util.Objects;

import graphviz.api.Formatter;

public class Link extends AttributedObject {
    public Node from;
    public Node[] to;

    private boolean isMultiLink;
    private boolean directed;

    public Link(Node from, Node to) {
        this.from = from;
        this.to = new Node[]{to};
        isMultiLink = false;
        directed = true;
    }

    public Link(Node from, Node ... to) {
        Objects.requireNonNull(to);

        if (to.length == 0)
            throw new IllegalArgumentException("Link must have atleast one target node");
        
        this.from = from;
        this.to = to;

        isMultiLink = to.length == 1 ? false : true;
        directed = true;
    }

    public boolean isMultiLink() {
        return isMultiLink;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void setLabel(String label) {
        addAtr("label", label);
    }

    public void setColor(String color) {
        addAtr("color", color);
    }

    public void setPenWidth(double penwidth) {
        setPenWidth(String.format("%.2f", penwidth));
    }

    public void setPenWidth(String penwidth) {
        addAtr("penwidth", penwidth);
    }

    public void setWeight(double weight) {
        setWeight(String.format("%.2f", weight));
    }

    public void setWeight(String weight) {
        addAtr("weight", weight);
    }

    public String toString() {
        return Formatter.fmtLink(this, true, false);
    }
}
