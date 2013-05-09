package star.genetics.v2.ui.fly.experiment;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.CrateMateRaiser;
import star.genetics.v1.ui.events.CrateParentsRaiser;
import star.genetics.v1.ui.events.CrateProgeniesRaiser;
import star.genetics.v1.ui.events.ListItemSelectedRaiser;
import star.genetics.v2.ui.common.Button;
import star.genetics.v2.yeast.events.StorableExperimentRaiser;
import utils.UIHelpers;

@SignalComponent(extend = Button.class, raises = { CrateMateRaiser.class, ListItemSelectedRaiser.class, StorableExperimentRaiser.class })
public class Mate extends Mate_generated
{
	private static final long serialVersionUID = 1L;
	private int count;

	@Override
	protected String getButtonText()
	{
		return Messages.getString("Mate.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_M;
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

	class PossibleValue
	{

		public PossibleValue(int i)
		{
			count = i;
		}

		int count;

		@Override
		public String toString()
		{
			return "" + count; //$NON-NLS-1$
		}
	}

	@Override
	protected void onAction()
	{
		if (count == 0)
		{
			String text = Messages.getString("Mate.2"); //$NON-NLS-1$
			setText(text);
			setToolTipText(text);
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
			int MAX = values.length;
			PossibleValue[] possibleValues = new PossibleValue[MAX];

			for (int i = 0; i < MAX; i++)
			{
				possibleValues[i] = new PossibleValue(values[i]);
			}
			PossibleValue selectedValue = (PossibleValue) JOptionPane.showInputDialog(this, Messages.getString("Mate.3"), Messages.getString("Mate.4"), JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]); //$NON-NLS-1$ //$NON-NLS-2$
			if (selectedValue == null)
			{
				return;
			}
			raise_ListItemSelectedEvent();
			ccount = selectedValue.count;
			final JDialog d = new JDialog(UIHelpers.getFrame(this), Messages.getString("Mate.5"), true); //$NON-NLS-1$
			d.add(new JLabel(Messages.getString("Mate.6"))); //$NON-NLS-1$
			d.pack();
			UIHelpers.centerOnParent(d);
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					raise_CrateMateEvent();
					SwingUtilities.invokeLater(new Runnable()
					{

						@Override
						public void run()
						{
							raise_StorableExperimentEvent();
							d.setVisible(false);
							d.dispose();
						}
					});
				}
			}).start();
			if (d.isDisplayable())
			{
				d.setVisible(true);
			}
			// raise_CrateMateEvent();
			// raise_StorableExperimentEvent();
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

	@Override
	public void setProgress(int percentage)
	{
		// TODO Auto-generated method stub

	}
}
