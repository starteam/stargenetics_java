package star.genetics.v1.ui.crate.progenies;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ProgeniesListSelectionListener implements ListSelectionListener
{
	public void valueChanged(ListSelectionEvent e)
	{
		Object src = e.getSource();
		if (e.getSource() instanceof ProgeniesList)
		{
			((ProgeniesList) src).raise_ProgenySelectedEvent();
			((ProgeniesList) src).raise_ListItemSelectedEvent();
		}

	}
}
