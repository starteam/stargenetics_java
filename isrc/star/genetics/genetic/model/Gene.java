package star.genetics.genetic.model;

import java.io.Serializable;
import java.util.List;

public interface Gene extends Serializable, Comparable<Gene>
{
	Chromosome getChromosome();

	Allele getAlleleByName(String name);

	List<Allele> getGeneTypes();

	String getId();

	String getName();

	float getPosition();

}
