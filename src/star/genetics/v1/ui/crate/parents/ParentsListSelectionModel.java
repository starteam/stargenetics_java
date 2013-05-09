package star.genetics.v1.ui.crate.parents;

import javax.swing.DefaultListSelectionModel;

public class ParentsListSelectionModel extends DefaultListSelectionModel
{
	private static final long serialVersionUID = 1L;

	@Override
	public void setSelectionInterval(int index0, int index1)
	{
		clearSelection();
	}

	@Override
	public void addSelectionInterval(int index0, int index1)
	{
		clearSelection();
	}
}
