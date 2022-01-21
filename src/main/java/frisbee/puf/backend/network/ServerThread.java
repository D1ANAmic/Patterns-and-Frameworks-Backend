package frisbee.puf.backend.network;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

@Slf4j
@Component
class ServerThread extends Thread {

    private Socket client;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private boolean isRunning;
    private String teamName;
    private ServerThread otherClient;


    // Default constructor required to autowire class
    public ServerThread(){}

    public ServerThread(Socket client) {
        this.client = client;
        try {
            outToClient = new ObjectOutputStream(client.getOutputStream());
            inFromClient = new ObjectInputStream(client.getInputStream());
            isRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            log.info(client.getInetAddress().getLocalHost() + " established a connection to the server.");
        }  catch(Exception e){
            e.printStackTrace();
        }

        // TODO: stop if something breaks in client
        while (this.isRunning) {
            try {
                String receivedJsonString = (String) inFromClient.readObject();

                ObjectMapper objectMapper = new ObjectMapper();
                SocketRequest clientRequest = objectMapper.readValue(receivedJsonString, new TypeReference<>() {
                });

                SocketRequestType messageType = clientRequest.getRequestType();

                switch (messageType) {
                    case INIT -> initClient(clientRequest);
                    case MOVE -> moveCharacter(clientRequest);
                    case THROW -> throwFrisbee(clientRequest);
                    case GAME_RUNNING -> startGame(clientRequest);
                    case DISCONNECT -> disconnect();
                }

            } catch(Exception e){
                isRunning = false;
            }
        }
        disconnect();
        try {
            log.info("Connection to " + client.getInetAddress().getLocalHost() + " closed");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    private void initClient(SocketRequest clientRequest){
        log.info(client.getInetAddress().getLocalHost() + "has sent a message of type INIT.");

        this.teamName = clientRequest.getValue();
        //add client to list of client threads
        SocketServer.addClient(this.teamName, this);
        Set<ServerThread> teamClientThreads = SocketServer.getClientThreadsByTeam().get(this.teamName);
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
        SocketRequest response = new SocketRequest(SocketRequestType.READY, Boolean.toString(canStartGame));
        if (canStartGame){
            this.otherClient.sendToClient(response);
        }
        sendToClient(response);

    }

    @SneakyThrows
    private void moveCharacter(SocketRequest clientRequest){
        log.info(client.getInetAddress().getLocalHost() + " has sent a message of type MOVE.");
        this.otherClient.sendToClient(clientRequest);
    }

    @SneakyThrows
    private void throwFrisbee(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + " has sent a message of type THROW.");
        this.otherClient.sendToClient(clientRequest);
    }

    @SneakyThrows
    private void startGame(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + " has sent a message of type GAME_RUNNING.");
        this.otherClient.sendToClient(clientRequest);
    }

    @SneakyThrows
    private void disconnect() {
        log.info(client.getInetAddress().getLocalHost() + " was disconnected.");
        // remove this client thread from client map
        SocketServer.removeClient(this.teamName, this);
        // if other client initialized the disconnect, this.otherclient was simultaneously set to null and doesn't need
        // to be informed again about the disconnect
        if (this.otherClient != null) {
            SocketRequest response = new SocketRequest(SocketRequestType.READY, "false");
            this.otherClient.sendToClient(response);
            // notify the other client, that this client is no longer connected by removing this client
            this.otherClient.otherClient = null;
            this.isRunning = false;
        }
    }

    private void sendToClient(SocketRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(request);
            outToClient.writeObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: stop thread, e.g. when client is shut down
}
