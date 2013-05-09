/**
 * 
 */
package star.genetics.v1.ui.organism;

import javax.swing.event.ListSelectionEvent;

class OrganismListSelectionListener implements javax.swing.event.ListSelectionListener
{
	private final OrganismList organismList;

	OrganismListSelectionListener(OrganismList organismList)
	{
		this.organismList = organismList;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			this.organismList.raise_OrganismSelectedEvent();
		}
		this.organismList.raise_ListItemSelectedEvent();
	}
}