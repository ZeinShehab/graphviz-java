package graphviz;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public String name;

    private List<Attribute> attributes;

    public Node(String name) {
        this.name = name;
        attributes = new ArrayList<>();
    }

    public void addAtrribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void addAtrribute(String name, String value) {
        attributes.add(new Attribute(name, value));
    }

    public void setColor(String color) {
        addAtrribute("color", color);
    }

    public void setFillColor(String color) {
        addAtrribute("fillcolor", color);
    }

    public void setLabel(String label) {
        addAtrribute("label", label);
    }

    @Override
    public String toString() {
        return attributes.size() != 0 ? name + " " + attributes.toString() : name;
    }

    public static String[] letterLabels(int size) {
        if (size > 26) 
            throw new IllegalArgumentException("Max size for default label list is 26");
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return s.substring(0, size).split("");
    }
}
