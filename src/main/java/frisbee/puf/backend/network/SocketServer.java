package frisbee.puf.backend.network;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class SocketServer implements CommandLineRunner {

    public void run (String... args) throws Exception {
        ServerSocket server = new ServerSocket(9999);
        while (true) {
            //Wait for client connection, if no connection is obtained, wait at this step
            Socket client = server.accept();
            //Open a thread for each client connection
            new Thread(new ServerThread(client)).start();
        }
    }
}