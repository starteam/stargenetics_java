package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JOptionPane;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v2.ui.AboutBox;
import utils.Icons;

@SignalComponent(extend = MenuItem.class)
public class About extends About_generated implements ActionListener
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		System.err.println("Locale:  " + Locale.getDefault());
		System.err.println(Messages.getString("About.0"));
		return Messages.getString("About.0"); //$NON-NLS-1$
		
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_A);
	}

	@Override
	protected void onAction()
	{
		JOptionPane.showMessageDialog(utils.UIHelpers.getFrame(utils.UIHelpers.getComponent(this)), new AboutBox(), Messages.getString("About.1"), JOptionPane.INFORMATION_MESSAGE, Icons.ABOUT.getIcon()); //$NON-NLS-1$
	}
}
