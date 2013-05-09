package star.genetics.v1.ui.events;

import javax.swing.ImageIcon;
import javax.swing.JList;

import star.annotations.Raiser;
import utils.Icons;

@Raiser
public interface ListItemSelectedRaiser extends star.event.Raiser
{
	enum Action
	{
		SETASPARENT("Set as parent", Icons.SITE_MAP) //
		, REMOVE("Remove", Icons.REMOVE)//
		, PROPERTIES("Properties", Icons.CONFIG)//
		, ADDTOLIST("Add to Strains", Icons.ARROW_LEFT);

		String name;
		utils.Icons icon;

		Action(String name, utils.Icons icon)
		{
			this.icon = icon;
			this.name = name;
		}

		public ImageIcon getIcon()
		{
			return icon.getIcon();
		}

		public ImageIcon getIcon(int size)
		{
			return icon.getIcon(size);
		}

		public String getName()
		{
			return name;
		}
	};

	JList getJList();

	Tuple[] getActions();

	static class Tuple
	{
		Action actionName;
		Runnable action;
		int index;

		public Tuple(int index, Action a, Runnable action)
		{
			this.action = action;
			this.actionName = a;
			this.index = index;
		}

		public Runnable getAction()
		{
			return action;
		}

		public Action getActionName()
		{
			return actionName;
		}

		public int getIndex()
		{
			return index;
		}
	}

}
