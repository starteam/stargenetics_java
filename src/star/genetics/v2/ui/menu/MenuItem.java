package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

import utils.UIHelpers;

public abstract class MenuItem extends JMenuItem implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public MenuItem()
	{
		super();
		setText(getMenuName());
		if (getMenuShortcut() != null)
		{
			setMnemonic(getMenuShortcut().getKey());
			setAccelerator(UIHelpers.getAcceleratorKey(KeyEvent.getKeyText(getMenuShortcut().getKey()).charAt(0)));
		}
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		onAction();
	}

	protected abstract void onAction();

	protected abstract MenuShortcut getMenuShortcut();

	protected abstract String getMenuName();

}
