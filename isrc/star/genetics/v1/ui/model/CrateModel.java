package star.genetics.v1.ui.model;

import star.genetics.genetic.model.CrateExperimentMetadata;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.genetic.model.Genome.SexType;
import star.genetics.genetic.model.MatingEngine;

public interface CrateModel extends GeneticModel
{
	MatingEngine getMater();

	CreatureSet getParents();

	CreatureSet getProgenies();

	String getName();

	void setName(String name);

	void setVisible(boolean v);

	boolean isVisible();

	SexType getSexType();

	CrateExperimentMetadata getMetadata();
}
