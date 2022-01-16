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


    // Default constructor required to autowire class
    public ServerThread(){}

    public ServerThread(Socket client) {
        this.client = client;
        try {
            outToClient = new ObjectOutputStream(client.getOutputStream());
            inFromClient = new ObjectInputStream(client.getInputStream());
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
        while (true) {
            try {
                //TODO: we need a shared object between client and server, like a request object
                //Read request from client
                String direction = (String) inFromClient.readObject();
                log.info("Client:" + direction);

                // TODO: when we have a shared object, we need to differentiate between the characters
                // TODO: not sure how to do it with different threads for each client though
                // TODO: the connection somehow needs to be made over the team
                if (direction.equals("left") || direction.equals("right")) {
                    //Send request to client
                    log.info("To Client:" + direction);
                    // TODO: return real value, for now just return the opposite of the own character value
                    outToClient.writeObject(
                            direction.equals("left") ? "right" : "left"
                    );
                }
            } catch(Exception e){
                e.printStackTrace();
            }
         }
    }

    // TODO: stop thread, e.g. when client is shut down
}
