package star.genetics.v1.ui.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import star.genetics.Messages;
import utils.UIHelpers;

public class AddMoreMatingsDialog extends JDialog
{

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private Validator validator;
	final JComboBox cb;
	final JLabel status;
	final JButton ok;
	private String selectedValue;

	public String getSelectedValue()
	{
		return selectedValue;
	}

	public AddMoreMatingsDialog(Component parent, String message, String title, MatingDialogPossibleValue[] list, final Validator validator)
	{
		super(UIHelpers.getFrame(parent));
		this.validator = validator;
		setTitle(title);
		setModal(true);
		Container c = getContentPane();
		c.setLayout(new MigLayout());
		JLabel l = new JLabel(message);
		c.add(l);
		cb = new JComboBox(list);
		cb.setEditable(true);
		c.add(cb, "wrap"); //$NON-NLS-1$
		status = new JLabel("This is a status"); //$NON-NLS-1$
		status.setForeground(Color.red);
		c.add(status, "span 2, wrap"); //$NON-NLS-1$
		ok = new JButton(Messages.getString("AddMoreMatingsDialog.3")); //$NON-NLS-1$
		c.add(ok, "tag yes"); //$NON-NLS-1$
		JButton cancel = new JButton(Messages.getString("AddMoreMatingsDialog.5")); //$NON-NLS-1$
		c.add(cancel, "tag no"); //$NON-NLS-1$
		pack();

		cancel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
				selectedValue = null;
			}
		});

		ok.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
				String value = cb.getEditor().getItem().toString();
				selectedValue = validator.suggestedValid(value);
			}
		});

		cb.getEditor().getEditorComponent().addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				formValidate();
			}
		});

		cb.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				formValidate();
			}
		});

		formValidate();
	}

	void formValidate()
	{
		String value = cb.getEditor().getItem().toString();
		if (!validator.isValid(value))
		{
			status.setText(MessageFormat.format(Messages.getString("AddMoreMatingsDialog.7"), value, validator.suggestedValid(value))); //$NON-NLS-1$
			ok.setEnabled(false);
		}
		else
		{
			status.setText(" "); //$NON-NLS-1$
			ok.setEnabled(true);
		}
	}
}
