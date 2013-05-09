package star.genetics.v1.ui.crate.parents;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.GeneticModel;

public interface ParentsListInterface
{

	GeneticModel getGeneticModel();

	void setCreature(Creature c);

	Creature getSelectedCreature();

}
