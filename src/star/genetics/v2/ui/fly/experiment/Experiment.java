package star.genetics.v2.ui.fly.experiment;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import star.genetics.Messages;
import star.genetics.genetic.model.Model;
import star.genetics.v2.ui.common.CommonUI;
import star.genetics.v2.ui.common.CrateInterface;
import star.genetics.v2.ui.common.DiscardExperiment;
import star.genetics.v2.ui.common.NewExperiment;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.v2.ui.fly.CrateProvider;
import star.genetics.v2.ui.fly.common.Crate;

public class Experiment extends TitledContainer implements CrateProvider
{
	private static final long serialVersionUID = 1L;
	private Crate crate;

	public Experiment(Model model)
	{
		this(model, model.getCrateSet().size() != 0);
	}

	public Experiment(Model model, boolean b)
	{
		super();
		crate = new Crate(model, b);
		Component title = getTitle(model);
		Component body = crate;
		init(title, body, null);

	}

	private Component getTitle(Model model)
	{
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.add(new TitleLabel(Messages.getString("Experiment.0"))); //$NON-NLS-1$
		final JTextField n = new JTextField(crate.getCrateName(), 6);
		n.setEditable(false);
		n.setFont(CommonUI.get().getTitleFont(n.getFont()));
		n.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String name = (String) JOptionPane.showInputDialog(n, Messages.getString("Experiment.1"), Messages.getString("Experiment.2"), JOptionPane.QUESTION_MESSAGE, null, null, crate.getCrateName()); //$NON-NLS-1$ //$NON-NLS-2$
				if (name != null && name.length() != 0)
				{
					crate.setCrateName(name);
					n.setText(crate.getCrateName());
				}
			}
		});
		panel.add(n);
		panel.add(new Mate());
		panel.add(new NewExperiment(this));
		panel.add(new DiscardExperiment(this, true));
		return panel;
	}

	public CrateInterface getCrate()
	{
		return crate;
	}

	public void updateCrateProvier()
	{
	}

}
