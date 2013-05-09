package star.genetics.events;

import star.annotations.Raiser;

@Raiser
public interface ErrorDialogRaiser extends star.event.Raiser
{
	Exception getErrorMessage();
}
