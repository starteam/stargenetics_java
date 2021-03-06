/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.ui.yeast.history;

abstract class History_generated extends star.genetics.v2.ui.common.TitledContainer implements star.event.EventController, star.event.Listener
{
	private star.event.Adapter adapter;
	private static final long serialVersionUID = 1L;

	public  History_generated()
	{
		super();
	}
	 
	public  History_generated(java.awt.Component component, java.awt.Component component0, boolean boolean1, float float2, float float3)
	{
		super(component,component0,boolean1,float2,float3);
	}
	 
	public  History_generated(java.awt.Component component, java.awt.Component component0, boolean boolean1)
	{
		super(component,component0,boolean1);
	}
	 
	public void addNotify()
	{
		super.addNotify();
		getAdapter().addExcludeExternal(star.genetics.v1.ui.events.OrganismSetAsParentEvent.class );
		getAdapter().addHandled( star.genetics.events.LoadModelEvent.class );
		getAdapter().addHandled( star.genetics.v1.ui.events.CrateNewCrateEvent.class );
		getAdapter().addHandled( star.genetics.v2.yeast.events.NewMatingExperimentEvent.class );
		getAdapter().addHandled( star.genetics.v2.yeast.events.NewReplicationExperimentEvent.class );
	}
	 
	public void eventRaised(final star.event.Event event)
	{
		eventRaisedHandles(event);
	}
	 
	private void eventRaisedHandles(final star.event.Event event)
	{
		if( event.getClass().getName().equals( "star.genetics.events.LoadModelEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			updateCrates( (star.genetics.events.LoadModelRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".updateCrates "  + ( end-start )/1000000 ); } 
		}
		if( event.getClass().getName().equals( "star.genetics.v1.ui.events.CrateNewCrateEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			updateCrates( (star.genetics.v1.ui.events.CrateNewCrateRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".updateCrates "  + ( end-start )/1000000 ); } 
		}
		if( event.getClass().getName().equals( "star.genetics.v2.yeast.events.NewMatingExperimentEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			updateCrates( (star.genetics.v2.yeast.events.NewMatingExperimentRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".updateCrates "  + ( end-start )/1000000 ); } 
		}
		if( event.getClass().getName().equals( "star.genetics.v2.yeast.events.NewReplicationExperimentEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			updateCrates( (star.genetics.v2.yeast.events.NewReplicationExperimentRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".updateCrates "  + ( end-start )/1000000 ); } 
		}
	}
	 
	public star.event.Adapter getAdapter()
	{
		if( adapter == null )
		{
			adapter = new star.event.Adapter(this);
		}
		return adapter;
	}
	 
	public void removeNotify()
	{
		super.removeNotify();
		getAdapter().removeExcludeExternal(star.genetics.v1.ui.events.OrganismSetAsParentEvent.class );
		getAdapter().removeHandled( star.genetics.events.LoadModelEvent.class );
		getAdapter().removeHandled( star.genetics.v1.ui.events.CrateNewCrateEvent.class );
		getAdapter().removeHandled( star.genetics.v2.yeast.events.NewMatingExperimentEvent.class );
		getAdapter().removeHandled( star.genetics.v2.yeast.events.NewReplicationExperimentEvent.class );
	}
	 
	abstract void updateCrates(star.genetics.events.LoadModelRaiser LoadModelRaiser);
	 
	abstract void updateCrates(star.genetics.v1.ui.events.CrateNewCrateRaiser CrateNewCrateRaiser);
	 
	abstract void updateCrates(star.genetics.v2.yeast.events.NewMatingExperimentRaiser NewMatingExperimentRaiser);
	 
	abstract void updateCrates(star.genetics.v2.yeast.events.NewReplicationExperimentRaiser NewReplicationExperimentRaiser);
	 
	abstract void updateLayout();
	 
	void updateLayout_SwingUtilitiesInvokeLater()
	{
		javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable()
			{
			public void run() {
				updateLayout();
			}
		});
	}
	 
}