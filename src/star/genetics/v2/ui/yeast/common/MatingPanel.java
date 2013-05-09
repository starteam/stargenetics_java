package star.genetics.v2.ui.yeast.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.genetics.Messages;
import star.genetics.genetic.impl.SimpleEntry;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateParentsUpdatedRaiser;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.yeast.YeastUI;
import star.genetics.v2.ui.yeast.common.YeastParents.Labels;
import star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser;
import star.genetics.v2.yeast.events.StorableExperimentRaiser;
import utils.Icons;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class, raises = { StorableExperimentRaiser.class })
@Properties({ @Property(name = "model", type = CrateModel.class) })
public class MatingPanel extends MatingPanel_generated
{
	private static final long serialVersionUID = 1L;
	private YeastParents yp;
	private MatingPanel self = this;
	private Model mainModel;

	private Dimension step = new Dimension(32, 36);
	private HaploidViewMetadata metadata;
	private FixedGridLayout layoutEngine = new FixedGridLayout(this, step);
	private volatile boolean displayTetrads = false;
	private volatile boolean displaySummary = false;
	private boolean matingEnabled;

	public MatingPanel(CrateModel model, Model mainModel, boolean matingEnabled)
	{
		this.matingEnabled = matingEnabled;
		displayTetrads = (model.getProgenies().size() < 401);
		setModel(model);
		yp = new YeastParents(model, mainModel);
		this.mainModel = mainModel;
		initExperiments(model);
		putClientProperty("order_male_female", (int) Math.round(Math.random())); //$NON-NLS-1$
	}

	private void initExperiments(CrateModel model)
	{
		if (model.getMetadata().get(this.getClass()) == null)
		{
			HaploidViewMetadata metadata = new HaploidViewMetadata();
			metadata.experiments = new ArrayList<Entry<Creature, String>>();
			metadata.comments = new ArrayList<String>();
			model.getMetadata().put(this.getClass(), metadata);
		}
		this.metadata = (HaploidViewMetadata) model.getMetadata().get(this.getClass());
		int count = (model.getProgenies().size() / 4 * (this.metadata.experiments.size() + 1));

		if (count > 600)
		{
			if (!matingEnabled)
			{
				displayTetrads = false;
				displaySummary = true;
			}
		}
		else if (count > 1000)
		{
			if (!matingEnabled)
			{
				displayTetrads = false;
				displaySummary = false;
			}
		}
		else
		{
			displaySummary = true;
		}
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(layoutEngine);
		setBackground(YeastUI.getMediaColor());
		getAdapter().addComponent(yp);

		updateView_SwingUtilitiesInvokeLater();
	}

	@Override
	@Handles(raises = {})
	void handle(CreaturePropertiesChangedRaiser r)
	{
		if (r.getKind() == CreaturePropertiesChangedRaiser.Kind.NAME)
		{
			updateView_SwingUtilitiesInvokeLater();
		}
	}

	@Override
	@Wrap(type = Wrap.Types.SwingUtilitiesInvokeLater)
	void updateView()
	{
		UIHelpers.PleaseWait pleaseWait = new UIHelpers.PleaseWait(UIHelpers.getFrame(this), 2000, Messages.getString("MatingPanel.1") + getModel().getName()); //$NON-NLS-1$
		updateView2();
		pleaseWait.done();
	}

	void updateView2()
	{
		final int tetrade_step = 7;
		CrateModel model = getModel();
		String currentName = mainModel.getCrateSet().current().getName();
		// fix for slow history repaint
		if (!String.valueOf(currentName).equals(model.getName()) && getComponentCount() > 0)
		{
			return;
		}
		removeAll();
		if (model.getParents().size() != 2)
		{
			sillyMatingSite(new Point(6, 1));
		}
		else if (model.getProgenies().size() == 0)
		{
			if (false) // model.getParents().size() == 1)
			{
				Point p = new Point(0, 0);
				p.x += 5;
				if (metadata.experiments.size() != 0)
				{
					add(new TitleLabel(Messages.getString("MatingPanel.2")), new Rectangle(p.x + 5, p.y + 0, 12, 1)); //$NON-NLS-1$
				}
				p.y++;
				newReplicationExperiment(p);
				p.x += metadata.experiments.size() * 5;
				for (Entry<Creature, String> entry : metadata.experiments)
				{
					replica(p, entry.getKey(), entry.getValue(), true);
					p.x -= 5;
				}

			}
			Point p = new Point(6, 1);
			matingSite(p);

		}
		else
		{
			Point top = new Point(0, 0);
			if (true)
			{
				Point p = new Point(top.x, top.y);
				p.x += 17 - 7;
				p.x++;
				if (metadata.experiments.size() != 0)
				{
					add(new TitleLabel(Messages.getString("MatingPanel.3")), new Rectangle(p.x + 1, p.y + 0, 12, 1)); //$NON-NLS-1$
				}
				p.x++;
				p.y++;
				for (Entry<Creature, String> entry : metadata.experiments)
				{
					replica(p, entry.getKey(), entry.getValue(), false);
					p.x += tetrade_step;
				}
				p.x -= metadata.experiments.size() * tetrade_step;
			}
			Point p = new Point(top.x, top.y + 1);
			p.x += 6;
			matingSite(p);
			p.y -= 2;
			tetrads(p, null, null);
			p.y += 2;
			p.x -= 6;
			// metadata(p);
			if (true)
			{
				p.x += 1;
				newExperiment(p);
				p.x += 10;
				p.x++;
				p.y -= 2;
				for (Entry<Creature, String> entry : metadata.experiments)
				{
					tetrads(p, entry.getKey(), entry.getValue());
					p.x += tetrade_step;
				}
				p.x -= tetrade_step * metadata.experiments.size();
				p.x++;
			}
		}
		if (model.getParents().size() == 2)
		{
			raise_StorableExperimentEvent();
		}
		invalidate();
		synchronized (getTreeLock())
		{
			validateTree();
		}
		getParent().validate();

	}

	private void metadata(Point p)
	{
		CrateModel model = getModel();

		int y = p.y;
		p.x += 5;
		// add(new TitleLabel("Notes"), new Rectangle(p.x + 1, p.y + 0, 3, 1));
		add(new TitleLabel(Messages.getString("MatingPanel.4")), new Rectangle(p.x + 1, p.y + 0, 2, 1)); //$NON-NLS-1$
		add(new TitleLabel(Messages.getString("MatingPanel.5")), new Rectangle(p.x + 3, p.y + 0, 2, 1)); //$NON-NLS-1$
		add(new TitleLabel(Messages.getString("MatingPanel.6")), new Rectangle(p.x + 5, p.y + 0, 2, 1)); //$NON-NLS-1$
		p.y += 1;
		final LinkedHashMap<String, TitleLabel> labels = new LinkedHashMap<String, TitleLabel>();
		labels.put(Messages.getString("MatingPanel.7"), new TitleLabel(Messages.getString("MatingPanel.8"))); //$NON-NLS-1$ //$NON-NLS-2$
		labels.put(Messages.getString("MatingPanel.9"), new TitleLabel(Messages.getString("MatingPanel.10"))); //$NON-NLS-1$ //$NON-NLS-2$
		labels.put(Messages.getString("MatingPanel.11"), new TitleLabel(Messages.getString("MatingPanel.12"))); //$NON-NLS-1$ //$NON-NLS-2$
		for (int counter = 0; counter < model.getProgenies().size(); counter++)
		{
			if ((counter++ & 3) == 0)
			{
				final int row = (counter / 4);
				if (metadata.comments.size() <= row)
				{
					metadata.comments.add(Messages.getString("MatingPanel.13")); //$NON-NLS-1$
				}

				String comment = metadata.comments.get(row);

				final JCheckBox npd = new JCheckBox(Messages.getString("MatingPanel.14"), Messages.getString("MatingPanel.15").equals(comment)); //$NON-NLS-1$ //$NON-NLS-2$
				final JCheckBox pd = new JCheckBox(Messages.getString("MatingPanel.16"), Messages.getString("MatingPanel.17").equals(comment)); //$NON-NLS-1$ //$NON-NLS-2$
				final JCheckBox tt = new JCheckBox(Messages.getString("MatingPanel.18"), Messages.getString("MatingPanel.19").equals(comment)); //$NON-NLS-1$ //$NON-NLS-2$
				// final JCheckBox qq = new JCheckBox("??", true);

				final ButtonGroup bg = new ButtonGroup();
				bg.add(npd);
				bg.add(pd);
				bg.add(tt);
				// bg.add(qq);
				add(pd, new Rectangle(p.x + 1, p.y + 0, 2, 1));
				add(tt, new Rectangle(p.x + 3, p.y + 0, 2, 1));
				add(npd, new Rectangle(p.x + 5, p.y + 0, 2, 1));
				// add(qq, new Rectangle(p.x + 6, p.y + 0, 2, 1));

				final ActionListener al = new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (e.getActionCommand().equals(metadata.comments.get(row)))
						{
							bg.clearSelection();
							metadata.comments.set(row, ""); //$NON-NLS-1$
						}
						else
						{
							metadata.comments.set(row, e.getActionCommand());
						}
						for (Entry<String, TitleLabel> entry : labels.entrySet())
						{
							String key = entry.getKey();
							TitleLabel label = entry.getValue();
							if (label != null)
							{
								int c = 0;
								for (String md : metadata.comments)
								{
									if (key.equals(md))
									{
										c++;
									}
								}
								label.setText("" + c); //$NON-NLS-1$
							}
						}
					}
				};

				npd.addActionListener(al);
				pd.addActionListener(al);
				tt.addActionListener(al);
				// qq.addActionListener(al);
				p.y++;
			}
		}
		TitleLabel l = new TitleLabel(Messages.getString("MatingPanel.22")); //$NON-NLS-1$
		add(l, new Rectangle(p.x - 1, p.y, 6, 1));
		// l.setBorder( BorderFactory.createMatteBorder(1, 0, 0,0, Color.white));
		int px_offset = 1;
		for (Entry<String, TitleLabel> entry : labels.entrySet())
		{
			String key = entry.getKey();
			TitleLabel label = entry.getValue();
			int c = 0;
			for (String md : metadata.comments)
			{
				if (key.equals(md))
				{
					c++;
				}
			}
			label.setText("" + c); //$NON-NLS-1$
			label.setHorizontalTextPosition(SwingConstants.TRAILING);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.white));
			add(label, new Rectangle(p.x + px_offset, p.y, 2, 1));
			px_offset += 2;
		}
		p.y++;
		JButton clear = new JButton(Messages.getString("MatingPanel.24")); //$NON-NLS-1$
		add(clear, new Rectangle(p.x + 2, p.y++, 4, 1));
		clear.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (int row = 0; row < metadata.comments.size(); row++)
				{
					metadata.comments.set(row, ""); //$NON-NLS-1$
				}
				updateView_SwingUtilitiesInvokeLater();
			}
		});
		p.y = y;
		// p.x -= 2;
		p.x += 2;
	}

	private void matingSite(Point top)
	{
		add(new TitleLabel(Messages.getString("MatingPanel.26")), new Rectangle(top.x + 1, top.y + 0, 4, 1)); //$NON-NLS-1$
		if (sp1 != null)
		{
			add(yp.getLabel(sp1.getCreature().getSex() == Sex.FEMALE ? Labels.Female : Labels.Male), new Rectangle(top.x + 1, top.y + 1, 2, 2));
			add(yp.getLabel(sp1.getCreature().getSex() != Sex.FEMALE ? Labels.Female : Labels.Male), new Rectangle(top.x + 3, top.y + 1, 2, 2));
		}
		else
		{
			int r = (Integer) this.getClientProperty("order_male_female"); //$NON-NLS-1$
			add(yp.getLabel(r == 0 ? Labels.Female : Labels.Male), new Rectangle(top.x + 1, top.y + 1, 2, 2));
			add(yp.getLabel(r != 0 ? Labels.Female : Labels.Male), new Rectangle(top.x + 3, top.y + 1, 2, 2));

		}
		// add(yp.getLabel(Labels.Diploid), new Rectangle(top.x + 2, top.y + 3, 2, 2));
		top.y += 5;
	}

	private SillyParentLabel sp1;
	private SillyParentLabel sp2;
	private int sillyCounter = 0;

	@Override
	@Handles(raises = {})
	void setAsParent(OrganismSetAsParentRaiser r)
	{
		if (sp1.getCreature() == null)
		{
			sp1.setCreature(r.getSelectedCreature());
		}
		else if (sp2.getCreature() == null)
		{
			sp2.setCreature(r.getSelectedCreature());
		}
		else
		{
			(sillyCounter++ % 2 == 0 ? sp1 : sp2).setCreature(r.getSelectedCreature());
		}
	}

	public void updateSilly()
	{
		if (sp1.getCreature() != null)
		{
			yp.setCreature(sp1.getCreature());
		}
		if (sp2.getCreature() != null)
		{
			yp.setCreature(sp2.getCreature());
		}
	}

	private void sillyMatingSite(Point top)
	{
		if (sp1 == null)
		{
			sp1 = new SillyParentLabel(getModel().getVisualizerFactory(), this);
			sp2 = new SillyParentLabel(getModel().getVisualizerFactory(), this);

		}
		add(new TitleLabel(Messages.getString("MatingPanel.28")), new Rectangle(top.x + 1, top.y + 0, 4, 1)); //$NON-NLS-1$
		add(sp1, new Rectangle(top.x + 1, top.y + 1, 2, 2));
		add(sp2, new Rectangle(top.x + 3, top.y + 1, 2, 2));

		if (sp1.getCreature() != null && sp2.getCreature() != null)
		{
			add(new JLabel(Messages.getString("MatingPanel.29")), new Rectangle(top.x + 6, top.y + 1, 6, 2)); //$NON-NLS-1$
		}
		top.y += 5;
	}

	private JButton getRemoveButton(final Creature creature, final String property)
	{
		JButton remove = new JButton();
		remove.setToolTipText(Messages.getString("MatingPanel.30")); //$NON-NLS-1$
		remove.setEnabled(true);
		remove.setText(Messages.getString("MatingPanel.31")); //$NON-NLS-1$
		remove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				metadata.experiments.remove(new SimpleEntry<Creature, String>(creature, property));
				removeAll();
				updateView();
			}
		});
		return remove;
	}

	private void replica(Point top, final Creature creature, final String property, boolean remove)
	{
		CrateModel model = getModel();
		add(getLabel(creature, property), new Rectangle(top.x + 1, top.y + 0, 4, 1));
		if (remove)
		{
			add(getRemoveButton(creature, property), new Rectangle(top.x + 1, top.y + 5, 4, 1));
		}

		int index = 4;

		Creature[] parents = new Creature[2];

		if (sp1 != null)
		{
			parents[0] = yp.getCreature(sp1.getCreature().getSex() == Sex.FEMALE ? Sex.FEMALE : Sex.MALE);
			parents[1] = yp.getCreature(sp1.getCreature().getSex() != Sex.FEMALE ? Sex.FEMALE : Sex.MALE);
		}
		else
		{
			int r = (Integer) this.getClientProperty("order_male_female"); //$NON-NLS-1$
			parents[0] = model.getParents().get((r == 0) ? 0 : 1);
			parents[1] = model.getParents().get((r != 0) ? 0 : 1);
		}

		for (Creature c : parents)
		{

			YeastProgenyLabel label = new YeastProgenyLabel(true, c, creature, property, mainModel, step);
			label.setSelectable(false);
			add(label, new Rectangle(top.x + (index % 4) + 1, top.y + (index / 4), 2, 2));
			index += 2;
		}
		YeastProgenyLabel label = new YeastProgenyLabel(true, yp.getCreature(null), null, property, mainModel, step);
		label.setSelectable(false);
		// add(label, new Rectangle(top.x + 2, top.y + 3, 2, 2));
	}

	class TetradCounter
	{
		int count = 0;
		YeastProgenyLabel[] list;
	}

	private void tetrads(final Point top, final Creature creature, final String property)
	{
		CrateModel model = getModel();
		CreatureSet cs = model.getProgenies();
		TreeMap<String, TetradCounter> summaryMap = new TreeMap<String, MatingPanel.TetradCounter>();
		int last_index;
		boolean showSummary = true;
		if (creature == null)
		{
			if (property == null)
			{
				// JPanel p = new JPanel();
				// p.setLayout(new FlowLayout());
				// p.add(new TitleLabel("<html>Tetrads <font size='-2'>(Complete media)</font></html>"));
				// JLabel l = new JLabel("<html><font size=-2>(Complete media)</font></html>");
				// p.add(l);
				add(new TitleLabel(Messages.getString("MatingPanel.33")), new Rectangle(top.x + 1, top.y + 0, 3, 1)); //$NON-NLS-1$
				if (displayTetrads)
				{
					int index = 4;
					for (int i = 0; i < cs.size(); i++)
					{
						add(new TitleLabel(Messages.getString("MatingPanel.34") + (index / 4)), new Rectangle(top.x, top.y + (index / 4), 4, 1)); //$NON-NLS-1$
						index++;
					}
				}
				else
				{
					int index = 4;
					JButton displayTetradsButton = new JButton(Messages.getString("MatingPanel.35")); //$NON-NLS-1$
					// displayTetradsButton.setBackground(new Color(0, 198, 239));
					// displayTetradsButton.setOpaque(true);
					// displayTetradsButton.setBorderPainted(false);

					add(displayTetradsButton, new Rectangle(top.x + 1, top.y + (index / 4), 4, 2));
					displayTetradsButton.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent arg0)
						{
							displayTetrads = true;
							removeAll();
							updateView();
						}
					});
					if (!displaySummary)
					{
						index += 4;
						JButton displaySummaryButton = new JButton(Messages.getString("MatingPanel.36")); //$NON-NLS-1$
						add(displaySummaryButton, new Rectangle(top.x, top.y + (index / 4), 4, 1));
						displaySummaryButton.addActionListener(new ActionListener()
						{

							@Override
							public void actionPerformed(ActionEvent arg0)
							{
								displaySummary = true;
								removeAll();
								updateView();
							}
						});
					}
				}
			}
			else
			{
				// add(getLabel(creature, property), new Rectangle(top.x + 1, top.y + 0, 4, 1));
			}
			if (!displayTetrads && !displaySummary)
			{
				return;
			}
			int index = 4;
			int[] counter = new int[5];
			int inTetradCounter = 0;

			String[] tetradToolTips = new String[4];
			YeastProgenyLabel[] tetradVisualizer = new YeastProgenyLabel[4];

			for (Creature c : cs)
			{
				YeastProgenyLabel ypl = new YeastProgenyLabel(property != null, c, null, property, mainModel, step);
				tetradToolTips[index % 4] = ypl.getToolTipText();
				tetradVisualizer[index % 4] = displayTetrads ? new YeastProgenyLabel(property != null, c, null, property, mainModel, step) : ypl;

				try
				{
					float growth = (Float) ypl.getClientProperty(ypl.GROWS);
					boolean lawn = (Boolean) ypl.getClientProperty(ypl.LAWN_GROWS);
					if ((lawn == false) && (growth > .25))
					{
						inTetradCounter++;
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				if (displayTetrads)
				{
					add(ypl, new Rectangle(top.x + (index % 4) + 1, top.y + (index / 4), 1, 1));
				}
				index++;
				if (index % 4 == 0)
				{
					counter[inTetradCounter]++;
					inTetradCounter = 0;
					Arrays.sort(tetradToolTips);
					String tetradKey = Arrays.toString(tetradToolTips);
					if (!summaryMap.containsKey(tetradKey))
					{
						summaryMap.put(tetradKey, new TetradCounter());
						summaryMap.get(tetradKey).list = tetradVisualizer;
						tetradVisualizer = new YeastProgenyLabel[4];
					}
					summaryMap.get(tetradKey).count++;
				}
			}
			index += 0;
			if (property != null)
			{
				add(getRemoveButton(creature, property), new Rectangle(top.x + (index % 4) + 1, top.y + (displayTetrads ? (index / 4) : 1), 4, 1));
				index += 4;
			}
			else
			{
				showSummary = false;
			}
			last_index = index;
		}
		else
		{
			// add(getLabel(creature, property), new Rectangle(top.x + 1, top.y + 0, 4, 1));
			int index = 4;
			int[] counter = new int[5];
			int inTetradCounter = 0;

			// String[] tetradToolTips = new String[4];
			// YeastProgenyLabel[] tetradVisualizer = new YeastProgenyLabel[4];

			if (displayTetrads)
			{
				for (Creature c : cs)
				{
					YeastProgenyLabel ypl = new YeastProgenyLabel(true, c, creature, property, mainModel, step);

					// tetradToolTips[index % 4] = ypl.getToolTipText();
					// tetradVisualizer[index % 4] = new YeastProgenyLabel(true, c, creature, property, mainModel, step);

					try
					{
						float growth = (Float) ypl.getClientProperty(ypl.GROWS);
						boolean lawn = (Boolean) ypl.getClientProperty(ypl.LAWN_GROWS);
						if ((lawn == false) && (growth > .25))
						{
							inTetradCounter++;
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					add(ypl, new Rectangle(top.x + (index % 4) + 1, top.y + (index / 4), 1, 1));
					index++;
					if (index % 4 == 0)
					{
						counter[inTetradCounter]++;
						inTetradCounter = 0;

						// Arrays.sort(tetradToolTips);
						// String tetradKey = Arrays.toString(tetradToolTips);
						// if (!summaryMap.containsKey(tetradKey))
						// {
						// summaryMap.put(tetradKey, new TetradCounter());
						// summaryMap.get(tetradKey).list = tetradVisualizer;
						// tetradVisualizer = new YeastProgenyLabel[4];
						// }
						// summaryMap.get(tetradKey).count++;

					}
				}
				index += 4;
			}
			add(getRemoveButton(creature, property), new Rectangle(top.x + (index % 4) + 1, top.y + (index / 4) - (displayTetrads ? 1 : 0), 4, 1));
			last_index = index;
			showSummary = false;
		}

		if (showSummary)
		{
			int index = displayTetrads ? (last_index + 4) : 12;
			if (true)
			{
				JLabel ll = new TitleLabel(Messages.getString("MatingPanel.37")); //$NON-NLS-1$
				ll.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
				add(ll, new Rectangle(top.x + 5, top.y + (index / 4) - 1, 2, 1));
			}
			if (true)
			{
				JLabel ll = new TitleLabel(Messages.getString("MatingPanel.38")); //$NON-NLS-1$
				ll.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
				add(ll, new Rectangle(top.x + 1, top.y + (index / 4) - 1, 4, 1));
			}

			for (Entry<String, TetradCounter> entry : summaryMap.entrySet())
			{
				YeastProgenyLabel[] list = entry.getValue().list;
				Arrays.sort(list, new Comparator<YeastProgenyLabel>()
				{
					@Override
					public int compare(YeastProgenyLabel arg0, YeastProgenyLabel arg1)
					{
						return arg0.getToolTipText().compareTo(arg1.getToolTipText());
					}
				});
				for (YeastProgenyLabel visualizer : list)
				{
					if ((Boolean) visualizer.getClientProperty(visualizer.LAWN_GROWS))
					{
						JLabel l = new TitleLabel(visualizer.getToolTipText());
						l.setToolTipText(visualizer.getToolTipText());
						add(l, new Rectangle(top.x + (index % 4) + 1, top.y + (index / 4), 4, 1));
						index += 4;
						break;
					}
					visualizer.setSelectable(false);
					add(visualizer, new Rectangle(top.x + (index % 4) + 1, top.y + (index / 4), 1, 1));
					index++;
				}
				JPanel p = new JPanel();
				p.setBackground(new Color(0, 0, 0, 0));
				JLabel ll = new TitleLabel(Messages.getString("MatingPanel.39") + entry.getValue().count); //$NON-NLS-1$
				p.add(ll);
				add(p, new Rectangle(top.x + 5, top.y + (index / 4) - 1, 2, 1));
				last_index = index;
			}
		}
		else
		{
			if (property != null)
			{
				int index = displayTetrads ? (last_index + 4) : 12;
				if (false)
				{
					JLabel ll = new TitleLabel(Messages.getString("MatingPanel.40")); //$NON-NLS-1$
					ll.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
					add(ll, new Rectangle(top.x + 5, top.y + (index / 4) - 1, 2, 1));
				}
				if (false)
				{
					JLabel ll = new TitleLabel(Messages.getString("MatingPanel.41")); //$NON-NLS-1$
					ll.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
					add(ll, new Rectangle(top.x + 1, top.y + (index / 4) - 1, 4, 1));
				}
				if (false)
				{
					// index += 4;
					JPanel p = new JPanel();
					p.setBackground(new Color(0, 0, 0, 0));
					JLabel ll = new TitleLabel(Messages.getString("MatingPanel.42") + getModel().getProgenies().size() / 4); //$NON-NLS-1$
					p.add(ll);
					add(p, new Rectangle(top.x + 5, top.y + (index / 4) - 1, 2, 2));
				}
				if (true)
				{
					JLabel ll = new TitleLabel(Messages.getString("MatingPanel.43")); //$NON-NLS-1$
					ll.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
					add(ll, new Rectangle(top.x + 1, top.y + (index / 4) - 1, 4, 2));
				}

			}
		}
	}

	private JLabel getLabel(Creature creature, String property)
	{
		String text = Messages.getString("MatingPanel.44") + getCreatureName(creature, "") + "&nbsp;<br>" + property + " &nbsp;</body></html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String tooltip_text = (creature != null ? creature.getName() + ": " : "") + property; //$NON-NLS-1$ //$NON-NLS-2$
		JLabel ret = new TitleLabel(text);
		ret.setToolTipText(tooltip_text);
		return ret;
	}

	private String getCreatureName(Creature c, String postfix)
	{
		if (c != null)
		{
			String name = c.getName();
			if (name.length() > 18)
			{
				return name.substring(0, 10) + Messages.getString("MatingPanel.50") + postfix; //$NON-NLS-1$
			}
			return name + postfix;
		}
		return " "; //$NON-NLS-1$
	}

	private void newReplicationExperiment(Point top)
	{
		JLabel l = new TitleLabel(Messages.getString("MatingPanel.52")); //$NON-NLS-1$
		add(l, new Rectangle(top.x, top.y - 1, 6, 2));

		// JLabel l2 = new TitleLabel("Media");
		// add(l2, new Rectangle(top.x, top.y, 5, 1));

		DefaultListModel listModel2 = new DefaultListModel();
		for (String c : mainModel.getRules().getPropertyNames())
		{
			listModel2.addElement(c);
		}
		final JList list2 = new JList(listModel2);
		list2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if (lastEntry != null && lastEntry.getValue() != null)
		{
			list2.setSelectedValue(lastEntry.getValue(), true);
		}
		JScrollPane p = new JScrollPane(list2);
		p.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		add(p, new Rectangle(top.x + 0, top.y + 1, 4, 4));
		JButton add = new JButton(Messages.getString("MatingPanel.53"), Icons.ADD.getIcon(16)); //$NON-NLS-1$

		// JButton add = new JButton("Add 2", Icons.ADD.getIcon(16));
		JPanel addP = new JPanel();
		addP.setOpaque(false);
		// addP.setLayout(new BorderLayout());
		addP.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		addP.add(add);
		add(add, new Rectangle(top.x + 0, top.y + 5, 4, 1));
		add.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				StringBuffer errorMessages = new StringBuffer();
				final Creature c = null;
				for (Object obj : list2.getSelectedValues())
				{
					final String property = (String) obj;
					if (property != null)
					{
						Entry<Creature, String> entry = new SimpleEntry<Creature, String>(c, property);
						if (!metadata.experiments.contains(entry))
						{
							lastEntry = entry;
							metadata.experiments.add(entry);
						}
						else
						{
							errorMessages.append(MessageFormat.format(Messages.getString("MatingPanel.54"), entry.getValue())); //$NON-NLS-1$
						}
					}
				}
				if (errorMessages.length() != 0)
				{
					JOptionPane.showMessageDialog(self, errorMessages, Messages.getString("MatingPanel.0"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
				}
				removeAll();
				updateView_SwingUtilitiesInvokeLater();
			}
		});
	}

	Entry<Creature, String> lastEntry = null;

	private void newExperiment(Point p)
	{
		Point top = new Point(p);
		top.y -= 6;
		add(new TitleLabel(Messages.getString("MatingPanel.55")), new Rectangle(top.x + 0, top.y + 0, 8, 1)); //$NON-NLS-1$
		top.y++;
		// add(new JLabel(""), new Rectangle(top.x + 0, top.y + 0, 4, 1));
		add(new TitleLabel(Messages.getString("MatingPanel.56")), new Rectangle(top.x + 0, top.y + 0, 4, 1)); //$NON-NLS-1$

		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(null);
		for (Creature c : mainModel.getCreatures())
		{
			listModel.addElement(c);
		}
		final JList list = new JList(listModel);
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setCellRenderer(new ListCellRenderer()
		{
			DefaultListCellRenderer dlcr = new DefaultListCellRenderer();

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				String str = ""; //$NON-NLS-1$
				if (value != null)
				{
					str = ((Creature) value).getName();
				}
				else
				{
					str = Messages.getString("MatingPanel.58"); //$NON-NLS-1$
				}
				return dlcr.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
			}
		});
		if (lastEntry != null && lastEntry.getKey() != null)
		{
			list.setSelectedValue(lastEntry.getKey(), true);
		}

		add(new JScrollPane(list), new Rectangle(top.x + 0, top.y + 1, 4, 4));
		add(new TitleLabel(Messages.getString("MatingPanel.59")), new Rectangle(top.x + 0, top.y + 5, 4, 1)); //$NON-NLS-1$
		DefaultListModel listModel2 = new DefaultListModel();
		for (String c : mainModel.getRules().getPropertyNames())
		{
			listModel2.addElement(c);
		}
		final JList list2 = new JList(listModel2);
		if (lastEntry != null && lastEntry.getValue() != null)
		{
			list2.setSelectedValue(lastEntry.getValue(), true);
		}
		list2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(new JScrollPane(list2), new Rectangle(top.x + 0, top.y + 6, 4, 4));
		JButton add = new JButton(Messages.getString("MatingPanel.60"), Icons.ADD.getIcon(16)); //$NON-NLS-1$
		add(add, new Rectangle(top.x, top.y + 10, 4, 1));
		add.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				StringBuffer errorMessages = new StringBuffer();
				for (Object cr : list.getSelectedValues())
				{
					final Creature c = (Creature) cr;
					for (Object obj : list2.getSelectedValues())
					{
						final String property = (String) obj;
						if (property != null)
						{
							Entry<Creature, String> entry = new SimpleEntry<Creature, String>(c, property);

							if (!metadata.experiments.contains(entry))
							{
								boolean error = false;
								if (yp.getParents().size() == 1 && c != null)
								{
									Sex sex = c.getSex();
									if (yp.getCreature(sex) != null)
									{
										error = true;
									}
								}
								if (!error)
								{
									lastEntry = entry;
									metadata.experiments.add(entry);
								}
								else
								{
									errorMessages.append(Messages.getString("MatingPanel.61")); //$NON-NLS-1$
								}
							}
							else
							{
								Creature tmpC = entry.getKey();
								String name = tmpC != null ? tmpC.getName() : Messages.getString("MatingPanel.62"); //$NON-NLS-1$
								errorMessages.append(MessageFormat.format(Messages.getString("MatingPanel.63"), name, entry.getValue())); //$NON-NLS-1$
							}
						}
					}
				}
				if (errorMessages.length() != 0)
				{
					JOptionPane.showMessageDialog(self, errorMessages, Messages.getString("MatingPanel.0"), JOptionPane.PLAIN_MESSAGE);
				}
				removeAll();
				updateView_SwingUtilitiesInvokeLater();
			}
		});
	}

	@Override
	@Handles(raises = {})
	void progeniesEvent(CrateProgeniesRaiser r)
	{
		if (matingEnabled)
		{
			displayTetrads = (getModel().getProgenies().size() < (100 * 4 + 1));
			removeAll();
			updateView_SwingUtilitiesInvokeLater();
		}
	}

	@Override
	@Handles(raises = {})
	void parentsEvent(CrateParentsUpdatedRaiser r)
	{
		if (r.getModel() == getModel())
		{
			removeAll();
			updateView_SwingUtilitiesInvokeLater();
		}
	}
}
