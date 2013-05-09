package star.genetics.visualizers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JFrame;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.common.CommonData;
import utils.Colors;

public class Smiley extends JComponent implements Visualizer
{
	private enum Mouth
	{
		HAPPY, SAD, NEUTRAL;

		public static Mouth parse(String name)
		{
			if (HAPPY.toString().equalsIgnoreCase(name))
			{
				return HAPPY;
			}
			if (SAD.toString().equalsIgnoreCase(name))
			{
				return SAD;
			}
			if (NEUTRAL.toString().equalsIgnoreCase(name))
			{
				return NEUTRAL;
			}
			return HAPPY;
		}
	}

	private enum Eyes
	{
		CIRCLE, LINE, CROSS;

		public static Eyes parse(String name)
		{
			if (CIRCLE.toString().equalsIgnoreCase(name))
			{
				return CIRCLE;
			}

			if (CROSS.toString().equalsIgnoreCase(name))
			{
				return CROSS;
			}

			if (LINE.toString().equalsIgnoreCase(name))
			{
				return LINE;
			}
			return CIRCLE;
		}

	}

	private static final long serialVersionUID = 1L;
	private Creature.Sex sex = Sex.MALE;
	private Color bodyColor = Color.yellow;
	private Color eyeColor = Color.black;
	private Mouth mouth = Mouth.HAPPY;
	private Eyes eyes = Eyes.CIRCLE;
	private Map<String, String> properties = new TreeMap<String, String>();
	private String note = ""; //$NON-NLS-1$
	private boolean sterile = false;
	private String name;
	private String gelProperties;

	public void setNote(String note)
	{
		this.note = note;
		updateTooltipString();
	}

	@Override
	protected void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		if (g1 instanceof Graphics2D)
		{
			Graphics2D g = (Graphics2D) g1;
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Rectangle rect1 = this.getBounds();
			rect1.x = 0;
			rect1.y = 0;
			Rectangle2D labelRect = g.getFontMetrics(g.getFont()).getStringBounds(getName(), g);

			int by = 3;
			int bh = rect1.height - (by * 2 + (int) (1.5f * labelRect.getHeight()));

			int bx = rect1.x + rect1.width / 10;
			int bw = rect1.width - 2 * rect1.width / 10;

			bh = Math.min(bw, bh);
			bw = bh;
			bx = (rect1.width - bh) / 2;

			Rectangle body_rectange = new Rectangle(bx, by, bw, bh);
			drawBody(g, body_rectange);
			drawEyes(g, body_rectange);
			drawMouth(g, body_rectange);

			int rest = rect1.height - by - bh;
			Rectangle name_rectange = new Rectangle(bx, by + bh, bw, rest);
			drawName(g, name_rectange);
			drawSterile(g, body_rectange);
			drawMatings(g, body_rectange);
		}
	}

	private void drawMatings(Graphics2D g, Rectangle rect)
	{
		String matings = properties.get(GeneticModel.matings);
		if (matings != null && matings.length() != 0)
		{
			try
			{
				int m = Integer.parseInt(matings);
				if (m < 10)
				{
					matings = Messages.getString("Smiley.1") + matings; //$NON-NLS-1$
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

	private void drawName(Graphics2D g, Rectangle rect)
	{
		String name = getName();
		if (name != null && name.length() != 0)
		{
			g.setColor(java.awt.Color.black);
			Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);

			g.drawString(name, rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + (int) r.getHeight());
		}
	}

	private void drawSterile(Graphics2D g, Rectangle rect)
	{
		if (sterile)
		{
			String name = GeneticModel.sterile;
			g.setColor(java.awt.Color.red);
			java.awt.Font original = g.getFont();
			g.setFont(original.deriveFont(original.getSize2D() * 1.5f));
			Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);
			g.fillRect(rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + rect.height / 2 - (int) r.getHeight() + 2, (int) r.getWidth(), (int) r.getHeight());
			// g.drawLine((int) rect.getX(), (int) rect.getY(), (int) (rect.getX() + rect.getWidth()), (int) (rect.getY() + rect.getHeight()));
			// g.drawLine((int) (rect.getX() + rect.getWidth()), (int) rect.getY(), (int) rect.getX(), (int) (rect.getY() + rect.getHeight()));
			g.setColor(java.awt.Color.black);
			g.drawString(name, rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + rect.height / 2);
			g.setFont(original);
		}
	}

	private void drawBody(Graphics2D g, Rectangle rect)
	{
		Ellipse2D.Float ellipse = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		g.setStroke(new BasicStroke(0.015f * rect.width));
		g.setColor(bodyColor);
		g.fillOval(rect.x, rect.y, rect.width, rect.height);
		g.fill(ellipse);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.015f * rect.width));
		g.draw(ellipse);
		float center_x = rect.x + rect.width / 2;
		float center_y = rect.y + rect.height / 2;
		g.setStroke(new BasicStroke(0.020f * rect.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
		if (sex != null)
		{
			switch (sex)
			{
			case MALE:
				Line2D.Float line1 = new Line2D.Float(center_x + rect.width * (float) (Math.sqrt(.5) / 2 + .01f), center_y - rect.height * (float) Math.sqrt(.5) / 2, center_x + rect.width * .49f, center_y - rect.height * .49f);
				Line2D.Float line2 = new Line2D.Float(center_x + rect.width * .49f, center_y - rect.height * .34f, center_x + rect.width * .49f, center_y - rect.height * .49f);
				Line2D.Float line3 = new Line2D.Float(center_x + rect.width * .34f, center_y - rect.height * .49f, center_x + rect.width * .49f, center_y - rect.height * .49f);
				g.draw(line1);
				g.draw(line2);
				g.draw(line3);
				break;

			case FEMALE:
				Line2D.Float line4 = new Line2D.Float(center_x + rect.width * (float) (Math.sqrt(.5) / 2 + .01f), center_y + rect.height * (float) Math.sqrt(.5) / 2, center_x + rect.width * .49f, center_y + rect.height * .49f);
				Line2D.Float line5 = new Line2D.Float(center_x + rect.width * .49f, center_y + rect.height * .40f, center_x + rect.width * .40f, center_y + rect.height * .49f);
				g.draw(line4);
				g.draw(line5);
				break;
			}
		}
	}

	private void drawEyes(Graphics2D g, Rectangle rect)
	{
		int eye_left = rect.x + rect.width / 3;
		int eye_right = rect.x + rect.width / 3 * 2;
		int eye_top = rect.y + rect.height / 3;
		g.setColor(eyeColor);
		switch (eyes)
		{
		case LINE:
			Rectangle2D.Float rect_left = new Rectangle2D.Float(eye_left - rect.height * .10f / 2, eye_top, rect.height * .10f, rect.width * .10f);
			Rectangle2D.Float rect_right = new Rectangle2D.Float(eye_right - rect.height * .10f / 2, eye_top, rect.height * .10f, rect.width * .10f);
			g.fill(rect_left);
			g.fill(rect_right);
			break;
		case CROSS:
			Line2D.Float line_left1 = new Line2D.Float(eye_left - rect.height * .10f / 2, eye_top, eye_left + rect.height * .10f / 2, eye_top + rect.width * .10f);
			Line2D.Float line_left2 = new Line2D.Float(eye_left + rect.height * .10f / 2, eye_top, eye_left - rect.height * .10f / 2, eye_top + rect.width * .10f);
			Line2D.Float line_right1 = new Line2D.Float(eye_right - rect.height * .10f / 2, eye_top, eye_right + rect.height * .10f / 2, eye_top + rect.width * .10f);
			Line2D.Float line_right2 = new Line2D.Float(eye_right + rect.height * .10f / 2, eye_top, eye_right - rect.height * .10f / 2, eye_top + rect.width * .10f);

			g.draw(line_left1);
			g.draw(line_left2);
			g.draw(line_right1);
			g.draw(line_right2);

			break;
		case CIRCLE:
		default:
			Ellipse2D.Float ellipse_left = new Ellipse2D.Float(eye_left - rect.height * .10f / 2, eye_top, rect.height * .10f, rect.width * .10f);
			Ellipse2D.Float ellipse_right = new Ellipse2D.Float(eye_right - rect.height * .10f / 2, eye_top, rect.height * .10f, rect.width * .10f);
			g.fill(ellipse_left);
			g.fill(ellipse_right);
			break;
		}

	}

	private void drawMouth(Graphics2D g, Rectangle rect)
	{
		switch (mouth)
		{
		case SAD:
			drawMouthSad(g, rect);
			break;
		case NEUTRAL:
			drawMouthStraight(g, rect);
			break;
		case HAPPY:
		default:
			drawMouthSmily(g, rect);
			break;
		}
	}

	private void drawMouthSmily(Graphics2D g, Rectangle rect)
	{
		float center_x = rect.x + rect.width / 2;
		float center_y = rect.y + rect.height / 2;
		float dx = rect.width * .25f;
		float dy = rect.height * .25f;
		Rectangle2D.Float rect2 = new Rectangle2D.Float(center_x - dx, center_y - dy, dx * 2, dy * 2);
		Arc2D.Float arc = new Arc2D.Float(rect2, 270 - 45, 90, Arc2D.OPEN);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.05f * rect.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
		g.draw(arc);
	}

	private void drawMouthStraight(Graphics2D g, Rectangle rect)
	{
		float center_x = rect.x + rect.width / 2;
		float center_y = rect.y + rect.height / 2;
		float dx = rect.width * .20f;
		float dy = rect.height * .20f;
		Point2D.Float point1 = new Point2D.Float(center_x - dx, center_y + dy);
		Point2D.Float point2 = new Point2D.Float(center_x + dx, center_y + dy);
		Line2D.Float arc = new Line2D.Float(point1, point2);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.05f * rect.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
		g.draw(arc);
	}

	private void drawMouthSad(Graphics2D g, Rectangle rect)
	{
		float center_x = rect.x + rect.width / 2;
		float center_y = rect.y + rect.height / 2;
		float dx = rect.width * .25f;
		float dy = rect.height * .25f;
		Rectangle2D.Float rect2 = new Rectangle2D.Float(center_x - dx, center_y + dy, dx * 2, dy * 2);
		Arc2D.Float arc = new Arc2D.Float(rect2, 45, 90, Arc2D.OPEN);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.05f * rect.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
		g.draw(arc);

	}

	public void setSex(Sex sex)
	{
		this.sex = sex;

	}

	public void setProperties(Map<String, String> properties, Creature.Sex sex)
	{
		this.sex = sex;
		this.properties = properties;
		Map<String, String> filteredProperties = filter(properties);
		setBodyColor(filteredProperties);
		setEyeColor(filteredProperties);
		setEyeShape(filteredProperties);
		setMouthShape(filteredProperties);
		setSterile(filteredProperties);
		updateTooltipString();
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

	private void setBodyColor(Map<String, String> properties)
	{
		String key = "BodyColor".toLowerCase(); //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			bodyColor = Colors.parseName(value, Color.yellow);
		}
	}

	private void setEyeColor(Map<String, String> properties)
	{
		String key = "EyeColor".toLowerCase(); //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			eyeColor = Colors.parseName(value, Color.black);
		}
	}

	private void setEyeShape(Map<String, String> properties)
	{
		String key = "EyeShape".toLowerCase(); //$NON-NLS-1$
		String value = properties.get(key);
		try
		{
			eyes = Eyes.parse(value);
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
	}

	private void setMouthShape(Map<String, String> properties)
	{
		String key = "MouthShape".toLowerCase(); //$NON-NLS-1$
		String value = properties.get(key);
		try
		{
			mouth = Mouth.parse(value);
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
	}

	private void setSterile(Map<String, String> properties)
	{
		String key = GeneticModel.sterile.toLowerCase();
		String value = properties.get(key);
		try
		{
			sterile = Boolean.parseBoolean(value);
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public String getName()
	{
		String ret = super.getName();
		if (ret == null)
		{
			ret = this.name;
		}
		return ret != null ? ret : ""; //$NON-NLS-1$
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
		super.setName(name);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return CommonData.getPreferredSize();
	}

	public JComponent getJComponent()
	{
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : getTooltipProperties().entrySet())
		{
			sb.append("\n<br>" + entry.getKey() + ":\t" + entry.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (gelProperties != null)
		{
			sb.append("\n<br>" + gelProperties); //$NON-NLS-1$
		}
		sb.append("\n<br>Note:" + (note != null ? note : "")); //$NON-NLS-1$ //$NON-NLS-2$
		return "<html>Visualizer:\t" + this.getClass().getSimpleName() + " \n<br>Name:\t" + getName() + sb + " </html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		/*
		 * return "<html>Visualizer:\t" + this.getClass().getSimpleName() // + " \n<br>Name:\t" + getName() + "\n<br>" // + "Body Color:\t" +
		 * Colors.parseColor(bodyColor) + " \n<br>" // + "Eye Color:\t" + Colors.parseColor(eyeColor) + " \n<br>" // + "Eye Shape:\t" + eyes + " \n<br>" // +
		 * "Mouth Shape:\t" + mouth + " \n<br>" // + "Sex:\t" + sex + "<br>" // + "Sterile:\t" + sterile + "<br>" // + "Note:" + (note != null ? note : "") +
		 * "</html>";
		 */
	}

	private void updateTooltipString()
	{
		setToolTipText(toString());
	}

	public static void main(String[] args)
	{
		JFrame test = new JFrame();
		Fly s = new Fly();
		s.setName("My fly"); //$NON-NLS-1$
		test.getContentPane().setLayout(new BorderLayout());
		test.getContentPane().add(s);
		test.pack();
		test.setVisible(true);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Map<String, String> getTooltipProperties()
	{
		Map<String, String> ret = new TreeMap<String, String>();
		for (Entry<String, String> entry : properties.entrySet())
		{
			if (entry.getKey().equalsIgnoreCase(GeneticModel.lethal))
			{
				continue;
			}
			ret.put(entry.getKey(), entry.getValue());
		}
		return ret;
	}

	public UIClass getUIClass()
	{
		return UIClass.Fly;
	}

}
