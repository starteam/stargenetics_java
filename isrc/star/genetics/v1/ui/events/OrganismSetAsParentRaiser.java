package star.genetics.v1.ui.events;

import star.annotations.Raiser;
import star.genetics.genetic.model.Creature;

@Raiser
public interface OrganismSetAsParentRaiser extends star.event.Raiser
{
	public Creature getSelectedCreature();
}
