package star.genetics.v3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.SwingUtilities;

import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.genetics.events.OpenModelRaiser;
import star.genetics.events.SaveModelRaiser;
import utils.FileUtils;

@SignalComponent(raises = { OpenModelRaiser.class, SaveModelRaiser.class })
public class WebSocketComponent extends WebSocketComponent_generated implements StarXScriptingInterface
{

	WebSocketServer server;

	// open
	private InputStream stream;
	private String filename;
	private URL url;
	// save
	private OutputStream save_stream;

	public WebSocketComponent()
	{
		StarXWebSocket.starxcomponent = this;
		init();
	}

	void init()
	{
		server = new WebSocketServer();
	}

	@Override
	public void open(String surl, String title)
	{
		try
		{
			url = new URL(surl);
			stream = url.openStream();
			filename = title;
			try
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					@Override
					public void run()
					{
						raise_OpenModelEvent();
					}
				});
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			url = null;
			stream = null;
			filename = null;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void open_stream(String surl, String title, InputStream s)
	{
		try
		{
			url = new URL(surl);
			stream = s;
			filename = title;
			try
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					@Override
					public void run()
					{
						raise_OpenModelEvent();
					}
				});
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			url = null;
			stream = null;
			filename = null;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	// open model
	@Override
	public InputStream getOpenModelStream()
	{
		return stream;
	}

	@Override
	public String getModelFileName()
	{
		// TODO Auto-generated method stub
		return filename;
	}

	@Override
	public URL getModelURL()
	{
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public void save(OutputStream bos)
	{
		save_stream = bos;
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					raise_SaveModelEvent();
				}
			});
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		save_stream = null;
	}

	@Override
	public OutputStream getSaveModelStream()
	{
		return save_stream;
	}
}
