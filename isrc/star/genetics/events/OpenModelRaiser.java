package star.genetics.events;

import star.annotations.Raiser;

/**
 * Requests opening of the model represents by InputStream
 * 
 * @author ceraj
 */

@Raiser
public interface OpenModelRaiser extends star.event.Raiser
{
	java.io.InputStream getOpenModelStream();

	String getModelFileName();

	java.net.URL getModelURL();
}
