package star.genetics.v3;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class WebSocketServer {

	public static int port = 25261;
	private Server server;
	public WebSocketServer() {
		try {
			open(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void open(int port) throws Exception {
		this.server = new Server(port);

		ServletHandler servletHandler = new ServletHandler();
	    servletHandler.addServletWithMapping(WebSocketHandler.class,"/");
	    
	    DefaultHandler defaultHandler = new DefaultHandler();
	    
	    HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] {servletHandler,defaultHandler});
	    server.setHandler(handlers);

	    server.start();
	    server.join();
	}

	void close() throws Exception {
		if( server != null )
		{
			server.stop();
		}
	}
	
	public static void main(String[] args) {
		new WebSocketServer();
	}
	
}
