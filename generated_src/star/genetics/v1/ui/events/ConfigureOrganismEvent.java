/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v1.ui.events;

public class ConfigureOrganismEvent extends star.event.Event
{
	private static final long serialVersionUID = 1L;

	public  ConfigureOrganismEvent(star.event.Raiser raiser, boolean valid)
	{
		super( raiser , valid ) ;
	}
	 
	public  ConfigureOrganismEvent(star.genetics.v1.ui.events.ConfigureOrganismEvent event)
	{
		super( event ) ;
	}
	 
	public  ConfigureOrganismEvent(star.genetics.v1.ui.events.ConfigureOrganismRaiser raiser)
	{
		super( raiser ) ;
	}
	 
}