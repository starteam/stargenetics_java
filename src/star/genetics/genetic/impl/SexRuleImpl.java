package star.genetics.genetic.impl;

import java.io.Serializable;

import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.GeneticMakeup;

public class SexRuleImpl implements IndividualRule, Serializable
{
	private static final long serialVersionUID = 1L;
	private Sex s;

	public SexRuleImpl(Sex s)
	{
		this.s = s;
	}

	public boolean test(GeneticMakeup makeup, Sex sex)
	{

		return s.equals(sex);
	}

}
