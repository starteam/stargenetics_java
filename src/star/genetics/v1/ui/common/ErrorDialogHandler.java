package star.genetics.v1.ui.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.event.Event;
import star.events.common.DistributionExceptionRaiser;
import star.genetics.Messages;
import star.genetics.Version;
import star.genetics.events.ErrorDialogRaiser;
import star.ui.feedback.FeedbackDialog;
import utils.UIHelpers;

/**
 * Displays Error Dialog raised from other components
 * 
 * @author ceraj
 */
@SignalComponent()
public class ErrorDialogHandler extends ErrorDialogHandler_generated
{

	private static final long serialVersionUID = 1L;
	private String EXCEPTION_OCCURED = Messages.getString("ErrorDialogHandler.0"); //$NON-NLS-1$
	private String YES = Messages.getString("ErrorDialogHandler.1"); //$NON-NLS-1$
	private String NO = Messages.getString("ErrorDialogHandler.2"); //$NON-NLS-1$

	public ErrorDialogHandler()
	{
		Event.setDistributionExceptionEventListener(this);
	}

	@Override
	@Handles(raises = {})
	void openErrorDialog(ErrorDialogRaiser r)
	{
		try
		{
			Exception errorMessage = r.getErrorMessage();
			if (errorMessage != null && errorMessage.getCause() != null && errorMessage.getCause().getCause() != null && errorMessage.getCause().getCause() instanceof FileNotFoundException)
			{
				JOptionPane.showMessageDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)), new JLabel(errorMessage.getLocalizedMessage()));
			}
			else if (errorMessage != null && errorMessage.getCause() != null && errorMessage.getCause().getCause() != null && errorMessage.getCause().getCause() instanceof IOException)
			{
				String sb = MessageFormat.format(Messages.getString("ErrorDialogHandler.3"), r.getErrorMessage().getLocalizedMessage()); //$NON-NLS-1$
				String[] options = new String[] { YES, NO };
				int ret = JOptionPane.showOptionDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)), new JLabel(sb.toString()), EXCEPTION_OCCURED, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (ret == JOptionPane.YES_OPTION)
				{
					FeedbackDialog dialog = new FeedbackDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)), star.genetics.Version.getProject(), star.genetics.Version.getBuildDate().toString());
					dialog.reportBug(r.getErrorMessage().getCause(), String.valueOf(Version.getBuildDate()));
					dialog.pack();
					dialog.setVisible(true);

				}
			}
			else if (errorMessage != null && errorMessage.getCause() == null)
			{
				JOptionPane.showMessageDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)), new JLabel(errorMessage.getLocalizedMessage()));
			}
			else
			{
				String sb = MessageFormat.format(Messages.getString("ErrorDialogHandler.4"), r.getErrorMessage().getLocalizedMessage()); //$NON-NLS-1$
				String[] options = new String[] { YES, NO };
				int ret = JOptionPane.showOptionDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)), new JLabel(sb.toString()), EXCEPTION_OCCURED, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (ret == JOptionPane.YES_OPTION)
				{
					FeedbackDialog dialog = new FeedbackDialog(UIHelpers.getFrame(UIHelpers.getComponent(this)), star.genetics.Version.getProject(), star.genetics.Version.getBuildDate().toString());
					dialog.reportBug(r.getErrorMessage().getCause(), String.valueOf(Version.getBuildDate()));
					dialog.pack();
					dialog.setVisible(true);

				}
			}

		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	@Override
	@Handles(raises = { ErrorDialogRaiser.class })
	void openErrorDialogOnDistributionException(DistributionExceptionRaiser r)
	{
		errorMessage = new Exception(r.getException());
		raise_ErrorDialogEvent();
	}

	transient Exception errorMessage = null;

	@Override
	public Exception getErrorMessage()
	{
		// TODO Auto-generated method stub
		return errorMessage;
	}

}
