package star.genetics.v2.ui.menu;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import star.annotations.Handles;
import star.annotations.Preferences;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.events.OpenModelRaiser;
import utils.UIHelpers;

@SignalComponent(extend = JMenu.class, raises = { OpenModelRaiser.class })
@Preferences()
public class RecentDocuments extends RecentDocuments_generated
{
	private static final long serialVersionUID = 1L;
	private static final String recentDocuments = Messages.getString("RecentDocuments.0"); //$NON-NLS-1$
	private RecentDocuments self = this;

	public RecentDocuments()
	{
		super(Messages.getString("RecentDocuments.1")); //$NON-NLS-1$
	}

	@Override
	@Handles(raises = {})
	public void openFile(OpenModelRaiser r)
	{

		java.net.URL modelURL = r.getModelURL();

		if (modelURL != null && r.getModelFileName() != null)
		{
			try
			{
				String value = URLEncoder.encode(r.getModelFileName(), "utf-8") + " " + URLEncoder.encode(modelURL.toExternalForm(), "utf-8"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (true) // remove old key
				{
					String[] keys = getPreferences(recentDocuments).keys();
					for (String key : keys)
					{
						if (value.equals(getPreferences(recentDocuments).get(key, ""))) //$NON-NLS-1$
						{
							getPreferences(recentDocuments).remove(key);
						}
					}
				}
				getPreferences(recentDocuments).put("" + System.currentTimeMillis(), value); //$NON-NLS-1$
				if (true) // trim fat about X
				{
					int max_docs = 5;
					String[] keys = getPreferences(recentDocuments).keys();
					if (keys.length > max_docs)
					{
						Arrays.sort(keys);
						for (int i = keys.length - max_docs; i < keys.length; i++)
						{
							getPreferences(recentDocuments).remove(keys[i]);
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void addNotify()
	{

		super.addNotify();
		removeAll();
		try
		{
			String[] keys = getPreferences(recentDocuments).keys();
			Arrays.sort(keys);
			Stack<JMenuItem> s = new Stack<JMenuItem>();
			for (String key : keys)
			{
				String value = getPreferences(recentDocuments).get(key, ""); //$NON-NLS-1$
				String url;
				String name;
				if (value.indexOf(' ') != -1)
				{
					url = URLDecoder.decode(value.substring(value.indexOf(' ') + 1), "utf-8"); //$NON-NLS-1$
					name = URLDecoder.decode(value.substring(0, value.indexOf(' ')), "utf-8"); //$NON-NLS-1$
				}
				else
				{
					url = URLDecoder.decode(value.substring(value.indexOf(' ') + 1), "utf-8"); //$NON-NLS-1$
					name = url;
				}
				s.push(new JMenuItem(new OpenAction(name, url)));
			}
			while (!s.isEmpty())
			{
				add(s.pop());
			}

		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		addSeparator();
		add(new JMenuItem(clear));
	}

	@Override
	public void removeNotify()
	{
		super.removeNotify();
		removeAll();
	};

	class OpenAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;
		String uri;
		String name;

		public OpenAction(String name, String uri)
		{
			super(name);
			this.name = name;
			this.uri = uri;
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				filename = name;
				url = new java.net.URL(uri);
				if (Helper.checkOpen(UIHelpers.getFrame(self), uri))
				{
					raise_OpenModelEvent();
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
	}

	Action clear = new AbstractAction(Messages.getString("RecentDocuments.11")) //$NON-NLS-1$
	{
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				String[] keys = getPreferences(recentDocuments).keys();
				for (String key : keys)
				{
					getPreferences(recentDocuments).remove(key);
				}
			}
			catch (Throwable t)
			{
				throw new RuntimeException(Messages.getString("RecentDocuments.12"), t); //$NON-NLS-1$
			}
		}
	};

	String filename;
	java.net.URL url;

	public String getModelFileName()
	{

		return filename;
	}

	public URL getModelURL()
	{
		return url;
	}

	public InputStream getOpenModelStream()
	{
		try
		{
			return url.openStream();
		}
		catch (Exception ex)
		{
			throw new RuntimeException(Messages.getString("RecentDocuments.13") + url, ex); //$NON-NLS-1$
		}
	}

}
