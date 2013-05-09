package star.genetics.v2.ui.fly.strains;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.v1.ui.events.CrateMateRaiser;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v2.ui.common.Button;
import utils.Icons;

@SignalComponent(extend = Button.class, raises = { OrganismSetAsParentRaiser.class })
@Properties({ @Property(name = "selectedCreature", type = Creature.class, getter = Property.PUBLIC), @Property(name = "crateUsable", type = boolean.class, value = "true") })
public class Set extends Set_generated
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
		return Messages.getString("Set.1"); //$NON-NLS-1$
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_S;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return Icons.SITE_MAP.getIcon(16);
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
		setSelectedCreature(r.getSelectedCreature());
		setEnabled(getCrateUsable() && getSelectedCreature() != null);
	}

	@Override
	@Handles(raises = {})
	void organismSelected(ProgenySelectedRaiser r)
	{
		setEnabled(false);
	}

	@Override
	@Handles(raises = {})
	void crateMated(CrateMateRaiser r)
	{
		setCrateUsable(false);
	}

	@Override
	@Handles(raises = {})
	void newCrate(CrateNewCrateRaiser r)
	{
		setCrateUsable(true);
	}

	@Override
	protected void onAction()
	{
		setEnabled(false);
		raise_OrganismSetAsParentEvent();
	}
}
