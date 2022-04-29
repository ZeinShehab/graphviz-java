package graphviz;

import java.util.Objects;

public class Factory {
    public static Graph graph() {
        return new Graph();
    }

    public static Graph graph(String label) {
        Graph g = new Graph();
        g.setLabel(label);
        return g;
    }

    public static Graph diGraph() {
        return new Graph(true, false);
    }

    public static Graph diGraph(String label) {
        Graph g = new Graph(true, false);
        g.setLabel(label);
        return g;
    }

    public static Graph subGraph() {
        return new Graph(true);
    }

    public static Graph subGraph(String label) {
        Graph g = new Graph(false, true);
        g.setLabel(label);
        return g;
    }

    public static Node node(String name) {
        return new Node(name);
    }

    public static Link link(Node from, Node to) {
        return new Link(from, to);
    } 

    public static Link link(String from, String to) {
        return new Link(node(from), node(to));
    }

    public static Link link(Node from, Node ... to) {
        return new Link(from, to);
    }

    public static Link link(String from, String ... to) {
        Node[] nodes = new Node[to.length];
        for (int i = 0; i < to.length; i++) {
            nodes[i] = new Node(to[i]);
        }
        return link(node(from), nodes);
    }

    public static Path path(Node ... nodes) {
        return new Path(nodes);
    }

    public static Path path(String ... nodes) {
        return new Path(nodes);
    }

    public static Attribute atr(String name, String value) {
        return new Attribute(name, value);
    }

    public static Graph graph(int[][] adjMat) {
        return graph(adjMat, Node.letterLabels(adjMat.length));
    }

    public static Graph graph(int[][] adjMat, String ... nodes) {
        Graph g = graph();
        fillGraphFromAdjMat(g, adjMat, nodes);
        return g;

    }

    public static Graph graph(int[][] adjMat, Node[] nodes) {
        return graph(adjMat, nodesToLabels(nodes));
    }

    public static Graph diGraph(int[][] adjMat) {
        return diGraph(adjMat, Node.letterLabels(adjMat.length));
    }

    public static Graph diGraph(int[][] adjMat, String ... nodes) {
        Graph g = diGraph();
        fillGraphFromAdjMat(g, adjMat, nodes);
        return g;
    }

    public static Graph diGraph(int[][] adjMat, Node[] nodes) {
        return diGraph(adjMat, nodesToLabels(nodes));
    }
    
    private static String[] nodesToLabels(Node[] nodes) {
        String[] labels = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++)
            labels[i] = nodes[i].name;
        return labels;
    }

    private static void fillGraphFromAdjMat(Graph g, int[][] adjMat, String ... nodes) {
        Objects.requireNonNull(nodes);

        checkAdjMat(adjMat);

        if (nodes.length != adjMat.length)
            fail("Adjacency matrix and node list have different number of nodes.");

        boolean dir = g.isDirected();
        int numNodes = nodes.length;

        for (int i = 0; i < adjMat.length; i++) {
            for (int j = 0; j < (dir ? numNodes : i+1); j++) {
                if (adjMat[i][j] == 1) {
                    g.addLink(nodes[i], nodes[j]);
                }
            }
        }
    }

    private static void checkAdjMat(int[][] adjMat) {
        Objects.requireNonNull(adjMat);

        for (int i = 0; i < adjMat.length; i++) {
            for (int j = 0; j < adjMat[i].length; j++) {
                if (adjMat.length != adjMat[i].length) {
                    fail("Adjacency matrix must be square");
                }
                if (adjMat[i][j] != 0  && adjMat[i][j] != 1) {
                    fail("Adjacency matrix must only contains 1s and 0s");
                }
            }
        }
    }

    private static void fail(String message) {
        throw new IllegalArgumentException(message);
    }
}
