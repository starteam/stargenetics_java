package star.genetics.v2.ui.fly.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v2.gel.JGel;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.visualizers.Visualizer;

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
		body.add(new JLabel(Messages.getString("Properties.1"))); //$NON-NLS-1$
		init(title, body, true);
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

	@Override
	public Dimension getPreferredSize()
	{
		Dimension ret = super.getPreferredSize();
		return ret;
	}

	void handle(Creature c)
	{

		if (c != null)
		{
			Visualizer v = m.getVisualizerFactory().newVisualizerInstance();
			// v.setName(c.getName());
			v.setProperties(c.getProperties(), c.getSex());
			v.setNote(c.getNote());
			PropertiesPanel d = new PropertiesPanel();
			d.setCreature(c);
			d.setName(c.getName());
			d.setSex(c.getSex());
			d.setMap(v.getTooltipProperties());
			d.setComponent(v.getJComponent());
			d.setVisible(true);
			d.setNote(c.getNote());
			if (m.getGelRules() != null)
			{
				d.setGel(new JGel(m, c));
			}
			body.removeAll();
			body.add(new JScrollPane(d));
			invalidate();
			getParent().invalidate();
			getParent().validate();
			repaint();
		}

	}
}
