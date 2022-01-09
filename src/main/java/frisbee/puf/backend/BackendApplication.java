package frisbee.puf.backend;

import org.java_websocket.server.WebSocketServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.net.InetSocketAddress;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
public class BackendApplication {

    public static void main(String[] args) {

//        SpringApplication.run(BackendApplication.class, args);

        String host = "localhost";
        int port = 8080;
        WebSocketServer server = new FrisbeeWebSocketServer(new InetSocketAddress(host, port));
        server.run();

    }

}
