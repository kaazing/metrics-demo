package org.kaazing.monitoring.test;

import static java.lang.String.format;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Utility class which connects to a k3po process, read all the bytes written by the
 * k3po script, and writes them to a given file.
 *
 */
public class WriteToFileRule implements TestRule, Runnable {
    private final int port;
    private volatile AtomicReference<Exception> exception = new AtomicReference<Exception>();
    private final Path outputFile;
    
    public WriteToFileRule(int port) {
        this.port = port;
        try {
            outputFile = Files.createTempFile("metrics", ".dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                Thread t = new Thread(WriteToFileRule.this, "Writer");
                t.start();
                base.evaluate(); // test method execution, will call robot.finish() to execute robot script
                t.join(3000);
                WriteToFileRule.this.cleanup();
                Exception e = exception.get();
                if ( e != null) {
                    throw e;
                }
            }
            
        };
    }

    @Override
    public void run() {
        try {
            writeScriptOutputToFile();
        } catch (Exception e) {
            WriteToFileRule.this.exception.set(e);
        }
    }
    
    public Path getOutputFile() {
        return outputFile;
    }

    private void writeScriptOutputToFile() throws Exception {
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
    }
    
    private void cleanup() {
        try {
            Files.delete(outputFile);
        } catch (IOException e) {
            System.out.println(format("Unable to delete %s, Files.delete failed with exception %s",
                    outputFile, e));
        }
    }
    
}
