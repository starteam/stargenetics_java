package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;

import star.genetics.Messages;
import star.genetics.genetic.model.CrateExperimentMetadata;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.CreatureSet;

public class CrateModelImpl implements CrateModel, Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	private final CreatureSet parents = new CreatureSetImpl();
	private final CreatureSet progenies = new CreatureSetImpl();
	private final CrateExperimentMetadata experimentMetadata = new CrateExperimentMetadataImpl();
	private boolean visible = true;
	private String uuid;

	public CrateModelImpl(int id)
	{
		name = MessageFormat.format(Messages.getString("CrateModelImpl.0"), id); //$NON-NLS-1$
		this.uuid = generateUUID();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public CreatureSet getParents()
	{
		return parents;
	}

	public CreatureSet getProgenies()
	{
		return progenies;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public CrateExperimentMetadata getMetadata()
	{
		return experimentMetadata;
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
