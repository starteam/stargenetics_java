package star.genetics.v1.ui.impl;

import javax.swing.AbstractListModel;

import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v1.ui.model.ProgeniesListModel;

public class ProgeniesListModelImpl extends AbstractListModel implements ProgeniesListModel
{

	CrateModel model;
	private static final long serialVersionUID = 1L;

	public ProgeniesListModelImpl(CrateModel model)
	{
		this.model = model;
	}

	public Object getElementAt(int index)
	{
		return model.getProgenies().get(index);
	}

	public int getSize()
	{
		return model.getProgenies().size();
	}

	public void update()
	{
		fireContentsChanged(this, 0, getSize());
	}

	public void clear()
	{
		model.getProgenies().clear();
		fireContentsChanged(this, 0, getSize());
	}

}
