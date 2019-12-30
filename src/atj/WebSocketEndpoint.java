package atj;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
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

	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println(message);
		for (Session aSession:sessions) {
			if(!aSession.equals(session)){
				try {
					aSession.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	// metoda do odbierania plików w formacie binarnym
	@OnMessage(maxMessageSize = 1024000)
	public void onMessage(byte[] buffer, Session session) {
		try {
			//jak podłączyć, żeby wysyłać różne pliki, jak przesłać nazwę pliku
//			Files.write(new File("D:/ATJ/CHAT/file.txt").toPath(), buffer);
			Path fileName = new File(buffer.toString()).toPath().getFileName();
			Path filePath = Paths.get(("D:/ATJ/przeslaneNaServer/" + fileName));
			Files.write(filePath, buffer);


		//???
		} catch (IOException e) {
			e.printStackTrace();
		};

		System.out.println("New Binary Message Received");
		System.out.println(buffer.length);



	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason){
		sessions.remove(session);
	}
	@OnError
	public void onError(Throwable error) {}
	

}