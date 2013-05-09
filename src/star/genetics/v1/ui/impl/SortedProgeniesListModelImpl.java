package star.genetics.v1.ui.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import javax.swing.AbstractListModel;

import star.genetics.Helper;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v1.ui.model.ProgeniesListModel;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;

public class SortedProgeniesListModelImpl extends AbstractListModel implements ProgeniesListModel
{
	private CrateModel model;
	private VisualizerFactory factory;
	private static final long serialVersionUID = 1L;

	private Creature[] array;

	private Creature[] getArray()
	{
		if (array == null || array.length != model.getProgenies().size())
		{
			CreatureSet set = model.getProgenies();
			int size = set.size();
			Creature[] array = new Creature[size];
			for (int i = 0; i < size; i++)
			{
				array[i] = set.get(i);
			}
			Arrays.sort(array, new Comparator<Creature>()
			{
				Map<String, String> get(Creature c)
				{
					Visualizer v = factory.newVisualizerInstance();
					Helper.setVisualizerFromCreature(v, c);
					Map<String, String> properties = v.getTooltipProperties();
					properties.remove(GeneticModel.matings);
					return properties;
				}

				public int compare(Creature o1, Creature o2)
				{
					int ret = get(o1).toString().compareTo(get(o2).toString());
					return (ret != 0) ? ret : (o1.getSex() != null ? o1.getSex().compareTo(o2.getSex()) : 0);
				}
			});
			this.array = array;
		}
		return array;
	}

	public SortedProgeniesListModelImpl(CrateModel model, VisualizerFactory visualizerFactory)
	{
		this.model = model;
		this.factory = visualizerFactory;
	}

	public Object getElementAt(int index)
	{
		return getArray()[index];
	}

	public int getSize()
	{
		return model.getProgenies().size();
	}

	public void update()
	{
		array = null;
		fireContentsChanged(this, 0, getSize());
	}

	public void clear()
	{
		model.getProgenies().clear();
		array = null;
		fireContentsChanged(this, 0, getSize());
	}

}
