import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class RequestHandler implements Runnable {

    final static String DOCROOT = new File(System.getProperty("user.dir")).getAbsolutePath();    
    private final Socket socket;
    
    public RequestHandler(final Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try (final InputStream in = socket.getInputStream()) {
            System.out.println("Running with Socket at remote port" + socket.getPort());
            final InputStreamReader reader = new InputStreamReader(in);

            final char[] inbuffer = new char[255];
            final int r = reader.read(inbuffer);
            
            // Ende der ersten Zeile finden
            int newline = -1;
            for (int i=0; i < r; i++) {
                if (inbuffer[i] == '\n') {
                    newline = i;
                    break;
                }
            }
            
            // Protokoll implementieren
            final String elements[] = new String(inbuffer, 0, newline).split(" ");
            for (int i=0; i < elements.length; i++) {
                System.out.println("Elements #" + i + " => " + elements[i]);
            }
            if (elements.length == 3) {
                if (elements[0].equals("GET")) {
                    // Datei holen
                    final String requestedFilename = elements[1];
                    final String availableFilename;
                    if (requestedFilename.length() == 1 && requestedFilename.charAt(0) == '/') {
                        availableFilename = "/index.html";
                    } else {
                        availableFilename = requestedFilename;
                    }
                    final File f = new File(DOCROOT, availableFilename);
                    
                    System.out.println("Requested file: " + f.getAbsolutePath());
                    System.out.println("DocRoot       : " + DOCROOT);
                    
                    if (f.getAbsolutePath().startsWith(DOCROOT)) {
                        System.out.println("Serving file " + f.getAbsolutePath());

                        final OutputStream out = socket.getOutputStream();
                        try (final FileInputStream fis = new FileInputStream(f)) {
                            // Protokoll verschicken
                            out.write("HTTP/1.0 200 OK\n".getBytes());
                            out.write(String.format("Date: %s\n", new Date()).getBytes());
                            out.write("Server: Java\n".getBytes());
                            out.write(String.format("Content-Length: %d\n", f.length()).getBytes());
                            out.write("Content-Type: text/html\n\n".getBytes());
                            
                            // Datei verschicken
                            final byte[] outbuffer = new byte[1024];
                            int fd = 0;
                            while ((fd = fis.read(outbuffer)) > 0) {
                                out.write(outbuffer, 0, fd);
                            }
                            System.out.println("Done");
                        } catch(final FileNotFoundException fnf) {
                            System.err.println("File not found");
                            System.err.println(fnf);
                            out.write("HTTP/1.1 404 Not found\n".getBytes());
                            //out.write("Connection: Closed\n".getBytes());
                            System.out.println("Done 404");
                        } catch(final IOException ioex) {
                            System.err.println(ioex);
                        }
                        out.flush();
                        System.out.println("Done flushing");
                    }
                }
            }
            
        } catch (final IOException ex) {
            System.err.println(ex);
        }
        
    }
    
}
