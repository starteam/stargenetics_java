package star.genetics.v1.ui.events;

import star.annotations.Raiser;
import star.genetics.genetic.model.CreatureSet;

@Raiser
public interface CrateProgeniesRaiser extends star.event.Raiser
{
	public CreatureSet getProgenies();
}
