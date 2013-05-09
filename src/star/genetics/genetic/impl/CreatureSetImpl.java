package star.genetics.genetic.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import star.genetics.genetic.model.Creature;

public class CreatureSetImpl implements star.genetics.genetic.model.CreatureSet, Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<Creature> creatures = new ArrayList<Creature>();
	PropertyChangeSupport support = new PropertyChangeSupport(this);

	public Iterator<Creature> iterator()
	{
		return creatures.iterator();
	}

	public void add(Creature c)
	{
		creatures.add(c);
		modelChanged();
	}

	public void add(Creature c, int index)
	{
		creatures.add(index, c);
		modelChanged();
	}

	public void set(Creature c, int index)
	{
		creatures.set(index, c);
		modelChanged();
	}

	public void remove(Creature c)
	{
		creatures.remove(c);
		modelChanged();
	}

	public boolean contains(Creature c)
	{
		return creatures.contains(c);
	}

	public void move(Creature c, int newIndex)
	{
		int oldIndex = creatures.indexOf(c);
		if (oldIndex != newIndex)
		{
			creatures.remove(oldIndex);
			creatures.add(newIndex + (newIndex > oldIndex ? -1 : 0), c);
		}
	}

	public Creature get(int index)
	{
		return creatures.get(index);
	}

	public int size()
	{
		return creatures.size();
	}

	public void clear()
	{
		creatures.clear();
		modelChanged();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Creature c : this)
		{
			sb.append(c.getName());
			sb.append(","); //$NON-NLS-1$
		}
		return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : ""; //$NON-NLS-1$
	}

	public String toShortString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < creatures.size(); i++)
		{
			sb.append(" " + creatures.get(i).getName()); //$NON-NLS-1$
		}
		return sb.toString();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		support.addPropertyChangeListener(listener);

	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		support.removePropertyChangeListener(listener);
	}

	void modelChanged()
	{
		support.firePropertyChange(new PropertyChangeEvent(this, "", null, null)); //$NON-NLS-1$
	}
}
