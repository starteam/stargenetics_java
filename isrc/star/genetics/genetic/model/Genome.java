package star.genetics.genetic.model;

import java.util.Collection;

import star.genetics.genetic.model.Creature.Sex;

public interface Genome extends Iterable<Chromosome>
{
	public static enum SexType
	{
		XY(new String[] { "XY/XX", "XY", "XX/XY" }, new String[] { "XX", "xx", "Xx" }), //
		XO(new String[] { "XX/X0", "X0", "XO", "XX/XO" }, new String[] { "XX", "xx", "Xx" }), //
		Aa(new String[] { "MetA/Meta", "MATAa", "MATa/MATalpha" }, new String[] { "Alpha", "alpha" }), UNISEX(new String[] { "Unisex", "Self", "Hermafrodit" }, new String[] {});

		private final String[] name;
		private final String[] female;

		private SexType(String[] name, String[] female)
		{
			this.name = name;
			this.female = female;
		}

		public static SexType parse(String sexType)
		{
			SexType ret = null;
			for (SexType t : SexType.values())
			{
				for (String s : t.name)
				{
					if (sexType.equalsIgnoreCase(s))
					{
						ret = t;
					}
				}
			}
			return ret;
		}

		public Creature.Sex parseSex(String sex)
		{
			Creature.Sex ret = Sex.MALE;
			for (String str : female)
			{
				if (str.equals(sex))
				{
					ret = Sex.FEMALE;
					break;
				}
			}
			return ret;
		}

	};

	Chromosome getChromosomeByName(String name);

	void removeChromosome(Chromosome c);

	void addChromosome(Chromosome c);

	Collection<star.genetics.genetic.model.Gene> getGenes();

	SexType getSexType();

	void setSexType(String x);

	String getName();

	void setName(String name);
}
