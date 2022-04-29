package graphviz;

import static graphviz.Factory.*;
import static graphviz.api.Formatter.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import graphviz.api.Code;
import graphviz.api.Graphviz;

public class Graph extends AttributedObject {
    private Graphviz graphviz;

    private Set<Node>   nodes;
    private List<Link>  links;
    private List<Graph> subGraphs;
    private List<Path>  paths;

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
        paths      = new ArrayList<>();
        
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
        link.setDirected(directed);
        links.add(link);
    }

    public void addLink(Node from, Node to) {
        addLink(link(from, to));
    }

    public void addLink(String from, String to) {
        addLink(link(from, to));
    }

    public void addLink(Node from, Node ... to) {
        addLink(link(from, to));
    }

    public void addLink(String from, String ... to) {
        addLink(link(from, to));
    }

    public void addSubGraph(Graph subGraph) {
        subGraph.setDirected(directed);
        subGraphs.add(subGraph);
    }

    public void addPath(Path path) {
        path.setDirected(directed);
        paths.add(path);
    }

    public void addPath(Node ... nodes) {
        addPath(path(nodes));
    }

    public void addPath(String ... nodes) {
        addPath(path(nodes));
    }

    public void highlightPath(Node ... nodes) {
        highlightPath("red", path(nodes));
    }

    public void highlightPath(String ... nodes) {
        highlightPath("red", path(nodes));
    }

    public void highlightPath(Path path) {
        highlightPath("red", path);
    }

    public void highlightPath(String color, Path path) {
        Link[] pathLinks = path.getLinks();
        Link[] existingLinks = new Link[pathLinks.length];

        for (int i = 0; i < pathLinks.length; i++) {
            Link pathLink = pathLinks[i];
            pathLink.setDirected(directed);
            Link existing = getLink(pathLink);

            if (existing != null) {
                existingLinks[i] = existing;
            }
            else {
                System.out.println("Failed to highligh path. Path doesn't exist!");
                return;
            }
        }
        for (Link link : existingLinks) {
            link.setColor(color);
            link.setPenWidth(2.5);
        }
    }

    private Node getNode(Node node) {
        for (Node n : nodes) 
            if (Comparator.equals(n, node))
                return n;
        return null;
    }

    private Node getNode(String name) {
        return getNode(node(name));
    }

    private Link getLink(Link link) {
        for (Link l : links) {
            if (Comparator.equals(l, link))
                return l;
        }
        return null;
    }

    private Link getLink(Node from, Node ... to) {
        return getLink(link(from, to));
    }

    private Link getLink(String from, String ... to) {
        return getLink(link(from, to));
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

        for (Path path : paths) {
            graphviz.addln(fmtPath(path, directed));
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

    public Code save(String fileName) {
        return save(fileName, Graphviz.DEFAULT_FILE_TYPE, Graphviz.DEFAULT_DPI);   
    }

    public Code save(String fileName, String fileType) {
        return save(fileName, fileType, Graphviz.DEFAULT_DPI);
    }

    public Code save(String fileName, int dpi) {
        return save(fileName, Graphviz.DEFAULT_FILE_TYPE, dpi);
    }

    public Code save(String fileName, String fileType, int dpi) {
        pack();
        Code code = graphviz.writeGraphToFile(fileName, fileType, dpi); 
        
        if (code == Code.OK)
            System.out.println("Saved successfully.");
        else if (code == Code.SYNTAX_ERROR)
            System.out.println("Failed to save. Syntax error in generated dot file.");
        else if (code == Code.IO_ERROR)
            System.out.println("Failed to save. Error in writing/reading to temporary files.");
        else if (code == Code.PROCESS_INTERRUPTED)
            System.out.println("Failed to save. The process was interrupted.");
        else
            System.out.println("Unhandled return code: " + code);
        return code;
    }

    public String getDotSource() {
        pack();
        return graphviz.getSource();
    }
}
