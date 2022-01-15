package frisbee.puf.backend.network;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Slf4j
@Component
class ServerThread extends Thread {

    private Socket client;
    private BufferedReader br;
    private BufferedWriter bw;

    // Default constructor required to autowire class
    public ServerThread(){}

    public ServerThread(Socket client) {
        this.client = client;
        try {
            this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            log.info(client.getInetAddress().getLocalHost() + " established a connection to the server.");

            // TODO: some sort of shutdown connection close is needed
            //Read messages from client
            String msg = br.readLine();
            log.info("Client:" + msg);

            // TODO: use enums and stuff here, like in client
            if (msg != null) {
                //Send message to client
                log.info("To Client:" + msg);
                bw.write(msg + "\n");
                bw.flush();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
