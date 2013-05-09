package star.genetics.v2.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import star.genetics.Messages;
import star.genetics.Version;
import utils.UIHelpers;

public class AboutBox extends JPanel
{

	private static final long serialVersionUID = 1L;

	private String getBuild()
	{
		return Version.getBuildDate().toString();
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel(Messages.getString("AboutBox.0"))); //$NON-NLS-1$
		add(new JLabel(Messages.getString("AboutBox.1"))); //$NON-NLS-1$
		add(new JLabel(Messages.getString("AboutBox.2") + getBuild())); //$NON-NLS-1$
		add(new JLabel(Messages.getString("AboutBox.3"))); //$NON-NLS-1$
		add(new JLabel(Messages.getString("AboutBox.4") + System.getProperty("java.version") + Messages.getString("AboutBox.6") + System.getProperty("java.vendor"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		add(new JLabel(Messages.getString("AboutBox.8") + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		// add(new JLabel("Java 3D renderer: " + (System.getProperty("j3d.rend") != null ? System.getProperty("j3d.rend") : "default")));
		UIHelpers.track(Messages.getString("AboutBox.14")); //$NON-NLS-1$
	}
}
