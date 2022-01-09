package frisbee.puf.backend;


import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
@Component
public class WebSocketEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println ("Connected, Session ID = " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        if (message.equals("quit")) {
            try {
                session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Connection closed normally"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed because of " + closeReason + ", Session ID " + session.getId());
    }

}