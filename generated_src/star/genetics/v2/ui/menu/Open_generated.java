/* Generated by star.annotations.GeneratedClass, all changes will be lost */

package star.genetics.v2.ui.menu;

abstract class Open_generated extends star.genetics.v2.ui.menu.MenuItem implements star.event.EventController, star.genetics.events.ErrorDialogRaiser, star.genetics.events.OpenModelRaiser
{
	private star.event.Adapter adapter;
	private java.lang.Exception errorMessage;
	private byte[] modelBytes;
	private java.lang.String modelFileName;
	private static final long serialVersionUID = 1L;

	public  Open_generated()
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
	 
	protected byte getModelBytes(int i)
	{
		return this.modelBytes[i] ;
	}
	 
	protected byte[] getModelBytes()
	{
		return this.modelBytes ;
	}
	 
	public java.lang.String getModelFileName()
	{
		return this.modelFileName ;
	}
	 
	public void raise_ErrorDialogEvent()
	{
		(new star.genetics.events.ErrorDialogEvent(this)).raise();
	}
	 
	public void raise_OpenModelEvent()
	{
		(new star.genetics.events.OpenModelEvent(this)).raise();
	}
	 
	public void removeNotify()
	{
		super.removeNotify();
	}
	 
	protected void setErrorMessage(java.lang.Exception errorMessage)
	{
		this.errorMessage = errorMessage ;
	}
	 
	protected void setModelBytes(byte[] modelBytes)
	{
		this.modelBytes = modelBytes ;
	}
	 
	protected void setModelFileName(java.lang.String modelFileName)
	{
		this.modelFileName = modelFileName ;
	}
	 
}