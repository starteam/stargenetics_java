import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XLSTest
{

	public static void main(String[] args)
	{
		try
		{
			HSSFWorkbook wb = new HSSFWorkbook();
			for (int i = 0; i < 10000; i++)
			{
				HSSFSheet sheet = wb.createSheet("s " + i);
				for (int row = 0; row < 2; row++)
				{
					HSSFRow heading = sheet.createRow(row);
					for (int col = 0; col < 2; col++)
					{
						heading.createCell(col).setCellValue(new HSSFRichTextString("s " + i + " " + col + " " + row));
					}
				}
			}
			wb.write(new FileOutputStream("Q:\\test2.xls"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
