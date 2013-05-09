package star.genetics.v1.ui.punnett;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.UIManager;

public class GenotypeListSex extends JList
{
	private static final long serialVersionUID = 1L;
	private final static SexText[] data = new SexText[] { SexText.XY, SexText.xY };

	public GenotypeListSex(String title)
	{
		super(data);
		setSelectedIndex(0);
		setVisibleRowCount(1);
		setBorder(BorderFactory.createTitledBorder(title));
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setBackground(UIManager.getColor("Panel.background")); //$NON-NLS-1$
	}

	@Override
	public SexText getSelectedValue()
	{
		Object value = super.getSelectedValue();
		if (value instanceof SexText)
		{
			return ((SexText) value);
		}
		return null;
	}
}
