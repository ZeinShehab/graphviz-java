package graphviz;

public class Comparator {
    public static boolean equals(Node a, Node b) {
        return a.name.equals(b.name);
    }

    public static boolean equals(Link a, Link b) {
        return equals(a, b, false);
    }

    public static boolean equals(Link a, Link b, boolean order) {
        if ((a.isMultiLink() ^ b.isMultiLink())) {
            return false;
        }

        if ((a.isDirected() ^ b.isDirected())) {
            return false;
        }

        if (!a.isMultiLink()) {
            Node a1 = a.from;
            Node a2 = a.to[0];
            Node b1 = b.from;
            Node b2 = b.to[0];

            if (a.isDirected())
                return equals(a1, b1) && equals(a2, b2);
            return (equals(a1, b1) && equals(a2, b2)) || (equals(a1, b2) && equals(a2, b1));
        }

        if (!equals(a.from, b.from))
            return false;
        if (a.to.length != b.to.length)
            return false;

        
        for (int i = 0; i < a.to.length; i++) {
            if (order) {
                if (!equals(a.to[i], b.to[i]))
                    return false;
            }
            else {
                int j;
                for (j = 0; j < b.to.length; j++) {
                    if (equals(a.to[i], b.to[j]))
                        break;
                }
                if (j >= b.to.length)
                    return false;
            }
        }
        return true;
    }
}
