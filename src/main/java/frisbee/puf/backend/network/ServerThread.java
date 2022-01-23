package frisbee.puf.backend.network;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

@Slf4j
@Component
class ServerThread extends Thread {

    /**
     * The client's socket.
     */
    private Socket client;
    /**
     * The output stream to the client.
     */
    private ObjectOutputStream outToClient;
    /**
     * The input stream from the client.
     */
    private ObjectInputStream inFromClient;
    /**
     * symbolizes if thread is running.
     */
    private boolean isRunning;
    /**
     * String representation of a team name.
     */
    private String teamName;
    /**
     * Reference to the thread of the other player in the client' team.
     */
    private ServerThread otherClient;


    /**
     * Default constructor required to autowire class.
     */
    ServerThread() {
    }

    /**
     * Constructs a ServerThread and passes the client to establish a connection
     * between the two sockets. In case of success the output and input
     * streams are initialized and isRunning is set to true.
     *
     * @param client
     */
    ServerThread(Socket client) {
        this.client = client;
        try {
            outToClient = new ObjectOutputStream(client.getOutputStream());
            inFromClient = new ObjectInputStream(client.getInputStream());
            isRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets called after constructing a ServerThread instance.
     */
    @Override
    public void run() {
        try {
            log.info(client.getInetAddress().getLocalHost() + " established a"
                    + " connection to the server.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (this.isRunning) {
            try {
                String receivedJsonString = (String) inFromClient.readObject();

                ObjectMapper objectMapper = new ObjectMapper();
                SocketRequest clientRequest =
                        objectMapper.readValue(receivedJsonString,
                                new TypeReference<>() {
                                });

                SocketRequestType messageType = clientRequest.getRequestType();

                switch (messageType) {
                    case INIT -> initClient(clientRequest);
                    case MOVE -> moveCharacter(clientRequest);
                    case THROW -> throwFrisbee(clientRequest);
                    case GAME_RUNNING -> startGame(clientRequest);
                    case DISCONNECT -> disconnect();
                }

            } catch (Exception e) {
                isRunning = false;
            }
        }
        disconnect();
        try {
            log.info("Connection to " + client.getInetAddress().getLocalHost()
                    + " closed");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    /**
     * Receives a client request and tries to initialize the connection to
     * both team players in order to start the game.
     *
     * @param clientRequest the client's initialization request
     */
    @SneakyThrows
    private void initClient(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + "has sent a message"
                + " of type INIT.");

        this.teamName = clientRequest.getValue();
        //add client to list of client threads
        SocketServer.addClient(this.teamName, this);
        Set<ServerThread> teamClientThreads =
                SocketServer.getClientThreadsByTeam().get(this.teamName);
        boolean canStartGame = false;
        if (teamClientThreads.size() == 2) {
            for (ServerThread clientThread : teamClientThreads) {
                if (!clientThread.equals(this)) {
                    this.otherClient = clientThread;
                    canStartGame = true;
                }
            }
            this.otherClient.otherClient = this;
        }
        log.info(String.valueOf(canStartGame));
        SocketRequest response = new SocketRequest(SocketRequestType.READY,
                Boolean.toString(canStartGame));
        if (canStartGame) {
            this.otherClient.sendToClient(response);
        }
        sendToClient(response);

    }

    /**
     * Receives a client request with a movement instruction and sends it to
     * the other team player's client.
     *
     * @param clientRequest the client's movement request
     */
    @SneakyThrows
    private void moveCharacter(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + " has sent a "
                + "message of type MOVE.");
        this.otherClient.sendToClient(clientRequest);
    }

    /**
     * Receives a client request with a frisbee throwing instruction and
     * sends it to the other team player's client.
     *
     * @param clientRequest the client's frisbee throwing request
     */
    @SneakyThrows
    private void throwFrisbee(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + " has sent a "
                + "message of type THROW.");
        this.otherClient.sendToClient(clientRequest);
    }

    /**
     * Receives a client request to start the game and sends it to the other
     * team player's client.
     *
     * @param clientRequest the client's run game request
     */
    @SneakyThrows
    private void startGame(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + " has sent a "
                + "message of type GAME_RUNNING.");
        this.otherClient.sendToClient(clientRequest);
    }

    /**
     * Disconnects the client by removing its thread from the SocketServer's
     * list of running threads, notifies the other team player's client and
     * sets isRunning to false in order to stop this thread.
     */
    @SneakyThrows
    private void disconnect() {
        log.info(client.getInetAddress().getLocalHost() + " was disconnected.");
        // remove this client thread from client map
        SocketServer.removeClient(this.teamName, this);
        // if other client initialized the disconnect, this.otherclient was
        // simultaneously set to null and doesn't need
        // to be informed again about the disconnect
        if (this.otherClient != null) {
            SocketRequest response =
                    new SocketRequest(SocketRequestType.READY, "false");
            this.otherClient.sendToClient(response);
            // notify the other client, that this client is no longer
            // connected by removing this client
            this.otherClient.otherClient = null;
            this.isRunning = false;
        }
    }

    /**
     * Converts a SocketRequest object into a JSON-String and writes it into
     * a client's output stream.
     *
     * @param request the SocketRequest object to be sent to a client
     */
    private void sendToClient(SocketRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            outToClient.writeObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
