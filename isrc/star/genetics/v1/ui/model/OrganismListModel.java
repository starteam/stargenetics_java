package star.genetics.v1.ui.model;

import javax.swing.ListModel;

import star.genetics.genetic.model.Creature;

public interface OrganismListModel extends ListModel
{
	void addElementAt(Creature c, int index);

	boolean contains(Creature c);

	void move(Creature c, int newIndex);

	void remove(Creature c);

	void add(Creature c);

	Creature getElementAt(int index);

	void invalidateModel();

}
