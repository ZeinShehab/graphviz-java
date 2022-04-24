package graphviz;

public class Attribute {
    public String name;
    public String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=" + "\"" + value + "\"";
    }
}
