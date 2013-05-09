package star.genetics.v1.ui.common;

public class MatingDialogPossibleValue
{

	public MatingDialogPossibleValue(int i, int display)
	{
		count = i;
		this.display = display;
	}

	public int count;
	int display;

	@Override
	public String toString()
	{
		return "" + display; //$NON-NLS-1$
	}
}