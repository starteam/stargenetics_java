package star.genetics.v1.ui.crate.progenies;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.common.CreatureListRenderer;
import star.genetics.v1.ui.events.ConfigureOrganismRaiser;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.events.ListItemSelectedRaiser;
import star.genetics.v1.ui.events.NextFrameRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.events.ProgenyAddProgenyRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v1.ui.impl.ProgeniesListModelImpl;
import star.genetics.v1.ui.impl.SortedProgeniesListModelImpl;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v1.ui.model.ProgeniesListModel;
import star.genetics.v2.ui.common.ContextMenuMouseListener;
import star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser;

@SignalComponent(extend = JList.class, raises = { ProgenySelectedRaiser.class, ProgenyAddProgenyRaiser.class, ListItemSelectedRaiser.class, OrganismSetAsParentRaiser.class, ConfigureOrganismRaiser.class })
@Properties({ @Property(name = "crateModel", type = CrateModel.class), @Property(name = "listModel", type = ProgeniesListModel.class) })
public class ProgeniesList extends ProgeniesList_generated implements FocusListener
{
	private static final long serialVersionUID = 1L;
	private boolean active;
	private boolean sorted;

	public ProgeniesList(CrateModel model, boolean active, boolean sorted)
	{
		setCrateModel(model);
		this.active = active;
		this.sorted = sorted;
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		if (!sorted)
		{
			setListModel(new ProgeniesListModelImpl(getCrateModel()));
		}
		else
		{
			setListModel(new SortedProgeniesListModelImpl(getCrateModel(), getCrateModel().getVisualizerFactory()));
		}
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setVisible(true);
		setModel(getListModel());
		setDragEnabled(true);
		setTransferHandler(new ProgeniesTransferHandler(this));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellRenderer(new CreatureListRenderer(getCrateModel().getVisualizerFactory()));
		addListSelectionListener(new ProgeniesListSelectionListener());
		ProgeniesMouseListener ml = new ProgeniesMouseListener(this);
		addMouseListener(ml);
		addMouseMotionListener(ml);
		addMouseListener(new ContextMenuMouseListener(this));
		addFocusListener(this);
	}

	@Override
	@Handles(raises = {})
	void progeniesEvent(CrateProgeniesRaiser r)
	{
		getListModel().update();
	}

	@Override
	@Handles(raises = {})
	void handleCreatureChange(CreaturePropertiesChangedRaiser r)
	{
		if (r.getKind() == CreaturePropertiesChangedRaiser.Kind.NAME)
		{
			getListModel().update();
		}
	}

	public Creature getSelectedCreature()
	{
		return (Creature) getSelectedValue();
	}

	public GeneticModel getGeneticModel()
	{
		return getCrateModel();
	}

	public void focusGained(FocusEvent e)
	{
		if (getSelectedCreature() != null)
		{
			raise_ProgenySelectedEvent();
			raise_ListItemSelectedEvent();
		}
	}

	public void focusLost(FocusEvent e)
	{
		// clearSelection();
	}

	public Tuple[] getActions()
	{
		Tuple addProgeny = new Tuple(6, Action.ADDTOLIST, new Runnable()
		{
			public void run()
			{
				raise_ProgenyAddProgenyEvent();
			}
		});
		if (!active)
		{
			Tuple setAsParent = new Tuple(7, Action.SETASPARENT, new Runnable()
			{
				public void run()
				{
					raise_OrganismSetAsParentEvent();
				}
			});
			return new Tuple[] { setAsParent, addProgeny };
		}
		else
		{
			return new Tuple[] { addProgeny };
		}
	}

	public JList getJList()
	{
		return this;
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
