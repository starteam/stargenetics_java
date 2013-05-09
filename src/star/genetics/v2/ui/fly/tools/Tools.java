package star.genetics.v2.ui.fly.tools;

import java.awt.GridLayout;

import javax.swing.JPanel;

import star.genetics.v2.ui.common.Feedback;
import star.genetics.v2.ui.common.PunnettSqaure;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.Messages;

public class Tools extends TitledContainer
{
	private static final long serialVersionUID = 1L;

	public Tools()
	{
		JPanel title = new JPanel();
		title.setOpaque(false);
		title.add(new TitleLabel(Messages.getString("Tools.0"))); //$NON-NLS-1$
		JPanel body = new JPanel();
		body.setLayout(new GridLayout(2, 1));
		body.add(new PunnettSqaure());
		body.add(new Feedback());
		init(title, body, true);
	}
}
