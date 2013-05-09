package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v2.ui.fly.CrateProvider;

@SignalComponent(extend = Button.class, raises = { CrateNewCrateRaiser.class })
public class RenameExperiment extends RenameExperiment_generated
{
	private static final long serialVersionUID = 1L;
	private CrateProvider provider;

	public RenameExperiment(CrateProvider provider)
	{
		this.provider = provider;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("RenameExperiment.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_M;
	}

	@Override
	public Icon getButtonIcon()
	{
		return null;
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return provider.getCrate() != null;
	}

	@Override
	protected void onAction()
	{
		CrateInterface crate = provider.getCrate();
		String name = (String) JOptionPane.showInputDialog(this, Messages.getString("RenameExperiment.1"), Messages.getString("RenameExperiment.2"), JOptionPane.QUESTION_MESSAGE, null, null, crate.getCrateName()); //$NON-NLS-1$ //$NON-NLS-2$
		if (name != null && name.length() != 0)
		{
			crate.setCrateName(name);
		}
		provider.updateCrateProvier();

	}

	@Handles(raises = {})
	void handleLoadModel(CrateNewCrateRaiser r)
	{
		setEnabled(provider.getCrate() != null);
	}

}
