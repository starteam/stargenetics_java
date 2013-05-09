/**
 * 
 */
package star.genetics.v1.ui.organism;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.TransferHandler;

class OrganismMouseListener implements MouseMotionListener, MouseListener
{
	private final OrganismList organismList;
	private MouseEvent press;

	OrganismMouseListener(OrganismList organismList)
	{
		this.organismList = organismList;
	}

	public void mousePressed(MouseEvent e)
	{
		press = e;
	}

	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			this.organismList.raise_OrganismSetAsParentEvent();
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		TransferHandler th = this.organismList.getTransferHandler();
		if (th != null)
		{
			th.exportAsDrag(organismList, press, TransferHandler.COPY);
			press = null;
		}
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e)
	{
		press = null;
	}

}