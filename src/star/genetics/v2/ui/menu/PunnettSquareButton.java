package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;

import star.genetics.Messages;
import star.genetics.v1.ui.punnett.PunnettSquareDialog;
import utils.UIHelpers;

public class PunnettSquareButton extends MenuItem
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("PunnettSquareButton.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return new MenuShortcut(KeyEvent.VK_P);
	}

	@Override
	protected void onAction()
	{
		PunnettSquareDialog p = new PunnettSquareDialog(this);
		p.setVisible(true);
		UIHelpers.track("PunnettSquare"); //$NON-NLS-1$
	}

}
