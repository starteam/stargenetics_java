/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.yeast.events;

public class CreaturePropertiesChangedEvent extends star.event.Event
{
	private static final long serialVersionUID = 1L;

	public  CreaturePropertiesChangedEvent(star.event.Raiser raiser, boolean valid)
	{
		super( raiser , valid ) ;
	}
	 
	public  CreaturePropertiesChangedEvent(star.genetics.v2.yeast.events.CreaturePropertiesChangedEvent event)
	{
		super( event ) ;
	}
	 
	public  CreaturePropertiesChangedEvent(star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser raiser)
	{
		super( raiser ) ;
	}
	 
}