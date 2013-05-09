package star.genetics.v2.ui.common;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.ui.feedback.FeedbackDialog;
import utils.Icons;
import utils.UIHelpers;

@SignalComponent(extend = Button.class)
public class Feedback extends Feedback_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Icon getButtonIcon()
	{
		return Icons.DIALOG.getIcon(16);
	}

	@Override
	protected int getButtonMnemoic()
	{
		return KeyEvent.VK_R;
	}

	@Override
	protected String getButtonText()
	{
		return Messages.getString("Feedback.0"); //$NON-NLS-1$
	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected void onAction()
	{
		FeedbackDialog dialog = new FeedbackDialog(UIHelpers.getFrame(this), star.genetics.Version.getProject(), star.genetics.Version.getBuildDate().toString());
		dialog.pack();
		dialog.setVisible(true);
	}

}
