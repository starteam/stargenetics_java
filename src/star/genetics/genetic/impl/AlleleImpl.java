package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;

import star.genetics.genetic.model.Gene;

public class AlleleImpl implements star.genetics.genetic.model.Allele, Serializable
{
	private static final long serialVersionUID = 1L;
	private final String name;
	private final Gene gene;

	public AlleleImpl(String name, Gene gene)
	{
		this.name = name;
		this.gene = gene;
		gene.getGeneTypes().add(this);
	}

	public String getName()
	{
		return name;
	}

	public Gene getGene()
	{
		return gene;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("{0} {1}", gene.getName(), getName()); //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean ret = false;
		if (obj instanceof AlleleImpl)
		{
			AlleleImpl that = (AlleleImpl) obj;
			if (this.gene != null && this.gene.equals(that.gene))
			{
				ret = this.getName().equals(that.getName());
			}
		}
		return ret;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode() ^ gene.hashCode();
	}
}
