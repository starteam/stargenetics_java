package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.OrganismRemoveRaiser;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import utils.Icons;

@SignalComponent(extend = Button.class, raises = { OrganismRemoveRaiser.class })
public class RemoveOrganism extends RemoveOrganism_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getButtonText()
	{
		return Messages.getString("RemoveOrganism.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_R;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return Icons.REMOVE.getIcon();
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
