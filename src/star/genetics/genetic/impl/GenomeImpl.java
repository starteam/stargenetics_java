package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import star.genetics.genetic.model.Chromosome;
import star.genetics.genetic.model.Gene;

public class GenomeImpl implements star.genetics.genetic.model.Genome, Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Chromosome> chromosomes = new ArrayList<Chromosome>();
	private SexType sexType = SexType.XY;

	/**
	 * @param organismName
	 *            the organismName to set
	 */
	public void setSexType(String sexTypeName)
	{
		sexType = SexType.parse(sexTypeName);
	}

	public SexType getSexType()
	{
		return sexType;
	}

	public Chromosome getChromosomeByName(String name)
	{
		Chromosome ret = null;
		for (Chromosome x : chromosomes)
		{
			if (name.equals(x.getName()))
			{
				ret = x;
				break;
			}
		}
		return ret;
	}

	public void removeChromosome(Chromosome c)
	{
		getChromosomes().remove(c);
	}

	public void addChromosome(Chromosome c)
	{
		getChromosomes().add(c);
	}

	public Iterator<Chromosome> iterator()
	{
		return getChromosomes().iterator();
	}

	List<Chromosome> getChromosomes()
	{
		return chromosomes;
	}

	public void setChromosomes(List<Chromosome> chromosomes)
	{
		this.chromosomes = chromosomes;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public List<Gene> getGenes()
	{
		ArrayList<Gene> genes = new ArrayList<Gene>();
		for (star.genetics.genetic.model.Chromosome c : getChromosomes())
		{
			genes.addAll(c.getGenes());
		}
		return genes;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("[{0} name: {1} chromosomes: {2}]", this.getClass().getSimpleName(), getName(), getChromosomes()); //$NON-NLS-1$
	}
}
