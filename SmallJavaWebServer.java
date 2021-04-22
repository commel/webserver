import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SmallJavaWebServer {
    
    public static void main(String[] args) throws Exception {
	new SmallJavaWebServer(args.length == 1 ? Integer.parseInt(args[1]) : 8080);
    }

    public SmallJavaWebServer(final int port) throws IOException {
        System.out.println("Server listening on port " + port);
	final ServerSocket serverSocket = new ServerSocket(port);
        boolean running = true;
        
        while(running) {
            final Socket socket = serverSocket.accept();
            final RequestHandler req = new RequestHandler(socket);
            req.run();
        }
    }
}
