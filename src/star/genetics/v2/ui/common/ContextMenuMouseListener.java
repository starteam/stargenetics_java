package star.genetics.v2.ui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import star.genetics.v1.ui.events.ListItemSelectedRaiser;

public class ContextMenuMouseListener extends MouseAdapter
{
	private final JList list;
	private final ListItemSelectedRaiser raiser;
	private JPopupMenu p = new JPopupMenu();

	public ContextMenuMouseListener(JList list)
	{
		this.list = list;
		this.raiser = (ListItemSelectedRaiser) list;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		check(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		check(e);
	}

	void check(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			int index = list.locationToIndex(e.getPoint());
			if (index >= 0 && list.getCellBounds(index, index).contains(e.getPoint()))
			{
				list.setSelectedIndex(index);
				p.removeAll();
				for (final ListItemSelectedRaiser.Tuple t : raiser.getActions())
				{
					if (t != null)
					{
						JMenuItem i = new JMenuItem(t.getActionName().getName(), t.getActionName().getIcon(16));
						i.addActionListener(new ActionListener()
						{

							public void actionPerformed(ActionEvent e)
							{
								t.getAction().run();
							}
						});
						p.add(i);
					}
				}
				p.show(list, e.getPoint().x, e.getPoint().y);

			}
		}
	}
}
