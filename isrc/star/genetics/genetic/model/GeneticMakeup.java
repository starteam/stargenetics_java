package star.genetics.genetic.model;

import java.util.Map.Entry;

public interface GeneticMakeup
{
	boolean containsKey(Gene g);

	DiploidAlleles get(Gene g);

	// TODO: revisit
	DiploidAlleles put(Gene g, DiploidAlleles d);

	boolean test(Chromosome c, java.util.Map<Gene, DiploidAlleles> map);

	Iterable<Entry<Gene, DiploidAlleles>> entrySet();
}
