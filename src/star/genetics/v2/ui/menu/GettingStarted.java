package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import utils.FileUtils;
import utils.Icons;

@SignalComponent(extend = MenuItem.class)
public class GettingStarted extends GettingStarted_generated implements ActionListener
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("GettingStarted.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_G);
	}

	@Override
	protected void onAction()
	{
		String text;
		try
		{
			text = new String(FileUtils.getStreamToByteArray(this.getClass().getClassLoader().getResourceAsStream(Messages.getString("GettingStarted.1"))), Charset.forName("UTF-8")); //$NON-NLS-1$
			JScrollPane sp = new JScrollPane(new JLabel(text));

			JOptionPane.showMessageDialog(utils.UIHelpers.getFrame(utils.UIHelpers.getComponent(this)), sp, Messages.getString("GettingStarted.2"), JOptionPane.INFORMATION_MESSAGE, Icons.ABOUT.getIcon()); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}