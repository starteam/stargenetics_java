/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.ui.yeast.experiment;

abstract class ReplicaExperimentAddAll_generated extends star.genetics.v2.ui.common.Button implements star.event.EventController, star.genetics.v2.yeast.events.ReplicationExperimentAddAllRaiser
{
	private star.event.Adapter adapter;
	private static final long serialVersionUID = 1L;

	public  ReplicaExperimentAddAll_generated()
	{
		super();
	}
	 
	public void addNotify()
	{
		super.addNotify();
	}
	 
	public star.event.Adapter getAdapter()
	{
		if( adapter == null )
		{
			adapter = new star.event.Adapter(this);
		}
		return adapter;
	}
	 
	public void raise_ReplicationExperimentAddAllEvent()
	{
		(new star.genetics.v2.yeast.events.ReplicationExperimentAddAllEvent(this)).raise();
	}
	 
	public void removeNotify()
	{
		super.removeNotify();
	}
	 
}