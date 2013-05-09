package star.genetics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import star.annotations.SignalComponent;
import star.event.Adapter;
import star.genetics.events.OpenModelRaiser;

@SignalComponent(raises = { OpenModelRaiser.class })
public class CommandLineFileOpener extends CommandLineFileOpener_generated implements Serializable
{
	private static final long serialVersionUID = 1L;

	OpenModelRaiser raiser;

	public CommandLineFileOpener()
	{
	}

	@Override
	public InputStream getOpenModelStream()
	{
		return raiser.getOpenModelStream();
	}

	@Override
	public String getModelFileName()
	{
		return raiser.getModelFileName();
	}

	@Override
	public URL getModelURL()
	{
		return raiser.getModelURL();
	}

	public void openFile(final File file)
	{
		raiser = new OpenModelRaiser()
		{

			@Override
			public void removeNotify()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public Adapter getAdapter()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addNotify()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public InputStream getOpenModelStream()
			{
				InputStream is = null;
				try
				{
					is = new FileInputStream(file);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return is;
			}

			@Override
			public URL getModelURL()
			{
				URL ret = null;
				try
				{
					ret = file.toURI().toURL();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return ret;
			}

			@Override
			public String getModelFileName()
			{
				return file.getAbsolutePath();
			}
		};
		raise_OpenModelEvent();
	}

	public void openURL(final URL url)
	{
		raiser = new OpenModelRaiser()
		{

			@Override
			public void removeNotify()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public Adapter getAdapter()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addNotify()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public InputStream getOpenModelStream()
			{
				try
				{
					return url.openStream();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public URL getModelURL()
			{
				return url;
			}

			@Override
			public String getModelFileName()
			{
				return url.getFile();
			}
		};
		raise_OpenModelEvent();

	}
}
