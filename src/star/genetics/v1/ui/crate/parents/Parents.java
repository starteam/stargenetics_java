package star.genetics.v1.ui.crate.parents;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Genome.SexType;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.ui.common.CommonUI;
import star.genetics.v2.ui.common.TitleLabel;

@SignalComponent(extend = JPanel.class)
@Properties({ @Property(name = "model", type = CrateModel.class) })
public class Parents extends Parents_generated
{
	private static final long serialVersionUID = 1L;

	private boolean matingEnabled = true;

	public Parents(CrateModel model, boolean matingEnabled)
	{
		setModel(model);
		this.matingEnabled = matingEnabled;
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		setLayout(new BorderLayout());
		JLabel l = new TitleLabel(Messages.getString("Parents.0")); //$NON-NLS-1$
		l.setForeground(java.awt.Color.black);
		l.setBackground(CommonUI.get().getTitleBackground());
		add(BorderLayout.NORTH, l);
		add(BorderLayout.CENTER, getListComponent());
	}

	private Component getListComponent()
	{
		if (SexType.UNISEX.equals(getModel().getSexType()))
		{
			return new ParentsListUnisex(getModel(), matingEnabled);
		}
		else
		{
			return new ParentsList(getModel(), matingEnabled);
		}
	}
}
