package project;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceRetriever {
    public static URL getResource(final String filename) throws IOException {
        // Try to load resource from jar
        URL url = ClassLoader.getSystemResource(filename);
        // If not found in jar, then load from disk
        if (url == null) {
            return new URL("file", "localhost", filename);
        } else {
            return url;
        }
    }

    public static InputStream getResourceAsStream(final String filename) throws IOException {
        // Try to load resource from jar
        InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        // If not found in jar, then load from disk
        if (stream == null) {
            return new FileInputStream(filename);
        } else {
            return stream;
        }
    }
}
