package star.genetics.v1.ui.events;

import star.annotations.Raiser;

@Raiser
public interface ShowHistoryRaiser extends star.event.Raiser
{
	boolean isHistoryVisible();
}
