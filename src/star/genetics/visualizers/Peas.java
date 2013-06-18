package star.genetics.visualizers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import utils.Colors;
import utils.Icons;

public class Peas extends JComponent implements Visualizer
{
	private static final long serialVersionUID = 1L;

	public enum SeedShape
	{
		WRINKLED, SMOOTH
	};

	public enum PodsShape
	{
		PINCHED, INFLATED
	};

	public enum Flowers
	{
		AXIAL, TERMINAL
	};

	public enum Stems
	{
		TALL, SHORT
	};

	private Sex sex;
	private String name;
	private String note;
	private Map<String, String> properties;

	public SeedShape seedShape;
	public Color seedColor;
	public Color petalsColor;
	public PodsShape podsShape;
	public Color podsColor;
	public Flowers flowers;
	public Stems stems;

	String getColor(Color color)
	{
		String c = Colors.parseColor(color);
		if (c.equalsIgnoreCase("green1")) //$NON-NLS-1$
		{
			return "green"; //$NON-NLS-1$
		}
		if (c.equalsIgnoreCase("yellow1")) //$NON-NLS-1$
		{
			return "yellow"; //$NON-NLS-1$
		}
		if (c.equalsIgnoreCase("grey100")) //$NON-NLS-1$
		{
			return "white"; //$NON-NLS-1$
		}
		if (c.equalsIgnoreCase("red1")) //$NON-NLS-1$
		{
			return "red"; //$NON-NLS-1$
		}

		return c;
	}

	static HashMap<java.net.URL, ImageIcon> preload = new HashMap<java.net.URL, ImageIcon>();
	static
	{
		Peas p = new Peas();
		try
		{
			p.init();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	void init() throws Exception
	{
		java.net.URL url = this.getClass().getResource("/resources/peas/image_index.txt"); //$NON-NLS-1$
		BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
		String path;
		HashMap<java.net.URL, ImageIcon> list = new HashMap<java.net.URL, ImageIcon>();
		while ((path = r.readLine()) != null)
		{
			java.net.URL imgURL = this.getClass().getResource("/resources/peas/" + path.toLowerCase()); //$NON-NLS-1$
			ImageIcon icon = new ImageIcon(imgURL);
			list.put(imgURL, icon);
		}
		int c = 0;
		while (!list.isEmpty() && c < 25)
		{
			Iterator<Entry<java.net.URL, ImageIcon>> iter = list.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<java.net.URL, ImageIcon> entry = iter.next();
				ImageIcon icon = entry.getValue();
				if (icon.getImageLoadStatus() == MediaTracker.COMPLETE)
				{
					preload.put(entry.getKey(), entry.getValue());
					iter.remove();
				}
			}
			Thread.sleep(10);
			c++;
		}
	}

	private Image getImage(java.net.URL url)
	{
		if (preload.containsKey(url))
		{
			return preload.get(url).getImage();
		}
		else
		{
			return new ImageIcon(url).getImage();
		}
	}

	Image getPodImage()
	{
		String path = MessageFormat.format("{0} plant {1} {3} {2} pod-03.png", stems.toString(), flowers.toString(), getColor(podsColor), podsShape.toString()); //$NON-NLS-1$
		java.net.URL url = this.getClass().getResource("/resources/peas/" + path.toLowerCase()); //$NON-NLS-1$
		if (url == null)
		{
			System.out.println("Missing " + path); //$NON-NLS-1$
			return Icons.ALERT.getImage();
		}
		else
		{
			return getImage(url);
		}
	}

	Image getFlowerImage()
	{
		String path = MessageFormat.format("{0} plant {1} {2} flowers-03.png", stems.toString().toLowerCase(), flowers.toString().toLowerCase(), getColor(petalsColor).toLowerCase()); //$NON-NLS-1$
		java.net.URL url = this.getClass().getResource("/resources/peas/" + path.toLowerCase()); //$NON-NLS-1$
		if (url == null)
		{
			System.out.println("Missing " + path); //$NON-NLS-1$
			return Icons.ALERT.getImage();
		}
		else
		{
			return getImage(url);
		}
	}

	Image getPeaImage()
	{
		String path = MessageFormat.format("{0} plant {2} {1} peas-03.png", stems.toString(), getColor(seedColor), seedShape.toString()); //$NON-NLS-1$
		java.net.URL url = this.getClass().getResource("/resources/peas/" + path.toLowerCase()); //$NON-NLS-1$
		if (url == null)
		{
			System.out.println("Missing " + path); //$NON-NLS-1$
			return Icons.ALERT.getImage();
		}
		else
		{
			return getImage(url);
		}
	}

	Image getStemImage()
	{
		String path = MessageFormat.format("{0} plant stem-03.png", stems.toString()); //$NON-NLS-1$
		java.net.URL url = this.getClass().getResource("/resources/peas/" + path.toLowerCase()); //$NON-NLS-1$
		if (url == null)
		{
			System.out.println("Missing " + path); //$NON-NLS-1$
			return Icons.ALERT.getImage();
		}
		else
		{
			return getImage(url);
		}
	}

	int borderBottom = 15;

	public void paintComponent(Graphics g1)
	{
		Graphics2D g = (Graphics2D) g1.create();
		Stroke oldStroke = g.getStroke();
		int w = getWidth();
		int h = getHeight() - borderBottom;
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(getStemImage(), 0, 0, w, h, null);
		g.drawImage(getFlowerImage(), 0, 0, w, h, null);
		g.drawImage(getPodImage(), 0, 0, w, h, null);
		g.drawImage(getPeaImage(), 0, 0, w, h, null);

		h += borderBottom;
		/*
		 * g.scale(w/3,h/3); g.setStroke(new BasicStroke(1.0f/Math.min(w,h))); paintSeeds(g); g.scale(3.0/w, 3.0/h); g.scale(w/2,h/1); g.translate(1, 0);
		 * g.setStroke(new BasicStroke(1.0f/Math.min(w,h))); paintStalk(g); paintFlowers(g); g.translate(-1, 0); g.scale(2.0/w, 1.0/h); g.scale(w/2,h/2);
		 * g.translate(0, 1); g.setStroke(new BasicStroke(1.0f/Math.min(w,h))); paintPod(g); g.translate(0, -1); g.scale(2.0/w, 2.0/h);
		 */
		/*
		 * int d = Math.min(w / 2, h / 2); paintSex2(g, new Rectangle(0, h / 2, d, d), sex);
		 */
		g.setColor(Color.black);
		if (name != null)
		{
			Rectangle2D r = g.getFontMetrics().getStringBounds(String.valueOf(name), g);
			g.drawString(String.valueOf(name), Math.round((w - r.getWidth()) / 2), h - 5);
		}
		g.setStroke(oldStroke);
	};

	float factor = 1f;

	private Dimension prefSize = new Dimension((int) (156 * factor), (int) (180 * factor) + borderBottom);

	@Override
	public Dimension getPreferredSize()
	{
		return prefSize;
	}

	private void paintSex2(Graphics2D g, Rectangle rect, Creature.Sex sex)
	{
		if (sex != null)
		{
			float scale = .125f;
			float center_x = rect.x + rect.width * .5f;
			float center_y = rect.y + rect.height * .5f;
			float dx = (rect.width * scale);
			float dy = (rect.height * scale);

			switch (sex)
			{
			case FEMALE:
				center_y -= .30f * dy;
			}

			Ellipse2D.Float ellipse = new Ellipse2D.Float(center_x - dx / 2, center_y - dy / 2, dx, dy);

			g.setStroke(new BasicStroke(0.020f * rect.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
			g.setColor(Color.black);
			g.draw(ellipse);

			switch (sex)
			{
			case MALE:
				Line2D.Float line1 = new Line2D.Float(center_x + dx * (float) (Math.sqrt(.5) / 2 + .01f), center_y - dy * (float) Math.sqrt(.5) / 2, center_x + dx, center_y - dy);
				Line2D.Float line2 = new Line2D.Float(center_x + dx, center_y - dy * .34f, center_x + dx, center_y - dy);
				Line2D.Float line3 = new Line2D.Float(center_x + dx * .34f, center_y - dy, center_x + dx, center_y - dy);
				g.draw(line1);
				g.draw(line2);
				g.draw(line3);
				break;

			case FEMALE:

				// Line2D.Float line4 = new Line2D.Float(center_x + dx * (float) (Math.sqrt(.5) / 2 + .01f), center_y + dy * (float) Math.sqrt(.5) / 2,
				// center_x
				// + dx, center_y + dy);
				// Line2D.Float line5 = new Line2D.Float(center_x + dx, center_y + dy * .40f, center_x + dx * .40f, center_y + dy);
				Line2D.Float line4 = new Line2D.Float(center_x - dx * .4f, center_y + dy, center_x + dx * .4f, center_y + dy);
				Line2D.Float line5 = new Line2D.Float(center_x, center_y + dy * .4f, center_x, center_y + dy * 1.6f);

				g.draw(line5);
				g.draw(line4);
				break;
			}
		}
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
		parseProperties(properties);
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

	private static <T> T parse(T[] values, String in)
	{
		String name = in.trim().toLowerCase();
		for (T t : values)
		{
			if (t.toString().equalsIgnoreCase(name))
			{
				return t;
			}
		}
		return null;
	}

	private void parseProperties(Map<String, String> prop)
	{
		Map<String, String> properties = filter(prop);
		String key;
		key = "peacolor"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{
			seedColor = Colors.parseName(properties.get(key), Color.white);
		}
		else
		{
			seedColor = Color.white;
		}

		key = "peashape"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{
			seedShape = parse(SeedShape.values(), properties.get(key));
			// seedShape = SeedShape.valueOf(properties.get(key));
		}
		else
		{
			seedShape = SeedShape.SMOOTH;
		}

		key = "flowercolor"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{
			petalsColor = Colors.parseName(properties.get(key), Color.green);
		}
		else
		{
			petalsColor = Color.green;
		}

		key = "podcolor"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{
			podsColor = Colors.parseName(properties.get(key), Color.green);
		}
		else
		{
			podsColor = Color.green;
		}

		key = "podshape"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{
			podsShape = parse(PodsShape.values(), properties.get(key));
			// podsShape = PodsShape.valueOf(properties.get(key));
		}
		else
		{
			podsShape = PodsShape.INFLATED;
		}

		key = "flowerpodposition"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{
			flowers = parse(Flowers.values(), properties.get(key));
			// flowers = Flowers.valueOf(properties.get(key));
		}
		else
		{
			flowers = Flowers.TERMINAL;
		}

		key = "plantheight"; //$NON-NLS-1$
		if (properties.containsKey(key))
		{

			stems = parse(Stems.values(), properties.get(key));
			// stems = Stems.valueOf(properties.get(key));
		}
		else
		{
			stems = Stems.TALL;
		}
	}

	@Override
	public Map<String, String> getTooltipProperties()
	{
		Map<String, String> ret = new HashMap<String, String>();
		for (Entry<String, String> entry : properties.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			String translation_key = "Peas." + filter(key);
			String translated = Messages.getString(translation_key);
			if (translated.contains(translation_key))
			{
				translated = key;
			}
			ret.put(translated, value);
		}
		return ret;
	}

	public UIClass getUIClass()
	{
		return UIClass.Fly;
	}
}
