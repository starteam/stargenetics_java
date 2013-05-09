package star.genetics.v1.ui.events;

import star.annotations.Raiser;
import star.genetics.genetic.model.CreatureSet;

@Raiser
public interface CrateParentsRaiser extends star.event.Raiser
{
	CreatureSet getParents();
}
