package star.genetics.v1.ui.events;

import star.annotations.Raiser;
import star.genetics.v1.ui.model.CrateModel;

@Raiser
public interface CrateParentsUpdatedRaiser extends star.event.Raiser
{
	CrateModel getModel();
}
