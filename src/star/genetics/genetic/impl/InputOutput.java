package star.genetics.genetic.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.events.ExportModelRaiser;
import star.genetics.events.LoadModelRaiser;
import star.genetics.events.OpenModelRaiser;
import star.genetics.events.SaveModelRaiser;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.CrateSet;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.genetic.model.Model;
import star.genetics.genetic.model.RuleSet;
import star.genetics.visualizers.Visualizer;

@SignalComponent(raises = { ErrorDialogRaiser.class })
@Properties({ @Property(name = "model", type = Model.class, getter = Property.PUBLIC), @Property(name = "modelName", type = String.class, getter = Property.PUBLIC), @Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC) })
public class InputOutput extends InputOutput_generated
{
	@Override
	@Handles(raises = { LoadModelRaiser.class })
	public void load(OpenModelRaiser r)
	{
		if (r.getModelFileName().endsWith(".sg1")) //$NON-NLS-1$
		{
			try
			{
				java.io.ObjectInputStream serializer = new java.io.ObjectInputStream(r.getOpenModelStream());
				setModel((Model) serializer.readObject());
				setModelName(r.getModelFileName());
				raise_LoadModelEvent();
			}
			catch (Exception ex)
			{
				String fileName = Messages.getString("InputOutput.1"); //$NON-NLS-1$
				try
				{
					if (r != null)
					{
						fileName = MessageFormat.format(Messages.getString("InputOutput.2"), r.getModelFileName(), r.getModelURL()); //$NON-NLS-1$
					}
				}
				catch (Exception exc)
				{

				}
				ex.printStackTrace();
				setErrorMessage(new RuntimeException(ex.getLocalizedMessage() + " " + fileName, ex)); //$NON-NLS-1$
				raise_ErrorDialogEvent();
			}
		}
	}

	@Override
	@Handles(raises = {})
	public void save(SaveModelRaiser r)
	{
		try
		{
			java.io.ObjectOutputStream serializer = new java.io.ObjectOutputStream(r.getSaveModelStream())
			{
				@Override
				public void write(byte[] buf) throws IOException
				{
					super.write(buf);
				}

				final protected void writeObjectOverride(Object obj) throws java.io.IOException
				{
					System.out.println("writing " + obj.getClass());
					super.writeObjectOverride(obj);
				};
			};
			serializer.writeObject(getModel());
			serializer.flush();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			setErrorMessage(new RuntimeException(ex.getLocalizedMessage(), ex));
			raise_ErrorDialogEvent();
		}
	}

	@Override
	@Handles(raises = {})
	public void setModel(LoadModelRaiser r)
	{
		setModel(r.getModel());
	}

	@Override
	@Handles(raises = {})
	public void export(ExportModelRaiser r)
	{
		try
		{
			ArrayList<String> headings = new ArrayList<String>();
			headings.add(Messages.getString("InputOutput.4")); //$NON-NLS-1$
			headings.add(Messages.getString("InputOutput.5")); //$NON-NLS-1$
			headings.add(Messages.getString("InputOutput.6")); //$NON-NLS-1$
			HSSFWorkbook wb = new HSSFWorkbook();

			exportCrates(headings, wb, true);
			// exportCreatures(headings, wb);
			exportCrates(headings, wb, false);
			wb.write(r.getModelStream());
			r.getModelStream().close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			setErrorMessage(new RuntimeException(ex.getLocalizedMessage(), ex));
			raise_ErrorDialogEvent();
		}

	}

	private void exportCrates(ArrayList<String> headings, HSSFWorkbook wb, boolean visible)
	{
		ArrayList<String> creatureHeadings = new ArrayList<String>(headings);
		creatureHeadings.add(Messages.getString("InputOutput.7")); //$NON-NLS-1$

		Model model = getModel();
		CrateSet crateSet = model.getCrateSet();
		CrateModel[] array = new CrateModel[crateSet.size()];
		int index = crateSet.size() - 1;
		for (CrateModel m : crateSet)
		{
			array[index--] = m;
		}
		HSSFSheet sheet = wb.createSheet(visible ? Messages.getString("InputOutput.8") : Messages.getString("InputOutput.9")); //$NON-NLS-1$ //$NON-NLS-2$
		short current = 0;

		ArrayList<Integer> headLines = new ArrayList<Integer>();
		for (CrateModel crate : array)
		{
			if (wb.getNumberOfSheets() > 100)
			{
				continue;
			}
			if (crate.isVisible() != visible)
			{
				continue;
			}
			if (crate.getParents().size() == 2)
			{

				makeText(wb, sheet, current, new String[] { visible ? Messages.getString("InputOutput.10") : Messages.getString("InputOutput.0"), crate.getName() + (visible ? Messages.getString("InputOutput.12") : Messages.getString("InputOutput.13")) }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				line(wb, sheet, current, (short) 2, HSSFColor.TAN.index, 2);
				current++;
				headLines.add(new Integer(current));
				current++;
				makeCreature(sheet, current++, crate.getParents().get(0), creatureHeadings, Messages.getString("InputOutput.14")); //$NON-NLS-1$
				makeCreature(sheet, current++, crate.getParents().get(1), creatureHeadings, "Parent"); //$NON-NLS-1$

				CreatureSet creatures = crate.getProgenies();
				Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
				Summary counter = new Summary();
				for (Creature c : creatures)
				{
					v.setName(c.getName());
					Map<String, String> p = new TreeMap<String, String>();
					p.putAll(c.getProperties());
					p.remove(GeneticModel.matings);
					v.setProperties(p, c.getSex());
					counter.add(v, c.getSex());
				}

				int total_count = 0;
				int total_female = 0;
				for (String key : counter)
				{
					Map<String, String> properties = counter.getProperties(key);
					properties.remove(GeneticModel.matings);
					int count = counter.getCount(key);
					int female = counter.getCountFemale(key);
					int male = count - female;

					makeCreature(sheet, current, properties, creatureHeadings, Messages.getString("InputOutput.16"), Sex.FEMALE, female); //$NON-NLS-1$
					line(wb, sheet, current, (short) 0, HSSFColor.WHITE.index, creatureHeadings.size());
					current++;
					makeCreature(sheet, current, properties, creatureHeadings, "Offspring", Sex.MALE, male); //$NON-NLS-1$
					line(wb, sheet, current, (short) 0, HSSFColor.WHITE.index, creatureHeadings.size());
					current++;
					total_count += count;
					total_female += female;

				}
				line(wb, sheet, (short) (current - 1), (short) 1, HSSFColor.WHITE.index, creatureHeadings.size());
				current++;
				current++;
			}

		}
		for (Integer i : headLines)
		{
			current = i.shortValue();
			makeHeading(wb, sheet, current, creatureHeadings);
			line(wb, sheet, current, (short) 1, HSSFColor.WHITE.index, creatureHeadings.size());
		}
	}

	void line(HSSFWorkbook wb, HSSFSheet sheet, short row, short borderWidth, short color, int size)
	{
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(borderWidth);
		style.setFillForegroundColor(color);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFRow r = sheet.getRow(row);
		for (int col = 0; col < size; col++)
		{
			HSSFCell c = r.getCell(col);
			if (c == null)
			{
				c = r.createCell(col);
				c.setCellValue(new HSSFRichTextString("")); //$NON-NLS-1$
			}
		}

		Iterator<?> iter2 = r.cellIterator();
		while (iter2.hasNext())
		{
			((HSSFCell) iter2.next()).setCellStyle(style);
		}
	}

	void makeText(HSSFWorkbook wb, HSSFSheet sheet, short row, String[] headings)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (String h : headings)
		{
			list.add(h);
		}
		makeHeading(wb, sheet, row, list);
	}

	void makeHeading(HSSFWorkbook wb, HSSFSheet sheet, short row, ArrayList<String> headings)
	{
		HSSFRow heading = sheet.createRow(row);
		for (int col = 0; col < headings.size(); col++)
		{
			heading.createCell(col).setCellValue(new HSSFRichTextString(headings.get(col)));
			HSSFCellStyle cellStyle = wb.createCellStyle();
			heading.getCell(col).setCellStyle(cellStyle);
		}
	}

	void makeCreature(HSSFSheet sheet, short row, Map<String, String> properties, ArrayList<String> headings, String kind, Sex sex, int count)
	{
		HSSFRow heading = sheet.createRow(row);
		int col = 0;
		heading.createCell(col++).setCellValue(new HSSFRichTextString(kind));
		heading.createCell(col++).setCellValue(new HSSFRichTextString("")); //$NON-NLS-1$
		heading.createCell(col++).setCellValue(new HSSFRichTextString(sex.toString()));
		heading.createCell(col++).setCellValue(count);
		for (Entry<String, String> entry : properties.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			int index = headings.indexOf(key);
			if (index == -1)
			{
				headings.add(key);
				index = headings.indexOf(key);
			}
			heading.createCell(index).setCellValue(new HSSFRichTextString(value));
		}
	}

	void makeCreature(HSSFSheet sheet, short row, Creature creature, ArrayList<String> headings, String kind)
	{
		HSSFRow heading = sheet.createRow(row);

		RuleSet rules = getModel().getRules();
		Visualizer visualizer = getModel().getVisualizerFactory().newVisualizerInstance();
		int col = 0;
		heading.createCell(col++).setCellValue(new HSSFRichTextString(kind));
		heading.createCell(col++).setCellValue(new HSSFRichTextString(creature.getName()));
		heading.createCell(col++).setCellValue(new HSSFRichTextString(creature.getSex().toString()));
		Map<String, String> properties = rules.getProperties(creature.getMakeup(), creature.getSex());
		visualizer.setProperties(properties, creature.getSex());
		Iterator<String> keys = visualizer.getTooltipProperties().keySet().iterator();
		while (keys.hasNext())
		{
			String key = keys.next();
			String value = properties.get(key);
			int index = headings.indexOf(key);
			if (index == -1)
			{
				headings.add(key);
				index = headings.indexOf(key);
			}
			heading.createCell(index).setCellValue(new HSSFRichTextString(value));
		}
	}

}
