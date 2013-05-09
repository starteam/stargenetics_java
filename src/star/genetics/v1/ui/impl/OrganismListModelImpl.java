package star.genetics.v1.ui.impl;

import javax.swing.AbstractListModel;

import star.genetics.genetic.model.Creature;
import star.genetics.v1.ui.model.OrganismListModel;
import star.genetics.v1.ui.model.OrganismModel;

public class OrganismListModelImpl extends AbstractListModel implements OrganismListModel
{
	private static final long serialVersionUID = 1L;
	private OrganismModel model;

	public OrganismListModelImpl(OrganismModel model)
	{
		this.model = model;
	}

	public Creature getElementAt(int index)
	{
		return model.getCreatures().get(index);
	}

	public int getSize()
	{
		return model.getCreatures().size();
	}

	public void addElementAt(Creature c, int index)
	{
		model.getCreatures().add(c, index);
		fireContentsChanged(this, 0, getSize());
	}

	public boolean contains(Creature c)
	{
		return model.getCreatures().contains(c);
	}

	public void remove(Creature c)
	{
		model.getCreatures().remove(c);
		fireContentsChanged(this, 0, getSize());
	}

	public void add(Creature c)
	{
		model.getCreatures().add(c);
		fireContentsChanged(this, 0, getSize());
	}

	public void move(Creature c, int newIndex)
	{
		model.getCreatures().move(c, newIndex);
		fireContentsChanged(this, 0, getSize());
	}

	public void invalidateModel()
	{
		fireContentsChanged(this, 0, getSize());
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getSize(); i++)
		{
			sb.append(" " + getElementAt(i).getName()); //$NON-NLS-1$
		}
		return sb.toString();
	}
}