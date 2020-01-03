package atj;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/chatendpoint")

public class WebSocketEndpoint {
    private static final ConcurrentLinkedQueue<Session> sessions = new ConcurrentLinkedQueue<>();
    private String messageWithFileName;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
        if (message.contains("#123456789#")) {
            messageWithFileName = message;

        } else {
            for (Session aSession : sessions) {
                if (!aSession.equals(session)) {
                    try {
                        aSession.getBasicRemote().sendText("" + message);
//                        aSession.getBasicRemote().sendBinary();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    @OnMessage(maxMessageSize = 1024000)
    public void onMessage(byte[] buffer, Session session) {

//            Path fileName = new File(buffer.toString()).toPath().getFileName();
//            System.out.println(uploadingFileName);
//            Path filePath = Paths.get(("D:/ATJ/przeslaneNaServer/" + uploadingFileName));
//            Files.write(filePath, buffer);
        for (Session aSession : sessions) {
            if (!aSession.equals(session)) {
                try {
                    aSession.getBasicRemote().sendText(messageWithFileName);
                    aSession.getBasicRemote().sendBinary(ByteBuffer.wrap(buffer));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        System.out.println("New Binary Message Received");
        System.out.println(buffer.length);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable error) {
    }


}