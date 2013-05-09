package star.genetics.v1.ui.events;

import star.annotations.Raiser;

@Raiser
public interface CrateMateRaiser extends star.event.Raiser
{
	int getCount();

	void setProgress(int percentage);
}
