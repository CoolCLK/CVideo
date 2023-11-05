package coolclk.cvideo.api;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ExtensiveFile extends File {
    public ExtensiveFile(String pathname) {
        super(pathname);
    }

    public ExtensiveFile(String parent, String child) {
        super(parent, child);
    }

    public ExtensiveFile(File parent, String child) {
        super(parent, child);
    }

    public ExtensiveFile(URI uri) {
        super(uri);
    }

    public FileInputStream getInputStream() throws IOException {
        return new FileInputStream(this);
    }

    public FileOutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this);
    }

    public byte[] getBytes() throws IOException {
        try (FileInputStream fis = this.getInputStream()) {
            return fis.readAllBytes();
        }
    }

    public String getContent() throws IOException {
        return new String(this.getBytes());
    }

    public String getContent(Charset charset) throws IOException {
        return new String(this.getBytes(), charset);
    }

    public void writeBytes(byte[] b) throws IOException {
        try (FileOutputStream fos = this.getOutputStream()) {
            fos.write(b);
        }
    }
}
