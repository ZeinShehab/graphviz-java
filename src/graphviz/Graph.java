package graphviz;

import static graphviz.Factory.*;
import static graphviz.api.Formatter.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import graphviz.api.StatusCode;
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
        // graphviz   = new Graphviz("dot", "C:/Users/zeins/AppData/Local/Temp");
        graphviz   = new Graphviz("dot", "C:/Users/ZEINCH~1/AppData/Local/Temp");
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

    public StatusCode highlightPath(Node ... nodes) {
        return highlightPath("red", path(nodes));
    }

    public StatusCode highlightPath(String ... nodes) {
        return highlightPath("red", path(nodes));
    }

    public StatusCode highlightPath(Path path) {
        return highlightPath("red", path);
    }

    public StatusCode highlightPath(String color, Path path) {
        Link[] pathLinks = path.getLinks();
        Link[] linksToHighlight = new Link[pathLinks.length];

        for (int i = 0; i < pathLinks.length; i++) {
            Link pathLink = pathLinks[i];
            pathLink.setDirected(directed);
            Link linkToHighlight = null;

            for (Link link : links) {
                if (Comparator.equals(link, pathLink)) {
                    linkToHighlight = link;
                    break;
                }
                // The link exists in the graph as part of a multi-link
                if (pathLink.isSublink(link)) {
                    linkToHighlight = pathLink;
                    link.targetList.remove(pathLink.to);
                    addLink(linkToHighlight);
                    break;
                }
            }
            if (linkToHighlight == null) {
                System.out.println("Failed to highlight path. Path doesn't exist!");
                return StatusCode.INVALID_PATH;
            }

            linksToHighlight[i] = linkToHighlight;
        }
        for (Link link : linksToHighlight) {
            link.setColor(color);
            link.setPenWidth(2.5);
        }
        return StatusCode.OK;
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

    public StatusCode save(String fileName) {
        return save(fileName, Graphviz.DEFAULT_FILE_TYPE, Graphviz.DEFAULT_DPI);   
    }

    public StatusCode save(String fileName, String fileType) {
        return save(fileName, fileType, Graphviz.DEFAULT_DPI);
    }

    public StatusCode save(String fileName, int dpi) {
        return save(fileName, Graphviz.DEFAULT_FILE_TYPE, dpi);
    }

    public StatusCode save(String fileName, String fileType, int dpi) {
        pack();
        StatusCode code = graphviz.writeGraphToFile(fileName, fileType, dpi); 
        
        if (code == StatusCode.OK)
            System.out.println("Saved successfully.");
        else if (code == StatusCode.SYNTAX_ERROR)
            System.out.println("Failed to save. Syntax error in generated dot file.");
        else if (code == StatusCode.IO_ERROR)
            System.out.println("Failed to save. Error in reading/writing to temporary files.");
        else if (code == StatusCode.PROCESS_INTERRUPTED)
            System.out.println("Failed to save. The process was interrupted.");
        else
            System.out.println("Unhandled exit code: " + code);
        return code;
    }

    public String getDotSource() {
        pack();
        return graphviz.getSource();
    }
}
