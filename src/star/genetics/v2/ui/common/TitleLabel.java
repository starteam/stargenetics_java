package star.genetics.v2.ui.common;

import javax.swing.JLabel;

import star.genetics.Messages;
import utils.UIHelpers;

public class TitleLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	private static final String TITLELABEL = "TitleLabel"; //$NON-NLS-1$

	public TitleLabel(String text)
	{
		super(text);
		setForeground(java.awt.Color.white);
		setFont(CommonUI.get().getTitleFont(getFont()));
		putClientProperty(TITLELABEL, text);
	}

	private String getTitleLabel()
	{
		Object text = getClientProperty(TITLELABEL);
		if (text == null)
		{
			text = getText();
			putClientProperty(TITLELABEL, text);
		}
		return text != null ? String.valueOf(text) : ""; //$NON-NLS-1$
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
		if (UIHelpers.getFrame(this).getWidth() < 900)
		{
			String text = getTitleLabel();
			if (text != null && text.indexOf(' ') > 0)
			{
				setText(text.substring(0, text.indexOf(' ')));
			}
		}
		else
		{
			setText(getTitleLabel());
		}

	}
}
