package star.genetics.visualizers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JFrame;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.v1.ui.common.CommonData;

public class LegoFish extends JComponent implements Visualizer
{
	private static final long serialVersionUID = 1L;

	private enum Colors
	{
		BODY("body", Color.white), HEAD("head", Color.red), TAIL("tail", Color.green), FIN("fin", Color.red), SPOTS("spots", Color.blue), EYE("eye", Color.yellow); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		Color defaultColor = null;

		// private Color secondaryColor = null ;
		// String name;
		// String note;

		Colors(String name, Color color)
		{
			this.defaultColor = color;
			// this.name = name;
		}

		public Color getColor()
		{
			return defaultColor;
		}

	};

	private Map<String, String> properties = null;
	private Sex sex = null;
	private Map<Colors, Color> colors = new HashMap<Colors, Color>();
	private Color[] spotColors = null;
	// private String note = null;
	private String gelProperties;

	public LegoFish()
	{
		setColor(new HashMap<String, String>());
		setSex(Sex.MALE);
	}

	private void setColor(Map<String, String> properties)
	{
		for (Entry<String, String> entry : properties.entrySet())
		{
			String key = entry.getKey().toUpperCase().trim();
			Colors c = null;
			try
			{
				c = Colors.valueOf(key);
			}
			catch (IllegalArgumentException ex)
			{

			}
			catch (Throwable exc)
			{
				exc.printStackTrace();
			}
			if (c != null)
			{
				if (!Colors.SPOTS.equals(c))
				{
					colors.put(c, utils.Colors.parseName(entry.getValue().toLowerCase().trim(), c.getColor()));
				}
				else
				{
					String[] colorNames = entry.getValue().split(","); //$NON-NLS-1$
					spotColors = new Color[colorNames.length];
					for (int i = 0; i < colorNames.length; i++)
					{
						spotColors[i] = utils.Colors.parseName(colorNames[i].toLowerCase().trim(), c.getColor());
					}
				}
			}
		}
		for (Colors c : Colors.values())
		{
			if (!colors.containsKey(c))
			{
				colors.put(c, c.getColor());
			}
		}
	}

	public JComponent getJComponent()
	{
		return this;
	}

	public Map<String, String> getTooltipProperties()
	{
		return properties;
	}

	public void setNote(String note)
	{
		// this.note = note;
	}

	public void setProperties(Map<String, String> properties, Sex sex)
	{
		this.properties = properties;
		setSex(sex);
		setColor(properties);

	}

	public void setSex(Sex sex)
	{
		this.sex = sex;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return CommonData.getPreferredSize();
	}

	int getSpotColorOffset;

	private Color getSpotColor(int index)
	{
		if (spotColors == null || spotColors.length == 0)
		{
			return Colors.SPOTS.getColor();
		}
		if (index < 0)
		{
			getSpotColorOffset = Math.round((float) Math.random() * spotColors.length);
			getSpotColor(-1);
			return Colors.SPOTS.getColor();
		}
		return spotColors[(index + getSpotColorOffset) % spotColors.length];
	}

	@Override
	protected synchronized void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D) g1;
		Dimension d = getSize();
		int scale = Math.max(d.height, d.width) / 12;
		g.scale(scale, scale);
		g.translate(1, 1);

		// body
		paintPolygon(g, new int[] { 2, 2 }, new int[] { 0, 0, 0, 6, 6, 6, 6, 0 }, colors.get(Colors.BODY));

		// head
		paintPolygon(g, new int[] { 0, 2 }, new int[] { 2, 0, 0, 2, 0, 3, 6, 3, 6, 2, 4, 0 }, colors.get(Colors.HEAD));
		// tail
		paintPolygon(g, new int[] { 7, 2 }, new int[] { 2, 0, 0, 2, 0, 3, 6, 3, 6, 2, 4, 0 }, colors.get(Colors.TAIL));
		// fin
		paintPolygon(g, new int[] { 4, 0 }, new int[] { 2, 0, 3, 0, 3, 3, 0, 3, 0, 2 }, colors.get(Colors.FIN));
		// eye
		// paintPolygon(g, new int[] { 1, 3 }, new int[] { 0,0,0,1,1,1,1,0 }, colors.get(Colors.EYE));
		paintEye(g, new int[] { 1, 3 }, colors.get(Colors.EYE));
		// spots
		paintPolygon(g, new int[] { 3, 4 }, new int[] { 0, 0, 0, 1, 1, 1, 1, 0 }, getSpotColor(0));
		paintPolygon(g, new int[] { 5, 4 }, new int[] { 0, 0, 0, 1, 1, 1, 1, 0 }, getSpotColor(1));
		paintPolygon(g, new int[] { 6, 6 }, new int[] { 0, 0, 0, 1, 1, 1, 1, 0 }, getSpotColor(2));
		paintPolygon(g, new int[] { 3, 6 }, new int[] { 0, 0, 0, 1, 1, 1, 1, 0 }, getSpotColor(3));

		g.translate(-1, -1);
		g.scale(1.0 / scale, 1.0 / scale);
		paintSex(g, new Rectangle(8 * scale + 2 * scale / 3, 5 * scale + scale / 3, scale + scale / 2, scale + scale / 2), sex, getSexColor());
		g.setColor(Color.black);
		g.drawString(getName() != null ? getName() : "", d.width / 10, d.height * 9 / 10 + 1); //$NON-NLS-1$
	}

	private Color getSexColor()
	{
		Color ret;
		Color tail = colors.get(Colors.TAIL);
		ret = new Color(tail.getRGB() ^ 0x00ffffff);
		return ret;
	}

	private void paintEye(Graphics2D g, int[] is, Color c)
	{
		g.translate(is[0], is[1]);
		g.setColor(c);
		g.fillArc(0, 0, 1, 1, 0, 360);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.05f));
		g.drawArc(0, 0, 1, 1, 0, 360);
		g.translate(-is[0], -is[1]);
	}

	private void paintPolygon(Graphics2D g, int[] is, int[] is2, Color c)
	{
		g.translate(is[0], is[1]);
		Polygon p = new Polygon();
		for (int i = 0; i < is2.length; i += 2)
		{
			p.addPoint(is2[i + 1], is2[i]);
		}
		g.setColor(c);
		g.fillPolygon(p);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.05f));
		g.drawPolygon(p);
		g.translate(-is[0], -is[1]);
	}

	private void paintSex(Graphics2D g, Rectangle rect, Creature.Sex sex, Color c)
	{
		Ellipse2D.Float ellipse = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		g.setStroke(new BasicStroke(0.1f * rect.width));
		g.setColor(c);
		g.drawOval(rect.x, rect.y, rect.width, rect.height);
		// g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.1f * rect.width));
		g.draw(ellipse);
		float center_x = rect.x + rect.width / 2;
		float center_y = rect.y + rect.height / 2;
		g.setStroke(new BasicStroke(0.1f * rect.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
		if (sex != null)
		{
			float f = 1.6f;
			switch (sex)
			{
			case MALE:
				Line2D.Float line1 = new Line2D.Float(center_x + rect.width * (float) (Math.sqrt(.5) / 2 + .01f), center_y - rect.height * (float) Math.sqrt(.5) / 2, center_x + rect.width * .49f * f, center_y - rect.height * .49f * f);
				Line2D.Float line2 = new Line2D.Float(center_x + rect.width * .49f * f, center_y - rect.height * .34f, center_x + rect.width * .49f * f, center_y - rect.height * .49f * f);
				Line2D.Float line3 = new Line2D.Float(center_x + rect.width * .34f, center_y - rect.height * .49f * f, center_x + rect.width * .49f * f, center_y - rect.height * .49f * f);
				g.draw(line1);
				g.draw(line2);
				g.draw(line3);
				break;

			case FEMALE:
				Line2D.Float line4 = new Line2D.Float(center_x + rect.width * (float) (Math.sqrt(.5) / 2 + .01f), center_y + rect.height * (float) Math.sqrt(.5) / 2, center_x + rect.width * .49f * f, center_y + rect.height * .49f * f);
				Line2D.Float line5 = new Line2D.Float(center_x + rect.width * .49f * f, center_y + rect.height * .40f, center_x + rect.width * .40f, center_y + rect.height * .49f * f);
				g.draw(line4);
				g.draw(line5);
				break;
			}
		}
	}

	public static void main(String[] args)
	{
		JFrame test = new JFrame();
		test.getContentPane().setLayout(new BorderLayout());
		test.getContentPane().add(new LegoFish());
		test.pack();
		test.setVisible(true);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public UIClass getUIClass()
	{
		return UIClass.Fly;
	}
}
