package graphviz;

public class Node extends AttributedObject {
    public String name;

    public Node(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        addAtr("color", color);
    }

    public void setFillColor(String color) {
        addAtr("fillcolor", color);
    }

    public void setLabel(String label) {
        addAtr("label", label);
    }

    @Override
    public String toString() {
        return name;
    }

    public static String[] letterLabels(int size) {
        if (size > 26)
            throw new IllegalArgumentException("Max size for default label list is 26");
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return s.substring(0, size).split("");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
            
        Node other = (Node) obj;
        return Comparator.equals(this, other);
    }
}
