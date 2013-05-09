package star.genetics.v2.ui.yeast.experiment;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v2.ui.common.Button;
import star.genetics.v2.yeast.events.ReplicationExperimentAddAllRaiser;

@SignalComponent(extend = Button.class, raises = { ReplicationExperimentAddAllRaiser.class })
public class ReplicaExperimentAddAll extends ReplicaExperimentAddAll_generated
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Icon getButtonIcon()
	{
		return null;
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_L;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("ReplicaExperimentAddAll.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return Messages.getString("ReplicaExperimentAddAll.1"); //$NON-NLS-1$
	}

	@Override
	protected void onAction()
	{
		raise_ReplicationExperimentAddAllEvent();
	}

}
