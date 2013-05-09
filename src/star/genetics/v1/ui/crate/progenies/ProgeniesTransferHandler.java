package star.genetics.v1.ui.crate.progenies;

import java.awt.datatransfer.DataFlavor;

import javax.swing.JComponent;

import star.genetics.v1.ui.common.CreatureTransferHandler;

public class ProgeniesTransferHandler extends CreatureTransferHandler
{
	private static final long serialVersionUID = 1L;
	ProgeniesList list;

	public ProgeniesTransferHandler(ProgeniesList list)
	{
		super("selectedCreature", list.getGeneticModel().getVisualizerFactory()); //$NON-NLS-1$
		this.list = list;
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		super.canImport(comp, transferFlavors);
		return false;
	}

}
