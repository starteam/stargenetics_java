/**
 * 
 */
package star.genetics.xls;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

enum Genome
{
	ALLELES("Alleles")// //$NON-NLS-1$
	, GENE("Gene") // //$NON-NLS-1$
	, LOCATION("Gene Location") // //$NON-NLS-1$
	, CHROMOSOME("Chromosome") // //$NON-NLS-1$
	;

	String name;

	private Genome(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	static boolean hasRequired(Collection<String> set)
	{
		boolean ret = true;
		for (Genome g : Genome.values())
		{
			ret &= set.contains(g.toString());
		}
		return ret;
	}

	static Genome parse(String str)
	{
		Genome ret = null;
		for (Genome g : Genome.values())
		{
			if (g.toString().equalsIgnoreCase(str))
			{
				ret = g;
				break;
			}
		}
		return ret;
	}

	static Map<Integer, Genome> mapRow(HSSFRow row)
	{
		Map<Integer, Genome> map = new HashMap<Integer, Genome>();
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
		{
			HSSFCell cell = row.getCell(i);
			if (cell != null)
			{
				Genome g = Genome.parse(cell.toString());
				if (g != null)
				{
					map.put(i, g);
				}
			}
		}
		return map;
	}
}