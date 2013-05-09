package star.genetics.xls;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import star.genetics.Messages;
import star.genetics.genetic.impl.AlleleImpl;
import star.genetics.genetic.impl.ChromosomeImpl;
import star.genetics.genetic.impl.CreatureImpl;
import star.genetics.genetic.impl.CreatureSetImpl;
import star.genetics.genetic.impl.DiploidAllelesImpl;
import star.genetics.genetic.impl.GelRulesImpl;
import star.genetics.genetic.impl.GeneImpl;
import star.genetics.genetic.impl.GeneticMakeupImpl;
import star.genetics.genetic.impl.MatingEngineMetadata;
import star.genetics.genetic.impl.YeastUIMetadata;
import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Chromosome;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.DiploidAlleles;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.Genome.SexType;
import star.genetics.genetic.model.Model;
import star.genetics.genetic.model.ModelWriter;
import star.genetics.genetic.model.Rule;
import star.genetics.genetic.model.RuleSet;

public class Load2
{
	private static final String WILDTYPE = "wildtype"; //$NON-NLS-1$
	private static final String COL_NOTE = "note"; //$NON-NLS-1$
	private static final String COL_NOTES = "notes"; //$NON-NLS-1$
	private static final String YYY2 = "yyy"; //$NON-NLS-1$
	private static final String XXX2 = "xxx"; //$NON-NLS-1$
	private static final String YGENE = "ygene"; //$NON-NLS-1$
	private static final String XGENE = "xgene"; //$NON-NLS-1$
	final static String Y = "Y"; //$NON-NLS-1$
	final static String X = "X"; //$NON-NLS-1$

	Model load(HSSFWorkbook wb, ModelWriter model, star.genetics.genetic.model.Genome genome) throws ParseException
	{
		try
		{
			Sheets.checkAllSheets(wb);
			parsePropertiesSheet(wb.getSheet(Sheets.PROPERTIES.toString()), model, genome);
			parseGenomeSheet(wb.getSheet(Sheets.GENOME.toString()), model, genome);
			parsePhenotypeSheet(wb.getSheet(Sheets.PHENOTYPE.toString()), model, genome);
			parseOrganismSheet(wb.getSheet(Sheets.ORGANISM.toString()), model, genome);
			parseGelSheet(wb.getSheet(Sheets.GEL.toString()), model, genome);
			return model;
		}
		catch (Throwable t)
		{
			String string = t.getLocalizedMessage();
			if (string == null || string.length() == 0)
			{
				string = t.getClass().toString() + Messages.getString("Load2.0"); //$NON-NLS-1$
			}
			t.printStackTrace();
			throw new ParseException(string, t);
		}
	}

	private void parseOrganismSheet(HSSFSheet sheet, ModelWriter model, star.genetics.genetic.model.Genome genome) throws ParseException
	{
		@SuppressWarnings("unchecked")
		Iterator<HSSFRow> rows = sheet.rowIterator();
		HSSFRow row = null;
		boolean foundHeadingRow = false;
		while (rows.hasNext())
		{
			row = rows.next();
			if (hasRequiredColumnsForOrganism(rowToSet(row)))
			{
				foundHeadingRow = true;
				break;
			}
		}
		if (!foundHeadingRow)
		{
			throw new ParseException(MessageFormat.format(Messages.getString("Load2.10"), Sheets.ORGANISM.toString())); //$NON-NLS-1$
		}

		Map<Integer, String> headings = new LinkedHashMap<Integer, String>()
		{
			private static final long serialVersionUID = 1L;

			public String get(Object key)
			{
				String ret = super.get(key);
				return ret != null ? ret : ""; //$NON-NLS-1$
			}
		};

		Set<String> set = new HashSet<String>();
		parseHeading(row, headings, set);

		Map<String, GeneticMakeupImpl> organismMakeup = new LinkedHashMap<String, GeneticMakeupImpl>();
		Map<String, Sex> organismSex = new LinkedHashMap<String, Sex>();

		for (Entry<Integer, String> entry : headings.entrySet())
		{
			if (!(TRAIT.equalsIgnoreCase(entry.getValue().trim()) || DETERMINATION.equalsIgnoreCase(entry.getValue().trim())))
			{
				organismMakeup.put(entry.getValue(), new GeneticMakeupImpl());
			}
		}

		while (rows.hasNext())
		{
			Map<String, String> properties = new HashMap<String, String>();

			HSSFRow r = rows.next();

			boolean traitAvailable = false;
			boolean detAvailable = false;
			String trait = null;
			String det = null;

			@SuppressWarnings("unchecked")
			Iterator<HSSFCell> cells = r.cellIterator();
			while (cells.hasNext())
			{
				HSSFCell c = cells.next();
				String heading = headings.get(Integer.valueOf(c.getColumnIndex()));
				if (TRAIT.equalsIgnoreCase(heading))
				{
					trait = c.toString();
					traitAvailable = true;
				}
				else if (DETERMINATION.equalsIgnoreCase(heading))
				{
					det = c.toString();
					detAvailable = det.length() != 0;
				}
				else
				{
					String value = c.toString();
					if (value.length() != 0)
					{
						properties.put(heading, value);
					}
				}
			}

			if (traitAvailable && detAvailable && (properties.size() == 0))
			{
				throw new ParseException(MessageFormat.format(Messages.getString("Load2.12"), r.getRowNum())); //$NON-NLS-1$
			}

			if (traitAvailable && detAvailable)
			{
				if (trait.equalsIgnoreCase(SEX))
				{
					for (Entry<String, String> entry : properties.entrySet())
					{
						String org = entry.getKey();
						String sex = entry.getValue();
						organismSex.put(org, genome.getSexType().parseSex(sex));
					}

				}
				else
				{
					for (Entry<String, String> entry : properties.entrySet())
					{
						String org = entry.getKey();
						String makeup = entry.getValue();
						String[] alleleStr = makeup.split(","); //$NON-NLS-1$
						Gene g = getGene(genome, alleleStr);
						ArrayList<Allele> alleles = new ArrayList<Allele>();
						for (String allele : alleleStr)
						{

							try
							{
								alleles.add(g.getAlleleByName(allele.trim()));
							}
							catch (Exception ex)
							{
								throw new ParseException(MessageFormat.format(Messages.getString("Load2.14"), allele)); //$NON-NLS-1$
							}
						}
						organismMakeup.get(org).put(g, new DiploidAllelesImpl(alleles.toArray(new Allele[0])));
					}
				}
			}
		}
		CreatureSet c = new star.genetics.genetic.impl.CreatureSetImpl();
		for (Entry<String, GeneticMakeupImpl> entry : organismMakeup.entrySet())
		{
			String name = entry.getKey();
			GeneticMakeupImpl makeup = entry.getValue();
			Sex sex = organismSex.get(name);
			if (SexType.UNISEX.equals(genome.getSexType()))
			{
				sex = null;
			}
			fixGeneticMakeupSex(genome, makeup, sex);
			CreatureImpl creature = new CreatureImpl(name, genome, sex, makeup, model.getMatingsCount(), model.getRules().getProperties(makeup, sex), new CreatureSetImpl());
			creature.setReadOnly(true);
			c.add(creature);
		}
		model.setCreatures(c);
	}

	private void fixGeneticMakeupSex(star.genetics.genetic.model.Genome genome, GeneticMakeupImpl makeup, Sex sex)
	{
		if (SexType.Aa.equals(genome.getSexType()) || SexType.UNISEX.equals(genome.getSexType()))
		{
			return;
		}

		boolean hasX = false;
		boolean hasY = false;
		for (Entry<Gene, DiploidAlleles> entry : makeup.entrySet())
		{
			hasX |= entry.getKey().getChromosome().getName().equals(X);
			hasY |= entry.getKey().getChromosome().getName().equals(Y);
		}
		if (Sex.MALE.equals(sex))
		{
			if (!hasX)
			{
				Gene x = genome.getChromosomeByName(X).getGeneByName(XGENE);
				Allele xxx = new AlleleImpl(XXX2, x);
				makeup.put(x, new DiploidAllelesImpl(xxx, null));
			}
			if (!hasY)
			{
				if (genome.getChromosomeByName(Y) == null)
				{
					new ChromosomeImpl(Y, genome);
				}
				if (genome.getChromosomeByName(Y).getGeneByName(YGENE) == null)
				{
					new GeneImpl(YGENE, 0, genome.getChromosomeByName(Y));
				}
				Gene y = genome.getChromosomeByName(Y).getGeneByName(YGENE);
				Allele yyy = new AlleleImpl(YYY2, y);
				makeup.put(y, new DiploidAllelesImpl(yyy, null));
			}
		}
		else
		{
			if (!hasX)
			{
				Gene x = genome.getChromosomeByName(X).getGeneByName(XGENE);
				Allele xxx = new AlleleImpl(XXX2, x);
				makeup.put(x, new DiploidAllelesImpl(xxx, xxx));
			}
		}
	}

	private Gene getGene(star.genetics.genetic.model.Genome g, String[] alleleStr)
	{
		Gene ret = null;
		for (Gene gene : g.getGenes())
		{
			ret = gene;
			for (String str : alleleStr)
			{

				if (gene.getAlleleByName(str.trim()) == null)
				{
					ret = null;
					break;
				}
			}
			if (ret != null)
			{
				break;
			}
		}
		return ret;
	}

	private Allele getAllele(star.genetics.genetic.model.Genome g, String allele)
	{
		Allele ret = null;
		for (Gene gene : g.getGenes())
		{
			ret = gene.getAlleleByName(allele.trim());
			if (ret != null)
			{
				break;
			}
		}
		return ret;

	}

	private void parseGelSheet(HSSFSheet sheet, ModelWriter model, star.genetics.genetic.model.Genome genome) throws ParseException
	{
		if (sheet == null)
		{
			return;
		}
		@SuppressWarnings("unchecked")
		Iterator<HSSFRow> rows = sheet.rowIterator();
		HSSFRow row = null;
		boolean foundHeadingRow = false;
		while (rows.hasNext())
		{
			row = rows.next();
			if (hasRequiredColumnsForGel(rowToSet(row)))
			{
				foundHeadingRow = true;
				break;
			}
		}
		if (!foundHeadingRow)
		{
			throw new ParseException(MessageFormat.format(Messages.getString("Load2.15"), Sheets.GEL.toString())); //$NON-NLS-1$
		}

		Map<Integer, String> headings = new LinkedHashMap<Integer, String>()
		{
			private static final long serialVersionUID = 1L;

			public String get(Object key)
			{
				String ret = super.get(key);
				return ret != null ? ret : ""; //$NON-NLS-1$

			};
		};
		Set<String> set = new HashSet<String>();
		parseHeading(row, headings, set);

		GelRulesImpl gri = new GelRulesImpl();
		for (Entry<Integer, String> entry : headings.entrySet())
		{
			String name = entry.getValue();
			if ((!ALLELE.equalsIgnoreCase(name) && !name.toLowerCase().startsWith("note"))) //$NON-NLS-1$
			{
				gri.addGel(name);
			}
		}

		while (rows.hasNext())
		{
			Map<String, Float[]> properties = new LinkedHashMap<String, Float[]>();
			HSSFRow r = rows.next();

			Allele allele = null;
			@SuppressWarnings("unchecked")
			Iterator<HSSFCell> cells = r.cellIterator();
			while (cells.hasNext())
			{
				HSSFCell c = cells.next();
				String heading = headings.get(Integer.valueOf(c.getColumnIndex()));
				if (ALLELE.equalsIgnoreCase(heading))
				{
					if (c.toString().length() != 0)
					{
						allele = getAllele(genome, c.toString());
						if (allele == null)
						{
							throw new ParseException(MessageFormat.format(Messages.getString("Load2.18"), Sheets.GEL.toString(), c.toString())); //$NON-NLS-1$
						}
					}
				}
				else
				{
					try
					{
						if (c.toString().length() != 0)
						{
							properties.put(heading, parseFloatArray(c.toString()));
						}
					}
					catch (NumberFormatException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (allele != null)
			{
				for (Entry<String, Float[]> e : properties.entrySet())
				{
					gri.addGel(e.getKey(), allele, e.getValue());
				}
			}
		}
		model.setGelRules(gri);

	}

	private Float[] parseFloatArray(String string)
	{
		Float[] ret = null;
		if (string != null)
		{
			String[] list = string.split(","); //$NON-NLS-1$
			ret = new Float[list.length];
			for (int i = 0; i < list.length; i++)
			{
				ret[i] = Float.parseFloat(list[i]);
			}
		}
		return ret;
	}

	private void parsePropertiesSheet(HSSFSheet sheet, ModelWriter model, star.genetics.genetic.model.Genome genome)
	{
		@SuppressWarnings("unchecked")
		Iterator<HSSFRow> rows = sheet.rowIterator();
		while (rows.hasNext())
		{
			HSSFRow r = rows.next();
			if (r.getLastCellNum() - r.getFirstCellNum() != 0)
			{
				String key = String.valueOf(r.getCell(r.getFirstCellNum() + 0));
				String value = String.valueOf(r.getCell((r.getFirstCellNum() + 1)));
				if (key.toLowerCase().startsWith(Properties.NAME.toString().toLowerCase()))
				{
					genome.setName(value);
				}
				if (key.toLowerCase().startsWith(Properties.MATING_TYPE.toString().toLowerCase()))
				{
					genome.setSexType(value);
				}
				if (key.toLowerCase().startsWith(Properties.VISUALIZER.toString().toLowerCase()))
				{
					model.setVisualizerClass(value);
				}
				if (key.toLowerCase().startsWith(Properties.MALERECOMBINATIONRATE.toString().toLowerCase()))
				{
					float rate = Float.parseFloat(value);
					model.setRecombinationRate(rate / 100f, Sex.MALE);
				}
				if (key.toLowerCase().startsWith(Properties.FEMALERECOMBINATIONRATE.toString().toLowerCase()))
				{
					float rate = Float.parseFloat(value);
					model.setRecombinationRate(rate / 100f, Sex.FEMALE);
				}
				if (key.toLowerCase().startsWith(Properties.PROGENIESCOUNT.toString().toLowerCase()))
				{
					int progeniesCount = (int) Float.parseFloat(value);
					model.setProgeniesCount(progeniesCount);
				}
				if (key.toLowerCase().startsWith(Properties.MATINGSCOUNT.toString().toLowerCase()))
				{
					int matingsCount = (int) Float.parseFloat(value);
					model.setMatingsCount(matingsCount);
				}
				if (key.toLowerCase().startsWith(Properties.SPONTANIOUSMALES.toString().toLowerCase()))
				{
					float spontaniousMales = (int) Float.parseFloat(value);
					model.setSpontaniousMales(spontaniousMales);
				}
				if (key.toLowerCase().startsWith(Properties.AVAILABLEEXPERIMENTS.toString().toLowerCase()))
				{
					YeastUIMetadata metadata = (YeastUIMetadata) model.getModelMetadata().get(YeastUIMetadata.class);
					if (metadata == null)
					{
						metadata = new YeastUIMetadata();
					}
					metadata.setExperimentType(value);
					model.getModelMetadata().put(YeastUIMetadata.class, metadata);
				}
				if (key.toLowerCase().startsWith(Properties.TWINNINGFREQUENCY.toString().toLowerCase()))
				{
					MatingEngineMetadata metadata = (MatingEngineMetadata) model.getModelMetadata().get(MatingEngineMetadata.class);
					if (metadata == null)
					{
						metadata = new MatingEngineMetadata();
					}
					metadata.set(Properties.TWINNINGFREQUENCY, value);
					model.getModelMetadata().put(MatingEngineMetadata.class, metadata);
				}
				if (key.toLowerCase().startsWith(Properties.IDENTICALTWINSFREQUENCY.toString().toLowerCase()))
				{
					MatingEngineMetadata metadata = (MatingEngineMetadata) model.getModelMetadata().get(MatingEngineMetadata.class);
					if (metadata == null)
					{
						metadata = new MatingEngineMetadata();
					}
					metadata.set(Properties.IDENTICALTWINSFREQUENCY, value);
					model.getModelMetadata().put(MatingEngineMetadata.class, metadata);
				}

			}
		}

	}

	private Set<String> rowToSet(HSSFRow row)
	{
		Set<String> set = new LinkedHashSet<String>();
		@SuppressWarnings("unchecked")
		Iterator<HSSFCell> cells = row.cellIterator();
		while (cells.hasNext())
		{
			HSSFCell c = cells.next();
			set.add(c.toString());
		}
		return set;
	}

	private Map<Integer, Genome> genomeSheetParseHeading(Iterator<HSSFRow> rows) throws ParseException
	{
		HSSFRow row = null;
		boolean foundHeadingRow = false;
		while (rows.hasNext())
		{
			row = rows.next();
			if (Genome.hasRequired(rowToSet(row)))
			{
				foundHeadingRow = true;
				break;
			}
		}
		if (!foundHeadingRow)
		{
			throw new ParseException(MessageFormat.format(Messages.getString("Load2.20"), Sheets.GENOME.toString())); //$NON-NLS-1$
		}
		return Genome.mapRow(row);
	}

	static class GenomeSheetRow
	{
		public String chromosomeName = null;
		public String geneName = null;
		public float geneLocation = Float.NaN;
		public String[] alleles = null;

		boolean isRowValid = false;

		public GenomeSheetRow(HSSFRow row, Map<Integer, Genome> map) throws ParseException
		{
			for (Entry<Integer, Genome> entry : map.entrySet())
			{
				HSSFCell c = row.getCell(entry.getKey());
				if (c != null)
				{
					switch (entry.getValue())
					{
					case CHROMOSOME:
						if (c.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							double value = c.getNumericCellValue();
							if (value == Math.round(value))
							{
								chromosomeName = "" + (int) value; //$NON-NLS-1$
							}
						}
						else
						{
							chromosomeName = c.toString();
						}
						break;
					case ALLELES:
						alleles = c.toString().split(","); //$NON-NLS-1$
						break;
					case LOCATION:
						geneLocation = (float) c.getNumericCellValue();
						break;
					case GENE:
						geneName = c.toString() + "-" + row.getRowNum(); //$NON-NLS-1$
					}
				}
			}
			if ((chromosomeName == null || chromosomeName.length() == 0 || geneName == null || geneName.length() == 0))
			{
				isRowValid = false;
			}
			else
			{
				if (chromosomeName == null || geneName == null || Double.isNaN(geneLocation) || alleles == null || alleles.length == 0)
				{
					throw new ParseException(MessageFormat.format(Messages.getString("Load2.24"), row.getRowNum())); //$NON-NLS-1$
				}
				isRowValid = true;
			}
		}

		public boolean isRowValid()
		{
			return isRowValid;
		}
	}

	private void fixSexChromosomes(ModelWriter model, star.genetics.genetic.model.Genome genome) throws ParseException
	{
		if (star.genetics.genetic.model.Genome.SexType.XY.equals(genome.getSexType()))
		{
			Chromosome x = getChromosomeByName(genome, X);
			if (x.getGenes().size() == 0)
			{
				Gene g = getGeneByName(XGENE, 0, x, null, x.getName());
				generateAlleles(new String[] { XXX2.toUpperCase(), XXX2.toLowerCase() }, g);
			}
			Chromosome y = getChromosomeByName(genome, Y);
			if (y.getGenes().size() == 0)
			{
				Gene g = getGeneByName(YGENE, 0, y, null, y.getName());
				generateAlleles(new String[] { YYY2.toUpperCase(), YYY2.toLowerCase() }, g);
			}

		}
	}

	private void parseGenomeSheet(HSSFSheet sheet, ModelWriter model, star.genetics.genetic.model.Genome genome) throws ParseException
	{
		@SuppressWarnings("unchecked")
		Iterator<HSSFRow> rows = sheet.rowIterator();

		Map<Integer, Genome> map = genomeSheetParseHeading(rows);

		while (rows.hasNext())
		{
			HSSFRow r = rows.next();
			GenomeSheetRow data = new GenomeSheetRow(r, map);

			if (data.isRowValid())
			{
				Chromosome c = getChromosomeByName(genome, data.chromosomeName);
				Gene g = getGeneByName(data.geneName, data.geneLocation, c, r, data.chromosomeName);
				generateAlleles(data.alleles, g);
			}
		}
		fixSexChromosomes(model, genome);
	}

	private Chromosome getChromosomeByName(star.genetics.genetic.model.Genome genome, String chromosomeName)
	{
		Chromosome c = genome.getChromosomeByName(chromosomeName);

		if (c == null)
		{
			star.genetics.genetic.impl.ChromosomeImpl c1 = new star.genetics.genetic.impl.ChromosomeImpl(chromosomeName, genome);
			c = c1;
		}
		return c;
	}

	private Gene getGeneByName(String geneName, float geneLocation, Chromosome c, HSSFRow r, String chromosomeName) throws ParseException
	{
		if (c.getGeneByName(geneName) != null)
		{
			throw new ParseException(MessageFormat.format(Messages.getString("Load2.25"), geneName, r.getRowNum(), chromosomeName)); //$NON-NLS-1$
		}
		for (Gene gtest : c.getGenes())
		{
			if (gtest.getPosition() == geneLocation)
			{
				geneLocation += (float) (.01 * Math.random());
				// throw new
				// ParseException(MessageFormat.format("Duplicate gene location {0} on chromosome {2} in row {1}. \n This location is shared with gene {3}, please move one of the genes.",
				// geneName, r.getRowNum(), chromosomeName, gtest.getName()));
			}
		}
		star.genetics.genetic.impl.GeneImpl g = new star.genetics.genetic.impl.GeneImpl(geneName, geneLocation, c);
		return g;
	}

	private void generateAlleles(String[] alleles, Gene g)
	{
		for (String name : alleles)
		{
			new star.genetics.genetic.impl.AlleleImpl(name.trim(), g);
		}
	}

	@SuppressWarnings("unchecked")
	private void parsePhenotypeSheet(HSSFSheet sheet, ModelWriter model, star.genetics.genetic.model.Genome genome) throws ParseException
	{
		boolean defaultRuleExists = false;
		Iterator<HSSFRow> rows = sheet.rowIterator();
		HSSFRow row = null;
		boolean foundHeadingRow = false;
		while (rows.hasNext())
		{
			row = rows.next();
			if (hasRequiredColumnsForPhenotype(rowToSet(row)))
			{
				foundHeadingRow = true;
				break;
			}
		}
		if (!foundHeadingRow)
		{
			throw new ParseException(MessageFormat.format(Messages.getString("Load2.26"), Sheets.PHENOTYPE.toString())); //$NON-NLS-1$
		}

		LinkedHashMap<Integer, String> headings = new LinkedHashMap<Integer, String>()
		{
			private static final long serialVersionUID = 1L;

			public String get(Object key)
			{
				String ret = super.get(key);
				return ret != null ? ret : ""; //$NON-NLS-1$

			};
		};
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		parseHeading(row, headings, set);

		int rowCount = row.getRowNum();
		Set<String> dataSet = new HashSet<String>();
		while (rows.hasNext())
		{
			HSSFRow r = rows.next();
			Iterator<HSSFCell> cells = r.cellIterator();
			while (cells.hasNext())
			{
				HSSFCell c = cells.next();
				String heading = headings.get(Integer.valueOf(c.getColumnIndex())).trim();
				if (!(GENOTYPE.equalsIgnoreCase(heading) || (COL_NOTE.equalsIgnoreCase(heading) || COL_NOTES.equalsIgnoreCase(heading))))
				{
					String value = c.toString();
					if (value.length() != 0)
					{
						dataSet.add(heading.trim());
					}
				}
			}
		}

		rows = sheet.rowIterator();
		while (rows.next().getRowNum() != rowCount)
		{
			continue;
		}

		LinkedHashSet<String> orderedSet = new LinkedHashSet<String>();
		for (String s : set)
		{
			orderedSet.add(s.trim());
		}
		orderedSet.retainAll(dataSet);
		RuleSet rules = new star.genetics.genetic.impl.RuleSetImpl(orderedSet);
		while (rows.hasNext())
		{
			LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
			String rule = null;

			HSSFRow r = rows.next();

			Iterator<HSSFCell> cells = r.cellIterator();
			while (cells.hasNext())
			{
				HSSFCell c = cells.next();
				String heading = headings.get(Integer.valueOf(c.getColumnIndex())).trim();
				if (GENOTYPE.equalsIgnoreCase(heading))
				{
					rule = c.toString();
				}
				else if (COL_NOTE.equalsIgnoreCase(heading) || COL_NOTES.equalsIgnoreCase(heading))
				{

				}
				else
				{
					String value = c.toString();
					if (value.length() != 0)
					{
						properties.put(heading, value);
					}
				}
			}

			if (rule == null && properties.size() != 0)
			{
				throw new ParseException(MessageFormat.format(Messages.getString("Load2.28"), r.getRowNum())); //$NON-NLS-1$
			}

			if (properties.size() == 0)
			{
				continue;
			}

			if (Rule.DEFAULT.equalsIgnoreCase(rule))
			{
				defaultRuleExists = true;
				for (String key : set)
				{
					if (!properties.containsKey(key))
					{
						if (key.length() != 0 && !GENOTYPE.equalsIgnoreCase(key.trim()))
							properties.put(key, WILDTYPE);
					}
				}
			}
			rules.add(new star.genetics.genetic.impl.RuleImpl(rule, properties, genome));
		}

		if (!defaultRuleExists)
		{
			HashMap<String, String> properties = new HashMap<String, String>();
			for (String key : dataSet)
			{
				if (!properties.containsKey(key))
				{
					properties.put(key, WILDTYPE);
				}
			}
			rules.add(new star.genetics.genetic.impl.RuleImpl(Rule.DEFAULT, properties, genome));
		}
		model.setRules(rules);
	}

	private void parseHeading(HSSFRow r, Map<Integer, String> headings, Set<String> set) throws ParseException
	{
		headings.clear();
		set.clear();
		@SuppressWarnings("unchecked")
		Iterator<HSSFCell> cells = r.cellIterator();
		while (cells.hasNext())
		{
			HSSFCell c = cells.next();
			headings.put(Integer.valueOf(c.getColumnIndex()), String.valueOf(c));
			set.add(String.valueOf(c));
		}
	}

	private String GENOTYPE = "Genotype"; //$NON-NLS-1$

	private boolean hasRequiredColumnsForPhenotype(Collection<String> keys) throws ParseException
	{
		return keys.contains(GENOTYPE);
	}

	private String TRAIT = "Trait"; //$NON-NLS-1$
	private String DETERMINATION = "Determinants"; //$NON-NLS-1$
	private String SEX = "Sex"; //$NON-NLS-1$

	private boolean hasRequiredColumnsForOrganism(Collection<String> keys) throws ParseException
	{
		return keys.contains(TRAIT) && keys.contains(DETERMINATION);
	}

	private String ALLELE = "Alleles"; //$NON-NLS-1$

	private boolean hasRequiredColumnsForGel(Collection<String> keys) throws ParseException
	{
		return keys.contains(ALLELE);
	}

}
