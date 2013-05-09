package star.genetics.v2.ui.yeast.properties;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.Yeast;
import utils.UIHelpers;

@SignalComponent(extend = TitledContainer.class)
public class Properties extends Properties_generated
{

	private static final long serialVersionUID = 1L;
	private JPanel body;
	private Model m;

	public Properties(Model model)
	{
		JPanel title = new JPanel();
		title.setOpaque(false);
		title.add(new TitleLabel(Messages.getString("Properties.0"))); //$NON-NLS-1$
		body = new JPanel();
		body.setOpaque(true);
		body.setLayout(new BorderLayout());
		init(title, body, true);
		handle((Creature) null);
		this.m = model;
	}

	@Override
	@Handles(raises = {})
	void handle(OrganismSelectedRaiser r)
	{
		Creature c = r.getSelectedCreature();
		handle(c);
	}

	@Override
	@Handles(raises = {})
	void handle(ProgenySelectedRaiser r)
	{
		Creature c = r.getSelectedCreature();
		handle(c);
	}

	private void handle(Creature c)
	{

		if (c != null)
		{
			Visualizer v = m.getVisualizerFactory().newVisualizerInstance();
			// v.setName(c.getName());
			HashMap<String, String> additional = new HashMap<String, String>();
			additional.put(Yeast.NOTEXT, "true"); //$NON-NLS-1$
			Helper.setVisualizerFromCreature(v, c, additional);
			v.setNote(c.getNote());
			PropertiesPanel d = new PropertiesPanel();
			d.setCreature(c);
			d.setName(c.getName());
			d.setComponent(v.getJComponent());
			d.setVisible(true);
			d.setNote(c.getNote());
			body.removeAll();
			body.add(d);
			invalidate();
			getParent().invalidate();
			getParent().validate();
			repaint();
		}
		else
		{
			body.removeAll();
			final PropertiesPanel p = new PropertiesPanel();
			body.add(p);
			SwingUtilities.invokeLater(new Runnable()
			{

				public void run()
				{
					UIHelpers.setEnabled(p, false);
				}
			});
		}
	}
}
