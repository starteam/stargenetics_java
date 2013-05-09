package star.genetics.v1.ui.crate.groupby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.gel.JGel;
import star.genetics.visualizers.Visualizer;
import utils.FileUtils;

@SignalComponent(extend = JPanel.class)
public class GroupByGel extends GroupByGel_generated
{
	private static final long serialVersionUID = 1L;
	private CrateModel model;
	private Model mainModel;
	private GroupByGel main;
	boolean disclosure = false;

	public GroupByGel(Model mainModel, CrateModel model)
	{
		this.mainModel = mainModel;
		this.model = model;
		this.main = this;
	}

	@Override
	@Wrap(type = Wrap.Types.SwingUtilitiesInvokeLater)
	void updateTree()
	{
		update();
		synchronized (getTreeLock())
		{
			validateTree();
		}
		repaint();
	}

	@Override
	@Handles(raises = {})
	void progeniesEvent(CrateProgeniesRaiser r)
	{
		updateTree_SwingUtilitiesInvokeLater();
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		update();
	}

	static class Counter implements Serializable
	{
		private static final long serialVersionUID = 1L;
		int count = 0;

		void add()
		{
			count++;
		}

		public int getCount()
		{
			return count;
		}
	}

	class Summary implements Serializable
	{
		private static final long serialVersionUID = 1L;
		ArrayList<Visualizer> visualizers = new ArrayList<Visualizer>();
		ArrayList<JGel> gels = new ArrayList<JGel>();

		ArrayList<Counter> males = new ArrayList<GroupByGel.Counter>();
		ArrayList<Counter> females = new ArrayList<GroupByGel.Counter>();
		TreeSet<String> allProperties = new TreeSet<String>();

		void addCreature(Creature c, Visualizer v, JGel g)
		{
			boolean found = false;
			for (int i = 0; i < visualizers.size(); i++)
			{
				if (true && visualizers.get(i).getTooltipProperties().toString().equals(v.getTooltipProperties().toString()) && gels.get(i).getProperties().equals(g.getProperties().toString()))
				{
					found = true;
					(c.getSex().equals(Sex.FEMALE) ? females : males).get(i).add();
					break;
				}
			}
			if (!found)
			{
				visualizers.add(v);
				gels.add(g);
				females.add(new Counter());
				males.add(new Counter());
				(c.getSex().equals(Sex.FEMALE) ? females : males).get(visualizers.size() - 1).add();
				allProperties.addAll(v.getTooltipProperties().keySet());
			}
		}

		void print()
		{
			for (int i = 0; i < visualizers.size(); i++)
			{
				System.out.println(visualizers.get(i).getTooltipProperties());
				System.out.println(gels.get(i).getProperties());
				System.out.println(males.get(i).getCount());
				System.out.println(females.get(i).getCount());
				System.out.println();
			}
		}

		class Orderable implements Comparable<Orderable>
		{
			String visualizer;
			String gels;
			int index;

			public Orderable(Visualizer visualizer2, JGel jGel, int i)
			{
				this.visualizer = visualizer2.getTooltipProperties().toString();
				this.gels = jGel.getProperties();
				this.index = i;
			}

			@Override
			public String toString()
			{
				return visualizer + " " + gels; //$NON-NLS-1$
			}

			@Override
			public int compareTo(Orderable o)
			{
				return toString().compareTo(o.toString());
			}
		}

		void sort()
		{
			Orderable[] array = new Orderable[visualizers.size()];
			for (int i = 0; i < array.length; i++)
			{
				array[i] = new Orderable(visualizers.get(i), gels.get(i), i);
			}
			Arrays.sort(array);
			ArrayList<Visualizer> visualizers = new ArrayList<Visualizer>();
			ArrayList<JGel> gels = new ArrayList<JGel>();

			ArrayList<Counter> males = new ArrayList<GroupByGel.Counter>();
			ArrayList<Counter> females = new ArrayList<GroupByGel.Counter>();

			for (int i = 0; i < array.length; i++)
			{
				int index = array[i].index;
				visualizers.add(this.visualizers.get(index));
				gels.add(this.gels.get(index));
				males.add(this.males.get(index));
				females.add(this.females.get(index));
			}

			this.visualizers = visualizers;
			this.gels = gels;
			this.males = males;
			this.females = females;

		}

		JPanel getComponent()
		{
			sort();

			JPanel panel = new JPanel();
			panel.setLayout(new MigLayout("")); //$NON-NLS-1$

			JPanel panel3 = new JPanel();
			panel3.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)));
			panel3.setBackground(Color.white);
			panel3.setLayout(new FlowLayout());
			JButton summary = new JButton(disclosure ? Messages.getString("GroupByGel.8") : Messages.getString("GroupByGel.7")); //$NON-NLS-1$ //$NON-NLS-2$
			panel3.add(new JLabel(Messages.getString("GroupByGel.6"))); //$NON-NLS-1$
			panel3.add(summary);
			panel.add(panel3, "spanx " + visualizers.size() + ", wrap"); //$NON-NLS-1$ //$NON-NLS-2$
			summary.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					disclosure = !disclosure;
					updateTree_SwingUtilitiesInvokeLater();
				}
			});

			panel.add(new JLabel(Messages.getString("GroupByGel.5"))); //$NON-NLS-1$
			for (Visualizer v : visualizers)
			{
				JComponent cc = v.getJComponent();
				cc.setPreferredSize(cc.getPreferredSize());
				cc.setMinimumSize(cc.getPreferredSize());
				panel.add(cc, "center"); //$NON-NLS-1$
			}
			panel.add(new JLabel(), "wrap"); //$NON-NLS-1$

			if (disclosure)
			{
				if (true)
				{
					JPanel panel2 = new JPanel();
					panel2.setLayout(new MigLayout());
					for (String property : allProperties)
					{
						panel2.add(new JLabel(property), "center, wrap"); //$NON-NLS-1$
					}
					panel.add(panel2, "center"); //$NON-NLS-1$
				}
				for (Visualizer v : visualizers)
				{
					Map<String, String> properties = v.getTooltipProperties();
					JPanel panel2 = new JPanel();
					panel2.setLayout(new MigLayout());
					for (String property : allProperties)
					{
						String value = properties.get(property);
						if (value != null)
						{
							panel2.add(new JLabel(value), "center, wrap"); //$NON-NLS-1$
						}
						else
						{
							panel2.add(new JLabel(" "), "center, wrap"); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					panel.add(panel2, "center"); //$NON-NLS-1$
				}
				panel.add(new JLabel(), "wrap"); //$NON-NLS-1$
			}
			for (int i = 0; i < visualizers.size(); i++)
			{
				panel.add(new JLabel(), ""); //$NON-NLS-1$
			}
			panel.add(new JLabel(), ""); //$NON-NLS-1$
			panel.add(new JLabel(Messages.getString("GroupByGel.3")), "aligny bottom, wrap"); //$NON-NLS-1$ //$NON-NLS-2$

			panel.add(new JLabel("Count")); //$NON-NLS-1$
			int total = 0;
			for (int i = 0; i < visualizers.size(); i++)
			{
				total += males.get(i).getCount() + females.get(i).getCount();
			}
			for (int i = 0; i < visualizers.size(); i++)
			{
				int nr = males.get(i).getCount() + females.get(i).getCount();
				panel.add(new JLabel(MessageFormat.format(Messages.getString("GroupByGel.4"), nr, 100 * nr / total)), "align center"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			panel.add(new JLabel("" + total), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$

			int male_total = 0;
			panel.add(new JLabel(Messages.getString("GroupByGel.2"))); //$NON-NLS-1$
			for (int i = 0; i < visualizers.size(); i++)
			{
				int nr = males.get(i).getCount();
				male_total += nr;
				panel.add(new JLabel("" + nr), "align center"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			panel.add(new JLabel("" + male_total), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$

			int female_total = 0;
			panel.add(new JLabel(Messages.getString("GroupByGel.1"))); //$NON-NLS-1$
			for (int i = 0; i < visualizers.size(); i++)
			{
				int nr = females.get(i).getCount();
				female_total += nr;
				panel.add(new JLabel("" + nr), "align center"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			panel.add(new JLabel("" + female_total), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$

			GelFilter gf = new GelFilter(mainModel, gels, model, main);
			gf.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)));
			gf.setBackground(Color.white);
			panel.add(gf, "wrap, growx , span " + (visualizers.size() + 2)); //$NON-NLS-1$

			panel.add(new JLabel("")); //$NON-NLS-1$
			for (JGel g : gels)
			{
				panel.add(g);
			}

			return panel;
		}
	}

	@SuppressWarnings("unchecked")
	private void update()
	{
		removeAll();
		setLayout(new BorderLayout());
		CreatureSet p = model.getProgenies();
		if (p.size() != 0)
		{
			Summary s = new Summary();
			for (Creature c : p)
			{
				Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
				// v.setName(c.getName());
				Map<String, String> properties = new TreeMap<String, String>();
				properties.putAll(c.getProperties());
				if (properties.containsKey(GeneticModel.matings))
				{
					properties.remove(GeneticModel.matings);
				}
				v.setProperties(properties, c.getSex());
				v.setNote(""); //$NON-NLS-1$
				v.setSex(null);
				JGel g = new JGel(mainModel, c);
				try
				{
					g.updateDisplayedGels((Iterable<Gel>) model.getMetadata().get(GelFilter.class));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
				s.addCreature(c, v, g);
			}
			add(s.getComponent());
		}
		else
		{
			try
			{
				String text = new String(FileUtils.getStreamToByteArray(this.getClass().getClassLoader().getResourceAsStream(Messages.getString("GroupByGel.0"))), Charset.forName("UTF-8")); //$NON-NLS-1$
				JPanel p2 = new JPanel();
				p2.add(new JLabel(text), BorderLayout.CENTER);
				add(p2, BorderLayout.NORTH);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
	}
}
