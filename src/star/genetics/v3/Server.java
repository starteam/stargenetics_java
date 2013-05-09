package star.genetics.v3;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import utils.FileUtils;

public class Server
{
	private org.eclipse.jetty.server.Server server;
	private ResourceHandler resource_handler;
	private Handler star_handler;
	private ExternalScriptingInterfaceImpl ifc = new ExternalScriptingInterfaceImpl();
	static String prefix = "/Users/ceraj/Sites";
	final static String uri_prefix = "/genetics/server/";

	void initializeStaticHandler()
	{
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		System.out.println("PREFIX: " + prefix);
		resource_handler.setResourceBase(prefix);
		this.resource_handler = resource_handler;
	}

	void initializeStarGeneticsHandler()
	{
		Handler starGeneticsHandler = new AbstractHandler()
		{

			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
			{
				if (baseRequest.isHandled())
				{
					return;
				}
				if (target != null && target.startsWith(uri_prefix))
				{
					String data = new String(FileUtils.getStreamToByteArray(request.getInputStream()));
					if ("GET".equals(request.getMethod()))
					{
						data = request.getParameter("data");
					}

					if (data != null && !data.equals(""))
					{
						response.getWriter().print(ifc.invoke(data));
						response.flushBuffer();
					}
					else
					{
						response.getWriter().print("REQUEST EMPTY");
						response.flushBuffer();
					}
				}
			}
		};
		this.star_handler = starGeneticsHandler;
	}

	void run()
	{
		try
		{

			server = new org.eclipse.jetty.server.Server(8080);

			HandlerList handlers = new HandlerList();
			initializeStaticHandler();
			initializeStarGeneticsHandler();
			handlers.setHandlers(new Handler[] { star_handler, resource_handler, new DefaultHandler() });
			server.setHandler(handlers);
			server.start();
			server.join();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		prefix = args[0];
		(new Server()).run();
	}
}
