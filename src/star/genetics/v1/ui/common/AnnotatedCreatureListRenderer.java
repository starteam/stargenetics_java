package star.genetics.v1.ui.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;
import star.genetics.genetic.model.Creature;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;

public class AnnotatedCreatureListRenderer implements ListCellRenderer
{
	private VisualizerFactory visualizer;

	public AnnotatedCreatureListRenderer(VisualizerFactory visualizer)
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
		boolean isExporting = CreatureTransferHandler.creatureExporting != 0;
		if (value instanceof JComponent)
		{
			return (JComponent) value;
		}
		else if (value instanceof Creature)
		{
			JPanel panel = new JPanel();
			panel.setBackground(Color.white);
			Creature c = (Creature) value;

			Object obj = list.getClientProperty("VISUALIZER");  //$NON-NLS-1$
			if (obj == null)
			{
				list.putClientProperty("VISUALIZER", new TreeMap<String, JComponent>());  //$NON-NLS-1$
			}
			TreeMap<String, JComponent> tree = (TreeMap<String, JComponent>) list.getClientProperty("VISUALIZER");  //$NON-NLS-1$
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
			f.setBorder(BorderFactory.createEmptyBorder());

			int width = f.getPreferredSize().width;
			int width2 = f.getClientProperty("NONOTE") == null ? width : 0; // fix  //$NON-NLS-1$
			                                                                // for
			                                                                // peas!
			String widthConstraint = "[" + width + ":" + width + ":" + width + "]";     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			String widthConstraint2 = "[" + width2 + ":" + width2 + ":" + width2 + "]";     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			panel.setLayout(new MigLayout("gap 0 0", widthConstraint + widthConstraint2));  //$NON-NLS-1$

			f.setBackground(Color.white);
			f.setOpaque(false);
			if (isExporting)
			{
				if (isSelected)
				{
					f.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.gray));
				}
				else
				{
					f.setBorder(null);
				}
			}
			else
			{
				if (isSelected && !cellHasFocus)
				{
					f.setBorder(getBorder(false));
				}
				if (isSelected && cellHasFocus)
				{
					f.setBorder(getBorder(true));
				}
			}
			if (cellHasFocus)
			{
				// f.setBackground(Color.lightGray);
			}
			panel.add(f);
			JComponent data;
			if (c.getNote() != null && c.getNote().length() != 0)
			{
				JTextArea data2 = new JTextArea(c.getNote(), 5, 10);
				data2.setLineWrap(true);
				data2.setMaximumSize(new Dimension(width2, f.getPreferredSize().height * 2 / 2));
				data2.setBorder(null);
				data = data2;
				data.setBackground(Color.white);
				panel.add(data);
			}
			else
			{
				data = new JTextArea("", 4, 10);  //$NON-NLS-1$
				data.setBorder(BorderFactory.createEmptyBorder());
				data.setMaximumSize(f.getPreferredSize());
				data.setBackground(Color.white);
				panel.add(data, "top");  //$NON-NLS-1$
			}
			return panel;
		}
		else
		{
			return new JLabel(String.valueOf(value));
		}
	}

	private javax.swing.border.Border b1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private javax.swing.border.Border b2 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

	private javax.swing.border.Border getBorder(boolean strong)
	{
		return strong ? b1 : b2;
	}
}
