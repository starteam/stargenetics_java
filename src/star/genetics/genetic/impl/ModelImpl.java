package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;

import star.genetics.genetic.model.CrateSet;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.GelRules;
import star.genetics.genetic.model.Genome;
import star.genetics.genetic.model.MatingEngine;
import star.genetics.genetic.model.ModelMetadata;
import star.genetics.visualizers.VisualizerFactory;

public class ModelImpl implements star.genetics.genetic.model.ModelWriter, Serializable
{
	private static final long serialVersionUID = 1L;

	private Genome genome;
	private star.genetics.genetic.model.CreatureSet creatures;
	private star.genetics.genetic.model.RuleSet rules;
	private MatingEngine mater = null;
	private float maleRecombinationRate = 1f;
	private float femaleRecombinationRate = 1f;
	private int progeniesCount = 50;
	private int matingsCount = Integer.MAX_VALUE;
	private CrateSet crateSet = new CrateSetImpl();
	private VisualizerFactory visualFactory = null;
	private GelRules gelRules;
	private float spontaniousMales = 0.001f;
	private ModelMetadata modelMetadata = new ModelMetadataImpl();

	public void setVisualizerClass(String className)
	{
		visualFactory = new VisualizerFactoryImpl(className);
	}

	public void setCreatures(star.genetics.genetic.model.CreatureSet creatures)
	{
		this.creatures = creatures;
	}

	public void setRules(star.genetics.genetic.model.RuleSet rules)
	{
		this.rules = rules;

	}

	public star.genetics.genetic.model.RuleSet getRules()
	{
		return rules;
	}

	public void setMater(MatingEngineImpl_XY mater)
	{
		this.mater = mater;
	}

	public void setRecombinationRate(float rate, Sex sex)
	{
		if (Sex.MALE.equals(sex))
		{
			maleRecombinationRate = rate;
		}
		else
		{
			femaleRecombinationRate = rate;
		}
	}

	public void setGenome(Genome genome)
	{
		this.genome = genome;
	}

	public Genome getGenome()
	{
		return genome;
	}

	public MatingEngine getMatingEngine()
	{
		if (mater == null)
		{
			if (Genome.SexType.XY.equals(getGenome().getSexType()))
			{
				float twinning = 0;
				float identical = 0;
				MatingEngineMetadata md = (MatingEngineMetadata) modelMetadata.get(MatingEngineMetadata.class);
				if (md != null)
				{
					twinning = md.getTwinningFrequency();
					identical = md.getIdenticalTwinsFrequency();
				}
				mater = new MatingEngineImpl_XY(maleRecombinationRate, femaleRecombinationRate, 0.5f, progeniesCount, twinning, identical);
			}
			else if (Genome.SexType.XO.equals(getGenome().getSexType()))
			{
				mater = new MatingEngineImpl_XO(maleRecombinationRate, femaleRecombinationRate, 0.5f, progeniesCount, spontaniousMales);
			}
			else if (Genome.SexType.Aa.equals(getGenome().getSexType()))
			{
				mater = new MatingEngineImpl_MAT(maleRecombinationRate, femaleRecombinationRate, 0.5f, progeniesCount);
			}
			else if (Genome.SexType.UNISEX.equals(getGenome().getSexType()))
			{
				mater = new MatingEngineImpl_UNISEX(femaleRecombinationRate, progeniesCount);
			}
		}
		return mater;
	}

	public star.genetics.genetic.model.CreatureSet getCreatures()
	{
		return creatures;
	}

	public float getRecombinationRate(Sex sex)
	{
		return (Sex.MALE.equals(sex)) ? maleRecombinationRate : femaleRecombinationRate;
	}

	public CrateSet getCrateSet()
	{
		return crateSet;
	}

	public int getProgeniesCount()
	{
		return progeniesCount;
	}

	public VisualizerFactory getVisualizerFactory()
	{
		return visualFactory;
	}

	public void setProgeniesCount(int progeniesCount)
	{
		this.progeniesCount = progeniesCount;
		if (this.progeniesCount < 1)
		{
			this.progeniesCount = 1;
		}
	}

	public void setMatingsCount(int matingsCount)
	{
		this.matingsCount = matingsCount;
	}

	public int getMatingsCount()
	{
		return matingsCount;
	}

	public void setSpontaniousMales(float ratio)
	{
		this.spontaniousMales = ratio;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("[{0} visualizer:{1} genome:{2} creatures:{3} rules:{4} gelRules:{5}]", getClass().getSimpleName(), getVisualizerFactory().newVisualizerInstance().getClass().getName(), getGenome(), getCreatures(), getRules(), getGelRules()); //$NON-NLS-1$
	}

	public void setGelRules(GelRules gri)
	{
		this.gelRules = gri;
	}

	public GelRules getGelRules()
	{
		return gelRules;
	}

	@Override
	public ModelMetadata getModelMetadata()
	{
		return modelMetadata;
	}
}
