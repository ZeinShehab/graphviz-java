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

    public static final String DEFAULT_FILE_TYPE = "png";
    public static final int DEFAULT_DPI = 300;

    public Graphviz() {
        this("/opt/homebrew/bin/dot", "/tmp");
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

    public StatusCode writeGraphToFile(String fileName, String fileType, int dpi) {
        try {
            File imageFile = new File(fileName + "." + fileType);
            imageFile.createNewFile();
            File sourceFile = writeSourceToTempFile();

            Runtime runtime = Runtime.getRuntime();
            String[] cmd = { executablePath, "-T"+fileType, "-Gdpi="+dpi, sourceFile.getAbsolutePath(), "-o", imageFile.getAbsolutePath() };
            Process p = runtime.exec(cmd);
            return p.waitFor() == 0 ? StatusCode.OK : StatusCode.SYNTAX_ERROR;
        } 
        catch (IOException e) {
            e.printStackTrace();
            return StatusCode.IO_ERROR;
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
            return StatusCode.PROCESS_INTERRUPTED;
        }
    }

    private File writeSourceToTempFile() throws IOException {
        File sourceFile = File.createTempFile("graph_", ".dot.tmp", new File(tempDir));
        PrintStream writer = new PrintStream(sourceFile);
        writer.print(source.toString());
        writer.close();

        return sourceFile;
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
