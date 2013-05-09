package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;

import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.v1.ui.common.WebSamples;
import utils.UIHelpers;

public class New extends MenuItem
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("New.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{

		return new MenuShortcut(KeyEvent.VK_N);
	}

	@Override
	protected void onAction()
	{
		if (Helper.continueIfModelModified(this))
		{
			WebSamples s = new WebSamples(UIHelpers.getFrame(this));
			s.pack();
			UIHelpers.centerOnParent(s);
			s.setVisible(true);
		}
	}

}
