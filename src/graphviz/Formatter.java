package graphviz;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Formatter {
    public static String fmtNode(Node node) {
        return fmtNode(node, true);
    }

    public static String fmtNode(Node node, boolean showAttribs) {
        return showAttribs ? node.name + " "  + fmtAttribMap(node) : node.name;
    }

    public static String fmtLink(Link link, boolean directed) {
        return fmtLink(link, directed, true);
    }

    public static String fmtLink(Link link, boolean directed, boolean showAttribs) {
        String arrow = directed ? "->" : "--";
        String prefix  = link.from.name + " " + arrow  + " ";
        prefix += link.isMultiLink() ? fmtLinkTargetList(link.to) : link.to[0].name;

        return showAttribs ? prefix + fmtAttribMap(link) : prefix;
    }

    public static String fmtAttrib(Attribute atrrib) {
        return atrrib.getName() + "=" + "\"" + atrrib.getValue() + "\"";
    }

    public static String fmtAttribMap(Map<String, String> attribs) {
        if (attribs.size() == 0) return "";
        StringBuilder sb = new StringBuilder("[");

        Iterator<Entry<String, String>> it = attribs.entrySet().iterator();

        while (it.hasNext()) {
            Entry<String, String> atr = it.next();
            Attribute atrrib = Factory.atr(atr.getKey(), atr.getValue());
            sb.append(fmtAttrib(atrrib));

            if (it.hasNext())
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String fmtAttribMap(AttributedObject obj) {
        return fmtAttribMap(obj.attributes);
    }

    private static String fmtLinkTargetList(Node[] nodes) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < nodes.length-1; i++) {
            sb.append(nodes[i]);
            sb.append(", ");
        }
        sb.append(nodes[nodes.length-1]);
        sb.append("}");
        return sb.toString();
    }
}
