package star.genetics.visualizers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JFrame;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.common.CommonData;

public class Worm extends JComponent implements Visualizer
{

	private static final long serialVersionUID = 1L;
	private Creature.Sex sex = Sex.MALE;

	private float bodyLength = 1;
	private float bodyWidth = 1;
	private boolean roller = false;
	private boolean blistered = false;
	private boolean unc = false;
	private Map<String, String> properties = null;
	private String note = "";
	private boolean sterile = false;

	public void setNote(String note)
	{
		this.note = note;
		updateTooltipString();
	}

	protected double getOffset()
	{
		double offset = (System.currentTimeMillis() % 10000) / 10000.0 - .5;
		offset = offset > 0 ? offset : -offset;
		return offset;
	}

	public Creature.Sex getSex()
	{
		return sex;
	}

	@Override
	protected void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		Graphics g = g1;
		try
		{
			g = g.create();
		}
		catch (Exception ex)
		{

		}
		if (g instanceof Graphics2D)
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.black);

			drawName(g2, new Rectangle(0, getHeight() - 20, getWidth(), 20));
			paintSex2(g2, new Rectangle(getWidth() - 30, 0, 30, 30), getSex());
			drawSterile(g2, new Rectangle(0, 0, getWidth(), getHeight()));
			drawWorm(g2, getOffset());
		}
	}

	void drawSterile(Graphics2D g, Rectangle rect)
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

	void paintSex2(Graphics2D g, Rectangle rect, Creature.Sex sex)
	{
		if (sex != null)
		{
			float scale = .375f;
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

				// Line2D.Float line4 = new Line2D.Float(center_x + dx * (float) (Math.sqrt(.5) / 2 + .01f), center_y + dy * (float) Math.sqrt(.5) / 2, center_x
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

	void drawName(Graphics2D g, Rectangle rect)
	{
		String name = getName();
		if (name != null && name.length() != 0)
		{
			g.setColor(java.awt.Color.black);
			Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);
			g.drawString(getName(), rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + (int) r.getHeight());
		}
	}

	void drawWorm(Graphics2D g, double offset)
	{
		Dimension d = getSize();
		if (unc)
		{
			offset = ((System.currentTimeMillis() * 10) % 10000) / 10000.0 - .5;
			offset = offset > 0 ? offset : -offset;
			offset /= 8;
		}
		g.scale(d.width / 3.0, d.height / 3.0);
		if (!roller)
		{
			offset *= 3;
			g.translate(offset / 2 * bodyLength, 1);
			g.scale(.25, .25);
			GeneralPath body = new GeneralPath();
			body.moveTo(0f, (float) Math.cos(offset));
			double step = .2;
			double multip = 3;
			double yoffset = .25;
			double axis;
			boolean last = true;
			for (axis = step; axis < multip * Math.PI; axis += step)
			{
				if (blistered && Math.abs(axis - 2.25 * Math.PI) < 5 * step && !last)
				{

					body.curveTo((float) ((axis - 3 * step) * bodyLength), (float) (bodyWidth * Math.cos(offset + axis - step) - .5), (float) ((axis + step) * bodyLength), (float) (bodyWidth * Math.cos(offset + axis - step) - .5), (float) (axis * bodyLength), (float) (bodyWidth * Math.cos(offset + axis)));
					last = true;
				}
				else
				{
					body.quadTo((float) ((axis - step) * bodyLength), (float) (bodyWidth * Math.cos(offset + axis - step)), (float) (axis * bodyLength), (float) (bodyWidth * Math.cos(offset + axis)));
					last = false;
				}
			}
			body.lineTo((float) (axis * bodyLength), (float) (Math.cos(offset + axis) + bodyWidth * (1 / Math.cosh((axis - multip / 2 * Math.PI) / Math.PI) - 1 / Math.cosh(-multip / 2) + yoffset)));
			for (axis = multip * Math.PI; axis > 0; axis -= step)
			{
				body.quadTo((float) ((axis + step) * bodyLength), (float) (Math.cos(offset + axis + step) + bodyWidth * (1 / Math.cosh((axis - multip / 2 * Math.PI) / Math.PI) - 1 / Math.cosh(-multip / 2) + yoffset)), (float) (axis * bodyLength),
				        (float) (bodyWidth * Math.cos(offset + axis) + (1 / Math.cosh((axis - multip / 2 * Math.PI) / Math.PI) - 1 / Math.cosh(-multip / 2) + yoffset)));
			}
			body.lineTo(0f, (float) Math.cos(offset));

			g.setStroke(new BasicStroke(.05f));
			g.draw(body);
		}
		else
		{
			g.translate(1.5, 1.25);
			g.rotate(offset * 10);
			g.scale(bodyLength, bodyLength);
			GeneralPath body = new GeneralPath();
			body.moveTo(1, 0);

			body.quadTo(1, 1, 0, 1);
			body.quadTo(-1, 1, -1, 0);
			body.quadTo(-1, -1, 0, -1);

			body.lineTo(0f, -.95f);
			body.quadTo(-.9f, -.9f, -.9f, 0f);
			body.quadTo(-.86f, .86f, 0.0f, .86f);
			body.quadTo(.88f, .88f, .95f, 0f);
			body.lineTo(1, 0);
			g.setColor(new Color(200, 200, 200));
			g.setStroke(new BasicStroke(.01f));
			g.fill(body);
			if (blistered)
			{
				for (int i = 0; i < 3; i++)
				{
					double angle = Math.PI / 3 + Math.PI / 8 * Math.pow(i, 1.15);
					Ellipse2D e = new Ellipse2D.Double(Math.cos(angle) * .95, Math.sin(angle) * .95, .2, .2);
					g.fill(e);
				}
			}
		}
		g.dispose();
	}

	//
	//
	// private double getY(double x)
	// {
	// return .5 - .125 * Math.sin(x * 12f);
	// }
	//
	// private double getX(double x)
	// {
	// return x * bodyLength;
	// }
	//
	// private double getF(double x)
	// {
	// if (x < .2)
	// {
	// return 1 + x * bodyWidth;
	// }
	// if (x > .8)
	// {
	// return 1 + (1 - x) * bodyWidth;
	// }
	//
	// return 1 + .2 * bodyWidth;
	// }
	//
	// private void drawWorm2(Graphics2D g)
	// {
	// Dimension d = getSize();
	// double offset = (System.currentTimeMillis() % 10000) / 10000.0 - .5;
	// offset = offset > 0 ? offset : -offset;
	// if (unc)
	// {
	// offset = ((System.currentTimeMillis() * 10) % 10000) / 10000.0 - .5;
	// offset = offset > 0 ? offset : -offset;
	// offset /= 8;
	// }
	// double step = .01;
	// g.scale(d.width / 2, d.height / 2);
	// if (!roller)
	// {
	// g.translate(offset + .5, .5);
	// for (double i = 0.00; i < 1; i += step)
	// {
	// g.setStroke(new BasicStroke(.01f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	// Line2D l = new Line2D.Double(getX(i), getY(i + offset), getX(i + step), getY(i + step + offset));
	// Line2D l2 = new Line2D.Double(getX(i), getF(i) * getY(i + offset), getX(i + step), getF(i + step) * getY(i + step + offset));
	// g.draw(l);
	// g.draw(l2);
	// }
	// if (blistered)
	// {
	// double i = .8;
	// double bx = .1;
	// double by = .1;
	// double delta = .06;
	// Point2D p1 = new Point2D.Double(getX(i), getY(i + offset));
	// Point2D p2 = new Point2D.Double(getX(i + delta), getY(i + delta + offset));
	// CubicCurve2D line = new CubicCurve2D.Double(p1.getX(), p1.getY(), p1.getX(), p1.getY() / getF(i), p2.getX(), p2.getY() / getF(i), p2.getX(), p2.getY());
	// g.draw(line);
	//
	// }
	// g.translate(-offset, 0);
	// }
	// else
	// {
	// g.translate(.5, .5);
	//
	// double xf = .1;
	// double yf = .2;
	// double centerX = .5;
	// double centerY = .5;
	// double wX = .5;
	// double wY = .5;
	// g.rotate(offset * 5, centerX, centerY);
	// Arc2D arc_inner = new Arc2D.Double(centerX - wX, centerY - wY, centerX + wX, centerY + wY, 0, 270, Arc2D.OPEN);
	// g.setStroke(new BasicStroke(.01f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	// g.draw(arc_inner);
	//
	// double centerX2 = wX * Math.sin(Math.PI / 180 * 45);
	// double centerY2 = wY * Math.cos(Math.PI / 180 * 45);
	// Arc2D arc_inner2 = new Arc2D.Double(centerX2 / 10 + centerX - wX, centerY2 / 10 + centerY - wY, centerX + wX * 9 / 10, centerY + wY * 9 / 10, 0, 270,
	// Arc2D.OPEN);
	// g.draw(arc_inner2);
	// if (blistered)
	// { // Blistered
	// double centerX3 = wX * Math.sin(Math.PI / 180 * 75);
	// double centerY3 = wY * Math.cos(Math.PI / 180 * 75);
	// Point2D p1 = new Point2D.Double(centerX + centerX2, centerY - centerY2);
	// Point2D p2 = new Point2D.Double(centerX + centerX3, centerY - centerY3);
	// CubicCurve2D line = new CubicCurve2D.Double(p1.getX(), p1.getY(), p1.getX() * 1.1, p1.getY() * 1.1, p2.getX() * 1.2, p2.getY() * 1.2, p2.getX(),
	// p2.getY());
	// g.draw(line);
	// }
	// g.translate(-.5, -.5);
	// }
	// g.scale(1.0 / d.width * 2, 1.0 / d.height * 2);
	//
	// repaint();
	// }

	public void setSex(Sex sex)
	{
		this.sex = sex;

	}

	public void setProperties(Map<String, String> properties, Creature.Sex sex)
	{
		this.sex = sex;
		this.properties = properties;
		Map<String, String> filteredProperties = filter(properties);
		setBodyLength(filteredProperties);
		setBodyWidth(filteredProperties);
		setRoller(filteredProperties);
		setBlistered(filteredProperties);
		setUnc(filteredProperties);
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

	@Override
	public void setName(String name)
	{
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
			sb.append("\n<br>" + entry.getKey() + ":\t" + entry.getValue());
		}
		sb.append("\n<br>Note:" + (note != null ? note : ""));
		return "<html>Visualizer:\t" + this.getClass().getSimpleName() + " \n<br>Name:\t" + getName() + sb + " </html>";
	}

	private void updateTooltipString()
	{
		setToolTipText(toString());
	}

	public static void main(String[] args)
	{
		JFrame test = new JFrame();
		Worm s = new Worm();
		s.setName("My Worm");
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

	private void setUnc(Map<String, String> filteredProperties)
	{
		unc = parseBoolean(filteredProperties, "unc");
	}

	private void setRoller(Map<String, String> filteredProperties)
	{
		roller = parseBoolean(filteredProperties, "roller");
	}

	private void setBlistered(Map<String, String> filteredProperties)
	{
		blistered = parseBoolean(filteredProperties, "blistered");
	}

	private void setBodyWidth(Map<String, String> filteredProperties)
	{
		bodyWidth = parseFloat(filteredProperties, "bodywidth");
	}

	private void setBodyLength(Map<String, String> filteredProperties)
	{
		bodyLength = parseFloat(filteredProperties, "bodylength");
	}

	private void setSterile(Map<String, String> filteredProperties)
	{
		String key = GeneticModel.sterile.toLowerCase();
		sterile = parseBoolean(filteredProperties, key);

	}

	private float parseFloat(Map<String, String> filteredProperties, String key)
	{
		float ret = 1;
		String value = filteredProperties.get(key);
		if (value != null)
		{
			try
			{
				if ("wildtype".equals(value))
				{
					ret = 1;
				}
				else
				{
					ret = Float.parseFloat(value);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return ret;
	}

	private boolean parseBoolean(Map<String, String> filteredProperties, String key)
	{
		boolean ret = false;
		String value = filteredProperties.get(key);
		if (value != null)
		{
			try
			{
				if ("wildtype".equals(value))
				{
					ret = false;
				}
				else
				{
					ret = Boolean.parseBoolean(value);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return ret;
	}

	public UIClass getUIClass()
	{
		return UIClass.Fly;
	}

}
