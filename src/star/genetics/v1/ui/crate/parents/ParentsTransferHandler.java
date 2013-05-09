package star.genetics.v1.ui.crate.parents;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;

import javax.swing.JComponent;

import star.genetics.genetic.model.Creature;
import star.genetics.v1.ui.common.CreatureTransferHandler;
import star.genetics.visualizers.Visualizer;
import utils.UIHelpers;

public class ParentsTransferHandler extends CreatureTransferHandler
{
	private static final long serialVersionUID = 1L;
	private ParentsListInterface parentList;
	private Creature myCreature;

	public ParentsTransferHandler(ParentsListInterface self)
	{
		super("selectedCreature", self.getGeneticModel().getVisualizerFactory()); //$NON-NLS-1$
		this.parentList = self;
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		return super.canImport(comp, transferFlavors);
	}

	@Override
	public boolean importData(JComponent comp, Transferable t)
	{
		super.importData(comp, t);
		boolean ret = false;
		Creature c = getCreature(t);
		if (c != null)
		{
			ret = true;
			parentList.setCreature(c);
		}
		return ret;
	}

	@Override
	public void exportAsDrag(JComponent comp, InputEvent e, int action)
	{
		myCreature = parentList.getSelectedCreature();
		Visualizer v = parentList.getGeneticModel().getVisualizerFactory().newVisualizerInstance();
		v.setName(myCreature.getName());
		v.setProperties(myCreature.getProperties(), myCreature.getSex());
		UIHelpers.setVisual(v.getJComponent());
		super.exportAsDrag(comp, e, action);
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		myCreature = null;
		super.exportDone(source, data, action);
	}
}