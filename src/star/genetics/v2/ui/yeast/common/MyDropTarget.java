/**
 * 
 */
package star.genetics.v2.ui.yeast.common;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;

public class MyDropTarget extends JLabel
{
	private static final long serialVersionUID = 1L;
	private final ReplicaPanel replicaPanel;
	private Creature c;

	public MyDropTarget(ReplicaPanel replicaPanel, Dimension step)
	{
		this.replicaPanel = replicaPanel;
		setTransferHandler(new star.genetics.v1.ui.common.CreatureTransferHandler("creature", replicaPanel.getModel().getVisualizerFactory())); //$NON-NLS-1$
		setBorder(BorderFactory.createEtchedBorder());
		setSize(step);
		setText(Messages.getString("MyDropTarget.1")); //$NON-NLS-1$
	}

	public void setCreature(Creature c)
	{
		replicaPanel.addCreature(c, true);
	}

	public Creature getCreature()
	{
		return c;
	}
}