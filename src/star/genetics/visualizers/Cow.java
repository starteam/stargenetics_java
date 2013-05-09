package star.genetics.visualizers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.GeneticModel;
import utils.Colors;
import utils.Icons;

public class Cow extends JComponent implements Visualizer
{
	private static final long serialVersionUID = 1L;

	enum Hump
	{
		HUMP, HUMPLESS;

		public static Hump value(String str)
		{
			Hump ret = null;
			if (str != null)
			{
				if (str.equalsIgnoreCase("yes")) //$NON-NLS-1$
				{
					ret = HUMP;
				}
				else if (str.equalsIgnoreCase("no")) //$NON-NLS-1$
				{
					ret = HUMPLESS;
				}
				if (ret == null)
					for (Hump i : Hump.values())
					{
						if (str.equalsIgnoreCase(i.name()))
						{
							return i;
						}
					}
			}
			if (ret == null)
			{
				ret = HUMPLESS;
			}
			return ret;
		}
	}

	enum BodySpots
	{
		SOLID, SPOTTED;

		public static BodySpots value(String str)
		{
			BodySpots ret = null;
			if (str != null)
			{
				if (str.equalsIgnoreCase("yes")) //$NON-NLS-1$
				{
					ret = SPOTTED;
				}
				else if (str.equalsIgnoreCase("no")) //$NON-NLS-1$
				{
					ret = SOLID;
				}
				if (ret == null)
					for (BodySpots i : BodySpots.values())
					{
						if (str.equalsIgnoreCase(i.name()))
						{
							ret = i;
							break;
						}
					}
			}
			if (ret == null)
				ret = SOLID;
			return ret;
		}
	}

	enum BodySpeckles
	{
		SOLID, SPECKLED;

		public static BodySpeckles value(String str)
		{
			BodySpeckles ret = null;
			if (str != null)
			{
				if (str.equalsIgnoreCase("yes")) //$NON-NLS-1$
				{
					ret = SPECKLED;
				}
				else if (str.equalsIgnoreCase("no")) //$NON-NLS-1$
				{
					ret = SOLID;
				}
				if (ret == null)
					for (BodySpeckles i : BodySpeckles.values())
					{
						if (str.equalsIgnoreCase(i.name()))
						{
							ret = i;
							break;
						}
					}
			}
			if (ret == null)
			{
				ret = SOLID;
			}
			return ret;
		}

	}

	enum Horns
	{
		HORNED, POLLED;

		public static Horns value(String str)
		{
			Horns ret = null;
			if (str != null)
			{
				if (str.equalsIgnoreCase("yes")) //$NON-NLS-1$
				{
					ret = HORNED;
				}
				if (str.equalsIgnoreCase("no")) //$NON-NLS-1$
				{
					ret = POLLED;
				}
				for (Horns i : Horns.values())
				{
					if (str.equalsIgnoreCase(i.name()))
					{
						return i;
					}
				}
			}
			if (ret == null)
			{
				ret = HORNED;
			}
			return ret;
		}
	}

	enum HornSize
	{
		LONG, SHORT;

		public static HornSize value(String str)
		{
			if (str != null)
			{
				for (HornSize i : HornSize.values())
				{
					if (str.equalsIgnoreCase(i.name()))
					{
						return i;
					}
				}
			}
			return SHORT;
		}
	}

	enum HornShape
	{
		UP("Up"), DOWN("Down"), TWISTED("Twisted"), STRAIGHT("Straight"), SCURRED("Scurred"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		String img;

		private HornShape(String img)
		{
			this.img = img;
		}

		public static HornShape value(String str)
		{
			if (str != null)
			{
				for (HornShape i : HornShape.values())
				{
					if (str.equalsIgnoreCase(i.name()))
					{
						return i;
					}
				}
			}
			return UP;
		}
	}

	public Cow()
	{
		super();
		setBackground(Color.white);
	}

	Image image;

	@Override
	protected void paintComponent(Graphics g2)
	{
		Graphics2D g = (Graphics2D) g2;
		super.paintComponent(g);
		if (image == null || image.getWidth(null) != getWidth() || image.getHeight(null) != getHeight())
		{
			image = createImage(prefSize.width, prefSize.height);
			Graphics2D gg = (Graphics2D) image.getGraphics();
			Color bg = Color.white;
			gg.setColor(bg);
			gg.fillRect(0, 0, prefSize.width, prefSize.height);
			gg.setColor(Color.black);
			paintBody(gg);
			gg.dispose();
			image.flush();

		}
		float ww = 1.0f * getWidth() / image.getWidth(null);
		float hh = 1.0f * getHeight() / image.getHeight(null);
		float rr = Math.min(ww, hh);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.drawImage(image, 0, 0, (int) (rr * image.getWidth(null)), (int) (rr * image.getHeight(null)), null);
	}

	private void paintMatings(Graphics2D g, Rectangle rect)
	{
		if (properties == null)
		{
			return;
		}
		String matings = properties.get(GeneticModel.matings);
		if (matings != null && matings.length() != 0 && name != null && name.length() != 0)
		{
			try
			{
				int m = Integer.parseInt(matings);
				if (m < 10)
				{
					matings = Messages.getString("Cow.0") + matings; //$NON-NLS-1$
					g.setColor(java.awt.Color.black);
					g.setColor(m >= 3 ? java.awt.Color.black : java.awt.Color.red);
					g.setFont(g.getFont().deriveFont(9.0f));
					Rectangle2D r = g.getFontMetrics().getStringBounds(matings, g);
					int x_offset = rect.x - (int) r.getWidth();
					g.drawString(matings, x_offset > 0 ? x_offset : 0, rect.y + (int) r.getHeight());
				}
			}
			catch (NumberFormatException ex)
			{
			}
		}
	}

	private void paintBody(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		paint(g, getBodyShapeImage());
		paint(g, getBodySpotsImage());
		paint(g, getBodySpeckledImage());
		paint(g, getFaceImage());
		paint(g, getUdderImage());
		paint(g, getHornsImage());
		paintXOR(g, getSexImage());
		paintName(g);
		paintMatings(g, new Rectangle(0, 0, prefSize.width, prefSize.height));

	}

	private void paintName(Graphics2D g)
	{
		if (name != null)
		{
			g.setColor(Color.black);
			Rectangle2D r = g.getFontMetrics().getStringBounds(String.valueOf(name), g);
			g.drawString(String.valueOf(name), Math.round((prefSize.width - r.getWidth()) / 2), prefSize.height - 5);
		}
	}

	private void paintXOR(Graphics2D g, Image img)
	{
		if (img != null)
		{
			g.setXORMode(Color.white);
			g.drawImage(img, 0, 0, prefSize.width, prefSize.height - borderBottom, null);
			g.setPaintMode();
		}
	}

	private void paint(Graphics2D g, Image img)
	{
		if (img != null)
		{
			g.drawImage(img, -10, 0, prefSize.width, prefSize.height - borderBottom, null);
		}
	}

	Image repaint(Image img, Color c)
	{
		final int cc = c.getRGB();
		ImageFilter imgF = new RGBImageFilter()
		{

			@Override
			public int filterRGB(int x, int y, int rgb)
			{
				if ((rgb & 0xffffff) == 0xffffff)
					return rgb;
				return cc;
			}
		};
		ImageProducer ip = new FilteredImageSource(img.getSource(), imgF);
		Image ret = java.awt.Toolkit.getDefaultToolkit().createImage(ip);
		ImageIcon iicon = new ImageIcon(ret);
		while (iicon.getImageLoadStatus() != MediaTracker.COMPLETE)
		{
			utils.UIHelpers.sleep(50);
		}

		return iicon.getImage();
	}

	Image getImage(String path)
	{
		Image ret = null;
		java.net.URL url = this.getClass().getResource("/resources/cow/" + path); //$NON-NLS-1$
		if (url == null)
		{
			System.out.println("Missing " + path); //$NON-NLS-1$
			ret = Icons.ALERT.getImage();
		}
		else
		{
			ret = new ImageIcon(url).getImage();
		}
		return ret;
	}

	private Image getFaceImage()
	{
		String path = "Cattle Face-72ppi.png"; //$NON-NLS-1$
		return repaint(getImage(path), faceColor);
	}

	private Image getBodyShapeImage()
	{
		String path = MessageFormat.format("{0} {1} Hump-72ppi.png", Sex.FEMALE.equals(sex) ? "Cow" : "Bull", Hump.HUMP.equals(hump) ? "with" : "without"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return repaint(getImage(path), bodyColor);
	}

	private Image getBodySpotsImage()
	{
		Image ret = null;
		if (!BodySpots.SOLID.equals(spots))
		{
			String path = MessageFormat.format("{0}-72ppi.png", "Spots"); //$NON-NLS-1$ //$NON-NLS-2$
			ret = repaint(getImage(path), spotsColor);
		}
		return ret;
	}

	private Image getBodySpeckledImage()
	{
		Image ret = null;
		if (!BodySpeckles.SOLID.equals(speckls))
		{
			String path = MessageFormat.format("{0}-72ppi.png", "Speckled-Roan"); //$NON-NLS-1$ //$NON-NLS-2$
			return repaint(getImage(path), specklsColor);
		}
		return ret;
	}

	private Image getUdderImage()
	{
		Image ret = null;
		if (Sex.FEMALE.equals(sex))
		{
			String path = "Cow Udder-72ppi.png"; //$NON-NLS-1$
			return getImage(path);
		}
		return ret;

	}

	private Image getHornsImage()
	{
		if (Horns.POLLED.equals(horns))
		{
			return null;
		}
		else
		{
			if (HornShape.SCURRED.equals(hornShape))
			{
				return getImage("Scurred-72ppi.png"); //$NON-NLS-1$
			}
			else
			{
				String path = MessageFormat.format("{0} Horn {1}-72ppi.png", HornSize.LONG.equals(size) ? "Long" : "Short", hornShape.img); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return getImage(path);
			}
		}
	}

	private Image getSexImage()
	{
		if (sex != null)
		{
			String path = MessageFormat.format("{0} brand-72ppi.png", Sex.FEMALE.equals(sex) ? "Female" : "Male"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Image orig = getImage(path);
			Image ret = new BufferedImage(orig.getWidth(null), orig.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = ret.getGraphics();
			g.setColor(getBackground() != null ? getBackground() : Color.white);
			g.fillRect(0, 0, ret.getWidth(null), ret.getHeight(null));
			g.drawImage(orig, -12, 0, null);
			g.dispose();
			ret.flush();
			return ret;
		}
		return null;
	}

	private int borderBottom = 20;
	private float factor = 0.75f;
	private Dimension prefSize = new Dimension((int) (306 * factor), (int) (221 * factor) + borderBottom);
	private Dimension prefSize2 = new Dimension((int) (306 * factor), (int) (221 * factor) + borderBottom);

	@Override
	public Dimension getPreferredSize()
	{
		return prefSize2;
	}

	Horns horns;
	Hump hump;
	HornSize size;
	HornShape hornShape;
	BodySpots spots;
	BodySpeckles speckls;
	Color bodyColor;
	Color spotsColor;
	Color specklsColor;
	Color faceColor;

	Sex sex;
	private String name;
	private String note;
	private Map<String, String> properties;

	Map<String, String> filter(Map<String, String> properties)
	{
		Map<String, String> ret = new HashMap<String, String>();
		for (Entry<String, String> entry : properties.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			ret.put(filter(key), value);
		}
		return ret;
	}

	String filter(String key)
	{
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < key.length(); i++)
		{
			char c = key.charAt(i);
			if (!(Character.isSpaceChar(c) || Character.isWhitespace(c) || c <= ' '))
			{
				ret.append(Character.toLowerCase(c));
			}
		}
		return ret.toString();
	}

	private void parseProperties()
	{
		Map<String, String> properties = filter(this.properties);
		bodyColor = Colors.parseName(properties.get("bodycolor"), Color.BLACK); //$NON-NLS-1$
		spots = BodySpots.value(properties.get("spots")); //$NON-NLS-1$
		spotsColor = Colors.parseName(properties.get("spotscolor"), Color.WHITE); //$NON-NLS-1$
		speckls = BodySpeckles.value(properties.get("speckles")); //$NON-NLS-1$
		specklsColor = Colors.parseName(properties.get("specklescolor"), Color.WHITE); //$NON-NLS-1$

		horns = Horns.value(properties.get("horns")); //$NON-NLS-1$
		hornShape = HornShape.value(properties.get("hornshape")); //$NON-NLS-1$

		size = HornSize.value(properties.get("hornsize")); //$NON-NLS-1$
		hump = Hump.value(properties.get("hump")); //$NON-NLS-1$
		faceColor = Colors.parseName(properties.get("facialfeaturescolor"), Color.BLACK); //$NON-NLS-1$

	}

	@Override
	public void setSex(Sex sex)
	{
		this.sex = sex;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void setNote(String note)
	{
		this.note = note;

	}

	@Override
	public JComponent getJComponent()
	{
		putClientProperty("NONOTE", 1); //$NON-NLS-1$
		return this;
	}

	@Override
	public void setProperties(Map<String, String> properties, Sex sex)
	{
		this.properties = properties;
		this.sex = sex;
		parseProperties();
	}

	@Override
	public Map<String, String> getTooltipProperties()
	{
		Map<String, String> ret = new LinkedHashMap<String, String>(properties);
		Iterator<Entry<String, String>> iter = ret.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, String> e = iter.next();
			if (filter(e.getKey()).startsWith("facial")) //$NON-NLS-1$
			{
				iter.remove();
			}
			if (BodySpeckles.SOLID.equals(speckls) && filter(e.getKey()).equalsIgnoreCase("specklescolor")) //$NON-NLS-1$
			{
				iter.remove();
			}
			if (BodySpots.SOLID.equals(spots) && filter(e.getKey()).equalsIgnoreCase("spotscolor")) //$NON-NLS-1$
			{
				iter.remove();
			}
			if (Horns.POLLED.equals(horns) && filter(e.getKey()).equalsIgnoreCase("hornshape")) //$NON-NLS-1$
			{
				iter.remove();
			}
			if (Horns.POLLED.equals(horns) && filter(e.getKey()).equalsIgnoreCase("hornsize")) //$NON-NLS-1$
			{
				iter.remove();
			}

		}
		return ret;
	}

	@Override
	public UIClass getUIClass()
	{
		return UIClass.Fly;
	}

}
