package star.genetics.genetic.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import star.genetics.genetic.model.Chromosome;

public class GeneImpl implements star.genetics.genetic.model.Gene
{
	private static final long serialVersionUID = 1L;

	private final String name;
	private final Chromosome chromosome;
	private final float position;
	private final List<star.genetics.genetic.model.Allele> geneTypes = new ArrayList<star.genetics.genetic.model.Allele>();

	public GeneImpl(String name, float position, Chromosome chromosome)
	{
		this.name = name;
		this.position = position;
		this.chromosome = chromosome;
		chromosome.getGenes().add(this);
	}

	public star.genetics.genetic.model.Chromosome getChromosome()
	{
		return chromosome;
	}

	public String getName()
	{
		return name;
	}

	public float getPosition()
	{
		return position;
	}

	public List<star.genetics.genetic.model.Allele> getGeneTypes()
	{
		return geneTypes;
	}

	public String getId()
	{
		return (getChromosome() != null ? getChromosome().getName() : "") + ":" + getName(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public star.genetics.genetic.model.Allele getAlleleByName(String name)
	{
		star.genetics.genetic.model.Allele ret = null;
		for (star.genetics.genetic.model.Allele a : getGeneTypes())
		{
			if (name.equals(a.getName()))
			{
				ret = a;
				break;
			}
		}
		return ret;
	}

	public int compareTo(star.genetics.genetic.model.Gene that)
	{
		if (this.getChromosome() != null && that.getChromosome() != null && this.getChromosome().equals(that.getChromosome()))
		{
			int ret = Float.compare(this.getPosition(), that.getPosition());
			return ret != 0 ? ret : this.getId().compareTo(that.getId());
		}
		return getId().compareTo(that.getId());
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean ret = false;
		if (obj instanceof GeneImpl)
		{
			GeneImpl that = (GeneImpl) obj;
			ret = getId().equals(that.getId());
		}
		return ret;
	}

	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}

	@Override
	public String toString()
	{

		return MessageFormat.format("[{0} name: {1} alleles: {2}", getClass().getSimpleName(), getName(), getGeneTypes()); //$NON-NLS-1$
	}

}
