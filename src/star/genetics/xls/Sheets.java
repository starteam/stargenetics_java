/**
 * 
 */
package star.genetics.xls;

import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

enum Sheets
{
	PROPERTIES("Mating engine") // //$NON-NLS-1$
	, GENOME("Genes & Alleles") // //$NON-NLS-1$
	, PHENOTYPE("Genes interaction") // //$NON-NLS-1$
	, ORGANISM("Organisms for mating") // //$NON-NLS-1$
	, GEL("Gel", false); //$NON-NLS-1$

	final String name;
	final boolean required;

	private Sheets(String name)
	{
		this(name, true);
	}

	private Sheets(String name, boolean required)
	{
		this.name = name;
		this.required = required;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public static void checkAllSheets(HSSFWorkbook wb) throws ParseException
	{
		boolean ret = true;
		for (Sheets s : Sheets.values())
		{
			if (s.required)
			{
				ret &= wb.getSheet(s.toString()) != null;
			}
		}
		if (!ret)
		{
			throw new ParseException(MessageFormat.format("XLS has to have following sheets: {0}", Arrays.toString(Sheets.values()))); //$NON-NLS-1$
		}
	}
}