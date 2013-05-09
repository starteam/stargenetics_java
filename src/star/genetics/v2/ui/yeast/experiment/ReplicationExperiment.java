package star.genetics.v2.ui.yeast.experiment;

import java.awt.Component;

import javax.swing.JPanel;

import star.genetic.yeast.ExperimentType;
import star.genetics.Messages;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.Model;
import star.genetics.v2.ui.common.CrateInterface;
import star.genetics.v2.ui.common.DiscardExperiment;
import star.genetics.v2.ui.common.NewExperiment;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.v2.ui.fly.CrateProvider;
import star.genetics.v2.ui.yeast.common.ReplicaPanelController;

public class ReplicationExperiment extends TitledContainer implements CrateProvider
{
	private static final long serialVersionUID = 1L;

	private ReplicaPanelController crate;

	public ReplicationExperiment(Model model, boolean useCurrent)
	{
		CrateModel crateModel = useCurrent ? model.getCrateSet().current() : model.getCrateSet().newCrateModel();
		crate = new ReplicaPanelController(model, crateModel, true);
		crateModel.getMetadata().put(ExperimentType.class, ExperimentType.REPLICA);
		init(getTitle(model), crate, null);

	}

	private Component getTitle(Model model)
	{
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(new TitleLabel(Messages.getString("ReplicationExperiment.0"))); //$NON-NLS-1$
		panel.add(new ReplicaExperimentAddAll());
		panel.add(new NewExperiment(this));
		panel.add(new DiscardExperiment(this, true));
		return panel;
	}

	@Override
	public CrateInterface getCrate()
	{
		return crate;
	}

	@Override
	public void updateCrateProvier()
	{
		// TODO Auto-generated method stub

	}

}