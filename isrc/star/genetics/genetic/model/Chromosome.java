package star.genetics.genetic.model;

import java.util.Collection;

public interface Chromosome
{
	public String getName();

	public Allele getAlleleByName(String name);

	public Gene getGeneByName(String name);

	public Collection<Gene> getGenes();
}
