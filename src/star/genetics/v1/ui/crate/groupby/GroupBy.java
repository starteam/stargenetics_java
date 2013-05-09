package star.genetics.v1.ui.crate.groupby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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
import star.genetics.genetic.impl.Summary;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.events.NextFrameRaiser;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.visualizers.Visualizer;
import utils.FileUtils;

@SignalComponent(extend = JPanel.class)
public class GroupBy extends GroupBy_generated
{
	private static final long serialVersionUID = 1L;
	private CrateModel model;

	public GroupBy(CrateModel model)
	{
		this.model = model;
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

	private enum Rows
	{
		VISUALIZER(0, Messages.getString("GroupBy.0")), TOTAL_COUNT(2, Messages.getString("GroupBy.1")), FEMALE_PERC(3, Messages.getString("GroupBy.2")), MALE_PERC(4, Messages.getString("GroupBy.3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		private String text;
		ArrayList<JComponent> components = new ArrayList<JComponent>();

		private Rows(int code, String text)
		{
			this.text = text;
		}

		public String getText()
		{
			return text;
		}

		public void add(JComponent c)
		{
			components.add(c);
		}

		public void clear()
		{
			components.clear();
		}

		public int size()
		{
			return components.size();
		}

		Iterable<JComponent> getComponents()
		{
			if (this.equals(TOTAL_COUNT))
			{
				int total = 0;
				for (int i = 1; i < components.size() - 1; i++)
				{
					JComponent c = components.get(i);
					if (c instanceof JLabel)
					{
						JLabel l = (JLabel) c;
						try
						{
							total = total + Integer.parseInt(l.getText().trim());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
				for (int i = 1; i < components.size() - 1; i++)
				{
					JComponent c = components.get(i);
					if (c instanceof JLabel)
					{
						JLabel l = (JLabel) c;
						try
						{
							int value = Integer.parseInt(l.getText().trim());
							l.setText(MessageFormat.format(Messages.getString("GroupBy.4"), l.getText(), Math.round(value * 100f / total))); //$NON-NLS-1$
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}

			}

			return components;
		}
	}

	boolean disclosure = false;

	private synchronized void update()
	{
		boolean nullSex = false;
		removeAll();
		CreatureSet p = model.getProgenies();
		if (p.size() != 0)
		{
			Summary counter = new Summary();
			for (Creature c : p)
			{
				Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
				v.setName(c.getName());
				Map<String, String> properties = new TreeMap<String, String>();
				properties.putAll(c.getProperties());
				if (properties.containsKey(GeneticModel.matings))
				{
					properties.remove(GeneticModel.matings);
				}
				v.setProperties(properties, c.getSex());
				v.setNote(""); //$NON-NLS-1$
				counter.add(v, c.getSex());
				if (c.getSex() == null)
				{
					nullSex = true;
				}
			}

			for (Rows row : Rows.values())
			{
				row.clear();
				row.add(new JLabel(row.getText()));
			}

			int total_count = 0;
			int total_female = 0;
			for (String key : counter)
			{
				Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
				Rows.VISUALIZER.add(counter.getComponent(key));
				String likeParentString = ""; //$NON-NLS-1$
				for (Creature c : model.getParents())
				{
					v.setProperties(c.getProperties(), c.getSex());
					v.setNote(""); //$NON-NLS-1$
					if (v.getTooltipProperties().toString().equals(key))
					{
						if (likeParentString.length() != 0)
						{
							likeParentString = Messages.getString("GroupBy.9"); //$NON-NLS-1$
						}
						else
						{
							likeParentString += c.getSex().toString();
						}
					}
				}
				int count = counter.getCount(key);
				int female = counter.getCountFemale(key);
				int male = count - female;
				Rows.TOTAL_COUNT.add(new JLabel("" + count)); //$NON-NLS-1$
				Rows.FEMALE_PERC.add(new JLabel("" + female)); //$NON-NLS-1$
				Rows.MALE_PERC.add(new JLabel("" + male)); //$NON-NLS-1$
				total_count += count;
				total_female += female;
			}
			Rows.VISUALIZER.add(new JLabel(" ")); //$NON-NLS-1$
			Rows.TOTAL_COUNT.add(new JLabel("" + total_count)); //$NON-NLS-1$
			Rows.FEMALE_PERC.add(new JLabel("" + (total_female))); //$NON-NLS-1$
			Rows.MALE_PERC.add(new JLabel("" + (total_count - total_female))); //$NON-NLS-1$

			boolean headRow = true;

			StringBuilder constrains = new StringBuilder("2"); //$NON-NLS-1$
			for (int i = 0; i < Rows.values().length; i++)
			{
				constrains.append("[]2"); //$NON-NLS-1$
			}
			setLayout(new MigLayout("", "", constrains.toString())); //$NON-NLS-1$ //$NON-NLS-2$
			for (Rows row : Rows.values())
			{
				if (nullSex & (Rows.FEMALE_PERC.equals(row) || Rows.MALE_PERC.equals(row)))
				{
					continue;
				}
				boolean head = true;
				if (headRow)// && !head)
				{
					JPanel panel3 = new JPanel();
					panel3.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)));

					panel3.setLayout(new FlowLayout());
					JButton summary = new JButton(disclosure ? Messages.getString("GroupBy.21") : Messages.getString("GroupBy.22")); //$NON-NLS-1$ //$NON-NLS-2$
					panel3.setBackground(Color.white);
					panel3.add(new JLabel(Messages.getString("GroupBy.23"))); //$NON-NLS-1$
					panel3.add(summary);
					add(panel3, "spanx " + row.size() + ", wrap"); //$NON-NLS-1$ //$NON-NLS-2$
					summary.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							disclosure = !disclosure;
							invalidate();
							repaint();
							update();
						}
					});
				}
				for (JComponent c : row.getComponents())
				{
					Dimension ps = c.getPreferredSize();
					if (ps != null && (c instanceof Visualizer))
					{
						c.setSize(ps);
						add(c, "center, wmin " + ps.getWidth() + ", hmin " + ps.getHeight()); //$NON-NLS-1$ //$NON-NLS-2$
					}
					else
					{
						add(c, "center"); //$NON-NLS-1$
					}
					head = false;
				}
				add(new JLabel(""), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$

				if (headRow && !head && disclosure)
				{
					boolean firstColumn = true;
					for (JComponent c : row.getComponents())
					{
						if (c instanceof Visualizer)
						{
							Visualizer v = (Visualizer) c;
							Map<String, String> properties = v.getTooltipProperties();
							JPanel panel = new JPanel();
							panel.setLayout(new MigLayout("")); //$NON-NLS-1$
							for (String property : counter.getProperties())
							{
								String value = properties.get(property);
								if (value != null)
								{
									panel.add(new JLabel(value), "hmax 20, hmin 20, height 20px!, center, wrap"); //$NON-NLS-1$
								}
								else
								{
									panel.add(new JLabel(" "), "hmax 20, hmin 20, height 20px!, center, wrap"); //$NON-NLS-1$ //$NON-NLS-2$
								}
							}
							add(panel, "center, growy"); //$NON-NLS-1$
						}
						else
						{
							if (firstColumn)
							{
								JPanel panel = new JPanel();
								panel.setLayout(new MigLayout("")); //$NON-NLS-1$
								for (String property : counter.getProperties())
								{
									panel.add(new JLabel("<html><b>" + property + "</b></html>"), "hmax 20, hmin 20, height 20px!, center, wrap"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								}
								add(panel, "center, growy"); //$NON-NLS-1$
							}
							else
							{
								add(new JLabel(" ")); //$NON-NLS-1$
							}
						}
						firstColumn = false;
					}
					add(new JLabel(""), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
					JPanel qp = new JPanel();
					qp.setMinimumSize(new Dimension(32, 32));

				}
				if (headRow)
				{
					for (int i = 1; i < row.size(); i++)
					{
						JPanel l = new JPanel();
						l.setLayout(new BorderLayout());
						add(l, "grow 1 1"); //$NON-NLS-1$
					}
					add(new JLabel(Messages.getString("GroupBy.45")), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				headRow = false;
			}

		}
		else
		{
			try
			{
				String text = new String(FileUtils.getStreamToByteArray(this.getClass().getClassLoader().getResourceAsStream(Messages.getString("GroupBy.6"))), Charset.forName("UTF-8")); //$NON-NLS-1$
				add(new JLabel(text), "span,wrap"); //$NON-NLS-1$
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}

		}
		validate();
		synchronized (getTreeLock())
		{
			validateTree();
		}

	}

	@Override
	@Handles(raises = {})
	void nextFrame(NextFrameRaiser r)
	{
		if (isShowing())
		{
			if (!r.needFocus() || hasFocus())
			{
				repaint(1000);
			}
		}
	}

}
