package frisbee.puf.backend.network;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
        while (isRunning) {
            try {
                //TODO: we need a shared object between client and server, like a request object
                // TODO: when we have a shared object, we need to differentiate between the characters
                // TODO: not sure how to do it with different threads for each client though
                // TODO: the connection somehow needs to be made over the team

                String receivedJsonString = (String) inFromClient.readObject();

                ObjectMapper objectMapper = new ObjectMapper();
                SocketRequest clientRequest = objectMapper.readValue(receivedJsonString, new TypeReference<>() {
                });

                SocketRequestType messageType = clientRequest.getRequestType();

                switch (messageType){
                    case INIT:
                        initClient(clientRequest);
                    case MOVE:
                        moveCharacter(clientRequest);
                    case THROW:
                        throwFrisbee(clientRequest);
                }

            } catch(Exception e){
                e.printStackTrace();
                isRunning = false;
                SocketRequest response = new SocketRequest(SocketRequestType.READY, "false");
                sendToClient(response);
            }
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
            canStartGame = true;
            for (ServerThread clientThread : teamClientThreads) {
                if (!clientThread.equals(this)) {
                    this.otherClient = clientThread;
                }
            }
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
        log.info(client.getInetAddress().getLocalHost() + "has sent a message of type MOVE.");
        this.otherClient.sendToClient(clientRequest);
    }

    @SneakyThrows
    private void throwFrisbee(SocketRequest clientRequest) {
        log.info(client.getInetAddress().getLocalHost() + "has sent a message of type THROW.");
        this.otherClient.sendToClient(clientRequest);
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
