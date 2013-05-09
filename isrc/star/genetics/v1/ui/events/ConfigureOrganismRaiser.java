package star.genetics.v1.ui.events;

import star.annotations.Raiser;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.GeneticModel;

@Raiser
public interface ConfigureOrganismRaiser extends star.event.Raiser
{
	Creature getSelectedCreature();

	GeneticModel getGeneticModel();
}
