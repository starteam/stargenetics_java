package star.genetics.v2.ui.fly.common;

import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.genetic.impl.MatingException;
import star.genetics.genetic.impl.ModelPropertiesSheet;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.MatingEngine;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.crate.groupby.GroupBy;
import star.genetics.v1.ui.crate.groupby.GroupByGel;
import star.genetics.v1.ui.crate.parents.Parents;
import star.genetics.v1.ui.crate.progenies.Progenies;
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
public class Crate extends Crate_generated implements CrateInterface
{
	private static final long serialVersionUID = 1L;
	private Exception errorMessage;
	private boolean matingEnabled = true;
	private Model mainModel;
	private FlyCrateMetadata flyCrateModel;
	private transient JTabbedPane tabs;
	private transient CrateMateRaiser raiser = null;
	private transient CrateParentsRaiser parents = null;

	void initMetadata()
	{
		CrateModel crateModel = getModel();
		if (crateModel.getMetadata().get(this.getClass()) == null)
		{
			crateModel.getMetadata().put(this.getClass(), new FlyCrateMetadata());
		}
		this.flyCrateModel = (FlyCrateMetadata) crateModel.getMetadata().get(this.getClass());
	}

	public Crate(Model model, boolean useCurrent)
	{
		this.mainModel = model;
		setModel(new CrateModelImpl(model, useCurrent ? model.getCrateSet().current() : model.getCrateSet().newCrateModel()));
		initMetadata();
	}

	public Crate(Model model, star.genetics.genetic.model.CrateModel crateModel, boolean matingEnabled)
	{
		this.mainModel = model;
		setModel(new CrateModelImpl(model, crateModel));
		this.matingEnabled = matingEnabled;
		initMetadata();
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
		invalidate();
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

	private void withoutMatingUI(JPanel middlePanel, String constraint)
	{
		getAdapter().addExcludeExternal(OrganismSetAsParentRaiser.class);
		getAdapter().addExcludeExternal(OrganismSetAsParentEvent.class);

		getAdapter().addExcludeExternal(CrateProgeniesRaiser.class);
		getAdapter().addExcludeExternal(CrateProgeniesEvent.class);

		getAdapter().addExcludeInternal(CrateProgeniesRaiser.class);
		getAdapter().addExcludeInternal(CrateProgeniesEvent.class);
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		final String constraint = "grow,wrap,center"; //$NON-NLS-1$
		setLayout(new MigLayout("novisualpadding , fill , ins 0", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new MigLayout());
		add(middlePanel);

		middlePanel.add(new Parents(getModel(), matingEnabled), "dock west"); //$NON-NLS-1$
		if (!matingEnabled)
		{
			withoutMatingUI(middlePanel, constraint);
		}

		tabs = new JTabbedPane();
		tabs.setTabPlacement(SwingConstants.TOP);

		String progenies = Messages.getString("Crate.5"); //$NON-NLS-1$
		String sorted = Messages.getString("Crate.6"); //$NON-NLS-1$
		String groupbyOnly = Messages.getString("Crate.7"); //$NON-NLS-1$
		String groupbyTrait = Messages.getString("Crate.8"); //$NON-NLS-1$
		String groupbyGel = Messages.getString("Crate.9"); //$NON-NLS-1$

		tabs.addTab(progenies, new Progenies(getModel(), matingEnabled, false));
		tabs.addTab(sorted, new Progenies(getModel(), matingEnabled, true));
		if (mainModel.getGelRules() != null)
		{
			tabs.addTab(groupbyTrait, new JScrollPane(new GroupBy(getModel())));
			JScrollPane p2 = new JScrollPane(new GroupByGel(mainModel, getModel()));
			p2.getVerticalScrollBar().setUnitIncrement(12);
			p2.getHorizontalScrollBar().setUnitIncrement(12);
			tabs.addTab(groupbyGel, p2);
		}
		else
		{
			tabs.addTab(groupbyOnly, new JScrollPane(new GroupBy(getModel())));
		}
		tabs.setSelectedIndex(flyCrateModel.getIndex());
		add(tabs, "center, growx,growy"); //$NON-NLS-1$
	}

	public void removeNotify()
	{
		flyCrateModel.setIndex(tabs.getSelectedIndex());
		super.removeNotify();
	};

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

	public int getMaxProgenies()
	{
		int max = Integer.MAX_VALUE;
		ModelPropertiesSheet properties = (ModelPropertiesSheet) mainModel.getModelMetadata().get(ModelPropertiesSheet.class);
		if (properties != null)
		{
			String str = properties.get("MaxChildrenPerExperiment");
			if (str != null)
			{
				try
				{
					max = Math.round(Float.parseFloat(str));
				}
				catch (NumberFormatException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		return max;
	}

	void handleMating(CrateParentsRaiser parents, CrateMateRaiser raiser)
	{
		if (matingEnabled && parents != null && raiser != null)
		{
			try
			{
				final int max = getMaxProgenies();
				CreatureSet children = getModel().getProgenies();
				boolean overflow = children.size() >= max;
				if (children.size() < max)
				{
					MatingEngine engine = getModel().getMater();
					int count = raiser.getCount();
					OUT: for (int i = 0; i < count; i++)
					{
						int start_from = getModel().getProgenies().size();
						CreatureSet progenies = engine.getProgenies(getModel().getName(), parents.getParents(), start_from + 1, mainModel.getMatingsCount(), mainModel.getRules());
						setProgenies(progenies);
						for (Creature c : getProgenies())
						{
							children.add(c);
							if (children.size() >= max)
							{
								overflow = true;
								break OUT;
							}
						}
					}
				}
				if( overflow )
				{
					final JComponent self = this;
					SwingUtilities.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							JOptionPane.showMessageDialog(self, MessageFormat.format("Too many progenies! Limit per experiment is: {0}",max));
						}
					});
				}
				raise_CrateProgeniesEvent();
				UIHelpers.getFrame(this).repaint();
			}
			catch (final MatingException ex)
			{
				errorMessage = new RuntimeException(ex.getLocalizedMessage(), ex);
				// raise_ErrorDialogEvent();
				final JComponent self = this;
				SwingUtilities.invokeLater(new Runnable()
				{

					@Override
					public void run()
					{
						JOptionPane.showMessageDialog(self, ex.getLocalizedMessage());
					}
				});
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
