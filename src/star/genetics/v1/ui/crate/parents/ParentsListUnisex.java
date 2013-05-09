package star.genetics.v1.ui.crate.parents;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.common.CreatureListRenderer;
import star.genetics.v1.ui.events.ConfigureOrganismRaiser;
import star.genetics.v1.ui.events.CrateMateRaiser;
import star.genetics.v1.ui.events.CrateParentsRaiser;
import star.genetics.v1.ui.events.ListItemSelectedRaiser;
import star.genetics.v1.ui.events.NextFrameRaiser;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.events.ProgenyAddProgenyRaiser;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v1.ui.model.ParentsListModel;
import star.genetics.v2.ui.common.ContextMenuMouseListener;

@SignalComponent(extend = JList.class, raises = { CrateParentsRaiser.class, ListItemSelectedRaiser.class, ConfigureOrganismRaiser.class, OrganismSetAsParentRaiser.class, ProgenyAddProgenyRaiser.class, OrganismSelectedRaiser.class })
@Properties({ @Property(name = "crateModel", type = CrateModel.class), @Property(name = "listModel", type = ParentsListModel.class) })
public class ParentsListUnisex extends ParentsListUnisex_generated implements FocusListener, ParentsListInterface
{
	private static final long serialVersionUID = 1L;
	private boolean matingEnabled = true;
	private boolean mated = false;
	private ParentsListUnisex self = this;

	public ParentsListUnisex(CrateModel model, boolean matingEnabled)
	{
		setCrateModel(model);
		this.matingEnabled = matingEnabled;
	}

	public Creature getSelectedCreature()
	{
		Object value = getSelectedValue();
		return value instanceof Creature ? (Creature) value : null;
	}

	public void focusGained(FocusEvent e)
	{
		if (getSelectedCreature() != null)
		{
			raise_ListItemSelectedEvent();
			raise_OrganismSelectedEvent();
		}
	}

	public void focusLost(FocusEvent e)
	{
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setListModel(new star.genetics.v1.ui.impl.ParentsListModelImplUnisex(getCrateModel()));
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisible(true);
		setVisibleRowCount(2);
		setModel(getListModel());
		setDragEnabled(true);
		setTransferHandler(new ParentsTransferHandler(this));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellRenderer(new CreatureListRenderer(getCrateModel().getVisualizerFactory()));
		addFocusListener(this);
		addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				raise_ListItemSelectedEvent();
				raise_OrganismSelectedEvent();
			}
		});
		addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					raise_ConfigureOrganismEvent();
				}

			}
		});
		addMouseListener(new ContextMenuMouseListener(this));
	}

	private void removeSelected()
	{
		if (getSelectedCreature() != null)
		{
			getListModel().clear(getSelectedCreature().getSex());
			clearSelection();
			raise_ListItemSelectedEvent();
		}

	}

	public void setCreature(Creature c)
	{
		if (c != null)
		{
			if (getListModel().getCreatures().size() < 2)
			{
				getListModel().set(c);
			}
			else
			{
				JOptionPane.showMessageDialog(this, Messages.getString("ParentsListUnisex.0")); //$NON-NLS-1$
			}
		}
		if (getListModel().isValid())
		{
			raise_CrateParentsEvent();
		}
	}

	@Override
	@Handles(raises = {})
	void onMate(CrateMateRaiser r)
	{
		setTransferHandler(new TransferHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
			{
				return true;
			}

			@Override
			public boolean importData(JComponent comp, Transferable t)
			{
				JOptionPane.showMessageDialog(self, Messages.getString("ParentsListUnisex.1")); //$NON-NLS-1$
				return false;
			}
		});
		mated = true;
	}

	public CreatureSet getParents()
	{
		return getListModel().getCreatures();
	}

	@Override
	@Handles(raises = {})
	void setAsParent(OrganismSetAsParentRaiser set)
	{
		Creature c = set.getSelectedCreature();
		if (c != null && isEnabled() && matingEnabled && !mated)
		{
			setCreature(c);
		}
	}

	public Tuple[] getActions()
	{
		ArrayList<Tuple> actions = new ArrayList<Tuple>();
		if (getSelectedCreature() != null)
		{

			Tuple renameTuple = new Tuple(8, Action.PROPERTIES, new Runnable()
			{
				public void run()
				{
					raise_ConfigureOrganismEvent();
				}
			});

			actions.add(renameTuple);

			if (!matingEnabled)
			{
				Tuple setAsParent = new Tuple(7, Action.SETASPARENT, new Runnable()
				{
					public void run()
					{
						raise_OrganismSetAsParentEvent();
					}
				});
				actions.add(setAsParent);
			}
			if (!mated && matingEnabled)
			{
				Tuple removeTuple = new Tuple(6, Action.REMOVE, new Runnable()
				{
					public void run()
					{
						int ret = JOptionPane.showConfirmDialog(getJList(), MessageFormat.format(Messages.getString("ParentsListUnisex.2"), getSelectedCreature().getName()), Messages.getString("ParentsListUnisex.3"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						if (ret == JOptionPane.YES_OPTION)
						{
							removeSelected();
						}
					}
				});
				actions.add(removeTuple);
			}
			else
			{
				Tuple addProgeny = new Tuple(6, Action.ADDTOLIST, new Runnable()
				{
					public void run()
					{
						raise_ProgenyAddProgenyEvent();
					}
				});
				actions.add(addProgeny);
			}
		}
		return actions.toArray(new Tuple[actions.size()]);
	}

	public JList getJList()
	{
		return this;
	}

	public GeneticModel getGeneticModel()
	{
		return getCrateModel();
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
