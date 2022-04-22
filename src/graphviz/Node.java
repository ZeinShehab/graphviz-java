package graphviz;

import java.util.ArrayList;
import java.util.List;

public class Node {
    String name;
    List<Attribute> attributes;

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

    @Override
    public String toString() {
        return attributes.size() != 0 ? name + " " + attributes.toString() : name;
    }
}
