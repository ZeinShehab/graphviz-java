package graphviz;

import static graphviz.Factory.atr;
import static graphviz.Factory.link;
import static graphviz.Factory.node;
import static graphviz.Formatter.fmtAttrib;
import static graphviz.Formatter.fmtLink;
import static graphviz.Formatter.fmtNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import graphviz.api.Graphviz;

public class Graph extends AttributedObject {
    private Graphviz graphviz;

    private Set<Node>   nodes;
    private List<Link>  links;
    private List<Graph> subGraphs;

    private boolean directed;
    private boolean subGraph;

    private int clusterId;
    private static int globalClusterId = 0;

    public Graph() {
        this(false, false);
    }

    public Graph(boolean subGraph) {
        this(false, subGraph);
    }

    public Graph(boolean directed, boolean subGraph) {
        graphviz   = new Graphviz();
        nodes      = new LinkedHashSet<>();
        links      = new ArrayList<>();
        subGraphs  = new ArrayList<>();
        
        this.directed = directed;
        this.subGraph = subGraph;

        if (subGraph) {
            clusterId = globalClusterId++;
        }
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addNode(String name) {
        nodes.add(node(name));
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public void addLink(Node from, Node to) {
        links.add(link(from, to));
    }

    public void addLink(String from, String to) {
        addLink(link(from, to));
    }

    public void addLink(Node from, Node ... to) {
        links.add(link(from, to));
    }

    public void addLink(String from, String ... to) {
        links.add(link(from, to));
    }

    public void addSubGraph(Graph subGraph) {
        subGraph.setDirected(directed);
        subGraphs.add(subGraph);
    }

    public boolean isDirected() {
        return directed;
    }

    public boolean isSubGraph() {
        return subGraph;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void setSubGraph(boolean isSubGraph) {
        this.subGraph = isSubGraph;
    }

    public void setLabel(String label) {
        addAtr("label", label);
    }

    public void setFontSize(int fontSize) {
        addAtr("fontsize", fontSize+"");
    }

    public void setFontColor(String fontColor) {
        addAtr("fontcolor", fontColor);
    }

    public void center(boolean center) {
        addAtr("center", center+"");
    }

    private void pack() {
        graphviz.clearGraph();

        if (subGraph)
            graphviz.startSubGraph(clusterId);
        else if (directed)
            graphviz.startDigraph();
        else
            graphviz.startGraph();

        attributes.forEach((name, value) -> 
            graphviz.addln(fmtAttrib(atr(name, value))
        ));

        for (Node node : nodes) {
            graphviz.addln(fmtNode(node));
        }

        for (Link link : links) {
            graphviz.addln(fmtLink(link, directed));
        }

        for (Graph subGraph : subGraphs) {
            graphviz.add(subGraph.getDotSource(), false);
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
            sb.append(fmtLink(links.get(i), directed, false) + ", ");
        }
        if (links.size() > 0)
            sb.append(fmtLink(links.get(links.size() - 1), directed, false));
        sb.append("]");
        System.out.println(sb.toString());
    }

    public int save(String fileName) {
        return save(fileName, Graphviz.DEFAULT_FILE_TYPE, Graphviz.DEFAULT_DPI);   
    }

    public int save(String fileName, String fileType) {
        return save(fileName, fileType, Graphviz.DEFAULT_DPI);
    }

    public int save(String fileName, int dpi) {
        return save(fileName, Graphviz.DEFAULT_FILE_TYPE, dpi);
    }

    public int save(String fileName, String fileType, int dpi) {
        pack();
        int code = graphviz.writeGraphToFile(fileName, fileType, dpi); 
        
        if (code == 0)
            System.out.println("Saved successfully.");
        else
            System.out.println("Failed to save.");
        return code;
    }

    public String getDotSource() {
        pack();
        return graphviz.getSource();
    }
}
