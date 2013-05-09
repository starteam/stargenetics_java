package star.genetics.genetic.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.CrateSet;

public class CrateSetImpl implements CrateSet, Serializable
{
	private static final long serialVersionUID = 1L;
	private int id = 1;
	private final ArrayList<CrateModel> set = new ArrayList<CrateModel>();

	private void add(CrateModel crate)
	{
		set.add(crate);
	}

	public CrateModel current()
	{
		return set.get(set.size() - 1);
	}

	static class CrateIterator implements Iterator<CrateModel>
	{
		public CrateIterator(ArrayList<CrateModel> set)
		{
			this.set = set;
			index = set.size();
		}

		int index;
		ArrayList<CrateModel> set;

		public boolean hasNext()
		{
			return index > 0;
		}

		public CrateModel next()
		{
			index--;
			if (index > set.size())
			{
				index = set.size() - 1;
			}
			return set.get(index);
		}

		public void remove()
		{

		}

	}

	public Iterator<CrateModel> iterator()
	{
		// return set.iterator();
		return new CrateIterator(set);
	}

	public CrateModel newCrateModel()
	{
		clearInvisibleCrates();
		add(new CrateModelImpl(id++));
		return current();
	}

	private void clearInvisibleCrates()
	{
		Iterator<CrateModel> iter = set.iterator();
		while (iter.hasNext())
		{
			CrateModel m = iter.next();
			if (!m.isVisible())
			{
				iter.remove();
			}
		}
	}

	public int size()
	{
		return set.size();
	}
}
