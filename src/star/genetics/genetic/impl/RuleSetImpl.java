package star.genetics.genetic.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import star.genetics.Helper;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.GeneticMakeup;
import star.genetics.genetic.model.Rule;

public class RuleSetImpl extends ArrayList<Rule> implements star.genetics.genetic.model.RuleSet
{
	private static final long serialVersionUID = 1L;

	LinkedHashSet<String> propertyNames = new LinkedHashSet<String>();

	public RuleSetImpl(LinkedHashSet<String> orderedSet)
	{
		propertyNames = orderedSet;
	}

	public RuleSetImpl()
	{
	}

	public Map<String, String> getProperties(GeneticMakeup genotype, Creature.Sex sex)
	{
		Map<String, String> ret = new TreeMap<String, String>();
		initialize(ret);
		for (Rule r : this)
		{
			if (r.isMatching(genotype, sex))
			{
				combine(ret, r.getProperties());
			}
		}
		LinkedHashMap<String, String> retRule = new LinkedHashMap<String, String>();
		for (String key : propertyNames)
		{
			if (ret.containsKey(key))
			{
				retRule.put(key, ret.get(key));
			}
		}
		return retRule;
	}

	private void combine(Map<String, String> target, Map<String, String> source)
	{
		for (Entry<String, String> entry : source.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			if (value.indexOf('=') != -1 && target.containsKey(key))
			{
				String old_value = target.get(key);
				Map<String, String> state = Helper.parse(old_value);
				Map<String, String> update = Helper.parse(value);
				state.putAll(update);
				value = Helper.export(state);
			}
			target.put(key, value);
		}
	}

	@Override
	public boolean add(Rule rule)
	{
		propertyNames.addAll(rule.getProperties().keySet());
		return super.add(rule);
	}

	private void initialize(Map<String, String> ret)
	{
		for (Rule r : this)
		{
			if (r.isDefault())
			{
				ret.putAll(r.getProperties());
			}
		}
	}

	public Set<String> getPropertyNames()
	{
		return Collections.unmodifiableSet(propertyNames);
	}

}