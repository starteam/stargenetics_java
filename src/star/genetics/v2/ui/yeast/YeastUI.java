package star.genetics.v2.ui.yeast;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.annotations.Wrap.Types;
import star.genetics.genetic.impl.YeastUIMetadata;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v2.ui.fly.strains.Strains;
import star.genetics.v2.ui.yeast.experiment.ChooseExperiment;
import star.genetics.v2.ui.yeast.experiment.MatingExperiment;
import star.genetics.v2.ui.yeast.experiment.ReplicationExperiment;
import star.genetics.v2.ui.yeast.history.History;
import star.genetics.v2.ui.yeast.properties.Properties;
import star.genetics.v2.ui.yeast.tools.Tools;
import star.genetics.v2.yeast.events.NewMatingExperimentRaiser;
import star.genetics.v2.yeast.events.NewReplicationExperimentRaiser;
import utils.Colors;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class)
public class YeastUI extends YeastUI_generated
{
	private static final long serialVersionUID = 1L;
	private Model model;
	private JPanel experiment;

	public YeastUI(Model model)
	{
		this.model = model;
	}

	private void chooseExperiment()
	{
		YeastUIMetadata metadata = (YeastUIMetadata) model.getModelMetadata().get(YeastUIMetadata.class);
		if (metadata == null)
		{
			metadata = new YeastUIMetadata();
		}
		if (YeastUIMetadata.Experiments.TETRAD.equals(metadata.getExperimentType()))
		{
			experiment.add(new MatingExperiment(model, false));
		}
		else if (YeastUIMetadata.Experiments.NONTETRAD.equals(metadata.getExperimentType()))
		{

			experiment.add(new ReplicationExperiment(model, false));
		}
		else
		{
			experiment.add(BorderLayout.CENTER, new ChooseExperiment(model, model.getCrateSet().size() != 0));
		}
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BorderLayout());
		experiment = new JPanel();
		experiment.setLayout(new BorderLayout());
		chooseExperiment();

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(BorderLayout.NORTH, new Tools());
		leftPanel.add(BorderLayout.CENTER, new Strains(model));
		leftPanel.add(BorderLayout.SOUTH, new Properties(model));

		// JPanel rightPanel = new TwoComponentPanel();
		JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rightPanel.add(experiment);
		rightPanel.add(new History(model));
		rightPanel.setOneTouchExpandable(true);
		rightPanel.setResizeWeight(.75f);

		add(BorderLayout.WEST, leftPanel);
		add(BorderLayout.CENTER, rightPanel);
	}

	@Override
	@Wrap(type = Types.SwingUtilitiesInvokeLater)
	void updateLayout()
	{
		UIHelpers.invalidate(this);
		UIHelpers.layout(this);
	}

	@Override
	@Handles(raises = {})
	void handle(CrateNewCrateRaiser r)
	{
		experiment.removeAll();
		chooseExperiment();
		updateLayout_SwingUtilitiesInvokeLater();
	}

	@Override
	@Handles(raises = {})
	void handle(NewReplicationExperimentRaiser r)
	{
		experiment.removeAll();
		experiment.add(new ReplicationExperiment(model, false));
		updateLayout_SwingUtilitiesInvokeLater();
	}

	@Override
	@Handles(raises = {})
	void handle(NewMatingExperimentRaiser r)
	{
		experiment.removeAll();
		experiment.add(new MatingExperiment(model, false));
		updateLayout_SwingUtilitiesInvokeLater();
	}

	public static java.awt.Color getMediaColor()
	{
		return Colors.CMYKtoRGBColor(new int[] { 26, 36, 38, 70 });
	}
}
