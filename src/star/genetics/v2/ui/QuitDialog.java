package star.genetics.v2.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import star.genetics.Messages;
import utils.UIHelpers;

public class QuitDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private String url = Messages.getString("QuitDialog.0"); //$NON-NLS-1$
	private int ret = JOptionPane.CANCEL_OPTION;

	public QuitDialog(Frame parent, String title)
	{
		super(parent, title, true);
		final QuitDialog self = this;
		final JButton quit = new JButton(Messages.getString("QuitDialog.1")); //$NON-NLS-1$
		quit.setMnemonic('Q');
		quit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				self.ret = JOptionPane.YES_OPTION;
				self.setVisible(false);
				self.dispose();
			}
		});
		final JButton cancel = new JButton(Messages.getString("QuitDialog.2")); //$NON-NLS-1$
		cancel.setMnemonic('C');
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				self.ret = JOptionPane.CANCEL_OPTION;
				self.setVisible(false);
				self.dispose();
			}
		});
		final JLabel thankyou = new JLabel(Messages.getString("QuitDialog.3")); //$NON-NLS-1$
		thankyou.setVisible(false);
		final JButton feedback = new JButton(Messages.getString("QuitDialog.4")); //$NON-NLS-1$
		feedback.setMnemonic('F');
		feedback.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				thankyou.setVisible(true);
				feedback.setVisible(false);
				UIHelpers.openWebBrowser(url);
			}
		});

		setLayout(new MigLayout("", "[][]")); //$NON-NLS-1$ //$NON-NLS-2$
		add(new JLabel(Messages.getString("QuitDialog.7")), "wrap,span 2, center"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel textArea = new JLabel(Messages.getString("QuitDialog.9")); //$NON-NLS-1$
		add(textArea, "span 2, growy 3, wrap "); //$NON-NLS-1$
		add(feedback, "wrap, span 2, center"); //$NON-NLS-1$
		add(thankyou, "wrap, span 2, center"); //$NON-NLS-1$
		add(new JLabel(" "), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		add(new JLabel(title), "wrap, span 2, center"); //$NON-NLS-1$
		add(quit, "center, sg sg1, tag ok"); //$NON-NLS-1$
		add(cancel, "center, sg sg1, tag cancel"); //$NON-NLS-1$
		pack();
		UIHelpers.centerOnParent(this);
		setVisible(true);
	}

	public int get()
	{
		return ret;
	}

}
