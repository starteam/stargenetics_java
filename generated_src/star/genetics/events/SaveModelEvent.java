/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.events;

public class SaveModelEvent extends star.event.Event
{
	private static final long serialVersionUID = 1L;

	public  SaveModelEvent(star.event.Raiser raiser, boolean valid)
	{
		super( raiser , valid ) ;
	}
	 
	public  SaveModelEvent(star.genetics.events.SaveModelEvent event)
	{
		super( event ) ;
	}
	 
	public  SaveModelEvent(star.genetics.events.SaveModelRaiser raiser)
	{
		super( raiser ) ;
	}
	 
}