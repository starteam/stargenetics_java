package star.genetics.v1.ui.punnett;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import star.genetics.Messages;

public class PunnettSquareModel3 extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	private SexText columns = null;
	private SexText rows = null;

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
			return "<html>" + rows.html(rowIndex - 1) + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (rowIndex == 0)
		{
			return "<html>" + columns.html(columnIndex - 1) + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		String x = rows.html(rowIndex - 1);
		String y = columns.html(columnIndex - 1);
		return "<html> " + (x.compareTo(y) < 0 ? x + " " + y : y + " " + x) + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		// return "<html> " + rows.html(rowIndex - 1) + " " + columns.html(columnIndex - 1) + "</html>";
	}

	public String getValueAt2(int rowIndex, int columnIndex)
	{
		if (columnIndex == 0 && rowIndex == 0)
		{
			return ""; //$NON-NLS-1$
		}
		if (columnIndex == 0)
		{
			return "" + rows.html(rowIndex - 1); //$NON-NLS-1$
		}
		if (rowIndex == 0)
		{
			return "" + columns.html(columnIndex - 1); //$NON-NLS-1$
		}
		String x = rows.charAt(rowIndex - 1);
		String y = columns.charAt(columnIndex - 1);
		return x.compareTo(y) < 0 ? x + " " + y : y + " " + x; //$NON-NLS-1$ //$NON-NLS-2$

		// return rows.charAt(rowIndex - 1) + " " + columns.charAt(columnIndex - 1);
	}

	public void update(SexText cols, SexText rows)
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
				String value0 = getValueAt2(y, x);
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
		sb.append("<html>\n"); //$NON-NLS-1$
		for (Entry<String, Integer> entry : map.entrySet())
		{
			String[] str = entry.getKey().split(" "); //$NON-NLS-1$
			for (String s : str)
			{
				sb.append(SexHTML.valueOf(s.trim()).str);
				// sb.append( s.trim() );
				sb.append(' ');
			}
			sb.append(": " + entry.getValue() + "<br>\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("<br></html>"); //$NON-NLS-1$
		return sb.toString();
	}

}
