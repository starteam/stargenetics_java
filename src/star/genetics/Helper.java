package star.genetics;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.ModelModifiedProvider;
import star.genetics.v1.ui.common.CreatureTransferHandler;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;
import utils.UIHelpers;

public class Helper
{
	public static void setVisualizerFromCreature(Visualizer v, Creature c)
	{
		v.setName(c.getName());
		v.setNote(c.getNote());
		v.setProperties(c.getProperties(), c.getSex());
	}

	public static void setVisualizerFromCreature(Visualizer v, Creature c, HashMap<String, String> additional)
	{
		v.setName(c.getName());
		v.setNote(c.getNote());
		// additional.putAll(c.getProperties());
		HashMap<String, String> prop = new HashMap<String, String>();
		prop.putAll(c.getProperties());
		prop.putAll(additional);
		v.setProperties(prop, c.getSex());
	}

	public static Map<String, String> parse(String value)
	{
		Map<String, String> ret = new TreeMap<String, String>();
		if (value != null)
		{
			if (value.contains("=")) //$NON-NLS-1$
			{
				String elements[] = value.split(","); //$NON-NLS-1$
				for (String element : elements)
				{
					if (element.indexOf('=') != -1)
					{
						String[] pair = element.split("=", 2); //$NON-NLS-1$
						ret.put(pair[0], pair[1]);
					}
					else
					{
						throw new RuntimeException(MessageFormat.format(Messages.getString("Helper.0"), element, value)); //$NON-NLS-1$
					}
				}
			}
			else if (!value.equalsIgnoreCase("wildtype")) //$NON-NLS-1$
			{
				throw new RuntimeException(Messages.getString("Helper.5")); //$NON-NLS-1$
			}
		}
		return ret;
	}

	public static String export(Map<String, String> source)
	{
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> entry : source.entrySet())
		{
			sb.append(entry.getKey() + "=" + entry.getValue() + ","); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	private static ModelModifiedProvider getModelModifiedProvider(Component c)
	{
		if (c == null)
		{
			return null;
		}
		if (c instanceof ModelModifiedProvider)
		{
			return (ModelModifiedProvider) c;
		}
		if (c instanceof JPopupMenu)
		{
			return getModelModifiedProvider(((JPopupMenu) c).getInvoker());
		}
		return getModelModifiedProvider(c.getParent());
	}

	private static String MODEL_PROVIDER_EXCEPTION = "Model Modified Provider not found"; //$NON-NLS-1$

	public static boolean isModelModified(Component c)
	{
		try
		{
			return getModelModifiedProvider(c).isModelModified();
		}
		catch (NullPointerException ex)
		{
			(new RuntimeException(MODEL_PROVIDER_EXCEPTION)).printStackTrace();
		}
		return false;
	}

	public static boolean continueIfModelModified(Component c)
	{
		try
		{
			boolean isModified = getModelModifiedProvider(c).isModelModified();
			if (isModified)
			{
				java.awt.Frame frame = UIHelpers.getFrame(c);
				int result = JOptionPane.showConfirmDialog(frame, new JLabel(Messages.getString("Helper.9")), Messages.getString("Helper.10"), JOptionPane.YES_NO_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (result == JOptionPane.NO_OPTION)
				{
					return true;
				}
				if (result == JOptionPane.YES_OPTION)
				{
					getModelModifiedProvider(c).saveModel();
					return true;
				}
				return false;
			}
		}
		catch (NullPointerException ex)
		{
			(new RuntimeException(MODEL_PROVIDER_EXCEPTION)).printStackTrace();
		}
		return true;
	}

	public static void setModelSaved(Component c)
	{
		try
		{
			getModelModifiedProvider(c).modelSaved();
		}
		catch (NullPointerException ex)
		{
			(new RuntimeException(MODEL_PROVIDER_EXCEPTION)).printStackTrace();
		}
	}

	public static void makeExportable(JComponent c, String propertyName, final int method, VisualizerFactory factory)
	{
		c.setTransferHandler(new CreatureTransferHandler(propertyName, factory));
		c.addMouseMotionListener(new MouseMotionListener()
		{

			public void mouseMoved(MouseEvent e)
			{

			}

			public void mouseDragged(MouseEvent e)
			{
				JComponent jc = (JComponent) e.getSource();
				javax.swing.TransferHandler th = jc.getTransferHandler();
				if (th != null)
				{
					th.exportAsDrag(jc, e, method);
				}
			};
		});
	}

	public static boolean checkOpen(Component self, String uri)
	{
		boolean canRaise = true;
		if (uri != null)
		{
			try
			{
				java.net.URL url = new java.net.URL(uri);
				if (url.getProtocol().equalsIgnoreCase("file")) //$NON-NLS-1$
				{
					java.io.File f = new java.io.File(URLDecoder.decode(url.getPath(), "UTF-8")); //$NON-NLS-1$
					boolean ok = f.exists() && f.canRead();
					if (!ok)
					{
						canRaise = false;
						if (!f.exists())
						{
							UIHelpers.track("OpenFailed/notexist/" + f.getAbsolutePath()); //$NON-NLS-1$
							JOptionPane.showMessageDialog(self, MessageFormat.format(Messages.getString("Helper.14"), f.getAbsolutePath())); //$NON-NLS-1$
						}
						else if (!f.canRead())
						{
							UIHelpers.track("OpenFailed/notreadable/" + f.getAbsolutePath()); //$NON-NLS-1$
							JOptionPane.showMessageDialog(self, MessageFormat.format(Messages.getString("Helper.16"), f.getAbsolutePath())); //$NON-NLS-1$
						}
					}
				}
			}
			catch (Throwable ex)
			{
				canRaise = false;
				UIHelpers.track("OpenFailed/malformedURI/" + uri); //$NON-NLS-1$
				JOptionPane.showMessageDialog(self, MessageFormat.format(Messages.getString("Helper.18"), uri)); //$NON-NLS-1$
			}
		}
		else
		{
			canRaise = false;
			UIHelpers.track("OpenFailed/uri=null"); //$NON-NLS-1$
			JOptionPane.showMessageDialog(self, Messages.getString("Helper.20")); //$NON-NLS-1$
		}
		return canRaise;
	}
}
