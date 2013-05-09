package star.genetics.v2.ui.fly.properties;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.v2.ui.common.CommonUI;
import star.genetics.v2.yeast.events.CreaturePropertiesChangedRaiser;
import utils.OS;

@SignalComponent(extend = JPanel.class, raises = { CreaturePropertiesChangedRaiser.class })
public class PropertiesPanel extends PropertiesPanel_generated implements Scrollable
{
	private static final long serialVersionUID = 1L;
	private Map<String, String> properties;
	private Sex sex;
	private Component visualization;
	private Component gel;
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

	public void setMap(Map<String, String> tooltipProperties)
	{
		this.properties = tooltipProperties;
	}

	public void setSex(Sex sex)
	{
		this.sex = sex;

	}

	private Sex getSex()
	{
		return sex;
	}

	public void setComponent(Component c)
	{
		visualization = c;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return super.getPreferredSize();
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		Container c = this;
		MigLayout ml = new MigLayout("wrap 3,ax leading, insets 0px,fill,novisualpadding,gapx 0", "[]4[]4", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		c.setLayout(ml);
		String str = getName();
		final JTextArea lname = new JTextArea(str);
		lname.setEditable(false);
		lname.setToolTipText(getName());
		if (OS.isMacOSX())
		{
			lname.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CommonUI.get().getTitleBackground(), 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		}
		;
		lname.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String name = (String) JOptionPane.showInputDialog(lname, Messages.getString("PropertiesPanel.3"), Messages.getString("PropertiesPanel.4"), JOptionPane.QUESTION_MESSAGE, null, null, creature.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				if (name != null && name.length() != 0)
				{
					creature.setName(name);
					visualization.setName(name);
					lname.setText(name);
					lname.setToolTipText(name);
					raise_CreaturePropertiesChangedEvent(CreaturePropertiesChangedRaiser.Kind.NAME);
				}

			}
		});
		c.add(new JLabel(Messages.getString("PropertiesPanel.5"))); //$NON-NLS-1$
		c.add(lname, "wrap"); //$NON-NLS-1$

		if (getSex() != null)
		{
			c.add(new JLabel(Messages.getString("PropertiesPanel.7"))); //$NON-NLS-1$
			c.add(new JLabel(String.valueOf(getSex())), "wrap"); //$NON-NLS-1$
		}

		// TreeMap<String, String> properties = new TreeMap<String, String>(this.properties);
		for (Entry<String, String> entry : properties.entrySet())
		{
			c.add(new JLabel(entry.getKey()));
			c.add(new JLabel(entry.getValue()), "wrap"); //$NON-NLS-1$
		}

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
			lname.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CommonUI.get().getTitleBackground(), 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		}
		;
		c.add(new JLabel(Messages.getString("PropertiesPanel.10"))); //$NON-NLS-1$
		c.add(new JScrollPane(lnote), "wrap"); //$NON-NLS-1$
		c.add(new JLabel(Messages.getString("PropertiesPanel.12"))); //$NON-NLS-1$
		c.add(visualization, "wrap"); //$NON-NLS-1$

		if (gel != null)
		{
			c.add(gel, "wrap, span 2 "); //$NON-NLS-1$
		}

	}

	public void setGel(Component gel)
	{
		this.gel = gel;
	}

	public Component getGel()
	{
		return gel;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return new Dimension(175, 250);
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		return 20;
	}

	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		// TODO Auto-generated method stub
		return 20;
	}

	Kind kind;

	private void raise_CreaturePropertiesChangedEvent(Kind kind)
	{
		this.kind = kind;
	}

	@Override
	public Kind getKind()
	{
		// TODO Auto-generated method stub
		return kind;
	};

}