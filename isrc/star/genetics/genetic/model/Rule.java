package star.genetics.genetic.model;

import java.util.HashMap;

public interface Rule
{
	static final String DEFAULT = "Default";

	public HashMap<String, String> getProperties();

	public boolean isDefault();

	public boolean isMatching(GeneticMakeup makeup, Creature.Sex sex);
}
