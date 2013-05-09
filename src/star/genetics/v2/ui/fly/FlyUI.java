package star.genetics.v2.ui.fly;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.genetic.model.Genome.SexType;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v2.ui.fly.experiment.Experiment;
import star.genetics.v2.ui.fly.history.History;
import star.genetics.v2.ui.fly.properties.Properties;
import star.genetics.v2.ui.fly.strains.Strains;
import star.genetics.v2.ui.fly.tools.Tools;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class)
public class FlyUI extends FlyUI_generated
{
	private static final long serialVersionUID = 1L;
	private Model model;
	private JPanel experiment = new JPanel();

	public FlyUI(Model model)
	{
		super();
		this.model = model;

	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		experiment.setLayout(new BorderLayout());
		experiment.add(new Experiment(model));
		JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);// = new TwoComponentPanel();
		if (!SexType.UNISEX.equals(model.getGenome().getSexType()))
		{
			setLayout(new BorderLayout());
			add(BorderLayout.WEST, leftPanel);
			add(BorderLayout.CENTER, rightPanel);
		}
		else
		{
			setLayout(new BorderLayout());
			JSplitPane wholeArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			wholeArea.add(leftPanel);
			wholeArea.add(rightPanel);
			wholeArea.setOneTouchExpandable(true);
			wholeArea.setDividerLocation(325);
			add(wholeArea);
		}
		rightPanel.add(experiment);
		rightPanel.add(new History(model));
		rightPanel.setOneTouchExpandable(true);
		rightPanel.setResizeWeight(.75f);
		leftPanel.add(BorderLayout.NORTH, new Tools());
		leftPanel.add(BorderLayout.CENTER, new Strains(model));
		leftPanel.add(BorderLayout.SOUTH, new Properties(model));
	}

	@Override
	@Handles(raises = {})
	void handle(CrateNewCrateRaiser r)
	{
		experiment.removeAll();
		experiment.add(new Experiment(model, false));
		experiment.invalidate();
		UIHelpers.getFrame(this).invalidate();
		UIHelpers.getFrame(this).validate();
	}
}
