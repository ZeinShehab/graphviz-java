package graphviz;

import java.util.HashMap;
import java.util.Map;

public class AttributedObject {
    protected Map<String, String> attributes;

    public AttributedObject() {
        attributes = new HashMap<>();
    }

    public void addAtr(String name, String value) {
        attributes.put(name, value);
    }

    public void addAtr(Attribute attrib) {
        attributes.put(attrib.getName(), attrib.getValue());
    }

    public void removeAtr(String name) {
        attributes.remove(name);
    }

    public void removeAtr(String name, String value) {
        attributes.remove(name, value);
    }

    public void removeAtr(Attribute attrib) {
        removeAtr(attrib.getName(), attrib.getValue());
    }

    public boolean hasAtr(String name) {
        return attributes.containsKey(name);
    }

    public String valueOfAtr(String name) {
        return attributes.get(name);
    }

    public Map<String, String> getAttributes() {
        Map<String, String> map = new HashMap<>(attributes);
        return map;
    }
}
