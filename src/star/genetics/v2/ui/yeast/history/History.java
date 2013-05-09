package star.genetics.v2.ui.yeast.history;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.WeakHashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.annotations.Wrap.Types;
import star.genetic.yeast.ExperimentType;
import star.genetics.Messages;
import star.genetics.events.LoadModelRaiser;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.CrateNewCrateRaiser;
import star.genetics.v1.ui.events.OrganismSetAsParentRaiser;
import star.genetics.v2.ui.common.CrateInterface;
import star.genetics.v2.ui.common.DiscardExperiment;
import star.genetics.v2.ui.common.RenameExperiment;
import star.genetics.v2.ui.common.TitleLabel;
import star.genetics.v2.ui.common.TitledContainer;
import star.genetics.v2.ui.fly.CrateProvider;
import star.genetics.v2.ui.yeast.common.MatingPanelController;
import star.genetics.v2.ui.yeast.common.ReplicaPanelController;
import star.genetics.v2.yeast.events.NewMatingExperimentRaiser;
import star.genetics.v2.yeast.events.NewReplicationExperimentRaiser;
import utils.FileUtils;
import utils.UIHelpers;

@SignalComponent(extend = TitledContainer.class, excludeExternal = { OrganismSetAsParentRaiser.class })
public class History extends History_generated implements CrateProvider
{
	private static final long serialVersionUID = 1L;
	private Model model;
	private JPanel body;
	private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
	private CrateInterface currentCrate;
	private JComboBox box;
	private boolean first = true;
	private boolean current;
	private RenameExperiment rename;
	private DiscardExperiment discard;

	private WeakHashMap<JFrame, CrateModel> dialog_map = new WeakHashMap<JFrame, CrateModel>();
	private JButton new_win;

	@Override
	public void removeNotify()
	{

		for (java.util.Map.Entry<JFrame, CrateModel> entry : dialog_map.entrySet())
		{
			JFrame d = entry.getKey();
			CrateModel c = entry.getValue();
			if (d != null && c != null)
			{
				d.dispose();
			}
		}
		super.removeNotify();
	}

	public History(Model model)
	{
		this.model = model;
		body = new JPanel();
		body.setLayout(new BorderLayout());
		showGettingStarted();
		init(getTitle(), body, null);
		// reset discard on model load
		DiscardExperiment.alwaysDiscard = false;

	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		updateModel();
	}

	private Component getTitle()
	{
		JPanel title = new JPanel();
		title.setOpaque(false);
		title.add(new TitleLabel(Messages.getString("History.0"))); //$NON-NLS-1$
		box = new JComboBox(comboBoxModel);
		box.setEditable(false);
		title.add(box);
		this.rename = new RenameExperiment(this);
		title.add(rename);
		this.discard = new DiscardExperiment(this, false);
		title.add(discard);
		box.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				updateCrate();
			}
		});
		this.new_win = new JButton(Messages.getString("History.1")); //$NON-NLS-1$
		new_win.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				UIHelpers.track("DetachExperiment/Yeast"); //$NON-NLS-1$
				final CrateModel crateModel = (CrateModel) comboBoxModel.getSelectedItem();
				final JFrame d = new JFrame();
				d.setAlwaysOnTop(false);
				d.setTitle(crateModel.getName());
				Container body = d.getContentPane();

				ExperimentType type = (ExperimentType) crateModel.getMetadata().get(ExperimentType.class);
				if (ExperimentType.MATING.equals(type))
				{
					MatingPanelController crate = new MatingPanelController(model, crateModel, false);
					body.removeAll();
					body.add(crate);
				}
				else if (ExperimentType.REPLICA.equals(type))
				{
					ReplicaPanelController crate = new ReplicaPanelController(model, crateModel, false);
					body.removeAll();
					body.add(crate);
				}
				else
				{
					body.removeAll();
					body.add(new JLabel(MessageFormat.format(Messages.getString("History.3"), type))); //$NON-NLS-1$
				}
				d.pack();
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				int frameHeight = UIHelpers.getFrame(box).getHeight();
				if (d.getHeight() > frameHeight)
				{
					d.setSize(d.getWidth(), frameHeight);
				}
				if (d.getHeight() < 400)
				{
					d.setSize(d.getWidth(), 400);
				}
				if (d.getWidth() < 600)
				{
					d.setSize(600, d.getHeight());
				}
				d.setVisible(true);
				UIHelpers.centerOnParent(d, UIHelpers.getFrame(box));

				d.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosed(WindowEvent e)
					{
						dialog_map.remove(d);
						super.windowClosed(e);
					}
				});
				dialog_map.put(d, crateModel);
			}
		});
		title.add(new_win);
		updateModel(false);
		return title;
	}

	private void updateModel(boolean current)
	{
		this.current = current;
		updateModel();
	}

	private void updateModel()
	{
		java.util.Iterator<CrateModel> iter = model.getCrateSet().iterator();
		Object item = comboBoxModel.getSelectedItem();

		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		while (iter.hasNext())
		{

			CrateModel crateModel = iter.next();
			if (crateModel.isVisible())
			{
				if (current == false && crateModel.equals(model.getCrateSet().current()) && crateModel.getProgenies().size() == 0)
				{
					continue;
				}
				comboBoxModel.addElement(crateModel);
				item = crateModel;
			}
		}
		comboBoxModel.setSelectedItem(item);
		box.setModel(comboBoxModel);
		this.comboBoxModel = comboBoxModel;
		boolean empty = comboBoxModel.getSize() == 0;
		if (empty)
		{
			box.setSelectedItem(null);
		}
		else
		{
			box.setSelectedIndex(0);
			// box.setSelectedItem(item);
			// if (first)
			// {
			// setExpanded(true);
			// first = false;
			// }
		}
		rename.setEnabled(!empty);
		discard.setEnabled(!empty);
		new_win.setEnabled(!empty);

		for (java.util.Map.Entry<JFrame, CrateModel> entry : dialog_map.entrySet())
		{
			JFrame d = entry.getKey();
			CrateModel c = entry.getValue();
			if (d != null && c != null)
			{
				d.setTitle(c.getName());
				d.repaint();
			}
		}

	}

	@Override
	@Handles(raises = {})
	void updateCrates(LoadModelRaiser r)
	{
		updateModel(false);
	}

	@Override
	@Handles(raises = {})
	void updateCrates(CrateNewCrateRaiser r)
	{
		updateModel(true);
	}

	@Override
	@Handles(raises = {})
	void updateCrates(NewMatingExperimentRaiser r)
	{
		// updateModel(false);
	}

	@Override
	@Handles(raises = {})
	void updateCrates(NewReplicationExperimentRaiser r)
	{
		// updateModel(false);
	}

	private void showGettingStarted()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		String text = ""; //$NON-NLS-1$
		try
		{
			text = new String(FileUtils.getStreamToByteArray(this.getClass().getClassLoader().getResourceAsStream(Messages.getString("History.5")))); //$NON-NLS-1$
			JScrollPane sp = new JScrollPane(new JLabel(text));
			p.add(sp);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		body.removeAll();
		body.add(p);
	}

	public CrateInterface getCrate()
	{
		return currentCrate;
	}

	public void updateCrateProvier()
	{
		updateModel();
		updateCrate();
	}

	@Override
	@Wrap(type = Types.SwingUtilitiesInvokeLater)
	void updateLayout()
	{
		UIHelpers.invalidate(this);
		UIHelpers.layout(this);
	}

	transient CrateModel currentCrateModel = null;

	protected void updateCrate()
	{
		CrateModel crateModel = (CrateModel) comboBoxModel.getSelectedItem();
		if (crateModel != null)
		{
			if (crateModel == currentCrateModel)
			{
				// we are already displaying it
				return;
			}
			ExperimentType type = (ExperimentType) crateModel.getMetadata().get(ExperimentType.class);
			if (ExperimentType.MATING.equals(type))
			{
				MatingPanelController crate = new MatingPanelController(model, crateModel, false);
				currentCrate = crate;
				body.removeAll();
				body.add(crate);
				updateLayout();
				currentCrateModel = crateModel;
			}
			else if (ExperimentType.REPLICA.equals(type))
			{
				ReplicaPanelController crate = new ReplicaPanelController(model, crateModel, false);
				currentCrate = crate;
				body.removeAll();
				body.add(crate);
				updateLayout();
				currentCrateModel = crateModel;
			}
			else
			{
				body.removeAll();
				body.add(new JLabel(MessageFormat.format(Messages.getString("History.6"), type))); //$NON-NLS-1$
				updateLayout();
				currentCrateModel = null;
			}
		}
		else
		{
			showGettingStarted();
			currentCrateModel = null;
		}
	}
}
