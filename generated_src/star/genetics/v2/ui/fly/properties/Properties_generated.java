/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.ui.fly.properties;

abstract class Properties_generated extends star.genetics.v2.ui.common.TitledContainer implements star.event.EventController, star.event.Listener
{
	private star.event.Adapter adapter;
	private static final long serialVersionUID = 1L;

	public  Properties_generated()
	{
		super();
	}
	 
	public  Properties_generated(java.awt.Component component, java.awt.Component component0, boolean boolean1, float float2, float float3)
	{
		super(component,component0,boolean1,float2,float3);
	}
	 
	public  Properties_generated(java.awt.Component component, java.awt.Component component0, boolean boolean1)
	{
		super(component,component0,boolean1);
	}
	 
	public void addNotify()
	{
		super.addNotify();
		getAdapter().addHandled( star.genetics.v1.ui.events.OrganismSelectedEvent.class );
		getAdapter().addHandled( star.genetics.v1.ui.events.ProgenySelectedEvent.class );
	}
	 
	public void eventRaised(final star.event.Event event)
	{
		eventRaisedHandles(event);
	}
	 
	private void eventRaisedHandles(final star.event.Event event)
	{
		if( event.getClass().getName().equals( "star.genetics.v1.ui.events.OrganismSelectedEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			handle( (star.genetics.v1.ui.events.OrganismSelectedRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".handle "  + ( end-start )/1000000 ); } 
		}
		if( event.getClass().getName().equals( "star.genetics.v1.ui.events.ProgenySelectedEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			handle( (star.genetics.v1.ui.events.ProgenySelectedRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".handle "  + ( end-start )/1000000 ); } 
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
	 
	abstract void handle(star.genetics.v1.ui.events.OrganismSelectedRaiser OrganismSelectedRaiser);
	 
	abstract void handle(star.genetics.v1.ui.events.ProgenySelectedRaiser ProgenySelectedRaiser);
	 
	public void removeNotify()
	{
		super.removeNotify();
		getAdapter().removeHandled( star.genetics.v1.ui.events.OrganismSelectedEvent.class );
		getAdapter().removeHandled( star.genetics.v1.ui.events.ProgenySelectedEvent.class );
	}
	 
}