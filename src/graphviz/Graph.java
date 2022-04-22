package graphviz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Graph {
    Graphviz graphviz;

    Set<Node> nodes;
    List<Link> links;

    private boolean directed;

    public Graph(boolean directed) {
        graphviz = new Graphviz();
        nodes = new LinkedHashSet<>();
        links = new ArrayList<>();
        
        this.directed = directed;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addNode(String name) {
        nodes.add(new Node(name));
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public void addLink(Node from, Node to) {
        links.add(new Link(from, to));
    }

    public void addLink(String from, String to) {
        Node node1 = null;
        Node node2 = null;

        for (Node node : nodes) {
            if (node.name.equals(from))
                node1 = node;
            if (node.name.equals(to))
                node2 = node;
        }
        if (node1 == null)
            node1 = new Node(from);
        if (node2 == null)
            node2 = new Node(to);
        addLink(node1, node2);
    }

    public boolean isDirected() {
        return directed;
    }

    private void pack() {
        graphviz.clearGraph();

        if (directed)
            graphviz.startDigraph();
        else 
            graphviz.startGraph();

        for (Node node : nodes) {
            graphviz.addln(node.toString());
        }

        for (Link link : links) {
            graphviz.addln(link.toString(directed));
        }

        graphviz.endGraph();
    }

    public void debug() {
        System.out.print("NODES: ");

        StringBuilder sb2 = new StringBuilder("[");
        
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
            String name = it.next().name;
            sb2.append(name);

            if (it.hasNext()) {
                sb2.append(", ");
            }
        } 
        sb2.append("]");
        System.out.println(sb2.toString());

        System.out.print("LINKS: ");

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < links.size() - 1; i++) {
            sb.append(links.get(i).toString(directed, false) + ", ");
        }
        sb.append(links.get(links.size()-1).toString(directed, false));
        sb.append("]");
        System.out.println(sb.toString());
    }

    public int save(String file) {
        pack();
        return graphviz.writeGraphToFile(file);
    }

    public String getDotSource() {
        pack();
        return graphviz.getSource();
    }
}
