package star.genetics.v1.ui.organism;

import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.MessageFormat;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.annotations.Wrap.Types;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.common.AnnotatedCreatureListRenderer;
import star.genetics.v1.ui.events.ConfigureOrganismRaiser;
import star.genetics.v1.ui.events.CrateParentsRaiser;
import star.genetics.v1.ui.events.ListItemSelectedRaiser;
import star.genetics.v1.ui.events.NextFrameRaiser;
import star.genetics.v1.ui.events.OrganismRemoveRaiser;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.events.ProgenyAddProgenyRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v1.ui.impl.OrganismListModelImpl;
import star.genetics.v1.ui.impl.OrganismModelImpl;
import star.genetics.v1.ui.model.OrganismListModel;
import star.genetics.v1.ui.model.OrganismModel;
import star.genetics.v2.ui.common.ContextMenuMouseListener;
import star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser;

@SignalComponent(extend = JList.class, raises = { OrganismSelectedRaiser.class, OrganismSetAsParentRaiser.class, ListItemSelectedRaiser.class, ConfigureOrganismRaiser.class, ErrorDialogRaiser.class })
public class OrganismList extends OrganismList_generated implements FocusListener
{
	private static final long serialVersionUID = 1L;
	private transient OrganismModel model;
	private OrganismListModel listmodel;
	public boolean draggingOver;

	@Override
	public void addNotify()
	{
		super.addNotify();
		listmodel = new OrganismListModelImpl(model);
		setModel(listmodel);
		setVisibleRowCount(0);
		setDragEnabled(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellRenderer(new AnnotatedCreatureListRenderer(model.getVisualizerFactory()));
		setTransferHandler(new OrganismTransferHandler(this));
		OrganismMouseListener ml = new OrganismMouseListener(this);
		addMouseListener(ml);
		addMouseMotionListener(ml);
		addListSelectionListener(new OrganismListSelectionListener(this));
		addFocusListener(this);
		addMouseListener(new ContextMenuMouseListener(this));
		initialSelect_SwingUtilitiesInvokeLater();
	}

	@Wrap(type = Types.SwingUtilitiesInvokeLater)
	void initialSelect()
	{
		if (listmodel.getSize() > 0)
		{
			setSelectedIndex(0);
			raise_OrganismSelectedEvent();
		}
	}

	@Override
	public Rectangle getCellBounds(int index0, int index1)
	{
		Rectangle r = super.getCellBounds(index0, index1);
		// if (OS.isMacOSX())
		// {
		// r.width = CommonData.getPreferredSize().width;
		// if (index1 - index0 <= 1)
		// {
		// r.height = CommonData.getPreferredSize().height;
		// }
		// }
		return r;
	}

	public OrganismList(Model model)
	{
		this.model = new OrganismModelImpl(model);
	}

	public OrganismModel getGeneticModel()
	{
		return model;
	}

	public Creature getSelectedCreature()
	{
		return (Creature) getSelectedValue();
	}

	public OrganismListModel getListModel()
	{
		return listmodel;
	}

	@Override
	@Handles(raises = {})
	void renameCreature(CreaturePropertiesChangedRaiser r)
	{
		getListModel().invalidateModel();
	}

	@Override
	@Handles(raises = {})
	void validParents(CrateParentsRaiser r)
	{
		clearSelection();
	}

	@Override
	@Handles(raises = {})
	void organismSelected(ProgenySelectedRaiser r)
	{
		if (r.getSelectedCreature() != null)
		{
			clearSelection();
		}
	}

	@Override
	@Handles(raises = {})
	void removeOrganism(OrganismRemoveRaiser r)
	{
		removeSelected();
	}

	private void removeSelected()
	{
		Creature c = getSelectedCreature();
		if (c != null)
		{
			getListModel().remove(c);
			clearSelection();
		}
	}

	@Override
	@Handles(raises = {})
	protected void progenyAdded(ProgenyAddProgenyRaiser r)
	{
		Creature c = r.getSelectedCreature();
		if (c != null)
		{
			if (!getListModel().contains(c))
			{
				getListModel().add(c);
			}
			else
			{

				errorMessage = new RuntimeException(Messages.getString("OrganismList.0")); //$NON-NLS-1$
				raise_ErrorDialogEvent();
			}
		}
	}

	public void focusGained(FocusEvent e)
	{
		if (getSelectedCreature() != null)
		{
			raise_OrganismSelectedEvent();
			raise_ListItemSelectedEvent();
		}
	}

	public void focusLost(FocusEvent e)
	{
	}

	public JList getJList()
	{
		return this;
	}

	public Tuple[] getActions()
	{
		Tuple removeTuple = new Tuple(6, Action.REMOVE, new Runnable()
		{
			public void run()
			{
				int ret = JOptionPane.showConfirmDialog(getJList(), MessageFormat.format(Messages.getString("OrganismList.1"), getSelectedCreature().getName()), Messages.getString("OrganismList.2"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				if (ret == JOptionPane.YES_OPTION)
				{
					removeSelected();
				}
			}
		});
		Tuple setAsParentTuple = new Tuple(7, Action.SETASPARENT, new Runnable()
		{
			public void run()
			{
				raise_OrganismSetAsParentEvent();
			}
		});
		if (!listmodel.getElementAt(getSelectedIndex()).isReadOnly())
		{
			return new Tuple[] { removeTuple, setAsParentTuple };
		}
		else
		{
			return new Tuple[] { setAsParentTuple };
		}
	}

	Exception errorMessage = null;

	public Exception getErrorMessage()
	{

		return errorMessage;
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
