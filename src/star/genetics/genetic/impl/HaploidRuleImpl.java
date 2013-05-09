package star.genetics.genetic.impl;

import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.GeneticMakeup;

public class HaploidRuleImpl implements IndividualRule
{
	private static final long serialVersionUID = 1L;

	public boolean test(GeneticMakeup makeup, Sex sex)
	{

		return sex != null;
	}

}
