package star.genetics.v2.ui.yeast.properties;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.v2.ui.common.CommonUI;
import star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser;
import utils.OS;

@SignalComponent(extend = JPanel.class, raises = { CreaturePropertiesChangedRaiser.class })
public class PropertiesPanel extends PropertiesPanel_generated
{
	private static final long serialVersionUID = 1L;
	private Component visualization;
	private Creature creature;
	private String note;

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public void setCreature(Creature c)
	{
		this.creature = c;
	}

	public void setComponent(Component c)
	{
		visualization = c;
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		Container c = this;
		c.setLayout(new MigLayout("wrap 3,ax leading, insets 0px,fill,novisualpadding,gapx 0", "[50]8[50:60:160]2[60:60:60]")); //$NON-NLS-1$ //$NON-NLS-2$
		String str = getName();
		final JTextArea creatureName = new JTextArea(str);
		creatureName.setEditable(false);
		creatureName.setToolTipText(getName());
		if (OS.isMacOSX())
		{
			creatureName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CommonUI.get().getTitleBackground(), 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		}
		creatureName.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String name = (String) JOptionPane.showInputDialog(creatureName, Messages.getString("PropertiesPanel.22"), Messages.getString("PropertiesPanel.23"), JOptionPane.QUESTION_MESSAGE, null, null, creature.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				if (name != null && name.length() != 0)
				{
					creature.setName(name);
					visualization.setName(name);
					creatureName.setText(name);
					creatureName.setToolTipText(name);
					raise_CreaturePropertiesChangedEvent(CreaturePropertiesChangedRaiser.Kind.NAME);
				}

			}
		});
		c.add(new JLabel(Messages.getString("PropertiesPanel.24"))); //$NON-NLS-1$
		c.add(creatureName);
		c.add(visualization != null ? visualization : new JPanel(), "wrap"); //$NON-NLS-1$

		final JTextArea lnote = new JTextArea(5, 12);
		lnote.setText(getNote());
		lnote.setWrapStyleWord(false);
		lnote.setLineWrap(true);
		lnote.getDocument().addDocumentListener(new DocumentListener()
		{

			public void removeUpdate(DocumentEvent e)
			{
				update();
			}

			public void insertUpdate(DocumentEvent e)
			{
				update();
			}

			public void changedUpdate(DocumentEvent e)
			{
				update();
			}

			void update()
			{
				String set = lnote.getText();
				setNote(set);
				creature.setNote(set);
				raise_CreaturePropertiesChangedEvent(CreaturePropertiesChangedRaiser.Kind.NOTE);
			}
		});
		if (OS.isMacOSX())
		{
			creatureName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CommonUI.get().getTitleBackground(), 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		}
		;
		c.add(new JLabel(Messages.getString("PropertiesPanel.6"))); //$NON-NLS-1$
		c.add(lnote);
	}

	CreaturePropertiesChangedRaiser.Kind kind = null;

	void raise_CreaturePropertiesChangedEvent(CreaturePropertiesChangedRaiser.Kind kind)
	{
		this.kind = kind;
		raise_CreaturePropertiesChangedEvent();
	}

	@Override
	public Kind getKind()
	{
		return kind;
	}
}