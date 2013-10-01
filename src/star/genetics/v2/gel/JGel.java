package star.genetics.v2.gel;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import star.genetics.Messages;
import star.genetics.genetic.model.Allele;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.DiploidAlleles;
import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.GelPosition;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.Model;

public class JGel extends JComponent
{
	private static final long serialVersionUID = 1L;
	private Iterable<GelPosition> gp;
	private Iterable<Gel> displayedGels;
	private ArrayList<Entry<Sex, Iterable<GelPosition>>> parentPositions;
	JGel self = this;
	private Model model;

	Iterable<Allele> getCreatureAlleels(Creature c)
	{
		ArrayList<Allele> alleles = new ArrayList<Allele>();
		for (Entry<Gene, DiploidAlleles> e : c.getMakeup().entrySet())
		{
			Allele a1 = e.getValue().get(0);
			Allele a2 = e.getValue().get(1);
			if (a1 != null)
			{
				alleles.add(a1);
			}
			if (a2 != null)
			{
				alleles.add(a2);
			}
		}
		return alleles;
	}

	public JGel(Model m, Creature c)
	{
		this.model = m;
		this.gp = m.getGelRules().getGel(getCreatureAlleels(c));
		this.parentPositions = new ArrayList<Entry<Sex, Iterable<GelPosition>>>();
		for (Creature cc : c.getParents())
		{
			final Iterable<GelPosition> value = m.getGelRules().getGel(getCreatureAlleels(cc));
			final Sex sex = cc.getSex();
			this.parentPositions.add(new Entry<Creature.Sex, Iterable<GelPosition>>()
			{

				@Override
				public Iterable<GelPosition> setValue(Iterable<GelPosition> value)
				{
					return getValue();
				}

				@Override
				public Iterable<GelPosition> getValue()
				{
					return value;
				}

				@Override
				public Sex getKey()
				{
					// TODO Auto-generated method stub
					return sex;
				}
			});
		}
		// setToolTipText(getProperties());
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		update();
	}

	void update()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		removeAll();
		if (displayedGels != null)
		{
			JPanel pq = new JPanel();
			pq.setLayout(new BorderLayout());
			pq.add(BorderLayout.WEST, new JLabel("<html><body><b>" + Messages.getString("JGel.0") + "</b><br>" + getProperties(), JLabel.CENTER)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			add(pq);
			for (Gel g : displayedGels)
			{
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				p.setBorder(new EmptyBorder(15, 0, 0, 0));
				p.add(BorderLayout.WEST, new JLabel("<html><b>" + g.getName() + "</b></html>")); //$NON-NLS-1$ //$NON-NLS-2$
				add(p);
				add(new GelVisual(g, gp, parentPositions));
				add(new JLabel(" ")); //$NON-NLS-1$
			}
		}
	}

	public String getProperties()
	{
		String SUFFIX = "/"; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>"); //$NON-NLS-1$
		Map<String, StringBuffer> messages = new TreeMap<String, StringBuffer>();
		for (GelPosition g : gp)
		{
			String key = g.getGel().getName();
			if (!messages.containsKey(key))
			{
				messages.put(key, new StringBuffer());
			}
			Float[] pos = g.getPosition();
			if (pos.length != 1)
			{
				messages.get(key).append(Arrays.toString(pos) + SUFFIX);
			}
			else
			{
				messages.get(key).append(pos[0] + SUFFIX);
			}
		}
		HashSet<String> names = new HashSet<String>();
		if (displayedGels != null)
			for (Gel g : displayedGels)
			{
				names.add(g.getName());
			}
		for (Entry<String, StringBuffer> e : messages.entrySet())
		{
			String key = e.getKey();
			StringBuffer value = e.getValue();
			value.setLength(value.length() - SUFFIX.length());
			if (names.contains(key))
			{
				sb.append(MessageFormat.format(" {0}: {1} <br>", key, value)); //$NON-NLS-1$
			}
		}
		sb.append("</body></html>"); //$NON-NLS-1$
		return sb.toString();
	}

	public void updateDisplayedGels(Iterable<Gel> displayedGels2)
	{
		this.displayedGels = displayedGels2;
	}
}