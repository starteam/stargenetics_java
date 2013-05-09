package star.genetics.v1.ui.punnett;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

public class PunnettSquareModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	private String columns = null;
	private String rows = null;

	public int getColumnCount()
	{
		return columns != null ? columns.length() + 1 : 0;
	}

	public int getRowCount()
	{
		return rows != null ? rows.length() + 1 : 0;
	}

	public String getValueAt(int rowIndex, int columnIndex)
	{
		if (columnIndex == 0 && rowIndex == 0)
		{
			return ""; //$NON-NLS-1$
		}
		if (columnIndex == 0)
		{
			return "" + rows.charAt(rowIndex - 1); //$NON-NLS-1$
		}
		if (rowIndex == 0)
		{
			return "" + columns.charAt(columnIndex - 1); //$NON-NLS-1$
		}
		char x = rows.charAt(rowIndex - 1);
		char y = columns.charAt(columnIndex - 1);
		return x < y ? x + " " + y : y + " " + x; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void update(String cols, String rows)
	{
		this.columns = cols;
		this.rows = rows;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column)
	{
		return column != 0 ? "" + columns.charAt(column - 1) : ""; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getReport()
	{
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		for (int x = 1; x < getColumnCount(); x++)
		{
			for (int y = 1; y < getRowCount(); y++)
			{
				String value0 = getValueAt(y, x);
				String[] values = value0.split(" "); //$NON-NLS-1$
				Arrays.sort(values);
				StringBuffer s = new StringBuffer();
				for (String v : values)
				{
					s.append(v);
					s.append(' ');
				}
				String value = s.toString();
				if (map.containsKey(value))
				{
					map.put(value, map.get(value) + 1);
				}
				else
				{
					map.put(value, 1);
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<html>\n"); //$NON-NLS-1$
		for (Entry<String, Integer> entry : map.entrySet())
		{
			sb.append(entry.getKey() + ": " + entry.getValue() + "<br>\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("<br></html>"); //$NON-NLS-1$
		return sb.toString();
	}

}
