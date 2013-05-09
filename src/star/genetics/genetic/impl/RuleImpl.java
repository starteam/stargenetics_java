package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import star.genetics.Messages;
import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Chromosome;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.GeneticMakeup;
import star.genetics.genetic.model.Genome;
import star.genetics.genetic.model.Genome.SexType;

public class RuleImpl implements star.genetics.genetic.model.Rule, Serializable
{
	private static final long serialVersionUID = 1L;

	private final String rule;
	private final HashMap<String, String> properties;
	private final ArrayList<IndividualRule> compiledRules = new ArrayList<IndividualRule>();

	public RuleImpl(String rule, HashMap<String, String> properties, Genome g)
	{
		this.rule = rule;
		this.properties = properties;
		parseRules(rule, g);
	}

	private void parseRules(String rule, Genome g)
	{
		compiledRules.clear();
		StringTokenizer ruleSplit = new StringTokenizer(rule, ";"); //$NON-NLS-1$
		while (ruleSplit.hasMoreTokens())
		{
			String oneRule = ruleSplit.nextToken().trim();
			boolean isRuleParsed = false;
			if (!isRuleParsed)
			{
				isRuleParsed = parseSexRule(oneRule, g);
			}
			if (!isRuleParsed)
			{
				isRuleParsed = parseHaploidRule(oneRule, g);
			}
			if (!isRuleParsed)
			{
				parseOneRule(oneRule, g);
			}
		}
	}

	private boolean parseHaploidRule(String oneRule, Genome g)
	{
		boolean ret = false;
		String s = oneRule.trim().toLowerCase();
		if (SexType.Aa.equals(g.getSexType()))
		{
			if (s.startsWith("haploid")) //$NON-NLS-1$
			{
				ret = true;
				compiledRules.add(new HaploidRuleImpl());
			}
		}
		return ret;

	}

	private boolean parseSexRule(String oneRule, Genome g)
	{
		boolean ret = false;
		String s = oneRule.trim().toLowerCase();
		if (SexType.XY.equals(g.getSexType()) || SexType.XO.equals(g.getSexType()))
		{
			if (s.startsWith("sex:")) //$NON-NLS-1$
			{
				s = s.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
				if (s.equals("sex:male")) //$NON-NLS-1$
				{
					ret = true;
					compiledRules.add(new SexRuleImpl(Creature.Sex.MALE));
				}
				else if (s.equals("sex:female")) //$NON-NLS-1$
				{
					ret = true;
					compiledRules.add(new SexRuleImpl(Creature.Sex.FEMALE));
				}
			}
		}
		else if (SexType.Aa.equals(g.getSexType()))
		{
			if (s.startsWith("sex:")) //$NON-NLS-1$
			{
				s = s.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
				if (s.equals("sex:mata")) //$NON-NLS-1$
				{
					ret = true;
					compiledRules.add(new SexRuleImpl(Creature.Sex.MALE));
				}
				else if (s.equals("sex:matalpha")) //$NON-NLS-1$
				{
					ret = true;
					compiledRules.add(new SexRuleImpl(Creature.Sex.FEMALE));
				}
			}
		}
		return ret;
	}

	private void parseOneRule(String oneRule, Genome g)
	{
		int chromosomeSplit = oneRule.indexOf(':');
		String chromosomeName = null;
		Chromosome chromosome = null;
		if (chromosomeSplit != -1)
		{
			chromosomeName = oneRule.substring(0, chromosomeSplit).trim();
			chromosome = g.getChromosomeByName(chromosomeName);
		}
		else
		{
			int indexC = oneRule.indexOf(',');
			int indexS = oneRule.indexOf(' ');
			if (indexC == -1)
			{
				indexC = oneRule.length();
			}
			if (indexS == -1)
			{
				indexS = oneRule.length();
			}
			int index = Math.min(indexC, indexS);
			String alleleName = oneRule.substring(0, index);
			for (Gene gene : g.getGenes())
			{
				if (gene.getAlleleByName(alleleName) != null)
				{
					chromosome = gene.getChromosome();
					chromosomeName = chromosome.getName();
				}
			}
		}
		if (chromosome != null)
		{
			String alleles = oneRule.substring(chromosomeSplit + 1).trim();
			ChromosomeRuleImpl cr = makeChromosomeRule(alleles, chromosome);
			if (cr != null)
			{
				compiledRules.add(cr);
			}
		}
		else
		{
			if (!"default".equalsIgnoreCase(oneRule.trim())) //$NON-NLS-1$
			{
				throw new RuntimeException(MessageFormat.format(Messages.getString("RuleImpl.2"), oneRule)); //$NON-NLS-1$
			}
		}
	}

	private ChromosomeRuleImpl makeChromosomeRule(String alleles, Chromosome chromosome)
	{
		ChromosomeRuleImpl cr = new ChromosomeRuleImpl(chromosome);
		StringTokenizer strandSplit = new StringTokenizer(alleles, ","); //$NON-NLS-1$
		boolean add = false;
		int strand = 0;
		while (strandSplit.hasMoreTokens())
		{
			String strandRule = strandSplit.nextToken().trim();
			StringTokenizer allelesSplit = new StringTokenizer(strandRule, " "); //$NON-NLS-1$
			while (allelesSplit.hasMoreTokens())
			{
				String alleleName = allelesSplit.nextToken().trim();
				Allele allele = chromosome.getAlleleByName(alleleName);
				if (allele != null)
				{
					cr.addAllele(strand, allele);
					add = true;
				}
				else
				{
					throw new RuntimeException(MessageFormat.format(Messages.getString("RuleImpl.1"), alleleName, rule)); //$NON-NLS-1$
				}
			}
			strand++;
		}
		return add ? cr : null;
	}

	public boolean isDefault()
	{
		return DEFAULT.equalsIgnoreCase(rule);
	}

	public boolean isMatching(GeneticMakeup makeup, Creature.Sex sex)
	{
		boolean ret = compiledRules.size() > 0;
		for (IndividualRule c : compiledRules)
		{
			ret &= c.test(makeup, sex);
		}
		return ret;
	}

	public HashMap<String, String> getProperties()
	{
		return properties;
	}

	@Override
	public String toString()
	{

		return MessageFormat.format("{0} rule:{1} properties:{2}", this.getClass().getName(), rule, properties); //$NON-NLS-1$
	}
}
