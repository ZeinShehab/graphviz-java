package graphviz;

public class Factory {
    public static Graph graph() {
        return new Graph();
    }

    public static Graph graph(String label) {
        Graph g = new Graph();
        g.addAtrribute("label", label);
        return g;
    }

    public static Graph diGraph() {
        return new Graph(true, false);
    }

    public static Graph diGraph(String label) {
        Graph g = new Graph(true, false);
        g.addAtrribute("label", label);
        return g;
    }

    public static Graph subGraph() {
        return new Graph(true);
    }

    public static Graph subGraph(String label) {
        Graph g = new Graph(false, true);
        g.addAtrribute("label", label);
        return g;
    }

    public static Node node(String label) {
        return new Node(label);
    }

    public static Link link(Node from, Node to) {
        return new Link(from, to);
    } 
}
