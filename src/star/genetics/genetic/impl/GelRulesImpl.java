package star.genetics.genetic.impl;

import java.io.Serializable;
import java.util.ArrayList;

import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.GelPosition;
import star.genetics.genetic.model.GelRules;

public class GelRulesImpl implements GelRules, Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<GelPosition> pos = new ArrayList<GelPosition>();
	ArrayList<Gel> gels = new ArrayList<Gel>();

	public GelRulesImpl()
	{
	}

	private Gel getOrCreateGel(String name)
	{
		for (Gel g : gels)
		{
			if (g.getName().equals(name))
			{
				return g;
			}
		}
		GelImpl gi = new GelImpl(name, gels.size());
		gels.add(gi);
		return gi;
	}

	public void addGel(String gelName)
	{
		getOrCreateGel(gelName);
	}

	public void addGel(String gelName, Allele allele, Float[] position)
	{
		Gel g = getOrCreateGel(gelName);
		GelPositionImpl gpi = new GelPositionImpl(g, position, allele);
		g.addGelPosition(gpi);
		pos.add(gpi);
	}

	@Override
	public Iterable<Gel> getAllGelNames()
	{
		return gels;
	}

	@Override
	public Iterable<GelPosition> getGel(Iterable<Allele> alleles)
	{
		ArrayList<GelPosition> ret = new ArrayList<GelPosition>();
		for (GelPosition g : pos)
		{
			for (Allele a : alleles)
			{
				if (g.getAllele().equals(a))
				{
					ret.add(g);
				}
			}
		}
		return ret;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[" + this.getClass().getName()); //$NON-NLS-1$
		for (GelPosition gp : pos)
		{
			sb.append("\n\t" + gp); //$NON-NLS-1$
		}
		sb.append("\n]"); //$NON-NLS-1$
		return sb.toString();
	}

	@Override
	public Iterable<GelPosition> getAllGelPositions()
	{
		return pos;
	}

	@Override
	public int sizeGels()
	{
		return gels.size();
	}
}
