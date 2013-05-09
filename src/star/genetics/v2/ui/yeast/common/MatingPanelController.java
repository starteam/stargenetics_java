package star.genetics.v2.ui.yeast.common;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.genetic.impl.MatingException;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.MatingEngine;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateMateRaiser;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v1.ui.events.CrateParentsRaiser;
import star.genetics.v1.ui.events.CrateProgeniesEvent;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentEvent;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v1.ui.impl.CrateModelImpl;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.ui.common.CrateInterface;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class, raises = { CrateProgeniesRaiser.class, CrateNewCrateRaiser.class, ErrorDialogRaiser.class })
@Properties({ @Property(name = "progenies", type = CreatureSet.class, getter = Property.PUBLIC), @Property(name = "model", type = CrateModel.class, getter = Property.PUBLIC) })
public class MatingPanelController extends MatingPanelController_generated implements CrateInterface
{
	private static final long serialVersionUID = 1L;
	private Exception errorMessage;
	private boolean matingEnabled = true;
	private Model mainModel;
	private CrateParentsRaiser parents = null;
	private CrateMateRaiser raiser = null;

	public MatingPanelController(Model model, star.genetics.genetic.model.CrateModel crateModel, boolean matingEnabled)
	{
		this.mainModel = model;
		setModel(new CrateModelImpl(model, crateModel));
		this.matingEnabled = matingEnabled;
	}

	public String getCrateName()
	{
		return getModel().getName();
	}

	public void setCrateName(String name)
	{
		String oldName = getCrateName();
		getModel().setName(name);
		updateProgenies(oldName);
		UIHelpers.invalidate(this);
		repaint();
	}

	private void updateProgenies(String oldName)
	{
		String name = getCrateName();
		for (Creature c : getModel().getProgenies())
		{
			if (c.getName().startsWith(oldName))
			{
				c.setName(name + c.getName().substring(oldName.length()));
			}
		}
	}

	private void withoutMatingUI()
	{
		getAdapter().addExcludeExternal(OrganismSetAsParentRaiser.class);
		getAdapter().addExcludeExternal(OrganismSetAsParentEvent.class);

		getAdapter().addExcludeInternal(OrganismSetAsParentRaiser.class);
		getAdapter().addExcludeInternal(OrganismSetAsParentEvent.class);

		getAdapter().addExcludeExternal(CrateProgeniesRaiser.class);
		getAdapter().addExcludeExternal(CrateProgeniesEvent.class);

		getAdapter().addExcludeInternal(CrateProgeniesRaiser.class);
		getAdapter().addExcludeInternal(CrateProgeniesEvent.class);
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BorderLayout());
		if (!matingEnabled)
		{
			withoutMatingUI();
		}
		JScrollPane sp = new JScrollPane(new MatingPanel(getModel(), mainModel, matingEnabled), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.getVerticalScrollBar().setUnitIncrement(25);
		add(sp);

	}

	@Override
	@Handles(raises = { ErrorDialogRaiser.class })
	void handleMatingParent(CrateParentsRaiser parents)
	{
		this.parents = parents;
		handleMating(parents, raiser);
	}

	@Override
	@Handles(raises = { ErrorDialogRaiser.class })
	void handleMatingMate(CrateMateRaiser raiser)
	{
		this.raiser = raiser;
		handleMating(parents, raiser);
	}

	private void handleMating(CrateParentsRaiser parents, final CrateMateRaiser raiser)
	{
		if (matingEnabled && parents != null && raiser != null)
		{
			try
			{
				final int count = raiser.getCount();
				for (int i = 0; i < count; i++)
				{
					MatingEngine engine = getModel().getMater();
					int start_from = getModel().getProgenies().size();
					CreatureSet progenies = engine.getProgenies(getModel().getName(), parents.getParents(), start_from + 1, mainModel.getMatingsCount(), mainModel.getRules());
					setProgenies(progenies);
					for (Creature c : getProgenies())
					{
						getModel().getProgenies().add(c);
					}
				}
				raise_CrateProgeniesEvent();
				UIHelpers.getFrame(this).repaint();
			}
			catch (MatingException ex)
			{
				errorMessage = new RuntimeException(ex.getLocalizedMessage(), ex);
				raise_ErrorDialogEvent();
			}
		}
	}

	@Override
	public String toString()
	{
		return getClass() + "-" + getModel().getName() + "-" + matingEnabled; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Exception getErrorMessage()
	{
		return errorMessage;
	}

}
