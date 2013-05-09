package star.genetics.genetic.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Chromosome;
import star.genetics.genetic.model.DiploidAlleles;
import star.genetics.genetic.model.Gene;

public class GeneticMakeupImpl extends TreeMap<Gene, DiploidAlleles> implements star.genetics.genetic.model.GeneticMakeup
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean equals(Object other)
	{
		boolean ret = false;
		if (other != null && other.getClass().equals(this.getClass()))
		{
			GeneticMakeupImpl that = (GeneticMakeupImpl) other;
			if (that.size() == this.size())
			{
				ret = true;
				for (Gene g : this.keySet())
				{
					DiploidAlleles thisDiploid = this.get(g);
					DiploidAlleles thatDiploid = that.get(g);
					if (thisDiploid == null || thatDiploid == null)
					{
						ret = false;
						break;
					}
					if (!thisDiploid.equals(thatDiploid))
					{
						ret = false;
						break;
					}
				}
			}
		}
		return ret;
	}

	public boolean containsKey(Gene g)
	{
		return super.containsKey(g);
	}

	public DiploidAlleles get(Gene g)
	{
		return super.get(g);
	}

	private boolean test(Allele a, Allele b, Allele x, Allele y)
	{
		boolean ax, by;
		if (a != null)
		{
			ax = a.equals(x);
		}
		else
		{
			ax = true;
		}
		if (b != null)
		{
			by = b.equals(y);
		}
		else
		{
			by = true;
		}
		return ax & by;
	}

	private boolean test(Map<Gene, DiploidAlleles> map, boolean swap)
	{
		boolean ret = true;
		try
		{
			for (Entry<Gene, DiploidAlleles> entry : map.entrySet())
			{
				DiploidAlleles rule = entry.getValue();
				DiploidAlleles organism = get(entry.getKey());

				if (organism == null)
				{
					ret = false;
					break;
				}
				Allele r0 = rule.get(swap ? 0 : 1);
				Allele r1 = rule.get(swap ? 1 : 0);

				Allele o0 = organism.get(0);
				Allele o1 = organism.get(1);

				ret &= test(r0, r1, o0, o1);
				if (!ret)
				{
					break;
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return ret;
	}

	public boolean test(Chromosome chromosome, Map<Gene, DiploidAlleles> map)
	{
		boolean ret = test(map, false) || test(map, true);
		return ret;
	}

	public String toShortString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<Gene, DiploidAlleles> entry : this.entrySet())
		{
			DiploidAlleles alleles = entry.getValue();
			if (alleles instanceof DiploidAllelesImpl)
			{
				sb.append(((DiploidAllelesImpl) alleles).toStortString());
			}
			else
			{
				sb.append(alleles.toString());
			}
		}
		return sb.toString();
	}
}
