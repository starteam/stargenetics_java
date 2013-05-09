package star.genetics.v2.ui.yeast.common;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.impl.CreatureImpl;
import star.genetics.genetic.impl.CreatureSetImpl;
import star.genetics.genetic.impl.DiploidAllelesImpl;
import star.genetics.genetic.impl.GeneticMakeupImpl;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.GeneticMakeup;
import star.genetics.genetic.model.Genome;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateMateRaiser;
import star.genetics.v1.ui.events.CrateParentsRaiser;
import star.genetics.v1.ui.events.CrateParentsUpdatedRaiser;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;

@SignalComponent(raises = { CrateParentsRaiser.class, CrateParentsUpdatedRaiser.class })
@Properties({ @Property(name = "model", type = CrateModel.class, getter = Property.PUBLIC) })
public class YeastParents extends YeastParents_generated
{
	private boolean locked = false;

	public enum Labels
	{
		Female(0, Sex.FEMALE), Male(1, Sex.MALE), Diploid(2, null);
		int i;
		Sex sex;

		private Labels(int i, Sex sex)
		{
			this.i = i;
			this.sex = sex;
		}

		int getIndex()
		{
			return i;
		}

		public Sex getSex()
		{
			return sex;
		}

		static int getIndex(Sex x)
		{
			if (x == null)
			{
				return Diploid.getIndex();
			}
			for (Labels l : values())
			{
				if (x.equals(l.getSex()))
				{
					return l.getIndex();
				}
			}
			return -1;
		}

	};

	private YeastParentLabel[] labels = new YeastParentLabel[Labels.values().length];
	private Creature[] data = new Creature[2];
	private Model mainModel;

	public YeastParents(CrateModel crateModel, Model mainModel)
	{
		this.mainModel = mainModel;
		setModel(crateModel);
		for (Labels l : Labels.values())
		{
			labels[l.getIndex()] = new YeastParentLabel(this, l.getSex());
		}

		for (Creature c : crateModel.getParents())
		{
			int index = Labels.getIndex(c.getSex());
			data[index] = c;
			labels[index].update();
		}
		if (crateModel.getParents().size() == 2)
		{
			locked = true;
			labels[Labels.Diploid.getIndex()].update();
		}
	}

	public CreatureSet getParents()
	{
		return getModel().getParents();
	}

	public YeastParentLabel getLabel(Labels type)
	{
		return labels[type.getIndex()];
	}

	public Creature getCreature(Sex sex)
	{
		if (sex == null && data[0] != null && data[1] != null)
		{
			// construct diploid...
			GeneticMakeup c1 = data[0].getMakeup();
			GeneticMakeup c2 = data[1].getMakeup();
			Genome genome = data[0].getGenome();

			GeneticMakeupImpl gmd = new GeneticMakeupImpl();
			for (Gene g : genome.getGenes())
			{
				gmd.put(g, new DiploidAllelesImpl(c1.get(g).get(0), c2.get(g).get(0)));
			}
			CreatureSet parents = new CreatureSetImpl();
			parents.add(data[0]);
			parents.add(data[1]);
			CreatureImpl cimpl = new CreatureImpl(Messages.getString("YeastParents.0"), genome, null, gmd, 0, mainModel.getRules().getProperties(gmd, null), parents); //$NON-NLS-1$
			return cimpl;
		}
		if (sex == null)
		{
			return null;
		}
		try
		{
			return data[Labels.getIndex(sex)];
		}
		catch (IndexOutOfBoundsException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public void setCreature(Creature c)
	{
		if (locked)
		{
			return;
		}
		int index = Labels.getIndex(c.getSex());
		if (index == 2)
		{
			return;
		}
		if (data[index] == null)
		{
			getModel().getParents().add(c);
			data[index] = c;
		}
		else
		{
			getModel().getParents().remove(data[index]);
			getModel().getParents().add(c);
			data[index] = c;
		}

		if (getModel().getParents().size() == 2)
		{
			getModel().getParents().remove(data[Labels.getIndex(Sex.FEMALE)]);
			getModel().getParents().remove(data[Labels.getIndex(Sex.MALE)]);
			getModel().getParents().add(data[Math.min(Labels.getIndex(Sex.FEMALE), Labels.getIndex(Sex.MALE))]);
			getModel().getParents().add(data[Math.max(Labels.getIndex(Sex.FEMALE), Labels.getIndex(Sex.MALE))]);
			labels[Labels.Diploid.getIndex()].update();
			raise_CrateParentsEvent();
		}
		labels[index].update();
		raise_CrateParentsUpdatedEvent();
	}

	@Override
	@Handles(raises = {})
	void handleMating(CrateMateRaiser r)
	{
		locked = true;
	}

	public Visualizer getCreatureVisualizer(Sex type)
	{
		Creature c = getCreature(type);
		Visualizer v = mainModel.getVisualizerFactory().newVisualizerInstance();
		Helper.setVisualizerFromCreature(v, c);
		return v;
	}

	public VisualizerFactory getVisualizerFactory()
	{
		return mainModel.getVisualizerFactory();
	}

}
