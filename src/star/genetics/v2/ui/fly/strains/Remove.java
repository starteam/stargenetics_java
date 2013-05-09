package star.genetics.v2.ui.fly.strains;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.OrganismRemoveRaiser;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v2.ui.common.Button;
import utils.Icons;

@SignalComponent(extend = Button.class, raises = { OrganismRemoveRaiser.class })
public class Remove extends Remove_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getButtonText()
	{
		return ""; //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return Messages.getString("Remove.1"); //$NON-NLS-1$
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_R;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return Icons.REMOVE.getIcon(16);
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return false;
	}

	@Override
	@Handles(raises = {})
	void organismSelected(OrganismSelectedRaiser r)
	{
		setEnabled(r.getSelectedCreature() != null && !r.getSelectedCreature().isReadOnly());
	}

	@Override
	@Handles(raises = {})
	void organismSelected(ProgenySelectedRaiser r)
	{
		if (r.getSelectedCreature() != null && !r.getSelectedCreature().isReadOnly())
		{
			setEnabled(false);
		}
	}

	@Override
	protected void onAction()
	{
		setEnabled(false);
		raise_OrganismRemoveEvent();
	}
}
