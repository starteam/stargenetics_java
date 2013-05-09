package star.genetics.genetic.model;

public interface CrateModel
{
	public CreatureSet getParents();

	public CreatureSet getProgenies();

	public String getName();

	public void setName(String name);

	public boolean isVisible();

	public void setVisible(boolean visible);

	public CrateExperimentMetadata getMetadata();

	public String getUUID();

}
