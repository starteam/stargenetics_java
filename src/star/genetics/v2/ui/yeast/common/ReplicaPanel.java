package star.genetics.v2.ui.yeast.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.impl.SimpleEntry;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.yeast.YeastUI;
import star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser;
import star.genetics.v2.yeast.events.ReplicationExperimentAddAllRaiser;
import star.genetics.v2.yeast.events.StorableExperimentRaiser;
import star.genetics.visualizers.Visualizer;
import utils.Icons;

@SignalComponent(extend = JPanel.class, raises = { StorableExperimentRaiser.class })
@Properties({ @Property(name = "model", type = CrateModel.class) })
public class ReplicaPanel extends ReplicaPanel_generated
{
	private static final long serialVersionUID = 1L;
	private Model mainModel;
	private ReplicaPanel self = this;
	private final Dimension step = new Dimension(32, 40);// new Dimension(32, 36);
	private HaploidViewMetadata metadata;
	private FixedGridLayout layoutEngine = new FixedGridLayout(this, step, new Point(12, 0));
	private boolean matingEnabled = true;

	public ReplicaPanel(CrateModel model, Model mainModel, boolean matingEnabled)
	{
		setModel(model);
		this.mainModel = mainModel;
		this.matingEnabled = matingEnabled;
		initExperiments(model);
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
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(layoutEngine);
		setBackground(YeastUI.getMediaColor());
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
		removeAll();
		final CrateModel model = getModel();
		Point p = new Point(7, 1);
		newExperiment(new Point(0, 1));
		int y = 1;
		if (metadata.experiments.size() != 0)
		{
			add(new TitleLabel(Messages.getString("ReplicaPanel.0")), new Rectangle(p.x + 1, p.y - 1, 12, 1)); //$NON-NLS-1$
		}
		add(new TitleLabel(Messages.getString("ReplicaPanel.1")), new Rectangle(p.x - 7, p.y - 1, 8, 1)); //$NON-NLS-1$
		for (final Creature c : model.getProgenies())
		{
			Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
			Helper.setVisualizerFromCreature(v, c);
			final JComponent label = v.getJComponent();
			label.addMouseListener(new MouseAdapter()
			{
				ImageIcon delete = Icons.DELETE.getIcon(16);

				@Override
				public void mouseClicked(MouseEvent arg0)
				{
					model.getProgenies().remove(c);
					updateView_SwingUtilitiesInvokeLater();
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{
					label.setBorder(new Border()
					{

						@Override
						public void paintBorder(Component cc, Graphics gg, int x, int y, int w, int h)
						{
							gg.setColor(Color.gray);
							gg.drawRoundRect(0, 0, w - 1, h - 1, 5, 5);
							delete.paintIcon(cc, gg, x + w - 16, 0);
						}

						@Override
						public boolean isBorderOpaque()
						{
							return false;
						}

						@Override
						public Insets getBorderInsets(Component arg0)
						{
							return new Insets(1, 1, 1, 8);
						}
					});
					super.mouseEntered(e);
				}

				@Override
				public void mouseExited(MouseEvent e)
				{
					label.setBorder(null);
					super.mouseExited(e);
				}
			});
			add(label, new Rectangle(p.x - 2, p.y + y, 2, 1));
			y++;
		}
		addRow(p);
		if (matingEnabled)
		{
			addPlaceHolder(new Point(p.x - 2, p.y + y));
		}
		invalidate();
		synchronized (getTreeLock())
		{
			validateTree();
		}
		getParent().validate();
	}

	private String getCreatureName(Creature c, String postfix)
	{
		if (c != null)
		{
			String name = c.getName();
			if (name.length() > 18)
			{
				return name.substring(0, 10) + Messages.getString("ReplicaPanel.2") + postfix; //$NON-NLS-1$
			}
			return name + postfix;
		}
		return " "; //$NON-NLS-1$
	}

	private JLabel getLabel(Creature creature, String property)
	{
		String text = "<html><body>" + getCreatureName(creature, "") + "<br>" + property + "</body></html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String tooltip_text = (creature != null ? creature.getName() + ": " : "") + property; //$NON-NLS-1$ //$NON-NLS-2$
		JLabel ret = new TitleLabel(text);
		ret.setToolTipText(tooltip_text != null ? tooltip_text : "");
		return ret;
	}

	void addRow(Point top2)
	{
		Point top = new Point(top2);
		top.x -= 6;
		CrateModel model = getModel();
		int x = 0;
		for (final Entry<Creature, String> e : metadata.experiments)
		{
			int y = 0;
			JLabel label = getLabel(e.getKey(), e.getValue());
			add(label, new Rectangle(top.x + x * 4 + 7, top.y, 4, 1));
			y++;
			for (Creature c : model.getProgenies())
			{
				YeastProgenyLabel replica = new YeastProgenyLabel(true, c, e.getKey(), e.getValue(), mainModel, step);
				add(replica, new Rectangle(top.x + x * 4 + 8, top.y + y, 2, 1));
				y++;
			}
			y++;
			JButton discard = new JButton(Messages.getString("ReplicaPanel.10"), Icons.REMOVE.getIcon(16)); //$NON-NLS-1$
			discard.setToolTipText(Messages.getString("ReplicaPanel.11")); //$NON-NLS-1$
			discard.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent ee)
				{
					metadata.experiments.remove(e);

					updateView();
				}
			});
			add(discard, new Rectangle(top.x + x * 4 + 8, top.y + Math.max(10, y), 3, 1));
			// // TODO: Chris comment nr 3
			// String text = (e.getKey() != null ? e.getKey().getName() + "\n\n" : "") + e.getValue();
			// final JTextArea lnote = new JTextArea(5, 12);
			// lnote.setText(text);
			// lnote.setWrapStyleWord(false);
			// lnote.setLineWrap(true);
			// lnote.setBackground(YeastUI.getMediaColor());
			// lnote.setForeground(Color.white);
			// add(lnote, new Rectangle(top.x + x * 3 + 7, top.y + Math.max(10, y) + 1, 3, 5));
			x++;
		}
	}

	private void newExperiment(Point top)
	{
		add(new TitleLabel(Messages.getString("ReplicaPanel.12")), new Rectangle(top.x + 0, top.y + 0, 4, 1)); //$NON-NLS-1$

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
					str = Messages.getString("ReplicaPanel.14"); //$NON-NLS-1$
				}
				return dlcr.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
			}
		});
		if (lastEntry != null && lastEntry.getKey() != null)
		{
			list.setSelectedValue(lastEntry.getKey(), true);
		}
		add(new JScrollPane(list), new Rectangle(top.x + 0, top.y + 1, 4, 4));
		add(new TitleLabel(Messages.getString("ReplicaPanel.15")), new Rectangle(top.x + 0, top.y + 5, 4, 1)); //$NON-NLS-1$
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
		add(new JScrollPane(list2), new Rectangle(top.x + 0, top.y + 6, 4, 4));
		JButton add = new JButton(Messages.getString("ReplicaPanel.16"), Icons.ADD.getIcon(16)); //$NON-NLS-1$
		add.setEnabled(getModel().getProgenies().size() != 0);
		add(add, new Rectangle(top.x + 0, top.y + 10, 4, 1));
		add.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				StringBuffer errorMessages = new StringBuffer();
				for (Object o : list.getSelectedValues())
				{
					final Creature c = (Creature) o;
					for (Object obj : list2.getSelectedValues())
					{
						final String property = (String) obj;
						if (property != null)
						{
							Entry<Creature, String> entry = new SimpleEntry<Creature, String>(c, property);

							if (!metadata.experiments.contains(entry))
							{
								boolean error = false;
								if (!error)
								{
									lastEntry = entry;
									metadata.experiments.add(entry);
								}
								else
								{
									errorMessages.append(Messages.getString("ReplicaPanel.17")); //$NON-NLS-1$
								}
							}
							else
							{
								Creature tmpC = entry.getKey();
								String name = tmpC != null ? tmpC.getName() : Messages.getString("ReplicaPanel.18"); //$NON-NLS-1$
								errorMessages.append(MessageFormat.format(Messages.getString("ReplicaPanel.19"), name, entry.getValue())); //$NON-NLS-1$
							}
						}
					}
				}
				if (errorMessages.length() != 0)
				{
					JOptionPane.showMessageDialog(self, errorMessages);
				}
				updateView_SwingUtilitiesInvokeLater();
			}
		});
	}

	Entry<Creature, String> lastEntry = null;

	void addPlaceHolder(Point top)
	{
		add(new MyDropTarget(this, step), new Rectangle(top.x, top.y, 2, 1));
	}

	public void addCreature(Creature c, boolean raiseDialog)
	{
		if (c != null)
		{
			if (!getModel().getProgenies().contains(c))
			{
				getModel().getProgenies().add(c);
				updateView_SwingUtilitiesInvokeLater();
				raise_StorableExperimentEvent();
			}
			else
			{
				if (raiseDialog)
				{
					showCreatureIsAlreadyInTheList_SwingUtilitiesInvokeLater();
				}
			}
		}
	}

	@Override
	@Wrap(type = Wrap.Types.SwingUtilitiesInvokeLater)
	void showCreatureIsAlreadyInTheList()
	{
		JOptionPane.showMessageDialog(this, Messages.getString("ReplicaPanel.20")); //$NON-NLS-1$
	}

	@Override
	@Handles(raises = {})
	void handle(OrganismSetAsParentRaiser r)
	{
		addCreature(r.getSelectedCreature(), true);
	}

	@Override
	@Handles(raises = {})
	void handle(ReplicationExperimentAddAllRaiser r)
	{
		for (Creature c : mainModel.getCreatures())
		{
			addCreature(c, false);
		}
	}
}
