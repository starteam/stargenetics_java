package star.genetics.genetic.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import star.genetics.genetic.model.ModelMetadata;

public class ModelMetadataImpl implements ModelMetadata, Serializable
{
	private static final long serialVersionUID = 2L;
	Map<String, Object> data = new HashMap<String, Object>();

	public Object get(Class<? extends Object> c)
	{
		return data.get(c.getName());
	}

	public void put(Class<? extends Object> c, Object o)
	{
		data.put(c.getName(), o);
	}

}
