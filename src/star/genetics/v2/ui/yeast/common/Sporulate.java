package star.genetics.v2.ui.yeast.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.common.AddMoreMatingsDialog;
import star.genetics.v1.ui.common.MatingDialogPossibleValue;
import star.genetics.v1.ui.common.Validator;
import star.genetics.v1.ui.events.CrateMateRaiser;
import star.genetics.v1.ui.events.CrateParentsRaiser;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.events.ListItemSelectedRaiser;
import star.genetics.v2.ui.common.Button;
import star.genetics.v2.yeast.events.StorableExperimentRaiser;

@SignalComponent(extend = Button.class, raises = { CrateMateRaiser.class, ListItemSelectedRaiser.class, StorableExperimentRaiser.class })
public class Sporulate extends Sporulate_generated
{
	private static final long serialVersionUID = 1L;
	private int count;
	private int matingCount = 1;

	public Sporulate(int matingsCount)
	{
		this.matingCount = matingsCount;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("Sporulate.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return Messages.getString("Sporulate.1"); //$NON-NLS-1$
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_S;
	}

	@Override
	public Icon getButtonIcon()
	{
		return null;
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return false;
	}

	@Override
	@Handles(raises = {})
	void validParents(CrateParentsRaiser r)
	{
		setEnabled(true);
		count = 0;
	}

	@Override
	@Handles(raises = {})
	void invalidCrate(CrateProgeniesRaiser r)
	{
		count++;
	}

	@Override
	protected void onAction()
	{
		if (count == 0)
		{
			String text = Messages.getString("Sporulate.2"); //$NON-NLS-1$
			setText(text);
			setToolTipText(text!=null?text:"");
			raise_ListItemSelectedEvent();
			ccount = 1;
			raise_CrateMateEvent();
			raise_StorableExperimentEvent();
		}
		else
		{
			int[] values;
			if (count == 1)
			{
				values = new int[] { 1, 2, 4, 9 };
			}
			else
			// count > 1
			{
				values = new int[] { 1, 2, 5, 10, 20, 50 };
			}

			MatingDialogPossibleValue[] possibleValues = new MatingDialogPossibleValue[values.length];
			for (int i = 0; i < values.length; i++)
			{
				possibleValues[i] = new MatingDialogPossibleValue((values[i]), (values[i]) * matingCount);
			}
			AddMoreMatingsDialog dd = new AddMoreMatingsDialog(this, Messages.getString("Sporulate.3"), Messages.getString("Sporulate.4"), possibleValues, new Validator() //$NON-NLS-1$ //$NON-NLS-2$
			        {
				        @Override
				        public boolean isValid(String input)
				        {
					        try
					        {
						        return Math.round(Float.parseFloat(input)) % matingCount == 0;
					        }
					        catch (Exception ex)
					        {
						        return false;
					        }
				        }

				        @Override
				        public String suggestedValid(String input)
				        {
					        try
					        {
						        int matings = Math.round(Float.parseFloat(input) / matingCount);
						        if (matings <= 0)
						        {
							        matings = 1;
						        }
						        return "" + matings * matingCount; //$NON-NLS-1$
					        }
					        catch (Exception ex)
					        {
						        return "" + matingCount; //$NON-NLS-1$
					        }
				        }
			        });
			MatingDialogPossibleValue selectedValue = (MatingDialogPossibleValue) JOptionPane.showInputDialog(this, Messages.getString("Sporulate.7"), Messages.getString("Sporulate.8"), JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]); //$NON-NLS-1$ //$NON-NLS-2$
			if (selectedValue == null)
			{
				return;
			}
			raise_ListItemSelectedEvent();
			ccount = selectedValue.count;
			/*
			 * final JDialog d = new JDialog(UIHelpers.getFrame(this), "Please wait...", true); d.setLayout(new MigLayout()); JLabel l = new
			 * JLabel("<html><h1>Please wait...</h1></html>"); l.setBorder(BorderFactory.createEmptyBorder(4, 12, 8, 12)); d.add(l, "wrap"); progressBar = new
			 * JProgressBar(0, 100); d.add(progressBar, "wrap"); progressBar.setIndeterminate(true); progressBar.setBorder(BorderFactory.createEmptyBorder(4,
			 * 12, 8, 12)); d.pack(); UIHelpers.centerOnParent(d); new Thread(new Runnable() {
			 * @Override public void run() { while (!d.isVisible()) { Thread.yield(); } raise_CrateMateEvent(); Thread.yield(); SwingUtilities.invokeLater(new
			 * Runnable() {
			 * @Override public void run() { Thread.yield(); raise_StorableExperimentEvent(); d.setVisible(false); d.dispose(); } }); } }).start(); if
			 * (d.isDisplayable()) { d.setVisible(true); }
			 */
			raise_CrateMateEvent();
			raise_StorableExperimentEvent();
		}
	}

	int ccount = 1;

	public int getCount()
	{
		return ccount;
	}

	public Tuple[] getActions()
	{
		return new Tuple[0];
	}

	public JList getJList()
	{
		return null;
	}

	JProgressBar progressBar;

	@Override
	public void setProgress(int percentage)
	{
		if (progressBar != null)
		{
			progressBar.setIndeterminate(false);
			progressBar.setValue(percentage);
		}
	}
}