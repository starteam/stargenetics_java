package star.genetics.v1.ui.organism;

import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import star.genetics.genetic.model.Creature;

public class OrganismTransferHandler extends star.genetics.v1.ui.common.CreatureTransferHandler
{
	private static final long serialVersionUID = 1L;
	OrganismList list;

	public OrganismTransferHandler(OrganismList list)
	{
		super("selectedCreature", list.getGeneticModel().getVisualizerFactory()); //$NON-NLS-1$
		this.list = list;

	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		super.canImport(comp, transferFlavors);
		return null != hasFlavor(transferFlavors, Creature.class);
	}

	Creature myCreature = null;
	int counter = 0;

	@Override
	public void exportAsDrag(final JComponent comp, InputEvent e, int action)
	{
		myCreature = list.getSelectedCreature();
		counter++;
		super.exportAsDrag(comp, e, action);
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		counter--;
		if (counter == 0)
		{
			myCreature = null;
		}
		super.exportDone(source, data, action);
	}

	@Override
	public boolean importData(JComponent comp, Transferable t)
	{
		java.awt.Point p = java.awt.MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, comp);

		boolean ret = false;
		Creature c = getCreature(t);
		if (myCreature != null)
		{
			c = myCreature;
		}
		if (c != null)
		{
			ret = true;
			int index = list.locationToIndex(p);
			if (list.getListModel().contains(c))
			{
				list.getListModel().move(c, index);
				list.setModel(list.getListModel());
				list.setSelectedValue(c, true);
				list.repaint();
			}
			else
			// add
			{
				Rectangle r = list.getCellBounds(index, index);
				if (r != null && !r.contains(p))
				{
					list.getListModel().add(c);
					list.setSelectedValue(c, true);
					list.repaint();
				}
				else
				{
					list.getListModel().addElementAt(c, index);
					list.setSelectedValue(c, true);
					list.repaint();
				}
				// }

			}
			creatureExporting--;
		}
		return ret;
	}

}
