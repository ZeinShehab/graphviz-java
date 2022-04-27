package graphviz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Link {
    public Node from;
    public Node[] to;

    protected List<Attribute> attributes;

    private boolean isMultiLink;

    public Link(Node from, Node to) {
        this.from = from;
        this.to = new Node[]{to};
        attributes = new ArrayList<>();
        isMultiLink = false;
    }

    public Link(Node from, Node ... to) {
        Objects.requireNonNull(to);

        if (to.length == 0)
            throw new IllegalArgumentException("Link must have atleast one target node");
        
        this.from = from;
        this.to = to;
        attributes = new ArrayList<>();

        isMultiLink = to.length == 1 ? false : true;
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

    private String targetList() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < to.length-1; i++) {
            sb.append(to[i]);
            sb.append(", ");
        }
        sb.append(to[to.length-1]);
        sb.append("}");
        return sb.toString();
    }

    public String toString(boolean directed) {
        return toString(directed, true);
    }

    public String toString(boolean directed, boolean showAttribs
    ) {
        String arrow = directed ? "->" : "--";
        String link = from.name + " " + arrow + " ";
        link += isMultiLink ? targetList() : to[0].name;
        
        if (showAttribs && attributes.size() != 0) 
            return link + " " + attributes.toString();
        return link;
    }
}
