/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.events;

public class ExportModelEvent extends star.event.Event
{
	private static final long serialVersionUID = 1L;

	public  ExportModelEvent(star.event.Raiser raiser, boolean valid)
	{
		super( raiser , valid ) ;
	}
	 
	public  ExportModelEvent(star.genetics.events.ExportModelEvent event)
	{
		super( event ) ;
	}
	 
	public  ExportModelEvent(star.genetics.events.ExportModelRaiser raiser)
	{
		super( raiser ) ;
	}
	 
}