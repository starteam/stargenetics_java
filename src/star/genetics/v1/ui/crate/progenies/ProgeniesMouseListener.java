/**
 * 
 */
package star.genetics.v1.ui.crate.progenies;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import star.genetics.genetic.model.Creature;

class ProgeniesMouseListener implements MouseListener, MouseMotionListener
{
	private MouseEvent press;
	private ProgeniesList parent;

	public ProgeniesMouseListener(ProgeniesList parent)
	{
		this.parent = parent;
	}

	public void mousePressed(MouseEvent e)
	{
		press = e;
	}

	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			parent.raise_OrganismSetAsParentEvent();
		}

	}

	public Creature getSelectedCreature()
	{
		return parent.getSelectedCreature();
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mouseReleased(MouseEvent e)
	{
		press = null;
	}

	public void mouseDragged(MouseEvent e)
	{
		TransferHandler th = ((JComponent) e.getSource()).getTransferHandler();
		if (th != null)
		{
			th.exportAsDrag(((JComponent) e.getSource()), press, TransferHandler.COPY);
			press = null;
		}
	}

	public void mouseMoved(MouseEvent e)
	{
	}
}