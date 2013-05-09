package star.genetics.v1.ui.events;

import star.annotations.Raiser;
import star.genetics.genetic.model.Creature;

@Raiser
public interface OrganismSelectedRaiser extends star.event.Raiser
{
	Creature getSelectedCreature();
}
