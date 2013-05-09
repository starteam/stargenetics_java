package star.genetics.v1.ui.common;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JComponent;

import star.genetics.genetic.model.Creature;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;
import utils.PropertyUtils;
import utils.UIHelpers;

public class CreatureTransferHandler extends javax.swing.TransferHandler
{
	private static final long serialVersionUID = 1L;

	public static int creatureExporting = 0;
	private String propertyName;
	private VisualizerFactory factory;

	public CreatureTransferHandler(String property, VisualizerFactory factory)
	{
		super(property);
		this.propertyName = property;
		this.factory = factory;
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		return null != hasFlavor(transferFlavors, Creature.class);
	}

	@Override
	public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException
	{
		// disabled
	}

	protected DataFlavor hasFlavor(DataFlavor[] transferFlavor, Class<? extends Object> c)
	{
		DataFlavor ret = null;
		if (transferFlavor != null && transferFlavor.length > 0)
		{
			for (int i = 0; i < transferFlavor.length; i++)
			{
				DataFlavor f = transferFlavor[i];
				if (f != null && c.isAssignableFrom(f.getRepresentationClass()))
				{
					ret = f;
					break;
				}
			}

		}
		return ret;
	}

	protected Creature getCreature(Transferable t)
	{
		try
		{
			return (Creature) t.getTransferData(hasFlavor(t.getTransferDataFlavors(), Creature.class));
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	int count = 0;
	Thread update;

	@Override
	public void exportAsDrag(final JComponent comp, InputEvent e, int action)
	{
		count++;
		creatureExporting++;
		if (count == 1)
		{
			Object value = PropertyUtils.getValue(comp, propertyName);
			if (value != null && value instanceof Creature)
			{
				Creature c = (Creature) value;
				Visualizer v = factory.newVisualizerInstance();
				v.setProperties(c.getProperties(), c.getSex());
				v.setName(c.getName());
				UIHelpers.setVisual(v.getJComponent());
			}
			update = new Thread(new Runnable()
			{
				public void run()
				{
					UIHelpers.displayVisual2(true, comp);
					try
					{
						while (!Thread.interrupted())
						{
							UIHelpers.displayVisual2();
							Thread.sleep(2);
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					UIHelpers.displayVisual2(false, comp);
				}
			});
		}
		super.exportAsDrag(comp, e, action);
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		count--;
		creatureExporting--;
		if (creatureExporting < 0)
		{
			creatureExporting = 0;
		}
		if (count == 0)
		{
			UIHelpers.setVisual(null);
			update.interrupt();
			try
			{
				update.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			update = null;

		}
		super.exportDone(source, data, action);
	}

	volatile Thread t = null;

	@Override
	public Icon getVisualRepresentation(Transferable t)
	{
		return super.getVisualRepresentation(t);
	}

}
