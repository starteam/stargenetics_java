package star.genetics.v3;

import java.io.IOException;
import java.util.Date;

import org.eclipse.jetty.websocket.WebSocket;

public class StarXWebSocket implements WebSocket.OnTextMessage {

    Connection connection;
    boolean run = true;
    Runnable r = new Runnable() {
		
		@Override
		public void run() {
			while( run )
			{
			try {
				System.out.println( "Thread - send message." );
				connection.sendMessage("Hello " + new Date());
				Thread.sleep( 1000 ) ;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	};

	@Override
	public void onClose(int closeCode, String message) {
		System.out.println( "onClose: closeCode=" + closeCode + ", message=" + message);
		run = false;
	}

	@Override
	public void onOpen(Connection connection) {
		System.out.println( "onOpen: connection=" + connection );
		this.connection = connection;
		run = true;
		(new Thread(r)).start();
	}

	@Override
	public void onMessage(String message) {
		System.out.println( "onMessage: message=" + message );
		try {
			if( message.startsWith("You said:")) return ;
			connection.sendMessage("Hello to you too!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}