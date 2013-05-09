package star.genetics.v1.ui.crate.groupby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.genetics.Messages;
import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.model.CrateModel;
import star.genetics.v2.gel.JGel;
import utils.UIHelpers;

@SignalComponent(extend = JPanel.class)
class GelFilter extends GelFilter_generated
{
	private static final long serialVersionUID = 1L;

	ArrayList<JGel> gels;
	private Model model;
	private GelFilter self = this;

	private CrateModel crateModel;

	private GroupByGel main;

	public GelFilter(Model model, ArrayList<JGel> gels, CrateModel crateModel, GroupByGel main)
	{
		this.model = model;
		this.gels = gels;
		this.crateModel = crateModel;
		this.main = main;
		update();
	}

	void initUI()
	{
		removeAll();
		setLayout(new BorderLayout());
		JPanel p = new JPanel();
		p.setBackground(Color.white);
		add(p, BorderLayout.WEST);
		p.setLayout(new FlowLayout());
		p.add(new JLabel(Messages.getString("GelFilter.0"))); //$NON-NLS-1$
		if (getDisplayGels() == null)
		{
			p.add(new JLabel(Messages.getString("GelFilter.1"))); //$NON-NLS-1$
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			for (Gel g : getDisplayGels())
			{
				sb.append(g.getName() + " "); //$NON-NLS-1$
			}
			if (sb.length() != 0)
			{
				p.add(new JLabel(sb.toString()));
			}
			else
			{
				p.add(new JLabel(Messages.getString("GelFilter.3"))); //$NON-NLS-1$
			}

		}
		JButton editSelection = new JButton(Messages.getString("GelFilter.4")); //$NON-NLS-1$
		p.add(editSelection);
		editSelection.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				final JDialog dialog = new JDialog((JFrame) UIHelpers.getFrame(self), true);
				UIHelpers.centerOnParent(dialog);
				dialog.setTitle(Messages.getString("GelFilter.5")); //$NON-NLS-1$
				dialog.setLayout(new BorderLayout());
				JPanel selectionPanel = new JPanel();
				selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));
				final Map<Gel, JCheckBox> map = new LinkedHashMap<Gel, JCheckBox>();
				for (Gel g : model.getGelRules().getAllGelNames())
				{
					JCheckBox cb = new JCheckBox(g.getName());
					map.put(g, cb);
					selectionPanel.add(cb);
				}
				if (getDisplayGels() != null)
				{
					for (Gel g : getDisplayGels())
					{
						JCheckBox cb = map.get(g);
						if (cb != null)
						{
							cb.setSelected(true);
						}
					}
				}
				dialog.add(selectionPanel, BorderLayout.CENTER);
				JButton apply = new JButton(Messages.getString("GelFilter.6")); //$NON-NLS-1$
				JButton displayAll = new JButton(Messages.getString("GelFilter.7")); //$NON-NLS-1$
				JButton cancel = new JButton(Messages.getString("GelFilter.8")); //$NON-NLS-1$
				JPanel buttons = new JPanel();
				buttons.add(apply);
				buttons.add(displayAll);
				buttons.add(cancel);
				dialog.add(buttons, BorderLayout.SOUTH);

				cancel.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						dialog.setVisible(false);
						dialog.dispose();
					}
				});
				displayAll.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						setDisplayGels(null);
						dialog.setVisible(false);
						dialog.dispose();
						update_SwingUtilitiesInvokeLater();
						main.updateTree_SwingUtilitiesInvokeLater();
					}
				});

				apply.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						ArrayList<Gel> gels = new ArrayList<Gel>();
						for (Entry<Gel, JCheckBox> entry : map.entrySet())
						{
							if (entry.getValue().isSelected())
							{
								gels.add(entry.getKey());
							}
						}
						setDisplayGels(gels);
						dialog.setVisible(false);
						dialog.dispose();
						update_SwingUtilitiesInvokeLater();
						main.updateTree_SwingUtilitiesInvokeLater();
					}

				});
				dialog.pack();
				dialog.setVisible(true);

			}
		});
	}

	@Wrap(type = Wrap.Types.SwingUtilitiesInvokeLater)
	void update()
	{
		initUI();
		for (JGel gel : gels)
		{
			gel.updateDisplayedGels(getDisplayGels());
		}
	}

	@SuppressWarnings("unchecked")
	Iterable<Gel> getDisplayGels()
	{
		return (Iterable<Gel>) crateModel.getMetadata().get(GelFilter.class);
	}

	private void setDisplayGels(ArrayList<Gel> gels)
	{
		crateModel.getMetadata().put(GelFilter.class, gels);
	}

}