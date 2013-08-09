package star.genetics.v3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import star.annotations.SignalComponent;
import star.genetics.events.OpenModelRaiser;
import star.genetics.events.SaveModelRaiser;

@SignalComponent(raises={OpenModelRaiser.class, SaveModelRaiser.class })
public class WebSocketComponent extends WebSocketComponent_generated implements StarXScriptingInterface {
 
	WebSocketServer server ;
	
	// open
	private InputStream stream;
	private String filename;
	private URL url;
	// save
	private OutputStream save_stream;
	
	public WebSocketComponent() {
		StarXWebSocket.starxcomponent = this;
		init();
	}

	void init()
	{
		server = new WebSocketServer();		
	}

	@Override
	public void open(String surl, String title ) {
		try {
			url = new URL(surl);
			stream = url.openStream();
			filename = title;
			raise_OpenModelEvent();
			url = null ;
			stream = null ;
			filename = null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// open model
	@Override
	public InputStream getOpenModelStream() {
		return stream;
	}

	@Override
	public String getModelFileName() {
		// TODO Auto-generated method stub
		return filename;
	}

	@Override
	public URL getModelURL() {
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public void save(ByteArrayOutputStream bos) {
		save_stream = bos;
		raise_SaveModelEvent();
		save_stream = null;
	}

	@Override
	public OutputStream getSaveModelStream() {
		return save_stream;
	}
}
