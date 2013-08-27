package star.genetics.v2.ui;

import java.awt.Component;
import java.io.File;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.events.SaveModelRaiser;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class, raises = { SaveModelRaiser.class, ErrorDialogRaiser.class })
@Properties({ @Property(name = "saveModelStream", type = OutputStream.class, getter = Property.PUBLIC), @Property(name = "modelFileName", type = String.class, getter = Property.PUBLIC), @Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC) })
public class ModelSaver extends ModelSaver_generated
{
	private static final long serialVersionUID = 1L;
	Component provider;

	public ModelSaver(Component mainFrame)
	{
		this.provider = mainFrame;
	}

	void saveModel()
	{
		JFileChooser saveFile = new JFileChooser();
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
				return Messages.getString("ModelSaver.1"); //$NON-NLS-1$
			}

		});
		saveFile.showSaveDialog(UIHelpers.getFrame(UIHelpers.getComponent(provider)));
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
				Component parent = UIHelpers.getFrame(provider);
				if (parent == null)
				{
					parent = UIHelpers.getComponent(provider);
				}
				overwrite = JOptionPane.showConfirmDialog(parent, MessageFormat.format(Messages.getString("ModelSaver.4"), f.getName()), Messages.getString("ModelSaver.5"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION; //$NON-NLS-1$ //$NON-NLS-2$
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
					Helper.setModelSaved(provider);
					UIHelpers.track(Messages.getString("ModelSaver.6")); //$NON-NLS-1$
				}
				catch (Exception ex)
				{
					setErrorMessage(new RuntimeException(ex.getLocalizedMessage(), ex));
					raise_ErrorDialogEvent();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(UIHelpers.getComponent(this), Messages.getString("ModelSaver.7")); //$NON-NLS-1$
			}
		}
	}
}