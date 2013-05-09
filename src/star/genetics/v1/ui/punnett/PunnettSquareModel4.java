package star.genetics.v1.ui.punnett;

import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import star.genetics.Messages;

public class PunnettSquareModel4 extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	private String[] columns = null;
	private String[] rows = null;

	public int getColumnCount()
	{
		return 5;
		// return columns != null ? columns.length + 1 : 0;
	}

	public int getRowCount()
	{
		return 5;
		// return rows != null ? rows.length + 1 : 0;
	}

	public void update(String[] cols, String[] rows)
	{
		this.columns = cols;
		this.rows = rows;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column)
	{
		return "test"; //$NON-NLS-1$
	}

	public String getValueAt(int rowIndex, int columnIndex)
	{
		if (rows == null || columns == null)
		{
			return " "; //$NON-NLS-1$
		}
		if (columnIndex == 0 && rowIndex == 0)
		{
			return " "; //$NON-NLS-1$
		}
		if (columnIndex == 0)
		{
			String[] str = getRow(rowIndex - 1);
			return str[0] + str[1];
		}
		if (rowIndex == 0)
		{
			String[] str = getCol(columnIndex - 1);
			return str[0] + str[1];
		}
		String[] str1 = getRow(rowIndex - 1);
		String[] str2 = getCol(columnIndex - 1);
		return str1[0] + str2[0] + str1[1] + str2[1];
	}

	public String[] getRow(int index)
	{
		return new String[] { "" + rows[0].charAt(index / 2), "" + rows[1].charAt(index % 2) }; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String[] getCol(int index)
	{
		return new String[] { "" + columns[0].charAt(index / 2), "" + columns[1].charAt(index % 2) }; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getReport()
	{
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		for (int x = 1; x < getColumnCount(); x++)
		{
			for (int y = 1; y < getRowCount(); y++)
			{
				if (map.containsKey(getValueAt(y, x)))
				{
					map.put(getValueAt(y, x), map.get(getValueAt(y, x)) + 1);
				}
				else
				{
					map.put(getValueAt(y, x), 1);
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if (rows == null || columns == null)
		{
			sb.append(Messages.getString("PunnettSquareModel4.0")); //$NON-NLS-1$
		}
		else
		{
			sb.append("<html>\n"); //$NON-NLS-1$
			for (Entry<String, Integer> entry : map.entrySet())
			{
				sb.append(entry.getKey() + ": " + entry.getValue() + "<br>\n"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		sb.append("<br></html>"); //$NON-NLS-1$
		return sb.toString();
	}
}