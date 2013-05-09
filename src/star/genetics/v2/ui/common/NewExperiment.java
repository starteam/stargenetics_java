package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v2.ui.fly.CrateProvider;
import star.genetics.v2.yeast.events.StorableExperimentRaiser;

@SignalComponent(extend = Button.class, raises = { CrateNewCrateRaiser.class })
public class NewExperiment extends NewExperiment_generated
{
	private static final long serialVersionUID = 1L;
	private final String name;
	private CrateProvider crate;
	boolean isStorable = false;

	public NewExperiment(CrateProvider crate)
	{
		this(crate, null);
	}

	public NewExperiment(CrateProvider crate, String name)
	{
		this.crate = crate;
		this.name = name != null ? name : Messages.getString("NewExperiment.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonText()
	{
		return name;
	}

	@Override
	protected String getButtonTooltipText()
	{
		return Messages.getString("NewExperiment.1"); //$NON-NLS-1$
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_N;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return null;
	}

	@Override
	protected void onAction()
	{
		if (isStorable)
		{
			raise_CrateNewCrateEvent();
		}
		else
		{
			this.crate.getCrate().getModel().setVisible(false);
			raise_CrateNewCrateEvent();
		}
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return true;
	}

	@Override
	@Handles(raises = {})
	protected void handle(StorableExperimentRaiser r)
	{
		isStorable = true;
		setEnabled(true);
	}
}
