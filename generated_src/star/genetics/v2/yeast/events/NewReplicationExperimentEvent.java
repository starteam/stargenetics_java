/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.yeast.events;

public class NewReplicationExperimentEvent extends star.event.Event
{
	private static final long serialVersionUID = 1L;

	public  NewReplicationExperimentEvent(star.event.Raiser raiser, boolean valid)
	{
		super( raiser , valid ) ;
	}
	 
	public  NewReplicationExperimentEvent(star.genetics.v2.yeast.events.NewReplicationExperimentEvent event)
	{
		super( event ) ;
	}
	 
	public  NewReplicationExperimentEvent(star.genetics.v2.yeast.events.NewReplicationExperimentRaiser raiser)
	{
		super( raiser ) ;
	}
	 
}