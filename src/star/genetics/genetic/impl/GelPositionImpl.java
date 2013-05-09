package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.GelPosition;

public class GelPositionImpl implements GelPosition, Serializable
{
	private static final long serialVersionUID = 1L;
	private final Gel gel;
	private final Float[] position;
	private final Allele allele;

	public GelPositionImpl(Gel gel, Float[] position, Allele allele)
	{
		this.gel = gel;
		this.position = position;
		this.allele = allele;
	}

	@Override
	public Gel getGel()
	{
		return gel;
	}

	@Override
	public Float[] getPosition()
	{
		return position;
	}

	@Override
	public Allele getAllele()
	{
		return allele;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("[{0} {1} {2} {3}]", this.getClass().getName(), gel.getName(), allele.getName(), Arrays.toString(getPosition())); //$NON-NLS-1$
	}
}
