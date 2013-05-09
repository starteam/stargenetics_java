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
import star.genetics.v2.ui.fly.CrateProvider;
import utils.UIHelpers;

@SignalComponent(extend = Button.class, raises = { CrateNewCrateRaiser.class })
public class DiscardExperiment extends DiscardExperiment_generated
{
	private static final long serialVersionUID = 1L;
	private final CrateProvider provider;
	private final boolean andCreateNewOne;

	public DiscardExperiment(CrateProvider provider, boolean andCreateNewOne)
	{
		this.provider = provider;
		this.andCreateNewOne = andCreateNewOne;
		updateEnabled();
	}

	@Override
	protected boolean isEnabledOnInit()
	{
		return provider.getCrate() != null;
	}

	@Override
	protected Icon getButtonIcon()
	{
		return null;
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_D;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("DiscardExperiment.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return andCreateNewOne ? Messages.getString("DiscardExperiment.1") : Messages.getString("DiscardExperiment.2"); //$NON-NLS-1$ //$NON-NLS-2$
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
			setTitle(MessageFormat.format(Messages.getString("DiscardExperiment.3"), provider.getCrate().getCrateName())); //$NON-NLS-1$
			Container container = getContentPane();
			container.setLayout(new MigLayout());
			JLabel l = new JLabel(MessageFormat.format(Messages.getString("DiscardExperiment.4"), provider.getCrate().getCrateName())); //$NON-NLS-1$
			container.add(l, "wrap"); //$NON-NLS-1$
			cb = new JCheckBox(Messages.getString("DiscardExperiment.6")); //$NON-NLS-1$
			container.add(cb, "wrap"); //$NON-NLS-1$
			b1 = new JButton(Messages.getString("DiscardExperiment.8")); //$NON-NLS-1$
			b1.setFocusPainted(true);
			JButton b2 = new JButton(Messages.getString("DiscardExperiment.9")); //$NON-NLS-1$
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
		CrateInterface crate = provider.getCrate();
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
		// int ret = JOptionPane.showConfirmDialog(this, "Are you sure you want to discard current generation?", "Discard current generation?",
		// JOptionPane.OK_CANCEL_OPTION);
		if (ret == JOptionPane.OK_OPTION)
		{
			crate.getModel().setVisible(false);
			if (andCreateNewOne)
			{
				raise_CrateNewCrateEvent();
			}
			provider.updateCrateProvier();
			updateEnabled();
		}
	}

	void updateEnabled()
	{
		setEnabled(provider.getCrate() != null);
	}

	@Handles(raises = {})
	void handleLoadModel(CrateNewCrateRaiser r)
	{
		updateEnabled();
	}

}
