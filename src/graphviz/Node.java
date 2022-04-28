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
}
