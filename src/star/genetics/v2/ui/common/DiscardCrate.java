/**
 * 
 */
package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;

@SignalComponent(extend = Button.class, raises = { CrateNewCrateRaiser.class })
public class DiscardCrate extends DiscardCrate_generated
{
	private static final long serialVersionUID = 1L;
	private final CrateInterface crate;

	public DiscardCrate(CrateInterface crate)
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
		return Messages.getString("DiscardCrate.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return Messages.getString("DiscardCrate.1"); //$NON-NLS-1$
	}

	@Override
	protected void onAction()
	{
		int ret = JOptionPane.showConfirmDialog(this.crate.getParent(), Messages.getString("DiscardCrate.2"), Messages.getString("DiscardCrate.3"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
		if (ret == JOptionPane.OK_OPTION)
		{
			this.crate.getModel().setVisible(false);
			raise_CrateNewCrateEvent();
		}
	}

}