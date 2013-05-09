package star.genetics.v2.ui.yeast.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FixedGridLayout implements LayoutManager2, Serializable
{
	private static final long serialVersionUID = 1L;
	private final Map<Rectangle, Component> components = new HashMap<Rectangle, Component>();
	private final Container container;
	private final Dimension step;
	private Dimension dimension = new Dimension();
	private Point offset = new Point();

	public FixedGridLayout(Container c, Dimension step, Point offset)
	{
		this.container = c;
		this.step = step;
		this.offset = offset;
	}

	public FixedGridLayout(Container c, Dimension step)
	{
		this.container = c;
		this.step = step;
	}

	public void addLayoutComponent(String name, Component comp)
	{
		throw new UnsupportedOperationException();
	}

	public void addLayoutComponent(Component comp, Object constraints)
	{
		synchronized (container.getTreeLock())
		{
			components.put((Rectangle) constraints, comp);
			invalidateLayout(container);
		}
	}

	public void removeLayoutComponent(Component comp)
	{
		synchronized (container.getTreeLock())
		{
			for (Entry<Rectangle, Component> entry : components.entrySet())
			{
				if (entry.getValue().equals(comp))
				{
					components.remove(entry.getKey());
					invalidateLayout(container);
					break;
				}
			}
			invalidateLayout(container);
		}
	}

	public void layoutContainer(Container parent)
	{
		Dimension d = new Dimension();
		for (Entry<Rectangle, Component> entry : components.entrySet())
		{
			Rectangle r = entry.getKey();
			Component c = entry.getValue();
			c.setBounds(new Rectangle(r.x * step.width + offset.x, r.y * step.height + offset.y, r.width * step.width, r.height * step.height));
			d.setSize(Math.max(d.getWidth(), (r.x + r.width) * step.width), Math.max(d.getHeight(), (r.y + r.height) * step.height));
		}
		this.dimension = d;
		parent.repaint();
	}

	public float getLayoutAlignmentX(Container target)
	{
		return 0.5f;
	}

	public float getLayoutAlignmentY(Container target)
	{
		return 0;
	}

	public void invalidateLayout(Container target)
	{
		dimension = null;
	}

	private void updateDimension()
	{
		Dimension d = new Dimension();
		for (Entry<Rectangle, Component> entry : components.entrySet())
		{
			Rectangle r = entry.getKey();
			d.setSize(Math.max(d.getWidth(), (r.x + r.width) * step.width), Math.max(d.getHeight(), (r.y + r.height) * step.height));
		}

		d.width += offset.x;
		d.height += offset.y;
		this.dimension = d;
	}

	public Dimension maximumLayoutSize(Container target)
	{
		if (dimension == null)
		{
			updateDimension();
		}
		return dimension;
	}

	public Dimension minimumLayoutSize(Container parent)
	{
		if (dimension == null)
		{
			updateDimension();
		}
		return dimension;
	}

	public Dimension preferredLayoutSize(Container parent)
	{
		if (dimension == null)
		{
			updateDimension();
		}
		return dimension;
	}

}
