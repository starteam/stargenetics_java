package star.genetics.v1.ui.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;

import star.genetics.genetic.model.Creature;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;

public class CreatureListRenderer implements ListCellRenderer
{

	private VisualizerFactory visualizer;

	public CreatureListRenderer(VisualizerFactory visualizer)
	{
		this.visualizer = visualizer;
	}

	private Visualizer getVisualizer(Map<String, String> properties, Creature.Sex sex, String name, String note)
	{
		Visualizer c = visualizer.newVisualizerInstance();
		c.setProperties(properties, sex);
		c.setName(name);
		c.setNote(note);
		return c;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (value instanceof JComponent)
		{
			return (JComponent) value;
		}
		else if (value instanceof Creature)
		{
			Creature c = (Creature) value;

			Object obj = list.getClientProperty("VISUALIZER"); //$NON-NLS-1$
			if (obj == null)
			{
				list.putClientProperty("VISUALIZER", new TreeMap<String, JComponent>()); //$NON-NLS-1$
			}
			TreeMap<String, JComponent> tree = (TreeMap<String, JComponent>) list.getClientProperty("VISUALIZER"); //$NON-NLS-1$
			String key = c.getName() + String.valueOf(c.getProperties()) + String.valueOf(c.getSex()) + String.valueOf(c.getNote());
			JComponent f;
			if (tree.containsKey(key))
			{
				f = tree.get(key);
			}
			else
			{
				f = getVisualizer(c.getProperties(), c.getSex(), c.getName(), c.getNote()).getJComponent();
				tree.put(key, f);
			}

			// JComponent f = getVisualizer(c.getProperties(), c.getSex(), c.getName(), c.getNote()).getJComponent();
			f.setBorder(BorderFactory.createEmptyBorder());
			if (isSelected && !cellHasFocus)
			{
				f.setBorder(getBorder(false));
			}
			if (isSelected && cellHasFocus)
			{
				f.setBorder(getBorder(true));
			}
			if (cellHasFocus)
			{
				// f.setBackground(Color.lightGray);
			}
			return f;
		}
		else
		{
			return new JLabel(String.valueOf(value));
		}
	}

	private javax.swing.border.Border b1 = new Border(Color.BLUE, .75f);
	private javax.swing.border.Border b2 = new Border(Color.LIGHT_GRAY, .25f);

	private javax.swing.border.Border getBorder(boolean strong)
	{
		return strong ? b1 : b2;
	}

	private static class Border extends AbstractBorder
	{
		private static final long serialVersionUID = 1L;

		private Color c;
		private float stroke;

		public Border(Color c, float stroke)
		{
			this.c = c;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
		{
			super.paintBorder(c, g, x, y, width, height);
			if (g instanceof Graphics2D)
			{
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(stroke));
			}
			g.setColor(this.c);
			g.drawRect(x, y, width - 2, height - 2);
		}
	}
}
