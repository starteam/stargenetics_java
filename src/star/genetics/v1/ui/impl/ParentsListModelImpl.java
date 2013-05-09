package star.genetics.v1.ui.impl;

import javax.swing.AbstractListModel;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Genome.SexType;
import star.genetics.v1.ui.crate.parents.ParentPlaceholder;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v1.ui.model.ParentsListModel;

public class ParentsListModelImpl extends AbstractListModel implements ParentsListModel
{
	private static final long serialVersionUID = 1L;

	private Object element0 = new ParentPlaceholder();
	CrateModel model;

	public ParentsListModelImpl(CrateModel model)
	{
		this.model = model;
	}

	public Object getElementAt(int index)
	{
		return get(index == 0 ? Creature.Sex.FEMALE : Creature.Sex.MALE);
	}

	public int getSize()
	{
		return 2;
	}

	public void set(Creature c)
	{
		if (c.getSex() != null)
		{
			for (Creature cr : model.getParents())
			{
				if (c.getSex().equals(cr.getSex()))
				{
					model.getParents().remove(cr);
					break;
				}
			}
			model.getParents().add(c);
			fireContentsChanged(this, 0, model.getParents().size());
		}
		else
		{
			model.getParents().add(c);
			fireContentsChanged(this, 0, model.getParents().size());
		}
	}

	public Object get(Creature.Sex index)
	{
		return find(index, model.getParents(), element0);
	}

	private Object find(Creature.Sex sex, CreatureSet set, Object defaultRet)
	{
		for (Creature c : set)
		{
			if (c.getSex() != null && sex.equals(c.getSex()))
			{
				return c;
			}
		}
		return defaultRet;
	}

	public void clear(Sex sex)
	{
		for (Creature c : model.getParents())
		{
			if (c.getSex() != null && sex.equals(c.getSex()))
			{
				model.getParents().remove(c);
				break;
			}
		}
	}

	public boolean isValid()
	{
		boolean ret = false;
		if (model.getParents().size() == 2)
		{
			ret = true;
		}
		else if (model.getParents().size() == 1)
		{
			Object cr = get(Sex.FEMALE);
			if (cr instanceof Creature)
			{
				if (SexType.XO.equals(((Creature) cr).getGenome().getSexType()))
				{
					ret = true;
				}
			}
		}
		return ret;
	}

	public CreatureSet getCreatures()
	{
		CreatureSet set = new star.genetics.genetic.impl.CreatureSetImpl();
		for (int i = 0; i < this.getSize(); i++)
		{
			Object c = getElementAt(i);
			if (c instanceof Creature)
			{
				set.add((Creature) c);
			}
		}
		return set;
	}

}
