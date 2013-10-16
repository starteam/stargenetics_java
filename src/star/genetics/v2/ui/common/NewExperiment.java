package star.genetics.v2.ui.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v2.ui.common.DiscardExperiment.DiscardExperimentConfirmation;
import star.genetics.v2.ui.fly.CrateProvider;
import star.genetics.v2.yeast.events.StorableExperimentRaiser;
import utils.UIHelpers;

@SignalComponent(extend = Button.class, raises = { CrateNewCrateRaiser.class })
public class NewExperiment extends NewExperiment_generated
{
	private static final long serialVersionUID = 1L;
	private final String name;
	private CrateProvider crate;
	boolean isStorable = false;

	public NewExperiment(CrateProvider crate)
	{
		this(crate, null);
	}

	public NewExperiment(CrateProvider crate, String name)
	{
		this.crate = crate;
		this.name = name != null ? name : Messages.getString("NewExperiment.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonText()
	{
		return name;
	}

	@Override
	protected String getButtonTooltipText()
	{
		return Messages.getString("NewExperiment.1"); //$NON-NLS-1$
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_N;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return null;
	}


	public static boolean alwaysDiscard = false;

	class DiscardExperimentConfirmation extends JDialog
	{
		private static final long serialVersionUID = 1L;

		JButton b1;
		JCheckBox cb;
		public boolean isDiscard = false;

		public DiscardExperimentConfirmation(Component c)
		{
			super(UIHelpers.getFrame(c), true);
			setTitle("New Experiment"); //$NON-NLS-1$
			Container container = getContentPane();
			container.setLayout(new MigLayout());
			JLabel l = new JLabel( "<html>When you click on the \"New Experiment\" button, your current experiment will move down to the \"Saved Experiment\" window. \n" + 
					"<br><br>"+"Within the \"Saved Experiment\" window you will not be able to add more matings to your experiment, but you will be able"+
					"<br>"+" to use any of the resulting progeny for future matings.\n" + 
					"</html>"); //$NON-NLS-1$
			container.add(l, "wrap"); //$NON-NLS-1$
			cb = new JCheckBox(Messages.getString("DiscardExperiment.6")); //$NON-NLS-1$
			container.add(cb, "wrap"); //$NON-NLS-1$
			b1 = new JButton("Yes"); //$NON-NLS-1$
			b1.setFocusPainted(true);
			JButton b2 = new JButton("No"); //$NON-NLS-1$
			b1.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					alwaysDiscard = cb.isSelected();
					isDiscard = true;
					dispose();
				}
			});
			b2.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					isDiscard = false;
					dispose();
				}
			});

			container.add(b1, "tag ok"); //$NON-NLS-1$
			container.add(b2, "tag cancel"); //$NON-NLS-1$
		}

		@Override
		public void addNotify()
		{
			super.addNotify();
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run()
				{
					cb.transferFocus();
				}
			});
		}

	}

	@Override
	protected void onAction()
	{
		int ret = JOptionPane.CANCEL_OPTION;

		if (!alwaysDiscard)
		{
			DiscardExperimentConfirmation d = new DiscardExperimentConfirmation(this);
			UIHelpers.centerOnParent(d);
			d.pack();
			d.setVisible(true);
			if (d.isDiscard)
			{
				ret = JOptionPane.OK_OPTION;
			}
		}
		else
		{
			ret = JOptionPane.OK_OPTION;
		}
		if( ret == JOptionPane.OK_OPTION )
		{
		onActionOK();
		}
	}

	protected void onActionOK()
	{
		if (isStorable)
		{
			raise_CrateNewCrateEvent();
		}
		else
		{
			this.crate.getCrate().getModel().setVisible(false);
			raise_CrateNewCrateEvent();
		}
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return true;
	}

	@Override
	@Handles(raises = {})
	protected void handle(StorableExperimentRaiser r)
	{
		isStorable = true;
		setEnabled(true);
	}
}
