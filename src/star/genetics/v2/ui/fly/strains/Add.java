package star.genetics.v2.ui.fly.strains;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v1.ui.events.ProgenyAddProgenyRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v2.ui.common.Button;
import utils.Icons;

@SignalComponent(extend = Button.class, raises = { ProgenyAddProgenyRaiser.class })
@Properties(@Property(name = "selectedCreature", type = Creature.class, getter = Property.PUBLIC))
public class Add extends Add_generated
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
		return Messages.getString("Add.1"); //$NON-NLS-1$
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_A;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return Icons.ARROW_LEFT.getIcon(16);
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return false;
	}

	@Override
	@Handles(raises = {})
	void disableOnNewCrate(CrateNewCrateRaiser r)
	{
		setEnabled(false);
	}

	@Override
	@Handles(raises = {})
	void enableOnProgenies(ProgenySelectedRaiser r)
	{
		setSelectedCreature(r.getSelectedCreature());
		setEnabled(getSelectedCreature() != null);
	}

	@Override
	protected void onAction()
	{
		setEnabled(false);
		raise_ProgenyAddProgenyEvent();
	}
}
