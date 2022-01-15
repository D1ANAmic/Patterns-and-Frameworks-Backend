package frisbee.puf.backend.network;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Slf4j
@Component
class ServerThread extends Thread {

    private Socket client;

    // Default constructor required to autowire class
    public ServerThread(){}

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            log.info(client.getInetAddress().getLocalHost() + " established a connection to the server.");

            //Read messages from client
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String msg = br.readLine();
            log.info("Client:" + msg );

            //Send message to client
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bw.write(msg);
            bw.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
