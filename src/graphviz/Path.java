package graphviz;

import java.util.Arrays;

import graphviz.api.Formatter;

// A path represents an ordered sequence of nodes
public class Path extends AttributedObject {
    private Node[] nodes;

    private boolean directed;

    public Path(Node ... nodes) {
        this.nodes = nodes;
        directed = true;
    }

    public Path(String ... nodes) {
        Node[] nodeArr = new Node[nodes.length];
        for (int i = 0; i < nodes.length; i++)
            nodeArr[i] = new Node(nodes[i]);
        this.nodes = nodeArr;
        directed = true;
    }

    public Node[] getNodes() {
        return Arrays.copyOf(nodes, nodes.length);
    }

    public Link[] getLinks() {
        Link[] links = new Link[nodes.length - 1];
        for (int i = 0; i < links.length; i++) {
            links[i] = new Link(nodes[i], nodes[i+1]);
        }
        return links;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void setColor(String color) {
        addAtr("color", color);
    }

    public void addNodeAtr(Attribute attrib) {
        for (Node node : nodes)
            node.addAtr(attrib);
    }

    public void addNodeAtr(String name, String value) {
        addNodeAtr(new Attribute(name, value));
    }

    public String toString() {
        return Formatter.fmtPath(this, directed, true);
    }
}
