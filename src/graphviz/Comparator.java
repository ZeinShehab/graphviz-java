package graphviz;

public class Comparator {
    public static boolean equals(Node a, Node b) {
        if (a == null ^ b == null)
            return false;
        if (a == null) 
            return true;
        return a.name.equals(b.name);
    }

    public static boolean equals(Link a, Link b) {
        if (a.isMultiLink() ^ b.isMultiLink()) {
            return false;
        }

        if ((a.isDirected() ^ b.isDirected())) {
            return false;
        }

        if (!a.isMultiLink()) {
            Node a1 = a.from;
            Node a2 = a.to;
            Node b1 = b.from;
            Node b2 = b.to;

            if (a.isDirected())
                return equals(a1, b1) && equals(a2, b2);
            return (equals(a1, b1) && equals(a2, b2)) || (equals(a1, b2) && equals(a2, b1));
        }

        if (!equals(a.from, b.from))
            return false;

        if (a.targetList.size() != b.targetList.size())
            return false;

        if (!a.targetList.containsAll(b.targetList))
            return false;
        
        return true;
    }
}
