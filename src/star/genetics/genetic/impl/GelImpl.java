package star.genetics.genetic.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.GelPosition;

public class GelImpl implements Gel, Serializable
{
	private static final long serialVersionUID = 1L;
	private final String name;
	private int index;
	private Set<GelPosition> set = new HashSet<GelPosition>();

	public GelImpl(String name, int index)
	{
		this.name = name;
		this.index = index;
	}

	@Override
	public String getName()
	{

		return name;
	}

	@Override
	public int getIndex()
	{
		return index;
	}

	public void addGelPosition(GelPosition gp)
	{
		set.add(gp);
	}

	@Override
	public Iterator<GelPosition> iterator()
	{
		// TODO Auto-generated method stub
		return set.iterator();
	}

}
