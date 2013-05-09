package star.genetics.visualizers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import star.genetics.Helper;
import star.genetics.Messages;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.v2.ui.common.CommonUI;

public class Yeast implements Visualizer
{
	public static final String DENSITY = "Density"; //$NON-NLS-1$

	public enum YeastColor
	{
		WT("W"), Red("R"), Blue("B"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		String prefix;

		private YeastColor(String prefix)
		{
			this.prefix = prefix;
		}

		public String getPrefix()
		{
			return prefix;
		}

		public static YeastColor parse(String str)
		{
			if (str.equalsIgnoreCase("blue") || str.startsWith("b") || str.startsWith("B")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				return Blue;
			}
			if (str.equalsIgnoreCase("red") || str.startsWith("r") || str.startsWith("R")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				return Red;
			}
			else
			{
				return WT;
			}
		}
	}

	public static enum YeastProperties
	{
		COLOR("Color"), SIZE("Size"), TRANSPARENCY("Growth"), REPLICA("Replica plate"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		String key;

		private YeastProperties(String value)
		{
			this.key = value;
		}

		public String getKey()
		{
			return key;
		}
	}

	public final static String REPLICA = "Replica"; //$NON-NLS-1$
	public final static String COMPLETE = "Complete"; //$NON-NLS-1$
	public final static String NOTEXT = "NoText"; //$NON-NLS-1$
	public final static String MEDIA = "Media"; //$NON-NLS-1$
	private final static String COMPLETE_ALT = "YPD"; //$NON-NLS-1$
	public final static String DIPLOIDSIZE = "DIPLOID_SIZE"; //$NON-NLS-1$

	private Map<String, String> properties;
	private String name;

	private YeastColor color;
	private float size;
	private float transparency;
	private boolean replica;

	public static String getCompleteName(Map<String, String> properties)
	{
		String complete = COMPLETE;
		if (!properties.containsKey(COMPLETE))
		{
			if (properties.containsKey(COMPLETE_ALT))
			{
				complete = COMPLETE_ALT;
			}
		}
		return complete;

	}

	private void update()
	{
		color = YeastColor.WT;
		size = 1;
		transparency = 1;

		String complete = getCompleteName(properties);
		String media = properties.get(MEDIA);

		if (media == null)
		{
			media = complete;
		}

		String value = properties.get(media);
		replica = Boolean.parseBoolean(properties.get(REPLICA));
		if (value != null)
		{
			Map<String, String> map = Helper.parse(value);
			for (Entry<String, String> entry : map.entrySet())
			{
				String key = entry.getKey();
				String v = entry.getValue();
				try
				{
					if (key.toLowerCase().startsWith("t") || key.toLowerCase().startsWith("g")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						transparency = Float.parseFloat(v);
					}
					if (key.toLowerCase().startsWith("s")) //$NON-NLS-1$
					{
						size = Float.parseFloat(v);
					}
					if (key.toLowerCase().startsWith("c")) //$NON-NLS-1$
					{
						color = YeastColor.parse(v);
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		if (properties.containsKey(DIPLOIDSIZE))
		{
			Map<String, String> map = Helper.parse(properties.get(DIPLOIDSIZE));
			for (Entry<String, String> entry : map.entrySet())
			{
				String key = entry.getKey();
				String v = entry.getValue();
				try
				{
					if (key.toLowerCase().startsWith("s")) //$NON-NLS-1$
					{
						size = Float.parseFloat(v);
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

			}
		}
	}

	public ImageIcon getIcon()
	{
		update();
		return getIcon(color, size, transparency, replica);
	}

	private char indexOfSize(float value)
	{
		if (value >= .8f)
			return '4';
		if (value >= .5f)
			return '3';
		if (value >= .3f)
			return '2';
		if (value >= .1f)
			return '1';
		return '0';
	}

	private char indexOfTransparency(float value)
	{
		if (value >= .8f)
			return '1';
		if (value >= .5f)
			return '2';
		if (value >= .3f)
			return '3';
		if (value >= .1f)
			return '4';
		return '5';
	}

	private static ImageIcon blank;

	private static ImageIcon getBlank()
	{
		if (blank == null)
		{
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
			blank = new ImageIcon(img);
		}
		return blank;
	}

	private static Image lawn;

	public static Image getLawn(YeastColor lawnColor)
	{
		String url = "resources/yeast/Lawn.png"; //$NON-NLS-1$
		if (YeastColor.Red.equals(lawnColor))
		{
			url = "resources/yeast/RedLawn.png"; //$NON-NLS-1$
		}
		else if (YeastColor.Blue.equals(lawnColor))
		{
			url = "resources/yeast/BlueLawn.png"; //$NON-NLS-1$
		}
		java.net.URL resource = Yeast.class.getClassLoader().getResource(url);
		if (resource != null)
		{
			ImageIcon original = new ImageIcon(resource);
			int width_original = original.getIconWidth();
			int height_original = original.getIconHeight();
			// float factor = .80f;
			float factor = 1f;
			int width = (int) (width_original * factor);
			int height = (int) (height_original * factor);
			lawn = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = lawn.getGraphics();
			g.drawImage(original.getImage(), -4, -4, width, height, null);
			g.dispose();
		}
		else
		{
			lawn = getBlank().getImage();
		}
		return lawn;
	}

	public ImageIcon getIcon(YeastColor color, float size, float transparency, boolean replica)
	{
		StringBuffer name = new StringBuffer(8);
		name.append(color.getPrefix());
		name.append(replica ? 'R' : 'O');
		name.append(indexOfSize(size));
		name.append(!replica ? '1' : indexOfTransparency(transparency));
		name.append(".png"); //$NON-NLS-1$
		java.net.URL resource = Yeast.class.getClassLoader().getResource("resources/yeast/" + name); //$NON-NLS-1$
		if (resource != null)
		{
			ImageIcon original = new ImageIcon(resource);

			int width_original = original.getIconWidth();
			int height_original = original.getIconHeight();
			float factor = replica ? .70f : .55f;
			int width = (int) (width_original * factor);
			int height = (int) (height_original * factor);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = image.getGraphics();
			g.drawImage(original.getImage(), 0, 0, width, height, null);
			g.dispose();

			return new ImageIcon(image);
		}
		else
		{
			return getBlank();
		}

	}

	public JComponent getJComponent()
	{
		String name = this.name;
		final StringBuilder label = new StringBuilder();
		label.append(name);
		JComponent ret = new JComponent()
		{
			private static final long serialVersionUID = 1L;

			String text = properties.get(NOTEXT) != null ? null : label.toString();
			Icon icon = getIcon();

			@Override
			protected void paintComponent(java.awt.Graphics g)
			{
				int icon_width = icon.getIconWidth();
				int icon_height = icon.getIconHeight();
				int width = getWidth();
				int height = getHeight();
				getIcon().paintIcon(this, g, (width - icon_width) / 2, (height - icon_height) / 2);
				if (text != null)
				{
					int string_width = g.getFontMetrics().stringWidth(text);
					if (string_width > width)
					{
						string_width = width;
					}
					g.drawString(text, (width - string_width) / 2, height - 4);
				}
			};

			@Override
			public Dimension getPreferredSize()
			{
				int w = Math.max(getIcon().getIconWidth(), 55);
				int h = getIcon().getIconHeight();
				if (text != null)
				{
					h += getFontMetrics(getFont()).getHeight();
				}
				else
				{
					h += 2;
				}
				h = Math.max(h, 55);
				return new Dimension(w, h);
			}
		};
		ret.setAlignmentX(.5f);
		ret.setFont(CommonUI.get().getSmallFont());
		return ret;
	}

	public Map<String, String> getTooltipProperties()
	{
		update();
		TreeMap<String, String> properties = new TreeMap<String, String>();
		properties.put("Color", String.valueOf(color)); //$NON-NLS-1$
		properties.put("Size", String.valueOf(size)); //$NON-NLS-1$
		properties.put(DENSITY, String.valueOf(transparency));
		return properties;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setNote(String note)
	{
	}

	public void setProperties(Map<String, String> properties, Sex sex)
	{
		this.properties = properties;
	}

	public void setSex(Sex sex)
	{
	}

	public UIClass getUIClass()
	{
		return UIClass.Yeast;
	}
}
