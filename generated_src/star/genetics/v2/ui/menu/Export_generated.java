/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.ui.menu;

abstract class Export_generated extends star.genetics.v2.ui.menu.MenuItem implements star.event.EventController, star.genetics.events.ErrorDialogRaiser, star.genetics.events.ExportModelRaiser
{
	private star.event.Adapter adapter;
	private java.lang.Exception errorMessage;
	private java.lang.String modelFileName;
	private java.io.OutputStream modelStream;
	private static final long serialVersionUID = 1L;

	public  Export_generated()
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
	 
	public java.lang.Exception getErrorMessage()
	{
		return this.errorMessage ;
	}
	 
	public java.lang.String getModelFileName()
	{
		return this.modelFileName ;
	}
	 
	public java.io.OutputStream getModelStream()
	{
		return this.modelStream ;
	}
	 
	public void raise_ErrorDialogEvent()
	{
		(new star.genetics.events.ErrorDialogEvent(this)).raise();
	}
	 
	public void raise_ExportModelEvent()
	{
		(new star.genetics.events.ExportModelEvent(this)).raise();
	}
	 
	public void removeNotify()
	{
		super.removeNotify();
	}
	 
	protected void setErrorMessage(java.lang.Exception errorMessage)
	{
		this.errorMessage = errorMessage ;
	}
	 
	protected void setModelFileName(java.lang.String modelFileName)
	{
		this.modelFileName = modelFileName ;
	}
	 
	protected void setModelStream(java.io.OutputStream modelStream)
	{
		this.modelStream = modelStream ;
	}
	 
}