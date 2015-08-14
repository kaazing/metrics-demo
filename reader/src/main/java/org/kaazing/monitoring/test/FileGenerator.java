package org.kaazing.monitoring.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class which connects to a k3po process, read all the bytes written by the
 * k3po script, and writes them to a given file.
 *
 */
public class FileGenerator {
    private static FileGenerator INSTANCE = new FileGenerator();
    
    private FileGenerator() {
        
    }
    
    public FileGenerator instance() {
        return INSTANCE;
    }
    
    public Path createFile(int port) throws Exception {
        Path outputFile = Files.createTempFile(
                Long.toString(System.currentTimeMillis()), "dat");
        try(
            OutputStream out = new FileOutputStream(outputFile.toString());
            Socket socket = new Socket("localhost", port);
            InputStream in = socket.getInputStream() 
           ) {
                socket.setSoTimeout(1000);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);;
            }
            out.flush();
        }
        return outputFile;
    }

}
