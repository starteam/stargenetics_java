package star.genetics.v2.yeast.events;

import star.annotations.Raiser;

@Raiser
public interface CreaturePropertiesChangedRaiser extends star.event.Raiser
{
	enum Kind
	{
		NOTE, NAME;
	}

	Kind getKind();
}
