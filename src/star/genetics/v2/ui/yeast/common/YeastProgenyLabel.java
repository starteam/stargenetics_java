package star.genetics.v2.ui.yeast.common;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import star.annotations.Handles;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.impl.CreatureImpl;
import star.genetics.genetic.impl.CreatureSetImpl;
import star.genetics.genetic.impl.DiploidAllelesImpl;
import star.genetics.genetic.impl.GeneticMakeupImpl;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Gene;
import star.genetics.genetic.model.GeneticMakeup;
import star.genetics.genetic.model.Model;
import star.genetics.v1.ui.events.OrganismSelectedRaiser;
import star.genetics.v1.ui.events.ProgenySelectedRaiser;
import star.genetics.v2.ui.yeast.YeastUI;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.Yeast;

@SignalComponent(extend = JComponent.class, raises = { OrganismSelectedRaiser.class, ProgenySelectedRaiser.class })
@Properties({ @Property(name = "creature", type = Creature.class, getter = Property.PUBLIC), @Property(name = "lawn", type = Creature.class, getter = Property.PUBLIC), @Property(name = "property", type = String.class) })
public class YeastProgenyLabel extends YeastProgenyLabel_generated
{
	private static final long serialVersionUID = 1L;
	private boolean lastSelection;
	private final static Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
	private final static Border lineBorder = BorderFactory.createEtchedBorder();
	private final static Border compoundBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), lineBorder);

	private final Dimension step;
	private Model model;
	private boolean isReplica = false;
	private boolean selectable = true;
	private transient Image renderedImage;

	public final String LAWN_GROWS = "lawn_grows"; //$NON-NLS-1$
	public final String GROWS = "grows"; //$NON-NLS-1$

	public void setSelectable(boolean selectable)
	{
		this.selectable = selectable;
	}

	private void updateImage()
	{
		setSize(step);
		renderedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
	}

	private Image getRenderedImage()
	{
		if (renderedImage == null)
		{
			updateImage();
		}
		return renderedImage;
	}

	private void init(Creature c, Creature lawn, String propertyName, Model model)
	{
		setCreature(c);
		setLawn(lawn);
		setProperty(propertyName);
		this.model = model;

		Helper.makeExportable(this, "creature", TransferHandler.COPY, model.getVisualizerFactory()); //$NON-NLS-1$
		updateImage();
		update(false);
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (selectable)
				{
					select();
				}
			}
		});
		lastSelection = true;
		setSelection(null);
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		update(true);
	}

	public YeastProgenyLabel(boolean isReplica, Creature c, Creature lawn, String propertyName, Model model, Dimension step)
	{
		this.isReplica = isReplica;
		this.step = step;
		init(c, lawn, propertyName, model);
	}

	public Creature getSelectedCreature()
	{
		return getCreature();
	}

	private void select()
	{
		raise_OrganismSelectedEvent();
		raise_ProgenySelectedEvent();
	}

	private void setSelection(Creature c)
	{
		boolean currentSelection = (c == getCreature());
		if (lastSelection != currentSelection)
		{
			if (c == getCreature())
			{
				lastSelection = true;
				this.setOpaque(true);
				this.setBorder(lineBorder);
			}
			else
			{
				lastSelection = false;
				this.setOpaque(false);
				this.setBackground(null);
				if ("name".equals(getProperty())) //$NON-NLS-1$
				{
					this.setBorder(compoundBorder);
				}
				else
				{
					this.setBorder(emptyBorder);
				}
			}
		}
	}

	@Override
	@Handles(raises = {})
	void handle(OrganismSelectedRaiser r)
	{
		if (selectable)
		{
			setSelection(r.getSelectedCreature());
		}
	}

	@Override
	public Creature getCreature()
	{
		return super.getCreature();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		RenderingHints rh = ((Graphics2D) g).getRenderingHints();
		rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		rh.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		((Graphics2D) g).setRenderingHints(rh);
		Rectangle clip = g.getClipBounds();
		if (g.getClipBounds().getHeight() == getHeight() && g.getClipBounds().getWidth() == getWidth())
		{
			clip.height += 5;
			clip.width += 5;
			g.setClip(clip);
		}
		Image img = getRenderedImage();
		int w = getWidth();
		int h = getHeight();
		int iw = img.getWidth(null);
		int ih = img.getHeight(null);

		int posX = Math.max(0, (w - iw) / 2);
		int posY = Math.max(-1, (h - ih) / 2);
		// g.drawImage(img, 0, -1, null);
		g.drawImage(img, posX, posY, null);
	}

	private Creature getDiploid(Creature a, Creature b)
	{
		if (a.getSex() != b.getSex())
		{
			Creature c;

			GeneticMakeup ma = a.getMakeup();
			GeneticMakeup mb = b.getMakeup();
			GeneticMakeup mc = new GeneticMakeupImpl();
			for (Gene g : a.getGenome().getGenes())
			{
				mc.put(g, new DiploidAllelesImpl(ma.get(g).get(0), mb.get(g).get(0)));
			}

			Map<String, String> properties = model.getRules().getProperties(mc, null);
			CreatureSet parents = new CreatureSetImpl();
			parents.add(a);
			parents.add(b);
			c = new CreatureImpl("", a.getGenome(), null, mc, 0, properties, parents); //$NON-NLS-1$
			return c;
		}
		else
		{
			return null;
		}
	}

	private void update(boolean render)
	{
		putClientProperty(LAWN_GROWS, false);
		putClientProperty(GROWS, 0f);
		float grows = 0;
		setToolTipText(""); //$NON-NLS-1$
		ArrayList<String> tooltip = new ArrayList<String>();
		if (getCreature() != null)
		{
			if (!isReplica)
			{
				Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
				HashMap<String, String> additional = new HashMap<String, String>();
				additional.put(Yeast.NOTEXT, "true"); //$NON-NLS-1$
				Helper.setVisualizerFromCreature(v, getCreature(), additional);
				if (render)
				{
					JComponent c = v.getJComponent();
					c.setSize(c.getPreferredSize());
					Graphics gr = getRenderedImage().getGraphics();
					gr.setColor(YeastUI.getMediaColor());
					gr.fillRect(0, 0, c.getWidth(), c.getHeight());
					gr.translate((getWidth() - c.getWidth()) / 2 - 1, (getHeight() - c.getHeight()) / 2 - 1);
					c.printAll(gr);
					gr.dispose();
				}
				tooltip.add(v.getTooltipProperties().toString());
				setToolTipText(getToolTipText() + "\n" + v.getTooltipProperties().toString()); //$NON-NLS-1$
			}
			else
			{
				boolean execute = true;
				Graphics gr = null;
				if (render)
				{
					gr = getRenderedImage().getGraphics();
					gr.setColor(YeastUI.getMediaColor());
					gr.fillRect(0, 0, getRenderedImage().getWidth(null), getRenderedImage().getHeight(null));
				}
				if (getLawn() != null)
				{
					// TODO: Lawn!!!
					if (true)
					{
						Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
						HashMap<String, String> additional = new HashMap<String, String>();
						additional.put(Yeast.NOTEXT, "true"); //$NON-NLS-1$
						additional.put(Yeast.MEDIA, getProperty());
						additional.put(Yeast.REPLICA, "true"); //$NON-NLS-1$
						Helper.setVisualizerFromCreature(v, getLawn(), additional);
						try
						{
							String val = v.getTooltipProperties().get(Yeast.DENSITY);
							if (val != null && (Float.parseFloat(val) > 0.25))
							{
								String color = v.getTooltipProperties().get(Yeast.YeastProperties.COLOR.getKey());
								Yeast.YeastColor lawnColor = Yeast.YeastColor.WT;
								if (color != null)
								{
									lawnColor = Yeast.YeastColor.parse(color);
								}
								if (render)
								{
									gr.setClip(0, 0, 70, 70);
									gr.drawImage(Yeast.getLawn(lawnColor), 0, 0, null);
									this.setTransferHandler(null);
								}
								setToolTipText(Messages.getString("YeastProgenyLabel.10")); //$NON-NLS-1$
								putClientProperty(LAWN_GROWS, true);
							}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
					float diploidTransparency = 1;
					Creature haploid = null;
					if (execute)
					{
						Creature creature = getCreature();
						haploid = creature;
						Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
						HashMap<String, String> additional = new HashMap<String, String>();
						additional.put(Yeast.NOTEXT, "true"); //$NON-NLS-1$
						additional.put(Yeast.MEDIA, getProperty());
						additional.put(Yeast.REPLICA, "true"); //$NON-NLS-1$
						Helper.setVisualizerFromCreature(v, creature, additional);
						JComponent c = null;
						if (render)
						{
							c = v.getJComponent();
							c.setSize(c.getPreferredSize());
							gr.translate(getWidth() / 2 - c.getWidth() / 2, getHeight() / 2 - c.getHeight() / 2);
						}
						float haploidTransparency = 0;
						try
						{
							haploidTransparency = Float.parseFloat(v.getTooltipProperties().get(Yeast.DENSITY));
						}
						catch (Exception e)
						{
							// e.printStackTrace();
						}
						if (!(diploidTransparency < .2 && haploidTransparency < .2))
						{
							if (render)
							{
								c.printAll(gr);
							}
							tooltip.add(v.getTooltipProperties().toString());
							setToolTipText(getToolTipText() + "\n" + v.getTooltipProperties().toString()); //$NON-NLS-1$
							grows += haploidTransparency;
						}
						if (render)
						{
							gr.translate(-(getWidth() / 2 - c.getWidth() / 2), -(getHeight() / 2 - c.getHeight() / 2));
						}
					}
					if (execute)
					{
						Creature creature = getDiploid(getCreature(), getLawn());
						if (creature != null)
						{

							Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
							HashMap<String, String> additional = new HashMap<String, String>();
							additional.put(Yeast.NOTEXT, "true"); //$NON-NLS-1$
							additional.put(Yeast.MEDIA, getProperty());
							additional.put(Yeast.REPLICA, "true"); //$NON-NLS-1$
							additional.put(Yeast.DIPLOIDSIZE, haploid.getProperties().get(Yeast.getCompleteName(haploid.getProperties())));
							Helper.setVisualizerFromCreature(v, creature, additional);
							JComponent c = null;
							if (render)
							{
								c = v.getJComponent();
								c.setSize(c.getPreferredSize());
								gr.translate((getWidth() - c.getWidth()) / 2, (getHeight() - c.getHeight()) / 2);
								c.printAll(gr);
							}
							if ("1.0".equals(v.getTooltipProperties().get(Yeast.DENSITY))) //$NON-NLS-1$
							{
								if (tooltip.size() > 0)
								{
									tooltip.remove(tooltip.size() - 1);
								}
							}
							tooltip.add(v.getTooltipProperties().toString());
							setToolTipText(getToolTipText() + "\n" + v.getTooltipProperties().toString()); //$NON-NLS-1$
							try
							{
								diploidTransparency = Float.parseFloat(v.getTooltipProperties().get(Yeast.DENSITY));
							}
							catch (Exception e)
							{
								// e.printStackTrace();
							}
							if (render)
							{
								gr.translate(-((getWidth() - c.getWidth()) / 2), -((getHeight() - c.getHeight()) / 2));
							}
							grows += diploidTransparency;

						}
					}

				}
				else
				{
					if (execute)
					{

						Creature creature = getCreature();
						Visualizer v = model.getVisualizerFactory().newVisualizerInstance();
						HashMap<String, String> additional = new HashMap<String, String>();
						additional.put(Yeast.NOTEXT, "true"); //$NON-NLS-1$
						additional.put(Yeast.MEDIA, getProperty());
						additional.put(Yeast.REPLICA, "true"); //$NON-NLS-1$
						Helper.setVisualizerFromCreature(v, creature, additional);
						if (render)
						{
							JComponent c = v.getJComponent();
							c.setSize(c.getPreferredSize());
							gr.translate(getWidth() / 2 - c.getWidth() / 2, getHeight() / 2 - c.getHeight() / 2);
							c.printAll(gr);
						}
						tooltip.add(v.getTooltipProperties().toString());
						setToolTipText(getToolTipText() + "\n" + v.getTooltipProperties().toString()); //$NON-NLS-1$

						float haploidTransparency = 0;
						try
						{
							haploidTransparency = Float.parseFloat(v.getTooltipProperties().get(Yeast.DENSITY));
						}
						catch (Exception e)
						{
							// e.printStackTrace();
						}
						if (haploidTransparency < .2)
						{
							setTransferHandler(null);
						}

						grows += haploidTransparency;
					}

				}
				if (render)
				{
					gr.dispose();
				}
			}
		}
		else
		{
			System.out.println(this.getClass().getName() + Messages.getString("YeastProgenyLabel.21")); //$NON-NLS-1$
		}
		putClientProperty(GROWS, grows);
		repaint();
		if (((Boolean) getClientProperty(LAWN_GROWS)).booleanValue())
		{
			setToolTipText(Messages.getString("YeastProgenyLabel.22")); //$NON-NLS-1$
		}
		else
		{
			String[] str = tooltip.toArray(new String[0]);
			Arrays.sort(str);
			StringBuffer b = new StringBuffer();
			for (String s : str)
			{
				b.append(s);
				b.append(' ');
			}
			setToolTipText(b.toString());
		}
	}

}
