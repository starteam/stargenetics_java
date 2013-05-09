package star.genetics.v1.ui.crate.progenies;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.v1.ui.model.CrateModel;

@SignalComponent(extend = JPanel.class)
@Properties({ @Property(name = "model", type = CrateModel.class) })
public class Progenies extends Progenies_generated
{
	private static final long serialVersionUID = 1L;
	private boolean active = true;
	private boolean sorted;

	public Progenies(CrateModel model, boolean active, boolean sorted)
	{
		setModel(model);
		this.active = active;
		this.sorted = sorted;
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JScrollPane(new ProgeniesList(getModel(), active, sorted), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
	}
}
