package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.TreeMap;

import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Chromosome;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.DiploidAlleles;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.GeneticMakeup;

class ChromosomeRuleImpl implements Serializable, IndividualRule
{
	private static final long serialVersionUID = 1L;
	private final Chromosome chromosome;
	private final Map<Gene, DiploidAlleles> map = new TreeMap<Gene, DiploidAlleles>();

	ChromosomeRuleImpl(Chromosome c)
	{
		this.chromosome = c;
	}

	public boolean test(GeneticMakeup makeup, Creature.Sex sex)
	{
		return makeup.test(chromosome, map);
	}

	void addAllele(int strand, Allele a)
	{
		if (strand == 0)
		{
			map.put(a.getGene(), new DiploidAllelesImpl(a, null));
		}
		else
		{
			DiploidAlleles diploid = map.get(a.getGene());
			map.put(a.getGene(), new DiploidAllelesImpl(diploid != null ? diploid.get(0) : null, a));
		}

	}

	private Chromosome getChromosome()
	{
		return chromosome;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("{0} c:{1} m:{2}", this.getClass().getName(), getChromosome().getName(), map); //$NON-NLS-1$
	}

}
