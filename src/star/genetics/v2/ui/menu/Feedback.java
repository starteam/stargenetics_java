package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.ui.feedback.FeedbackDialog;
import utils.UIHelpers;

@SignalComponent(extend = MenuItem.class)
public class Feedback extends Feedback_generated
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getMenuName()
	{
		return Messages.getString("Feedback.0"); //$NON-NLS-1$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return null;
	}

	@Override
	protected void onAction()
	{
		FeedbackDialog dialog = new FeedbackDialog(UIHelpers.getFrame(this), star.genetics.Version.getProject(), star.genetics.Version.getBuildDate().toString());
		dialog.pack();
		dialog.setVisible(true);
	}

}
