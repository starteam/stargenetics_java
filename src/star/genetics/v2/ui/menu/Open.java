package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.events.OpenModelRaiser;
import utils.FileUtils;
import utils.UIHelpers;

@SignalComponent(extend = MenuItem.class, raises = { OpenModelRaiser.class, ErrorDialogRaiser.class })
@Properties({ @Property(name = "modelBytes", type = byte[].class), @Property(name = "modelFileName", type = String.class, getter = Property.PUBLIC), @Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC) })
public class Open extends Open_generated
{
	private static final long serialVersionUID = 1L;

	transient Set<String> lastFolder = new HashSet<String>();

	public Open(Set<String> lastFolder)
	{
		super();
		this.lastFolder = lastFolder;
	}

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Open.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_O);
	}

	@Override
	protected void onAction()
	{
		if (Helper.continueIfModelModified(this))
		{
			onActionContinue();
		}
	}

	protected void onActionContinue()
	{
		JFileChooser openFile = new JFileChooser();
		if (lastFolder != null && lastFolder.size() != 0)
		{
			try
			{
				openFile.setCurrentDirectory(new File(lastFolder.iterator().next()));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
		openFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
		final FileFilter filter = new FileFilter()
		{

			@Override
			public boolean accept(File f)
			{
				return (f.getName().endsWith(".sg1")) || (f.getName().endsWith(".xls")) || (f.getName().endsWith(".sgz") || f.isDirectory()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

			@Override
			public String getDescription()
			{
				return Messages.getString("Open.4"); //$NON-NLS-1$
			}

		};
		openFile.addChoosableFileFilter(filter);
		if (openFile.getCurrentDirectory() != null)
		{
			File[] list = openFile.getCurrentDirectory().listFiles(new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					java.io.File f = new java.io.File(dir, name);
					return f.isFile() && filter.accept(new java.io.File(dir, name));
				}
			});
			if (list != null && list.length != 0)
			{
				openFile.setFileFilter(filter);
			}
		}
		openFile.showOpenDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)));
		if (openFile.getSelectedFile() != null)
		{
			try
			{
				lastFolder.clear();
				lastFolder.add(openFile.getSelectedFile().getParentFile().getAbsolutePath());
				setModelBytes(FileUtils.getStreamToByteArray(new FileInputStream(openFile.getSelectedFile())));
				setModelFileName(openFile.getSelectedFile().getName());
				modelURL = openFile.getSelectedFile().toURI().toURL();
				if (Helper.checkOpen(UIHelpers.getFrame(UIHelpers.getComponent(this)), modelURL.toURI().toString()))
				{
					raise_OpenModelEvent();
					UIHelpers.track("Open/" + openFile.getSelectedFile().getName()); //$NON-NLS-1$
				}
			}
			catch (FileNotFoundException ex)
			{
				JOptionPane.showMessageDialog(this, Messages.getString("Open.6") + openFile.getSelectedFile()); //$NON-NLS-1$
			}
			catch (Exception ex)
			{
				setErrorMessage(new RuntimeException(ex.getLocalizedMessage(), ex));
				raise_ErrorDialogEvent();
			}
		}
	}

	java.net.URL modelURL;

	public URL getModelURL()
	{
		return modelURL;
	}

	public InputStream getOpenModelStream()
	{
		return new java.io.ByteArrayInputStream(getModelBytes());
	}

}
