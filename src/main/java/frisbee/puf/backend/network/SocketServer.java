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

    /**
     * Map that stores all connected client threads by team name.
     */
    private static final Map<String, Set<ServerThread>> clientThreadsByTeam =
            new HashMap<>();

    /**
     * Establishes a client-server connection.
     *
     * @param args optional arguments that can be passed
     * @throws Exception if thread couldn't be started
     */
    public void run(String... args) throws Exception {
        ServerSocket server = new ServerSocket(9999);
        while (true) {
            //Wait for client connection, if no connection is obtained, wait
            // at this step
            Socket client = server.accept();
            //Open a thread for each client connection
            new Thread(new ServerThread(client)).start();
        }
    }

    /**
     * Adds a client thread to the map of currently running threads on server.
     *
     * @param team         name of the team this thread's player is a member of
     * @param serverThread the thread that represents the client connection
     */
    public static void addClient(String team, ServerThread serverThread) {
        clientThreadsByTeam.computeIfAbsent(team, k -> new HashSet<>());
        clientThreadsByTeam.get(team).add(serverThread);
    }

    /**
     * Removes a client thread from the map of currently running threads on
     * server.
     *
     * @param team         nem of the team this thread's player is a member of
     * @param serverThread the thread that represents the client connection
     */
    public static void removeClient(String team, ServerThread serverThread) {
        clientThreadsByTeam.get(team).remove(serverThread);
    }

    /**
     * Returns the map of currently running threads on the server.
     *
     * @return Map of client threads by team
     */
    public static Map<String, Set<ServerThread>> getClientThreadsByTeam() {
        return clientThreadsByTeam;
    }
}
