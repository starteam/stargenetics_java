package star.genetics.v1.ui.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v1.ui.model.ParentsListModel;

public class ParentsListModelImplUnisex extends AbstractListModel implements ParentsListModel
{
	class ParentPlaceHolder extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public ParentPlaceHolder(String text)
		{
			setLayout(new BorderLayout());
			setBackground(Color.white);
			JLabel l = new JLabel(text);
			l.setHorizontalAlignment(SwingConstants.CENTER);
			l.setBorder(BorderFactory.createLineBorder(new Color(192, 192, 192)));
			add(l);
		}

		@Override
		public Dimension getPreferredSize()
		{
			return model.getVisualizerFactory().newVisualizerInstance().getJComponent().getPreferredSize();
		}
	}

	private static final long serialVersionUID = 1L;
	private Object element0 = new ParentPlaceHolder(Messages.getString("ParentsListModelImplUnisex.0")); //$NON-NLS-1$
	private Object element1 = new ParentPlaceHolder(Messages.getString("ParentsListModelImplUnisex.1")); //$NON-NLS-1$
	CrateModel model;

	public ParentsListModelImplUnisex(CrateModel model)
	{
		this.model = model;
	}

	@Override
	public Object getElementAt(int index)
	{
		if (model.getParents().size() > index)
		{
			return model.getParents().get(index);
		}
		else
		{
			return index == 0 ? element0 : element1;
		}
	}

	@Override
	public int getSize()
	{
		return 2;
	}

	@Override
	public void set(Creature c)
	{
		if (model.getParents().size() < 2)
		{
			model.getParents().add(c);
			fireContentsChanged(this, 0, model.getParents().size());
		}
	}

	@Override
	public Object get(Sex creature)
	{
		return null;
	}

	@Override
	public void clear(Sex creature)
	{
		return;
	}

	public boolean isValid()
	{
		boolean ret = false;
		if (model.getParents().size() > 1)
		{
			ret = true;
		}
		return ret;
	}

	@Override
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
