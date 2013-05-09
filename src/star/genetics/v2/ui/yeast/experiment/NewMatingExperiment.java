package star.genetics.v2.ui.yeast.experiment;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.SignalComponent;
import star.genetics.v2.ui.common.Button;
import star.genetics.v2.yeast.events.NewMatingExperimentRaiser;
import star.genetics.Messages;

@SignalComponent(extend = Button.class, raises = { NewMatingExperimentRaiser.class })
public class NewMatingExperiment extends NewMatingExperiment_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getButtonText()
	{
		return Messages.getString("NewMatingExperiment.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
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
		raise_NewMatingExperimentEvent();
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return true;
	}
}