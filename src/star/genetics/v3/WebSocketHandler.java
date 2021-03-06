package star.genetics.v3;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;

public class WebSocketHandler extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private WebSocketFactory _wsFactory;

	public static StarXWebSocket messageHandler;

	@Override
	public void init() throws ServletException
	{
		// Create and configure WS factor
		_wsFactory = new WebSocketFactory(new WebSocketFactory.Acceptor()
		{
			public boolean checkOrigin(HttpServletRequest request, String origin)
			{
				// Allow all origins
				return true;
			}

			public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol)
			{
				if ("stargenetics".equals(protocol))
				{
					messageHandler = new StarXWebSocket();
					return messageHandler;
				}
				return null;
			}

		})
		{
			@Override
			public boolean acceptWebSocket(HttpServletRequest arg0, HttpServletResponse arg1) throws IOException
			{
				return super.acceptWebSocket(arg0, arg1);
			}

		};
		_wsFactory.setBufferSize(4096);
		_wsFactory.setMaxIdleTime(60000);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		if (_wsFactory.acceptWebSocket(request, response))
			return;
		else if (messageHandler != null)
		{
			String data = request.getParameter("data");
			messageHandler.onMessage(data);
		}
		else
		{
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Websocket only");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		if (messageHandler != null)
		{
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "POST OPTIONS");
			response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
			response.addHeader("Access-Control-Allow-Headers", "X-CSRFToken");

			String data = request.getParameter("data");
			messageHandler.onMessage(data);
		}
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
		response.addHeader("Access-Control-Allow-Headers", "X-CSRFToken");
	}
}
