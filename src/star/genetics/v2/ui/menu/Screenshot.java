package star.genetics.v2.ui.menu;

import java.awt.Graphics2D;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import utils.UIHelpers;

@SignalComponent(extend = MenuItem.class, raises = { ErrorDialogRaiser.class })
@Properties({ @Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC) })
public class Screenshot extends Screenshot_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Screenshot.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_F12);
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
				return (f.getName().endsWith(".png")); //$NON-NLS-1$
			}

			@Override
			public String getDescription()
			{
				return Messages.getString("Screenshot.2"); //$NON-NLS-1$
			}

		});
		saveFile.showSaveDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)));
		if (saveFile.getSelectedFile() != null)
		{
			File f = saveFile.getSelectedFile();
			if (!f.getName().endsWith(".png")) //$NON-NLS-1$
			{
				f = new java.io.File(f.getAbsoluteFile() + ".png"); //$NON-NLS-1$
			}
			try
			{
				final java.awt.Frame frame = UIHelpers.getFrame(UIHelpers.getComponent(this));
				final float multiplier = Float.parseFloat(JOptionPane.showInputDialog(frame, Messages.getString("Screenshot.5"), "1.0")); //$NON-NLS-1$ //$NON-NLS-2$
				final JDialog dialog = new JDialog(frame, true);
				final File file = f;
				dialog.setTitle(Messages.getString("Screenshot.7")); //$NON-NLS-1$
				dialog.add(new JLabel(Messages.getString("Screenshot.8"))); //$NON-NLS-1$
				UIHelpers.centerOnParent(dialog);
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							BufferedImage image = new BufferedImage((int) (frame.getWidth() * multiplier), (int) (frame.getHeight() * multiplier), BufferedImage.TYPE_4BYTE_ABGR);
							Graphics2D g = image.createGraphics();
							g.scale(multiplier, multiplier);
							frame.printAll(g);
							g.dispose();
							ImageIO.write(image, "png", file); //$NON-NLS-1$
							UIHelpers.track(Messages.getString("Screenshot.10")); //$NON-NLS-1$
						}
						catch (Throwable t)
						{
							setErrorMessage(new RuntimeException(t.getLocalizedMessage(), t));
							raise_ErrorDialogEvent();
						}
						finally
						{
							dialog.setVisible(false);
						}
					}
				}.start();
				dialog.pack();
				dialog.setVisible(true);

			}
			catch (Exception ex)
			{
				setErrorMessage(ex);
				raise_ErrorDialogEvent();
			}
		}

	}
}
