package star.genetics.v1.ui.model;

import javax.swing.ListModel;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;

public interface ParentsListModel extends ListModel
{
	public void set(Creature c);

	public Object get(Creature.Sex creature);

	public void clear(Creature.Sex creature);

	boolean isValid();

	CreatureSet getCreatures();
}
