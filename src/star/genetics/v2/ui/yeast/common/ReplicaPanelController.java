package star.genetics.v2.ui.yeast.common;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.impl.CrateModelImpl;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.ui.common.CrateInterface;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class)
@Properties({ @Property(name = "progenies", type = CreatureSet.class, getter = Property.PUBLIC), @Property(name = "model", type = CrateModel.class, getter = Property.PUBLIC) })
public class ReplicaPanelController extends ReplicaPanelController_generated implements CrateInterface
{
	private static final long serialVersionUID = 1L;
	private Model mainModel;
	private boolean matingEnabled = true;

	public ReplicaPanelController(Model model, star.genetics.genetic.model.CrateModel crateModel, boolean matingEnabled)
	{
		this.mainModel = model;
		this.matingEnabled = matingEnabled;
		setModel(new CrateModelImpl(model, crateModel));
	}

	public String getCrateName()
	{
		return getModel().getName();
	}

	public void setCrateName(String name)
	{
		getModel().setName(name);
		UIHelpers.invalidate(this);
		repaint();
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BorderLayout());
		JScrollPane sp = new JScrollPane(new ReplicaPanel(getModel(), mainModel, matingEnabled), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.getVerticalScrollBar().setUnitIncrement(25);
		add(sp);
	}
}
