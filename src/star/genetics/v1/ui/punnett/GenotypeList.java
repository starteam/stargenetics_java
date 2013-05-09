package star.genetics.v1.ui.punnett;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.UIManager;

import star.genetics.Messages;

public class GenotypeList extends JList
{
	private static final long serialVersionUID = 1L;
	private final static String[] data = new String[] { "AA ", "Aa ", "aa " }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public GenotypeList(String title)
	{
		super(data);
		setSelectedIndex(0);
		setVisibleRowCount(1);
		setBorder(BorderFactory.createTitledBorder(title));
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setBackground(UIManager.getColor("Panel.background")); //$NON-NLS-1$
	}

	@Override
	public String getSelectedValue()
	{
		return super.getSelectedValue() != null ? String.valueOf(super.getSelectedValue()).trim() : null;
	}
}
