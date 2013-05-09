package star.genetics.genetic.impl;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JComponent;

import star.genetics.genetic.model.Creature.Sex;
import star.genetics.visualizers.Visualizer;

public class Summary implements Iterable<String>
{
	private TreeMap<String, Integer> count = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> count_female = new TreeMap<String, Integer>();
	private TreeMap<String, JComponent> maps = new TreeMap<String, JComponent>();
	private TreeMap<String, Map<String, String>> stringMap = new TreeMap<String, Map<String, String>>();
	private TreeSet<String> allProperties = new TreeSet<String>();

	public void add(Visualizer v, Sex sex)
	{
		Map<String, String> tooltipProperties = v.getTooltipProperties();
		String key = tooltipProperties.toString();
		if (Sex.FEMALE.equals(sex))
		{
			if (count_female.containsKey(key))
			{
				count_female.put(key, count_female.get(key).intValue() + 1);
			}
			else
			{
				count_female.put(key, 1);
			}
		}

		if (count.containsKey(key))
		{
			count.put(key, count.get(key).intValue() + 1);
		}
		else
		{
			count.put(key, 1);
			v.setName(""); //$NON-NLS-1$
			v.setSex(null);
			JComponent c = v.getJComponent();
			Dimension d = c.getPreferredSize();
			d.height = d.height * 3 / 4;
			d.width = d.width * 3 / 4;
			c.setSize(d);
			maps.put(key, c);
			stringMap.put(key, tooltipProperties);
			allProperties.addAll(tooltipProperties.keySet());
		}
	}

	public Iterator<String> iterator()
	{
		return maps.keySet().iterator();
	}

	public int getCount(String key)
	{
		return count.containsKey(key) ? count.get(key) : 0;
	}

	public int getCountFemale(String key)
	{
		return count_female.containsKey(key) ? count_female.get(key) : 0;
	}

	public JComponent getComponent(String key)
	{
		return maps.get(key);
	}

	public Map<String, String> getProperties(String key)
	{
		return stringMap.get(key);
	}

	public ArrayList<String> getProperties()
	{
		return new ArrayList<String>(allProperties);
	}

}
