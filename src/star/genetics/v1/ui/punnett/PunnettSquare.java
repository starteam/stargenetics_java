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

public class PunnettSquare extends JPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	private GenotypeList parent1;
	private GenotypeList parent2;
	private JLabel report;
	private PunnettSquareModel punnetSqareModel;

	@Override
	public void addNotify()
	{
		super.addNotify();
		final JButton close = new JButton(Messages.getString("PunnettSquare.0")); //$NON-NLS-1$
		report = new JLabel();
		report.setBorder(BorderFactory.createTitledBorder(Messages.getString("PunnettSquare.1"))); //$NON-NLS-1$
		parent1 = new GenotypeList(Messages.getString("PunnettSquare.2")); //$NON-NLS-1$
		parent2 = new GenotypeList(Messages.getString("PunnettSquare.3")); //$NON-NLS-1$
		punnetSqareModel = new PunnettSquareModel();
		JTable table = new JTable();
		table.setRowHeight(22);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(table);
		tablePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PunnettSquare.4"))); //$NON-NLS-1$
		Container c = this;// getContentPane();
		c.setLayout(new MigLayout());
		c.add(new JLabel(Messages.getString("PunnettSquare.5")), "span 2, center, wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		c.add(parent1, "width 50%, grow"); //$NON-NLS-1$
		c.add(parent2, "width 50%, grow,wrap"); //$NON-NLS-1$
		c.add(tablePanel, "span 2, grow ,wrap"); //$NON-NLS-1$
		c.add(report, "span 2, grow , wrap"); //$NON-NLS-1$
		JPanel closePanel = new JPanel();
		closePanel.add(close);
		c.add(closePanel, "center,dock south"); //$NON-NLS-1$

		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				UIHelpers.getWindow(close).dispose();
			}
		});
		parent1.addListSelectionListener(this);
		parent2.addListSelectionListener(this);
		punnetSqareModel.update(parent1.getSelectedValue(), parent2.getSelectedValue());
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
			punnetSqareModel.update(parent1.getSelectedValue(), parent2.getSelectedValue());
			report.setText(punnetSqareModel.getReport());
		}
	}

}
