package star.genetics.v2.ui.menu;

import java.awt.Component;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
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
import star.genetics.events.SaveModelRaiser;
import utils.FileUtils;
import utils.UIHelpers;

@SignalComponent(extend = MenuItem.class, raises = { SaveModelRaiser.class, ErrorDialogRaiser.class })
@Properties({ @Property(name = "saveModelStream", type = OutputStream.class, getter = Property.PUBLIC), @Property(name = "modelFileName", type = String.class, getter = Property.PUBLIC), @Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC) })
public class Save extends Save_generated
{
	private static final long serialVersionUID = 1L;

	transient Set<String> lastFolder = new HashSet<String>();

	public Save(Set<String> lastFolder)
	{
		this.lastFolder = lastFolder;
	}

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Save.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_S);
	}

	@Override
	protected void onAction()
	{
		JFileChooser saveFile = new JFileChooser();
		if (lastFolder != null && lastFolder.size() != 0)
		{
			try
			{
				saveFile.setCurrentDirectory(new File(lastFolder.iterator().next()));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
		saveFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saveFile.addChoosableFileFilter(new FileFilter()
		{

			@Override
			public boolean accept(File f)
			{
				return (f.getName().endsWith(".sg1") || f.isDirectory()); //$NON-NLS-1$
			}

			@Override
			public String getDescription()
			{
				return Messages.getString("Save.2"); //$NON-NLS-1$
			}

		});
		saveFile.showSaveDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)));
		if (saveFile.getSelectedFile() != null)
		{
			File f = saveFile.getSelectedFile();
			if (!f.getName().endsWith(".sg1")) //$NON-NLS-1$
			{
				f = new java.io.File(f.getAbsoluteFile() + ".sg1"); //$NON-NLS-1$
			}
			boolean overwrite = true;
			if (f.exists())
			{
				System.out.println(MessageFormat.format("test {0} ", "a test"));
				System.out.println(MessageFormat.format(Messages.getString("Save.5"), "a test"));
				Component parent = UIHelpers.getFrame(this);
				if (parent == null)
				{
					parent = UIHelpers.getComponent(this);
				}
				overwrite = JOptionPane.showConfirmDialog(parent, MessageFormat.format(Messages.getString("Save.5"), f.getName()), Messages.getString("Save.6"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION; //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (overwrite)
			{
				try
				{
					java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
					setSaveModelStream(fos);
					setModelFileName(f.getAbsolutePath());
					raise_SaveModelEvent();
					fos.close();
					java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
					setSaveModelStream(bos);
					raise_SaveModelEvent();
					bos.close();
					byte[] fba = FileUtils.getStreamToByteArray(new java.io.FileInputStream(f));
					byte[] bba = bos.toByteArray();
					if (!Arrays.equals(fba, bba))
					{
						new RuntimeException(MessageFormat.format("Save failed for file {0}, please try again.", f.getAbsolutePath()));
					}
					Helper.setModelSaved(this);
					UIHelpers.track(Messages.getString("Save.1")); //$NON-NLS-1$
				}
				catch (Exception ex)
				{
					setErrorMessage(new RuntimeException(ex.getLocalizedMessage(), ex));
					raise_ErrorDialogEvent();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(UIHelpers.getComponent(this), Messages.getString("Save.8")); //$NON-NLS-1$
			}
			try
			{
				lastFolder.clear();
				lastFolder.add(f.getParentFile().getAbsolutePath());
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
	}
}
