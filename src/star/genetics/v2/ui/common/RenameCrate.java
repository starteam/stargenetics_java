/**
 * 
 */
package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import star.genetics.Messages;

public class RenameCrate extends Button
{
	private static final long serialVersionUID = 1L;
	private final CrateInterface crate;

	public RenameCrate(CrateInterface crate)
	{
		this.crate = crate;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return null;
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_C;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("RenameCrate.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected void onAction()
	{
		String name = (String) JOptionPane.showInputDialog(this.crate.getParent(), Messages.getString("RenameCrate.1"), Messages.getString("RenameCrate.2"), JOptionPane.QUESTION_MESSAGE, null, null, this.crate.getCrateName()); //$NON-NLS-1$ //$NON-NLS-2$
		if (name != null && name.length() != 0)
		{
			this.crate.setCrateName(name);

		}
	}
}