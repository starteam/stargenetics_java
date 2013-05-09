package star.genetics.v1.ui.punnett;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;

public class GenotypeList2 extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final static String[] data = new String[] { "AA", "Aa", "aa" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private final static String[] dataB = new String[] { "BB", "Bb", "bb" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private JList list1;
	private JList list2;

	public GenotypeList2(String title)
	{
		setBorder(BorderFactory.createTitledBorder(title));
		list1 = new JList(data);
		list2 = new JList(dataB);

		list1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list1.setVisibleRowCount(1);
		list2.setVisibleRowCount(1);
		list1.setSelectedIndex(0);
		list2.setSelectedIndex(0);

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(list1);
		add(new JLabel(" ")); //$NON-NLS-1$
		add(list2);
		setBackground(UIManager.getColor("Panel.background")); //$NON-NLS-1$
		list1.setBackground(UIManager.getColor("Panel.background")); //$NON-NLS-1$
		list2.setBackground(UIManager.getColor("Panel.background")); //$NON-NLS-1$

	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		list1.addListSelectionListener(listener);
		list2.addListSelectionListener(listener);
	}

	public String[] getSelectedValue()
	{
		if (list1.getSelectedValue() == null || list2.getSelectedValue() == null)
		{
			return null;
		}
		return new String[] { String.valueOf(list1.getSelectedValue()), String.valueOf(list2.getSelectedValue()) };
	}

}
