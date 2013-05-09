package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.events.ExportModelRaiser;
import utils.UIHelpers;

@SignalComponent(extend = MenuItem.class, raises = { ExportModelRaiser.class, ErrorDialogRaiser.class })
@Properties({ @Property(name = "modelStream", type = OutputStream.class, getter = Property.PUBLIC), @Property(name = "modelFileName", type = String.class, getter = Property.PUBLIC), @Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC) })
public class Export extends Export_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Export.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_E);
	}

	@Override
	protected void onAction()
	{
		JFileChooser saveFile = new JFileChooser();
		saveFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saveFile.addChoosableFileFilter(new FileFilter()
		{

			@Override
			public boolean accept(File f)
			{
				return (f.getName().endsWith(".xls") || f.isDirectory()); //$NON-NLS-1$
			}

			@Override
			public String getDescription()
			{
				return Messages.getString("Export.2"); //$NON-NLS-1$
			}

		});
		saveFile.showSaveDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)));
		if (saveFile.getSelectedFile() != null)
		{
			File f = saveFile.getSelectedFile();
			if (f.getName().indexOf('.') == -1)
			{
				f = new java.io.File(f.getAbsoluteFile() + ".xls"); //$NON-NLS-1$
			}
			try
			{
				java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
				setModelStream(new java.io.FileOutputStream(f));
				setModelFileName(f.getAbsolutePath());
				raise_ExportModelEvent();
				fos.close();
				UIHelpers.track(Messages.getString("Export.4")); //$NON-NLS-1$
			}
			catch (Exception ex)
			{
				setErrorMessage(new RuntimeException(ex.getLocalizedMessage(), ex));
				raise_ErrorDialogEvent();
			}
		}
	}

}
