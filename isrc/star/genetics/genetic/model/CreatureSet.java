package star.genetics.genetic.model;

import utils.PropertyChangeRaiser;

public interface CreatureSet extends Iterable<Creature>, PropertyChangeRaiser
{
	public void add(Creature c);

	public void add(Creature c, int index);

	public void set(Creature c, int index);

	public Creature get(int index);

	public boolean contains(Creature c);

	public void remove(Creature c);

	public void move(Creature c, int newIndex);

	public void clear();

	public int size();
}
