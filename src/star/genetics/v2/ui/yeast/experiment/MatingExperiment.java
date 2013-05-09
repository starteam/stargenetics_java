package star.genetics.v2.ui.yeast.experiment;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import star.genetic.yeast.ExperimentType;
import star.genetics.Messages;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.Model;
import star.genetics.v2.ui.common.CommonUI;
import star.genetics.v2.ui.common.CrateInterface;
import star.genetics.v2.ui.common.DiscardExperiment;
import star.genetics.v2.ui.common.NewExperiment;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.v2.ui.fly.CrateProvider;
import star.genetics.v2.ui.yeast.common.MatingPanelController;
import star.genetics.v2.ui.yeast.common.Sporulate;

public class MatingExperiment extends TitledContainer implements CrateProvider
{
	private static final long serialVersionUID = 1L;
	private MatingPanelController crate;

	public MatingExperiment(Model model, boolean useCurrent)
	{
		super();
		CrateModel crateModel = useCurrent ? model.getCrateSet().current() : model.getCrateSet().newCrateModel();
		crate = new MatingPanelController(model, crateModel, true);
		crateModel.getMetadata().put(ExperimentType.class, ExperimentType.MATING);
		Component title = getTitle(model);
		Component body = crate;
		init(title, body, null);
	}

	private Component getTitle(Model model)
	{
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(new TitleLabel(Messages.getString("MatingExperiment.0"))); //$NON-NLS-1$
		final JTextField n = new JTextField(crate.getCrateName(), 6);
		n.setEditable(false);
		n.setFont(CommonUI.get().getTitleFont(n.getFont()));
		n.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String name = (String) JOptionPane.showInputDialog(n, Messages.getString("MatingExperiment.1"), Messages.getString("MatingExperiment.2"), JOptionPane.QUESTION_MESSAGE, null, null, crate.getCrateName()); //$NON-NLS-1$ //$NON-NLS-2$
				if (name != null && name.length() != 0)
				{
					crate.setCrateName(name);
					n.setText(crate.getCrateName());
				}
			}
		});
		panel.add(n);
		panel.add(new Sporulate(model.getProgeniesCount()));
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
