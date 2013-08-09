package star.genetics.v3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

public class StarXWebSocket implements WebSocket.OnTextMessage {

	static StarXScriptingInterface starxcomponent;
	Connection connection;
	boolean run = true;

	@Override
	public void onClose(int closeCode, String message) {
		System.out.println("onClose: closeCode=" + closeCode + ", message="
				+ message);
		run = false;
	}

	@Override
	public void onOpen(Connection connection) {
		System.out.println("onOpen: connection=" + connection);
		this.connection = connection;
		run = true;
	}

	@Override
	public void onMessage(String str_message) {
		System.out.println("onMessage: message=" + str_message);
		try {
			Object json = JSON.parse(str_message);
			if (json instanceof HashMap) {
				HashMap message = (HashMap) json;
				String command = String.valueOf(message.get("command"));
				if ("open".equals(command)) {
					starxcomponent.open( message.get("url").toString() , message.get("title").toString());
				}
				if( "save".equals(command)) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					starxcomponent.save( bos );
					HashMap<String, String> data = new HashMap<String, String>();
					data.put( "command", "save_response");
					data.put( "blob" , new String(B64Code.encode(bos.toByteArray())));
					connection.sendMessage(JSON.toString(data));
				}
			} else {
				System.out.println(str_message + " is not valid command");
			}
			connection.sendMessage("{\"command\":\"ack\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

 }