package star.genetics.v1.ui.impl;

import star.genetics.genetic.model.CrateExperimentMetadata;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Genome.SexType;
import star.genetics.genetic.model.MatingEngine;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.visualizers.VisualizerFactory;

public class CrateModelImpl implements CrateModel
{
	private Model model;
	private star.genetics.genetic.model.CrateModel crateModel;

	public CrateModelImpl(Model model, star.genetics.genetic.model.CrateModel crateModel)
	{
		this.model = model;
		this.crateModel = crateModel;
	}

	public SexType getSexType()
	{
		return model.getGenome().getSexType();
	}

	public MatingEngine getMater()
	{
		return model.getMatingEngine();
	}

	public CreatureSet getParents()
	{
		return crateModel.getParents();
	}

	public CreatureSet getProgenies()
	{
		return crateModel.getProgenies();
	}

	public String getName()
	{
		return crateModel.getName();
	}

	public void setName(String name)
	{
		crateModel.setName(name);
	}

	public VisualizerFactory getVisualizerFactory()
	{
		return model.getVisualizerFactory();
	}

	public void setVisible(boolean visible)
	{
		crateModel.setVisible(visible);
	}

	public boolean isVisible()
	{
		return crateModel.isVisible();
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public CrateExperimentMetadata getMetadata()
	{
		return crateModel.getMetadata();
	}

}
