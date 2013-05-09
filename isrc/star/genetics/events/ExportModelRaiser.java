package star.genetics.events;

import star.annotations.Raiser;

@Raiser
public interface ExportModelRaiser extends star.event.Raiser
{
	java.io.OutputStream getModelStream();

	String getModelFileName();
}
