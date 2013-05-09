package star.genetics.v1.ui.punnett;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import star.genetics.Messages;

public class PunnettSquareModel2 extends AbstractTableModel
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
			return str[0] + " " + str[1]; //$NON-NLS-1$
		}
		if (rowIndex == 0)
		{
			String[] str = getCol(columnIndex - 1);
			return str[0] + " " + str[1]; //$NON-NLS-1$
		}
		String[] str1 = getRow(rowIndex - 1);
		String[] str2 = getCol(columnIndex - 1);

		String[] x = new String[] { str1[0], str2[0] };
		String[] y = new String[] { str1[1], str2[1] };
		Arrays.sort(x);
		Arrays.sort(y);
		return x[0] + " " + x[1] + " " + y[0] + " " + y[1]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
				String value0 = getValueAt(y, x);
				String[] values = value0.split(" "); //$NON-NLS-1$
				Arrays.sort(values, new Comparator<String>()
				{
					@Override
					public int compare(String a, String b)
					{
						int ret = a.toLowerCase().compareTo(b.toLowerCase());
						if (ret == 0)
						{
							ret = a.compareTo(b);
						}
						return ret;
					}
				});
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
		if (rows == null || columns == null)
		{
			sb.append(Messages.getString("PunnettSquareModel2.0")); //$NON-NLS-1$
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
