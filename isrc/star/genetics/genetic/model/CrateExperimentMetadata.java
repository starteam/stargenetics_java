package star.genetics.genetic.model;

public interface CrateExperimentMetadata
{
	Object get(Class<? extends Object> c);

	void put(Class<? extends Object> c, Object o);
}
