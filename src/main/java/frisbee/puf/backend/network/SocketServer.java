package frisbee.puf.backend.network;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class SocketServer implements CommandLineRunner {

    private static Map<String, Set<ServerThread>> clientThreadsByTeam = new HashMap<>();

    public void run (String... args) throws Exception {
        ServerSocket server = new ServerSocket(9999);
        while (true) {
            //Wait for client connection, if no connection is obtained, wait at this step
            Socket client = server.accept();
            //Open a thread for each client connection
            new Thread(new ServerThread(client)).start();
        }
    }

    public static void addClient(String team, ServerThread serverThread){
        clientThreadsByTeam.computeIfAbsent(team, k -> new HashSet<>());
        clientThreadsByTeam.get(team).add(serverThread);
    }

    public static Map<String, Set<ServerThread>> getClientThreadsByTeam(){
        return clientThreadsByTeam;
    }
}