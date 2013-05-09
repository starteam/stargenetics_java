package star.genetics.events;

import star.annotations.Raiser;

@Raiser
public interface SaveModelRaiser extends star.event.Raiser
{
	java.io.OutputStream getSaveModelStream();

	String getModelFileName();
}
