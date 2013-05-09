package star.genetics.v1.ui.events;

import star.annotations.Raiser;

@Raiser
public interface NextFrameRaiser extends star.event.Raiser
{
	boolean needFocus();
}
