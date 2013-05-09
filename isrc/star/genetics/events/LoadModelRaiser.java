package star.genetics.events;

import star.annotations.Raiser;
import star.genetics.genetic.model.Model;

/**
 * Raise parsed model
 * 
 * @author ceraj
 */
@Raiser()
public interface LoadModelRaiser extends star.event.Raiser
{
	Model getModel();

	String getModelName();
}
