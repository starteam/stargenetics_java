package star.genetics.v1.ui.punnett;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;
import star.genetics.Messages;
import utils.UIHelpers;

public class PunnettSquare4 extends JPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 1L;

	private GenotypeList2X parent11;
	private GenotypeList2Sex parent21;

	private JLabel report;
	private PunnettSquareModel4 punnetSqareModel;
	private JTable table;

	@Override
	public void addNotify()
	{
		super.addNotify();
		final JButton close = new JButton(Messages.getString("PunnettSquare4.0")); //$NON-NLS-1$
		report = new JLabel();
		report.setBorder(BorderFactory.createTitledBorder(Messages.getString("PunnettSquare4.1"))); //$NON-NLS-1$
		parent11 = new GenotypeList2X(Messages.getString("PunnettSquare4.2")); //$NON-NLS-1$
		parent21 = new GenotypeList2Sex(Messages.getString("PunnettSquare4.3")); //$NON-NLS-1$

		punnetSqareModel = new PunnettSquareModel4();
		table = new JTable();
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(table);
		tablePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PunnettSquare4.4"))); //$NON-NLS-1$
		Container c = this;// getContentPane();
		c.setLayout(new MigLayout());
		c.add(new JLabel(Messages.getString("PunnettSquare4.5")), "span 2, center, wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		c.add(parent11, "width 50%"); //$NON-NLS-1$

		c.add(parent21, "width 50%	,wrap"); //$NON-NLS-1$

		c.add(tablePanel, "span 2, grow ,wrap"); //$NON-NLS-1$
		c.add(report, "span 2, grow , wrap"); //$NON-NLS-1$

		c.add(close, "center,dock south"); //$NON-NLS-1$

		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				UIHelpers.getWindow(close).dispose();
			}
		});
		parent11.addListSelectionListener(this);
		parent21.addListSelectionListener(this);

		punnetSqareModel.update(parent11.getSelectedValue(), parent21.getSelectedValue());
		report.setText(punnetSqareModel.getReport());
		table.setModel(punnetSqareModel);
		table.setDefaultRenderer(Object.class, new TableCellRenderer()
		{

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				JLabel ret = new JLabel(value.toString());
				ret.setHorizontalAlignment(SwingConstants.CENTER);
				if (row == 0 || column == 0)
				{
					ret.setForeground(Color.blue);
					ret.setBackground(Color.lightGray);
					ret.setOpaque(true);
				}
				return ret;
			}
		});
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting() == false)
		{
			punnetSqareModel.update(parent11.getSelectedValue(), parent21.getSelectedValue());
			report.setText(punnetSqareModel.getReport());
			table.invalidate();
			table.setSize(1000, 1000);
			UIHelpers.getWindow(this).pack();
		}
	}

}