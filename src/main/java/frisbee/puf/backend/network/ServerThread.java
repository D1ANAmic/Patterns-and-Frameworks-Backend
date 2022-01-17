package frisbee.puf.backend.network;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Slf4j
@Component
class ServerThread extends Thread {

    private Socket client;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private boolean isRunning;


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
                //Read request from client
                String request = (String) inFromClient.readObject();
                log.info("Client:" + request);

                log.info("To Client:" + request);
                // TODO: return real value, for now just return the opposite of the own character value
                outToClient.writeObject(request);
            } catch(Exception e){
                e.printStackTrace();
                isRunning = false;
            }
         }
    }

    // TODO: stop thread, e.g. when client is shut down
}
