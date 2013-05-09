package star.genetics.genetic.impl;

import java.io.Serializable;

public class YeastUIMetadata implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Experiments
	{
		TETRAD, NONTETRAD, BOTH
	};

	private Experiments exp = Experiments.BOTH;

	public YeastUIMetadata()
	{
	}

	public void setExperimentType(String value)
	{
		if ("tetrad".equalsIgnoreCase(value)) //$NON-NLS-1$
		{
			exp = Experiments.TETRAD;
		}
		else if ("non-tetrad".equalsIgnoreCase(value)) //$NON-NLS-1$
		{
			exp = Experiments.NONTETRAD;
		}
		else
		{
			exp = Experiments.BOTH;
		}
	}

	public Experiments getExperimentType()
	{
		return exp;
	}

}
