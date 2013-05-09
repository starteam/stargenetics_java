package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;

import star.genetics.Messages;
import star.genetics.genetic.model.Allele;

public class DiploidAllelesImpl implements star.genetics.genetic.model.DiploidAlleles, Serializable
{
	private static final long serialVersionUID = 1L;
	private final Allele a1, a2;

	public DiploidAllelesImpl(Allele[] alleles)
	{
		if (alleles != null && (alleles.length == 1 || alleles.length == 2))
		{
			a1 = alleles[0];
			a2 = alleles.length == 2 ? alleles[1] : null;
		}
		else
		{
			throw new RuntimeException(Messages.getString("DiploidAllelesImpl.0")); //$NON-NLS-1$
		}
	}

	public DiploidAllelesImpl(Allele a1, Allele a2)
	{
		this.a1 = a1;
		this.a2 = a2;
	}

	Allele meiosis()
	{
		return (java.lang.Math.random() < .5) ? a1 : a2;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format("Diploid({0},{1})", a1 != null ? a1.getName() : "-", a2 != null ? a2.getName() : "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public String toStortString()
	{
		return MessageFormat.format("({0},{1})", a1 != null ? a1.getName() : "-", a2 != null ? a2.getName() : "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public int getAlleleCount()
	{
		int ret = 0;
		ret += a1 != null ? 1 : 0;
		ret += a2 != null ? 1 : 0;
		return ret;
	}

	public Allele get(int i)
	{
		Allele ret = null;
		if (i == 0)
		{
			ret = a1;
		}
		else if (i == 1)
		{
			ret = a2;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof DiploidAllelesImpl)
		{
			DiploidAllelesImpl that = (DiploidAllelesImpl) obj;
			Allele this1 = this.get(0);
			Allele this2 = this.get(1);
			Allele that1 = that.get(0);
			Allele that2 = that.get(1);
			return (star.genetics.genetic.impl.Utilities.compare(this1, that1) && star.genetics.genetic.impl.Utilities.compare(this2, that2)) || (star.genetics.genetic.impl.Utilities.compare(this1, that2) && star.genetics.genetic.impl.Utilities.compare(this2, that1));
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return toStortString().hashCode();
	}
}
