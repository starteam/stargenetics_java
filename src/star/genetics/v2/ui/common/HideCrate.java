/**
 * 
 */
package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import star.genetics.Messages;

public class HideCrate extends Button
{
	private static final long serialVersionUID = 1L;
	private final CrateInterface crate;

	public HideCrate(CrateInterface crate)
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
		return KeyEvent.VK_D;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("HideCrate.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected void onAction()
	{
		int ret = JOptionPane.showConfirmDialog(this.crate.getParent(), Messages.getString("HideCrate.1"), Messages.getString("HideCrate.2"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
		if (ret == JOptionPane.OK_OPTION)
		{
			this.crate.getModel().setVisible(false);
		}
	}

}