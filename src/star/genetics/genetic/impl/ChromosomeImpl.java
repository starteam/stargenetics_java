package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.Genome;

public class ChromosomeImpl implements star.genetics.genetic.model.Chromosome, Serializable
{
	private static final long serialVersionUID = 1L;
	private final String name;
	private final List<Gene> genes = new ArrayList<Gene>();

	public ChromosomeImpl(String name, Genome genome)
	{
		this.name = name;
		genome.addChromosome(this);
	}

	public String getName()
	{
		return name;
	}

	public List<Gene> getGenes()
	{
		return genes;
	}

	public Allele getAlleleByName(String name)
	{
		Allele ret = null;
		for (Gene g : genes)
		{
			Allele a = g.getAlleleByName(name);
			if (a != null)
			{
				ret = a;
				break;
			}
		}
		return ret;
	}

	public Gene getGeneByName(String name)
	{
		Gene ret = null;
		for (Gene g : genes)
		{
			if (name.equals(g.getName()))
			{
				ret = g;
				break;
			}
		}
		return ret;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("[{0} name: {1} genes: {2}]", this.getClass().getSimpleName(), getName(), getGenes()); //$NON-NLS-1$
	}
}
