package star.genetics.v2.ui.yeast.common;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v2.ui.yeast.YeastUI;
import star.genetics.visualizers.Visualizer;

@SignalComponent(extend = JPanel.class, raises = { OrganismSelectedRaiser.class })
public class YeastParentLabel extends YeastParentLabel_generated
{
	private static final long serialVersionUID = 1L;

	private YeastParents parent;
	private Sex type;

	public YeastParentLabel(YeastParents parent, Sex type)
	{
		this.parent = parent;
		this.type = type;
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setBackground(YeastUI.getMediaColor());
		if (type != null)
		{
			Helper.makeExportable(this, "creature", TransferHandler.COPY, parent.getVisualizerFactory()); //$NON-NLS-1$
		}
		if (type != null)
		{
			update();
		}
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				select();
			}
		});
	}

	@Override
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
			// this.setBorder(BorderFactory.createLineBorder(YeastUI.getMediaColor().darker()));
			this.setBorder(null);
		}
	}

	public Creature getSelectedCreature()
	{
		return getCreature();
	}

	void select()
	{
		raise_OrganismSelectedEvent();
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
			add(new JLabel(Messages.getString("YeastParentLabel.1"))); //$NON-NLS-1$
			setBorder(BorderFactory.createLineBorder(YeastUI.getMediaColor().darker()));
		}
	}

	public Visualizer getCreatureVisualizer()
	{
		return parent.getCreatureVisualizer(type);
	}

	public Creature getCreature()
	{
		return parent.getCreature(type);
	}

	public void setCreature(Creature creature)
	{
		try
		{
			parent.setCreature(creature);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	@Handles(raises = {})
	void setAsParent(OrganismSetAsParentRaiser r)
	{
		if (type != null && r.getSelectedCreature() != null && type.equals(r.getSelectedCreature().getSex()))
		{
			setCreature(r.getSelectedCreature());
			update();
		}
	}
}
