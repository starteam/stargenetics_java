package star.genetics.v1.ui.punnett;

import java.awt.BorderLayout;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import star.genetics.Messages;
import utils.UIHelpers;

public class PunnettSquareDialog extends JDialog implements Serializable
{
	private static final long serialVersionUID = 1L;

	public PunnettSquareDialog(JComponent owner)
	{
		super(UIHelpers.getFrame(owner), Messages.getString("PunnettSquareDialog.0"), false); //$NON-NLS-1$
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BorderLayout());
		JTabbedPane tabs = new JTabbedPane();
		tabs.add(Messages.getString("PunnettSquareDialog.1"), new PunnettSquare()); //$NON-NLS-1$
		tabs.add(Messages.getString("PunnettSquareDialog.2"), new PunnettSquare2()); //$NON-NLS-1$
		tabs.add(Messages.getString("PunnettSquareDialog.3"), new PunnettSquare3()); //$NON-NLS-1$
		// tabs.add("Dihybrid sex-linked", new PunnettSquare4());

		add(tabs);
		// pack();
		setSize(400, 500);
		UIHelpers.centerOnParent(this);
	}

}
