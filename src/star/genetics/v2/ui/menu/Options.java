package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;

import star.annotations.SignalComponent;
import star.genetics.Messages;

@SignalComponent(extend = MenuItem.class)
public class Options extends Options_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Options.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return null;
	}

	@Override
	protected void onAction()
	{
		System.out.println(Messages.getString("Options.1")); //$NON-NLS-1$
	}
}
