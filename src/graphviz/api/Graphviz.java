package graphviz.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Graphviz API for java
 */
public class Graphviz {
    private StringBuilder source = new StringBuilder();
    private String tempDir;
    private String executablePath;

    public Graphviz() {
        this("/usr/bin/dot", "/tmp");
    }

    public Graphviz(String executablePath, String tempDir) {
        this.executablePath = executablePath;
        this.tempDir = tempDir;
    }

    public String getSource() {
        return source.toString();
    }

    public void add(String line) {
        add(line, true);
    }

    public void add(String line, boolean semicolon) {
        source.append(line + (semicolon ? ";" : ""));
    }

    public void addln(String line) {
        if (line.length() == 0)
            addln(line, false);
        addln(line, true);
    }

    public void addln(String line, boolean semicolon) {
        String br = semicolon ? ";\n" : "\n";
        source.append(line + br);
    }

    public void clearGraph() {
        source = new StringBuilder();
    }

    public int writeGraphToFile(String filePath) {
        try {
            File imageFile = new File(filePath);
            imageFile.createNewFile();
            File sourceFile = writeSourceToTempFile();

            Runtime runtime = Runtime.getRuntime();
            String[] cmd = { executablePath, "-Tpng", sourceFile.getAbsolutePath(), "-o", imageFile.getAbsolutePath() };
            Process p = runtime.exec(cmd);
            return p.waitFor();
        } 
        catch (IOException e) {
            e.printStackTrace();
            return 1;
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private File writeSourceToTempFile() {
        try {
            File sourceFile = File.createTempFile("graph_", ".dot.tmp", new File(tempDir));
            PrintStream writer = new PrintStream(sourceFile);
            writer.print(source.toString());
            writer.close();

            return sourceFile;
        } 
        catch (IOException e) {
            System.err.println("Failed to write source to temp file");
            e.printStackTrace();
        }
        return null;
    }

    public void startDigraph() {
        addln("digraph G {", false);
    }

    public void startGraph() {
        addln("graph G {", false);
    }

    public void startSubGraph(int clusterId) {
        addln("subgraph cluster_" + clusterId + " {", false);
    }

    public void endGraph() {
        addln("}", false);
    }
}
