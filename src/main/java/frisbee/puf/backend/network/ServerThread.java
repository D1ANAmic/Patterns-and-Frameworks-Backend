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
                // TODO: some sort of shutdown connection close is needed
                //Read request from client
                //TODO: we need a shared object between client and server, like a request object
                String request = (String) inFromClient.readObject();
                log.info("Client:" + request);

                // TODO: use enums and stuff here, like in client
                if (request.equals("left") || request.equals("right")) {
                    //Send request to client
                    log.info("To Client:" + request);
                    outToClient.writeObject(request);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
         }
    }

    // TODO: stop thread, e.g. when client is shut down
}
