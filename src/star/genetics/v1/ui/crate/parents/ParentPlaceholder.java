package star.genetics.v1.ui.crate.parents;

import java.awt.Dimension;

import javax.swing.JComponent;

import star.genetics.v1.ui.common.CommonData;

public class ParentPlaceholder extends JComponent
{

	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getPreferredSize()
	{
		return CommonData.getPreferredSize();
	}
}
