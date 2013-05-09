package star.genetics.genetic.impl;

import java.io.Serializable;
import java.text.MessageFormat;

import star.genetics.Messages;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;

public class VisualizerFactoryImpl implements VisualizerFactory, Serializable
{
	private static final long serialVersionUID = 1L;
	private String visualizerClassName = null;
	private transient Class<Visualizer> visualizerClass = null;

	public VisualizerFactoryImpl(String name)
	{
		visualizerClassName = name;
		newVisualizerInstance();
	}

	@SuppressWarnings("unchecked")
	private Class<Visualizer> getClass(String className)
	{
		Class<Visualizer> ret = null;
		try
		{
			ret = (Class<Visualizer>) Class.forName(className);
		}
		catch (Throwable ex)
		{
		}
		return ret;

	}

	private void initializeClass()
	{
		if (visualizerClass == null)
		{
			visualizerClass = getClass(visualizerClassName);
		}
		if (visualizerClass == null)
		{
			visualizerClass = getClass("star.genetics.visualizers." + visualizerClassName); //$NON-NLS-1$
		}
		if (visualizerClass == null)
		{
			throw new RuntimeException(MessageFormat.format(Messages.getString("VisualizerFactoryImpl.1"), visualizerClass)); //$NON-NLS-1$
		}
	}

	public Visualizer newVisualizerInstance()
	{
		if (visualizerClass == null)
		{
			initializeClass();
		}
		try
		{
			return visualizerClass.newInstance();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

}
