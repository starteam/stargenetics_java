package star.genetics.v3;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket;

public class StarXWebSocket implements WebSocket.OnTextMessage {

    Connection connection;

	@Override
	public void onClose(int closeCode, String message) {
		System.out.println( "onClose: closeCode=" + closeCode + ", message=" + message);
	}

	@Override
	public void onOpen(Connection connection) {
		System.out.println( "onOpen: connection=" + connection );
		this.connection = connection;
	}

	@Override
	public void onMessage(String message) {
		System.out.println( "onMessage: message=" + message );
		try {
			connection.sendMessage("Hello to you too!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}