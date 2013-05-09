package star.genetics.v2.ui.yeast.common;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v2.ui.yeast.YeastUI;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;

@SignalComponent(extend = JPanel.class, raises = { OrganismSelectedRaiser.class })
public class SillyParentLabel extends SillyParentLabel_generated
{
	private static final long serialVersionUID = 1L;

	VisualizerFactory vf;
	Creature creature;
	MatingPanel mp;

	SillyParentLabel(VisualizerFactory vf, MatingPanel mp)
	{
		this.vf = vf;
		this.mp = mp;
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setBackground(YeastUI.getMediaColor());
		Helper.makeExportable(this, "creature", TransferHandler.COPY, vf); //$NON-NLS-1$
		update();
	}

	@Handles(raises = {})
	void handle(OrganismSelectedRaiser r)
	{
		if (r.getSelectedCreature() == getCreature())
		{
			this.setOpaque(true);
			this.setBorder(BorderFactory.createLineBorder(YeastUI.getMediaColor().darker(), 3));
		}
		else
		{
			this.setOpaque(false);
			this.setBackground(null);
			this.setBorder(BorderFactory.createLineBorder(YeastUI.getMediaColor().darker()));
		}
	}

	public Creature getSelectedCreature()
	{
		return getCreature();
	}

	public Creature getCreature()
	{
		return creature;
	}

	void update()
	{
		if (getCreature() != null)
		{
			removeAll();
			add(getCreatureVisualizer().getJComponent());
		}
		else
		{
			removeAll();
			add(new JLabel(Messages.getString("SillyParentLabel.1"))); //$NON-NLS-1$
			setBorder(BorderFactory.createLineBorder(YeastUI.getMediaColor().darker()));
		}
		invalidate();
		synchronized (getTreeLock())
		{
			validateTree();
		}

		doLayout();
	}

	public Visualizer getCreatureVisualizer()
	{
		Visualizer v = vf.newVisualizerInstance();
		Helper.setVisualizerFromCreature(v, getCreature());
		return v;
	}

	public void setCreature(Creature creature)
	{
		try
		{
			this.creature = creature;
			update();
			mp.updateSilly();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
