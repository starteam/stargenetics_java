package star.genetics.v3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.security.Credential.MD5;
import org.eclipse.jetty.websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import utils.FileUtils;

public class StarXWebSocket implements WebSocket.OnTextMessage
{

	static StarXScriptingInterface starxcomponent;
	Connection connection;
	String seed;
	boolean run = true;

	@Override
	public void onClose(int closeCode, String message)
	{
		System.out.println("onClose: closeCode=" + closeCode + ", message=" + message);
		run = false;
	}

	@Override
	public void onOpen(Connection connection)
	{
		System.out.println("onOpen: connection=" + connection);
		this.connection = connection;
		run = true;
	}

	private void command_new(HashMap message) throws IOException
	{
		starxcomponent.open(message.get("url").toString(), message.get("title").toString());
		seed = ((HashMap) message.get("wrap")).get("seed").toString();
		response_save();
	}

	private void command_open(HashMap message) throws IOException
	{
		try
		{
			byte[] cmp_data = B64Code.decode(message.get("stream").toString());
			byte[] data = FileUtils.inflate(cmp_data);
			System.out.println("Loaded:" + data.length + "\n" + MD5.digest(new String(data)));
			starxcomponent.open_stream("http://starx.mit.edu/web_stream.sg1", message.get("title").toString(), new ByteArrayInputStream(data));
			seed = ((HashMap) message.get("wrap")).get("seed").toString();
			response_save();
		}
		catch (DataFormatException ex)
		{
			throw new IOException("Data exception", ex);
		}
	}

	private void response_save() throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		starxcomponent.save(bos);
		bos.flush();
		bos.close();
		byte[] data = FileUtils.deflate(bos.toByteArray());
		System.out.println("Saved:" + data.length + "\t" + MD5.digest(new String(data)));
		HashMap<String, String> message = new HashMap<String, String>();
		message.put("command", "save_response");
		message.put("url", "http://starx.mit.edu/web_stream.sg1");
		message.put("stream", new String(B64Code.encode(data)));
		message.put("seed", seed);
		connection.sendMessage(JSON.toString(message));
	}

	private String wrap(HashMap<String, String> data, HashMap message)
	{
		return "wrapped data";
	}

	@Override
	public void onMessage(String str_message)
	{
		System.out.println("onMessage: message=" + str_message);
		try
		{
			Object json = JSON.parse(str_message);
			if (json instanceof HashMap)
			{
				HashMap message = (HashMap) json;
				String command = String.valueOf(message.get("command"));
				if ("new".equals(command))
				{
					command_new(message);
				}
				if ("open".equals(command))
				{
					command_open(message);
				}
				if ("save".equals(command))
				{
					response_save();
				}
			}
			else
			{
				System.out.println(str_message + " is not valid command");
			}
			connection.sendMessage("{\"command\":\"ack\"}");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}