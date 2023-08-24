import java.io.BufferedReader;
import java.io.IOException;

/**
 * Wrapper on top of BufferedReader
 * which supports extra functionality.
 */
public class BufferedReaderPro {

    private final BufferedReader fbr;

    private String cache;

    public BufferedReaderPro(BufferedReader r) throws IOException {
        this.fbr = r;
        reload();
    }

    public void close() throws IOException {
        this.fbr.close();
    }

    public boolean empty() {
        return this.cache == null;
    }

    public String peek() {
        return this.cache;
    }

    public String poll() throws IOException {
        String answer = peek(); // make a copy
        reload();
        return answer;
    }

    private void reload() throws IOException {
        this.cache = this.fbr.readLine();
    }
}