package star.genetics.v2.ui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

public abstract class Button extends JButton implements ActionListener
{
	private static final long serialVersionUID = 1L;

	@Override
	public void addNotify()
	{
		super.addNotify();
		addActionListener(this);
		setText(getButtonText());
		setMnemonic(getButtonMnemoic());
		setIcon(getButtonIcon());
		setEnabled(isEnabledOnInit());
		setToolTipText(getButtonTooltipText());
	}

	public void actionPerformed(ActionEvent e)
	{
		onAction();
	}

	protected abstract void onAction();

	protected abstract String getButtonText();

	protected abstract String getButtonTooltipText();

	protected abstract int getButtonMnemoic();

	protected abstract Icon getButtonIcon();

	protected boolean isEnabledOnInit()
	{
		return true;
	}
}
