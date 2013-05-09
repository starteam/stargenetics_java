package star.genetics.v2.ui.fly.common;

import java.io.Serializable;
import java.util.HashMap;

import star.genetics.Messages;

public class FlyCrateMetadata implements Serializable
{
	private static final long serialVersionUID = 1L;
	HashMap<String, String> properties = new HashMap<String, String>();
	private static String INDEX = "tabIndex"; //$NON-NLS-1$

	public int getIndex()
	{
		try
		{
			String index = properties.get(INDEX);
			return Integer.parseInt(index);
		}
		catch (Exception ex)
		{
			return 0;
		}
	}

	public void setIndex(int index)
	{
		properties.put(INDEX, Integer.toString(index));
	}
}
