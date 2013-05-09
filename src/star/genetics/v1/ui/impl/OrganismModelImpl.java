package star.genetics.v1.ui.impl;

import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.model.OrganismModel;
import star.genetics.visualizers.VisualizerFactory;

public class OrganismModelImpl implements OrganismModel
{
	private Model model;

	public OrganismModelImpl(Model model)
	{
		this.model = model;
	}

	public CreatureSet getCreatures()
	{
		return model.getCreatures();
	}

	public VisualizerFactory getVisualizerFactory()
	{
		return model.getVisualizerFactory();
	}

}
