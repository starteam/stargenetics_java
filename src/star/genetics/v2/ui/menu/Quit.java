package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;

import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Main;
import star.genetics.Messages;

@SignalComponent(extend = MenuItem.class)
public class Quit extends Quit_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Quit.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_Q);
	}

	@Override
	protected void onAction()
	{
		Main.close(Helper.isModelModified(this));
	}

}
