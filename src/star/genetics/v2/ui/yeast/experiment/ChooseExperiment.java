package star.genetics.v2.ui.yeast.experiment;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import star.genetics.Messages;
import star.genetics.genetic.model.Model;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;

public class ChooseExperiment extends TitledContainer
{
	private static final long serialVersionUID = 1L;

	public ChooseExperiment(Model model, boolean useCurrent)
	{
		init(getTitle(model), getBody(model, useCurrent), null);
	}

	private Component getTitle(Model model)
	{
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(new TitleLabel(Messages.getString("ChooseExperiment.0"))); //$NON-NLS-1$
		// panel.add(new NewMatingExperiment());
		// panel.add(new NewReplicationExperiment());
		return panel;
	}

	private Component getBody(Model model, boolean useCurrent)
	{
		JPanel ret = new JPanel();
		ret.setLayout(new MigLayout("alignx center, aligny center", "[][]")); //$NON-NLS-1$ //$NON-NLS-2$
		ret.add(new JLabel("<html>&nbsp;</html>"), "wrap,center"); //$NON-NLS-1$ //$NON-NLS-2$
		ret.add(new JLabel(Messages.getString("ChooseExperiment.5")), "wrap,center"); //$NON-NLS-1$ //$NON-NLS-2$
		ret.add(new NewReplicationExperiment(), "wrap, center"); //$NON-NLS-1$
		ret.add(new JLabel("<html><br></html>"), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		ret.add(new JLabel(Messages.getString("ChooseExperiment.10")), "wrap,center"); //$NON-NLS-1$ //$NON-NLS-2$
		ret.add(new NewMatingExperiment(), "wrap, center"); //$NON-NLS-1$
		return ret;
	}
}
