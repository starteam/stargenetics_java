package star.genetics.v2.ui.fly.strains;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
import star.genetics.Messages;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.organism.OrganismList;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledStatusContainer;

public class Strains extends TitledStatusContainer
{
	private static final long serialVersionUID = 1L;

	public Strains(Model model)
	{
		JPanel title = new JPanel();
		title.setOpaque(false);
		JLabel titleLabel = new TitleLabel(Messages.getString("Strains.0")); //$NON-NLS-1$
		titleLabel.setForeground(Color.white);
		title.add(titleLabel);

		JPanel body = new JPanel();
		body.setLayout(new MigLayout("fill")); //$NON-NLS-1$
		OrganismList list = new OrganismList(model);
		body.add(new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), "growy,center"); //$NON-NLS-1$

		JPanel status = new JPanel();
		status.setOpaque(false);
		status.add(new Add());
		status.add(new Set());
		status.add(new Remove());

		init(title, body, status, null);
	}
}
