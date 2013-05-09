package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.GeneticMakeup;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.genetic.model.Genome;

public class CreatureImpl implements star.genetics.genetic.model.Creature, Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	private final Genome genome;
	private Sex sex;
	private final GeneticMakeup makeup;
	private boolean readOnly = false;
	private String note;
	private int matingsAvailable = Integer.MAX_VALUE;
	private Map<String, String> properties;
	final private CreatureSet parents;
	private String uuid;

	public CreatureImpl(String name, Genome genome, Sex sex, GeneticMakeup makeup, int matingsAvailable, Map<String, String> properties, CreatureSet parents)
	{
		this.name = name;
		this.genome = genome;
		this.sex = sex;
		this.makeup = makeup;
		this.matingsAvailable = matingsAvailable;
		this.properties = new LinkedHashMap<String, String>();
		this.parents = parents;
		addProperties(properties);
		this.uuid = generateUUID();
	}

	public CreatureSet getParents()
	{
		return parents;
	}

	public Genome getGenome()
	{
		return genome;
	}

	public GeneticMakeup getMakeup()
	{
		return makeup;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Sex getSex()
	{
		return sex;
	}

	public void setSex(Sex sex)
	{
		this.sex = sex;
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}

	public void setReadOnly(boolean ro)
	{
		readOnly = ro;
	}

	// TODO: May be a problem - REVISIT
	public int compareTo(star.genetics.genetic.model.Creature o)
	{
		return this.getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Creature) && (compareTo((Creature) obj) == 0);
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() >> 1;
	}

	public String getNote()
	{
		return this.note;
	}

	public void setNote(String string)
	{
		this.note = string;
	}

	@Override
	public String toString()
	{
		return MessageFormat.format(Messages.getString("CreatureImpl.0"), getName(), genome.getName(), makeup, isSterile(), getMatingsAvailable()); //$NON-NLS-1$
	}

	public boolean isMateable()
	{
		return !isSterile() && getMatingsAvailable() != 0;
	}

	private boolean isSterile()
	{
		boolean ret = false;
		String sterile = getProperties().get(GeneticModel.sterile);
		if (sterile != null)
		{
			ret = Boolean.parseBoolean(sterile);
		}
		return ret;
	}

	private int getMatingsAvailable()
	{
		return matingsAvailable;
	}

	public void mated()
	{
		matingsAvailable--;
		updateMatings();
	}

	public void addProperties(Map<String, String> properties)
	{
		for (Entry<String, String> entry : properties.entrySet())
		{
			this.properties.put(entry.getKey(), entry.getValue());
		}
		updateMatings();
	}

	public Map<String, String> getProperties()
	{
		return properties;
	}

	private void updateMatings()
	{
		properties.put(GeneticModel.matings, (matingsAvailable > 100 ? "100+" : Integer.toString(matingsAvailable))); //$NON-NLS-1$
	}

	private String generateUUID()
	{
		long uuid1 = -(long) (Math.random() * Long.MAX_VALUE);
		long uuid2 = -(long) (Math.random() * Long.MAX_VALUE);
		return Long.toHexString(uuid1) + Long.toHexString(uuid2);

	}

	@Override
	public String getUUID()
	{
		return uuid;
	}
}
