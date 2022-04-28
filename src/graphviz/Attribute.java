package graphviz;

public class Attribute {
    private final String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String setValue(String newValue) {
        String s = value;
        value = newValue;
        return s;
    }

    @Override
    public String toString() {
        return Formatter.fmtAttrib(this);
    }
}
