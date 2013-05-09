package star.genetics.genetic.model;

import java.util.Map;
import java.util.Set;

public interface RuleSet
{
	public boolean add(Rule rule);

	public Map<String, String> getProperties(GeneticMakeup genotype, Creature.Sex sex);

	public Set<String> getPropertyNames();
}
