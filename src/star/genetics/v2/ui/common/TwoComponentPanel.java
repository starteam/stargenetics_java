package star.genetics.v2.ui.common;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import star.genetics.Messages;

public class TwoComponentPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	@Override
	public void doLayout()
	{
		switch (getComponentCount())
		{
		case 0:
			break;
		case 1:
			if (true)
			{
				Component top = getComponent(0);
				top.setBounds(0, 0, getWidth(), getHeight());
			}
			break;
		case 2:
			if (true)
			{
				Component top = getComponent(0);
				Component bottom = getComponent(1);
				Dimension bottomP = bottom.getPreferredSize();
				top.setBounds(0, 0, getWidth(), Math.max(getHeight() / 2, getHeight() - bottomP.height));
				bottom.setBounds(0, top.getHeight(), getWidth(), getHeight() - top.getHeight());
			}
			break;
		default:
			throw new RuntimeException("Supports only two components"); //$NON-NLS-1$
		}
		;
	}
}
