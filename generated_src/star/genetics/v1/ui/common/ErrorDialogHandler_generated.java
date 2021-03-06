/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v1.ui.common;

abstract class ErrorDialogHandler_generated extends java.lang.Object implements star.event.EventController, star.event.Listener, star.genetics.events.ErrorDialogRaiser
{
	private star.event.Adapter adapter;
	private static final long serialVersionUID = 1L;

	public  ErrorDialogHandler_generated()
	{
		super();
	}
	 
	public void addNotify()
	{
		getAdapter().addHandled( star.genetics.events.ErrorDialogEvent.class );
		getAdapter().addHandled( star.events.common.DistributionExceptionEvent.class );
	}
	 
	public void eventRaised(final star.event.Event event)
	{
		eventRaisedHandles(event);
	}
	 
	private void eventRaisedHandles(final star.event.Event event)
	{
		if( event.getClass().getName().equals( "star.genetics.events.ErrorDialogEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			
			openErrorDialog( (star.genetics.events.ErrorDialogRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".openErrorDialog "  + ( end-start )/1000000 ); } 
		}
		if( event.getClass().getName().equals( "star.events.common.DistributionExceptionEvent" ) && event.isValid() ) 
		{
			 long start = System.nanoTime();
			(new star.genetics.events.ErrorDialogEvent(this,false)).raise();
			openErrorDialogOnDistributionException( (star.events.common.DistributionExceptionRaiser)event.getSource());
			 long end = System.nanoTime();
			 if( end - start > 500000000 ) { System.out.println( this.getClass().getName() + ".openErrorDialogOnDistributionException "  + ( end-start )/1000000 ); } 
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
	 
	abstract void openErrorDialog(star.genetics.events.ErrorDialogRaiser ErrorDialogRaiser);
	 
	abstract void openErrorDialogOnDistributionException(star.events.common.DistributionExceptionRaiser DistributionExceptionRaiser);
	 
	public void raise_ErrorDialogEvent()
	{
		(new star.genetics.events.ErrorDialogEvent(this)).raise();
	}
	 
	public void removeNotify()
	{
		getAdapter().removeHandled( star.genetics.events.ErrorDialogEvent.class );
		getAdapter().removeHandled( star.events.common.DistributionExceptionEvent.class );
	}
	 
}