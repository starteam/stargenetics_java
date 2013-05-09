package star.genetics.visualizers;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JFrame;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.GeneticModel;
import star.genetics.v1.ui.common.CommonData;
import utils.Colors;

public class Fly extends JComponent implements Visualizer
{

	private static final long serialVersionUID = 1L;
	private float aristaeLength = .05f;
	private double wingSize = 1.0;
	private boolean wingVein = true;
	private java.awt.Color headColor = java.awt.Color.black;
	private java.awt.Color eyeColor = java.awt.Color.red;
	private java.awt.Color bodyColor = new java.awt.Color(152, 118, 84);
	private java.awt.Color wingColor = new java.awt.Color(90, 90, 128, 128);
	private java.awt.Color legsColor = java.awt.Color.black;
	private java.awt.Color veinColor = new java.awt.Color(64, 64, 96, 128);
	private Creature.Sex sex;
	private String note = ""; //$NON-NLS-1$
	private boolean sterile = false;
	private int patchesCount = 0;
	private java.awt.Color patchesColor = null;
	private java.awt.Color patchesBgColor = null;
	private float patchesSize = 0f;

	public void setNote(String note)
	{
		this.note = note;
		updateTooltipString();
	}

	private java.awt.Color getVeinColor()
	{
		return veinColor;
	}

	/**
	 * @return the aristaeLength
	 */
	private float getAristaeLength()
	{
		return aristaeLength;
	}

	/**
	 * @return the headColor
	 */
	private java.awt.Color getHeadColor()
	{
		return headColor;
	}

	/**
	 * @return the eyeColor
	 */
	private java.awt.Color getEyeColor()
	{
		return eyeColor;
	}

	/**
	 * @param eyeColor
	 *            the eyeColor to set
	 */
	private void setEyeColor(java.awt.Color eyeColor)
	{
		this.eyeColor = eyeColor;
	}

	/**
	 * @return the bodyColor
	 */
	private java.awt.Color getBodyColor()
	{
		return bodyColor;
	}

	/**
	 * @param bodyColor
	 *            the bodyColor to set
	 */
	private void setBodyColor(java.awt.Color bodyColor)
	{
		this.bodyColor = bodyColor;
	}

	/**
	 * @return the wingColor
	 */
	private java.awt.Color getWingColor()
	{
		return wingColor;
	}

	/**
	 * @return the legsColor
	 */
	private java.awt.Color getLegsColor()
	{
		return legsColor;
	}

	/**
	 * @param aristaeLength
	 *            the aristaeLength to set
	 */
	private void setAristaeLength(float aristaeLength)
	{
		this.aristaeLength = aristaeLength / 20;
	}

	public void setSex(Creature.Sex sex)
	{
		this.sex = sex;
	}

	public static void main(String[] args)
	{
		JFrame test = new JFrame();
		test.getContentPane().setLayout(new BorderLayout());
		test.getContentPane().add(new Fly());
		test.pack();
		test.setVisible(true);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	protected void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle rect1 = this.getBounds();
		if (getBackground() != null)
		{
			g.setColor(getBackground());
			g.fill(rect1);
		}
		rect1.x = 0;
		rect1.y = 0;
		Rectangle2D labelRect = getName() != null ? g.getFontMetrics().getStringBounds(getName(), g) : new Rectangle2D.Float(0, 0, 1, 1);

		int offset = (int) (rect1.getHeight() * 0.02f);
		int by = 0;
		int bh = rect1.height - (by * 2 + (int) (1.2f * labelRect.getHeight()) + offset);

		int bx = rect1.x + rect1.width / 10;
		int bw = rect1.width - 2 * rect1.width / 10;

		bh = Math.min(bw, bh);
		bw = bh;
		bx = (rect1.width - bh) / 2;

		Rectangle body_rectange = new Rectangle(bx + (patchesColor != null ? 15 : 0), by, bw, bh);

		Dimension d = getSize();
		// g.translate(d.height / 10 / 2, d.width / 10 / 2);
		d.height *= .9f;
		d.width *= .9f;
		paintLegs(g, getLegsColor(), body_rectange, sex);
		paintHead(g, getHeadColor(), body_rectange);
		paintAristae(g, getHeadColor(), getAristaeLength(), body_rectange);
		paintEyes(g, patchesBgColor != null ? patchesBgColor : getEyeColor(), body_rectange);
		paintBody(g, getBodyColor(), body_rectange);
		paintWings(g, getWingColor(), getVeinColor(), body_rectange);
		body_rectange.y = offset;
		body_rectange.height -= offset;
		paintSex2(g, body_rectange, sex);

		int rest = rect1.height - by - bh;
		Rectangle name_rectange = new Rectangle(bx, by + bh - offset * 3, bw, rest);
		drawName(g, name_rectange);
		drawSterile(g, body_rectange);
		drawMatings(g, body_rectange);
	}

	private void drawMatings(Graphics2D g, Rectangle rect)
	{
		if (properties == null)
		{
			return;
		}
		String matings = properties.get(GeneticModel.matings);
		if (sterile)
		{
			matings = "0"; //$NON-NLS-1$
		}
		if (matings != null && matings.length() != 0 && getName() != null && getName().length() != 0)
		{
			try
			{
				int m = Integer.parseInt(matings);
				if (m < 10)
				{
					matings = Messages.getString("Fly.2") + matings; //$NON-NLS-1$
					if (sterile)
					{
						matings = Messages.getString("Fly.3"); //$NON-NLS-1$
					}
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

	private void drawName(Graphics2D g, Rectangle rect)
	{
		String name = getName();
		if (name != null && name.length() != 0)
		{
			g.setColor(java.awt.Color.black);
			Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);
			g.drawString(getName(), rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + (int) r.getHeight());
		}
	}

	private void paintAristae(Graphics2D g, Color c, float length, Rectangle r)
	{
		float center_x = r.x + r.width / 2;
		float center_y = r.y + r.height / 2;
		float offset_x = r.width * .066f;
		float offset = r.width;
		float offset_yy = r.height * .30f;

		g.setColor(c);
		if (true)
		{
			Shape s = new Line2D.Double(center_x - offset_x, center_y - offset_yy, center_x - offset_x - offset * length, center_y - offset_yy - offset * length);
			g.setStroke(new BasicStroke(1f));
			g.draw(s);
		}
		if (true)
		{
			Shape s = new Line2D.Float(center_x + offset_x, center_y - offset_yy, center_x + offset_x + offset * length, center_y - offset_yy - offset * length);
			g.setStroke(new BasicStroke(1f));
			g.draw(s);
		}

	}

	private void paintLegs(Graphics2D g, Color c, Rectangle r, Creature.Sex sex)
	{
		if (true)
		{
			g.setColor(c);
			float center_x = r.x + r.width / 2;
			float center_y = r.y + r.height / 2;
			float leg_r_x = (int) (center_x - r.width * .1f);
			float leg_r_y = (int) (center_y - r.height * .1f);
			float leg_r_tx = r.width * .1f;
			float leg_r_ty = r.height * .1f;
			float leg_r_mx = r.width * .1f;
			float leg_r_my = -r.height * .05f;
			float leg_r_bx = r.width * .1f;
			float leg_r_by = -r.height * .3f;

			float leg_l_x = (int) (center_x + r.width * .1f);
			float leg_l_y = (int) (center_y - r.height * .1f);
			float leg_l_tx = -r.width * .1f;
			float leg_l_ty = r.height * .1f;
			float leg_l_mx = -r.width * .1f;
			float leg_l_my = -r.height * .05f;
			float leg_l_bx = -r.width * .1f;
			float leg_l_by = -r.height * .3f;

			g.setStroke(new BasicStroke(0.008f * r.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f));
			g.draw(new Line2D.Float(leg_r_x, leg_r_y, leg_r_x - leg_r_tx, leg_r_y - leg_r_ty));
			g.draw(new Line2D.Float(leg_r_x, leg_r_y, leg_r_x - leg_r_mx, leg_r_y - leg_r_my));
			g.draw(new Line2D.Float(leg_r_x, leg_r_y, leg_r_x - leg_r_bx, leg_r_y - leg_r_by));
			g.draw(new Line2D.Float(leg_l_x, leg_l_y, leg_l_x - leg_l_tx, leg_l_y - leg_l_ty));
			g.draw(new Line2D.Float(leg_l_x, leg_l_y, leg_l_x - leg_l_mx, leg_l_y - leg_l_my));
			g.draw(new Line2D.Float(leg_l_x, leg_l_y, leg_l_x - leg_l_bx, leg_l_y - leg_l_by));
			if (Creature.Sex.MALE.equals(sex))
			{
				g.fill(new Ellipse2D.Float(leg_r_x - leg_r_tx * .75f, leg_r_y - leg_r_ty * .75f, 2, 2));
				g.fill(new Ellipse2D.Float(leg_l_x - leg_l_tx * .75f - 2, leg_l_y - leg_l_ty * .75f, 2, 2));
			}

		}
	}

	private void paintHead(Graphics2D g, Color c, Rectangle r)
	{
		if (true)
		{
			float center_x = r.x + r.width / 2;
			float center_y = r.y + r.height / 2;
			float offset_x = r.width * .10f;
			float offset_y = r.height * .15f;
			float offset_yy = r.height * .30f;

			Rectangle2D headRect = new Rectangle2D.Float(center_x - offset_x, center_y - offset_yy, offset_x * 2, offset_y);
			Arc2D s = new Arc2D.Float(headRect, 0, 360, Arc2D.PIE);
			g.setColor(c);
			g.fill(s);
		}

	}

	private void paintEyes(Graphics2D g, Color c, Rectangle r)
	{
		float center_x = r.x + r.width / 2;
		float center_y = r.y + r.height / 2;
		float offset_x = r.width * .066f;
		float offset_xx = r.width * .06f;
		float offset_y = r.height * .10f;
		float offset_yy = r.height * .30f;

		float offset = r.width * .005f;
		if (true)
		{
			Rectangle2D rect = new Rectangle2D.Float(center_x - offset_x - offset_xx, center_y - offset_yy, 2 * offset_xx, offset_y);
			Rectangle2D rect2 = new Rectangle2D.Float(center_x - offset_x - offset_xx - offset, center_y - offset_yy - offset, 2 * offset_xx + 2 * offset, offset_y + 2 * offset);
			g.setColor(getHeadColor());
			g.fill(new Arc2D.Float(rect2, 0, 360, Arc2D.PIE));
			Arc2D s = new Arc2D.Float(rect, 40, 180, Arc2D.PIE);
			g.setColor(c);
			g.fill(s);
		}
		if (true)
		{
			Rectangle2D rect = new Rectangle2D.Float(center_x + offset_x - offset_xx, center_y - offset_yy, 2 * offset_xx, offset_y);
			Rectangle2D rect2 = new Rectangle2D.Float(center_x + offset_x - offset_xx - offset, center_y - offset_yy - offset, 2 * offset_xx + 2 * offset, offset_y + 2 * offset);
			g.setColor(getHeadColor());
			g.fill(new Arc2D.Float(rect2, 0, 360, Arc2D.PIE));
			Arc2D s = new Arc2D.Float(rect, 320, 180, Arc2D.PIE);
			g.setColor(c);
			g.fill(s);
		}
		if (patchesColor != null)
		{
			try
			{
				paintEyes2(g, c, r);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void paintEyes2(Graphics2D g, Color c, Rectangle r)
	{
		float center_x = r.x + r.width / 2;
		float center_y = r.y + r.height / 2;
		float offset_x = r.width * .666f;
		float offset_xx = r.width * .16f;
		float offset_y = r.height * .45f;
		float offset_yy = r.height * .40f;

		float offset = r.width * .005f;
		if (true)
		{
			Rectangle2D rect = new Rectangle2D.Float(center_x - offset_x - offset_xx, center_y - offset_yy, 2 * offset_xx, offset_y);
			Rectangle2D rect2 = new Rectangle2D.Float(center_x - offset_x - offset_xx - offset, center_y - offset_yy - offset, 2 * offset_xx + 2 * offset, offset_y + 2 * offset);
			// g.setColor(getHeadColor());
			// g.fill(new Arc2D.Float(rect2, 0, 360, Arc2D.PIE));
			Arc2D s = new Arc2D.Float(rect, 0, 360, Arc2D.OPEN);
			Color pc = getEyeColor();
			g.setColor(pc);
			GradientPaint gp = new GradientPaint(75, 60, pc, 95, 95, Color.white, true);
			// Fill with a gradient.
			g.setPaint(gp);
			g.fill(s);
			if ((pc.getRed() + pc.getGreen() + pc.getBlue()) > 600)
			{
				g.setStroke(new BasicStroke(.25f));
				g.setColor(Color.gray);
				g.draw(s);
			}
			Random random = new Random((getTooltipProperties() + getName()).toString().hashCode());
			if (!(patchesSize == 0 || patchesCount == 0))
			{

				int patch_count = patchesCount;
				float size = patchesSize;
				float radius = .3f;
				double factor = Math.PI * 2 / patch_count;
				g.setColor(patchesColor);
				if (patch_count == 1)
				{
					Rectangle2D rect_p = new Rectangle2D.Double(rect.getCenterX() - rect.getWidth() * size / 2, rect.getCenterY() - rect.getHeight() * size / 2, rect.getWidth() * size, rect.getHeight() * size);
					Arc2D s2 = new Arc2D.Float(rect_p, 0, 360, Arc2D.PIE);
					g.fill(s2);

				}
				else if (patch_count * size >= 5 || size > .9)
				{
					g.fill(s);

				}
				else

				{
					Shape clip = g.getClip();
					g.clip(s);
					for (int i = 0; i < patch_count; i++)
					{
						double foffset = random.nextFloat() * factor / 2;
						Rectangle2D rect_p = new Rectangle2D.Double(rect.getCenterX() + rect.getWidth() * Math.sin(i * factor + foffset) * radius * (1 + random.nextFloat() / 3) - rect.getWidth() * size / 2, rect.getCenterY() + rect.getHeight() * Math.cos(i * factor + foffset) * radius * (1 + random.nextFloat() / 3)
						        - rect.getHeight() * size / 2, rect.getWidth() * size * (7. / 8 + random.nextFloat() / 4), rect.getHeight() * size * (7. / 8 + random.nextFloat() / 4));
						Arc2D s2 = new Arc2D.Float(rect_p, 0, 360, Arc2D.PIE);
						g.fill(s2);
					}
					g.setClip(clip);
				}
			}
			g.setColor(new Color(128, 32, 32));
			g.setStroke(new BasicStroke(.45f));
			Line2D line1 = new Line2D.Double(r.getCenterX() - r.getWidth() * .5, r.getCenterY() - r.getHeight() * .4, r.getCenterX() - r.getWidth() * .1, r.getCenterY() - r.getWidth() * .32);
			g.draw(line1);
			Line2D line2 = new Line2D.Double(r.getCenterX() - r.getWidth() * .5, r.getCenterY() - r.getHeight() * 0, r.getCenterX() - r.getWidth() * .1, r.getCenterY() - r.getWidth() * .25);
			g.draw(line2);
			Rectangle2D rect_p = new Rectangle2D.Double(rect.getCenterX() - rect.getWidth() / 2, rect.getCenterY() - rect.getHeight() * 3 / 4, rect.getWidth() * 1.25, rect.getHeight() * 1.5);
			Arc2D s2 = new Arc2D.Float(rect_p, 335, 60, Arc2D.OPEN);
			g.draw(s2);
		}
	}

	private void paintBody(Graphics2D g, Color c, Rectangle r)
	{
		float center_x = r.x + r.width / 2;
		float center_y = r.y + r.height / 2;

		if (sex != null)
		{
			switch (sex)
			{
			case MALE:
				if (true)
				{
					float offset_x = r.width * .11f;
					float offset_y = r.height * .15f;
					float offset_y_1 = r.height * .5f;
					float offset_y_2 = r.height * .24f;
					g.setColor(c);
					g.fill(new Arc2D.Float(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y_2, offset_x * 2, offset_y_2), 0, 180, Arc2D.PIE));
					g.fill(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y, offset_x * 2, offset_y + 1));
					g.fill(new Arc2D.Float(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y_1 / 2, offset_x * 2, offset_y_1), 180, 180, Arc2D.PIE));
				}
				break;
			case FEMALE:
				if (true)
				{
					float offset_x = r.width * .11f;
					float offset_x_2 = r.width * .25f;
					float offset_x_3 = r.width * .11f * 1.35f;
					float offset_y = r.height * .15f;
					float offset_y_1 = r.height * .75f;
					float offset_y_2 = r.height * .24f;
					g.setColor(c);
					g.fill(new Arc2D.Float(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y_2, offset_x * 2, offset_y_2), 0, 180, Arc2D.PIE));
					g.fill(new CubicCurve2D.Float(center_x - offset_x, center_y - offset_y_2, center_x - offset_x, center_y, center_x - offset_x_2, center_y + offset_y, center_x, center_y + offset_y_2));
					g.fill(new CubicCurve2D.Float(center_x + offset_x, center_y - offset_y_2, center_x + offset_x, center_y, center_x + offset_x_2, center_y + offset_y, center_x, center_y + offset_y_2));
					g.fill(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y, offset_x * 2, offset_y + 1));
					g.fill(new Arc2D.Float(new Rectangle2D.Double(center_x - offset_x_3, center_y - offset_y_1 / 2, offset_x_3 * 2, offset_y_1), 180, 180, Arc2D.PIE));
				}
				break;
			}
		}
		else
		{
			if (true)
			{
				float offset_x = r.width * .11f;
				float offset_y = r.height * .15f;
				float offset_y_1 = r.height * .5f;
				float offset_y_2 = r.height * .24f;

				g.setColor(c);
				g.fill(new Arc2D.Float(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y_2, offset_x * 2, offset_y_2), 0, 180, Arc2D.PIE));
				g.fill(new Rectangle2D.Double(center_x - offset_x, center_y - offset_y, offset_x * 2, offset_y + 1));
				g.fill(new Arc2D.Float(new Rectangle2D.Float(center_x - offset_x, center_y - offset_y_1 / 2, offset_x * 2, offset_y_1), 180, 180, Arc2D.PIE));
			}
		}
	}

	private void drawSterile(Graphics2D g, Rectangle rect)
	{
		if (sterile)
		{
			// String name = " s ";
			// g.setColor(java.awt.Color.red);
			// java.awt.Font original = g.getFont();
			// g.setFont(original.deriveFont(original.getSize2D() * 1f));
			// Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);
			// g.fillRect(rect.x + rect.width - (int) r.getWidth() , rect.y + 2, (int) r.getWidth(), (int) r.getHeight());
			// //g.drawLine((int) rect.getX(), (int) rect.getY(), (int) (rect.getX() + rect.getWidth()), (int) (rect.getY() + rect.getHeight()));
			// //g.drawLine((int) (rect.getX() + rect.getWidth()), (int) rect.getY(), (int) rect.getX(), (int) (rect.getY() + rect.getHeight()));
			// g.setColor(java.awt.Color.black);
			// g.drawString(name, rect.x+ rect.width - (int) r.getWidth() , rect.y + (int) r.getHeight() );
			// g.setFont(original);

			// String name = GeneticModel.sterile;
			// g.setColor(java.awt.Color.red);
			// java.awt.Font original = g.getFont();
			// g.setFont(original.deriveFont(original.getSize2D() * 1f));
			// Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);
			// g.fillRect(rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + rect.height / 2 - (int) r.getHeight() + 2, (int) r.getWidth(), (int)
			// r.getHeight());
			// // g.drawLine((int) rect.getX(), (int) rect.getY(), (int) (rect.getX() + rect.getWidth()), (int) (rect.getY() + rect.getHeight()));
			// // g.drawLine((int) (rect.getX() + rect.getWidth()), (int) rect.getY(), (int) rect.getX(), (int) (rect.getY() + rect.getHeight()));
			// g.setColor(java.awt.Color.black);
			// g.drawString(name, rect.x + rect.width / 2 - (int) r.getWidth() / 2, rect.y + rect.height / 2 + (int) r.getHeight() );
			// g.setFont(original);

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

	private void paintWings(Graphics2D g, Color c, Color vein_c, Rectangle r)
	{
		float center_x = r.x + r.width / 2;
		float center_y = r.y + r.height / 2;
		double w1 = r.width * .20 * wingSize;
		double h1 = r.width * .65 * wingSize;

		if (true)
		{
			double x0 = center_x - w1 * .75f;
			double y0 = center_y - h1 * .30f;

			Rectangle2D rect = new Rectangle2D.Double(x0, y0, w1, h1);
			Arc2D s = new Arc2D.Float(rect, 90, 270, Arc2D.PIE);
			g.setColor(c);
			g.fill(s);
			g.setColor(Color.red);
			if (wingSize > .25 && wingVein)
			{

				double lx0 = x0 + w1 / 2;
				double ly0 = y0 + h1 / 2;
				double lx1 = lx0 - w1 / 2 * Math.sqrt(.5f);
				double ly1 = ly0 + h1 / 2 * Math.sqrt(.5f);
				g.setStroke(new BasicStroke(1f));
				g.setColor(vein_c);
				g.draw(new Line2D.Double(lx0, ly0, lx1, ly1));
			}
		}

		if (true)
		{

			double x0 = center_x - w1 * .25f;
			double y0 = center_y - h1 * .30f;

			Rectangle2D rect = new Rectangle2D.Double(x0, y0, w1, h1);
			Arc2D s = new Arc2D.Float(rect, 180, 270, Arc2D.PIE);
			g.setColor(c);
			g.fill(s);
			if (wingSize > .25 && wingVein)
			{
				double lx0 = x0 + w1 / 2;
				double ly0 = y0 + h1 / 2;
				double lx1 = lx0 + w1 / 2 * Math.sqrt(.5f);
				double ly1 = ly0 + h1 / 2 * Math.sqrt(.5f);
				g.setStroke(new BasicStroke(1f));
				g.setColor(vein_c);
				g.draw(new Line2D.Double(lx0, ly0, lx1, ly1));
			}
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		return CommonData.getPreferredSize();
	}

	Map<String, String> properties;
	private String gelProperties;

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

	public void setProperties(Map<String, String> properties, Creature.Sex sex)
	{
		this.properties = properties;
		setSex(sex);
		Map<String, String> filteredProperties = filter(properties);
		setBodyColor(filteredProperties);
		setEyeColor(filteredProperties);
		setAristaeLength(filteredProperties);
		setLegsLength(filteredProperties);
		setWingSize(filteredProperties);
		setWingVein(filteredProperties);
		setSterile(filteredProperties);
		try
		{
			setPatches(filteredProperties);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		updateTooltipString();
	}

	private void setPatches(Map<String, String> filteredProperties)
	{
		final String key1 = "numberofpatches"; //$NON-NLS-1$
		String value1 = filteredProperties.get(key1);
		if (value1 != null)
		{
			if (!"wildtype".equals(value1)) //$NON-NLS-1$
			{

				try
				{
					patchesCount = (int) Float.parseFloat(value1);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		final String key2 = "patchsize"; //$NON-NLS-1$
		String value2 = filteredProperties.get(key2);
		if (value2 != null)
		{
			if (!"wildtype".equals(value2)) //$NON-NLS-1$
			{
				try
				{
					patchesSize = Float.parseFloat(value2);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			else
			{
				patchesSize = 0;
			}
		}
		final String key3 = "eyepatchcolor"; //$NON-NLS-1$
		String value3 = filteredProperties.get(key3);
		if (value3 != null)
		{
			if (!"wildtype".equals(value3)) //$NON-NLS-1$
			{
				try
				{
					patchesColor = Colors.parseName(value3, null);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

			}
			else
			{
				patchesColor = Color.red;
			}
		}
		final String key4 = "overalleyecolor"; //$NON-NLS-1$
		String value4 = filteredProperties.get(key4);
		if (value3 != null)
		{
			if (!"wildtype".equals(value2)) //$NON-NLS-1$
			{
				try
				{
					patchesBgColor = Colors.parseName(value4, Color.red);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			{
				patchesBgColor = Color.red;
			}
		}

	}

	private void setBodyColor(Map<String, String> properties)
	{
		final String key = "bodycolor"; //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			setBodyColor(Colors.parseName(value, Color.blue));
		}
	}

	private void setEyeColor(Map<String, String> properties)
	{
		final String key = "eyecolor"; //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			if ("wildtype".equals(value)) //$NON-NLS-1$
			{
				setEyeColor(Color.red);
			}
			else
			{
				setEyeColor(Colors.parseName(value, Color.red));
			}
		}
	}

	private void setAristaeLength(Map<String, String> properties)
	{
		final String key = "aristae"; //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			try
			{
				if ("wildtype".equalsIgnoreCase(value)) //$NON-NLS-1$
				{
					setAristaeLength(1f);
				}
				else
				{
					setAristaeLength(Float.parseFloat(value));
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void setLegsLength(Map<String, String> properties)
	{
		final String key = "legs"; //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			try
			{
				// setLegsLength( Double.parseDouble( value ) ) ;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

	}

	private void setWingSize(Map<String, String> properties)
	{
		final String key = "wingsize"; //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			try
			{
				if ("wildtype".equals(value)) //$NON-NLS-1$
				{
					wingSize = 1;
				}
				else
				{
					wingSize = Double.parseDouble(value);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void setWingVein(Map<String, String> properties)
	{
		final String key = "wingvein"; //$NON-NLS-1$
		String value = properties.get(key);
		if (value != null)
		{
			try
			{
				wingVein = Boolean.parseBoolean(value);
				if (value.equalsIgnoreCase("wildtype")) //$NON-NLS-1$
				{
					wingVein = true;
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
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
			sb.append("\n<br>"); //$NON-NLS-1$
			sb.append(gelProperties);
		}
		sb.append("\n<br>Note:" + (note != null ? note : "")); //$NON-NLS-1$ //$NON-NLS-2$
		if (getName() != null)
		{
			return "<html>Visualizer:\t" + this.getClass().getSimpleName() + " \n<br>Name:\t" + getName() + sb + " </html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		else
		{
			return "<html>Visualizer:\t" + this.getClass().getSimpleName() + " \n<br>" + sb + " </html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	private void updateTooltipString()
	{
		setToolTipText(toString());
	}

	public Map<String, String> getTooltipProperties()
	{
		final String translation_prefix = "Fly.";
		Map<String, String> ret = new TreeMap<String, String>()
		{
			private static final long serialVersionUID = 1L;

			public String put(String key, String value)
			{
				String translation_key = translation_prefix + filter(key);
				String translated = Messages.getString(translation_key);
				if (translated.contains(translation_key))
				{
					translated = key;
				}
				return super.put(translated, value);
			};
		};
		if (properties != null)
		{
			boolean removePatchColor = false;
			for (Entry<String, String> entry : properties.entrySet())
			{
				if (filter(entry.getKey()).startsWith("numberofpatches")) //$NON-NLS-1$
				{
					String val = entry.getValue();
					if ("wildtype".equals(val)) //$NON-NLS-1$
					{
						removePatchColor = true;
					}
					else
					{
						try
						{
							float count = Float.parseFloat(val);
							if (count == 0)
							{
								removePatchColor = true;
							}
						}
						catch (Exception ex)
						{
						}
					}
				}
			}
			for (Entry<String, String> entry : properties.entrySet())
			{
				if (entry.getKey().equalsIgnoreCase(GeneticModel.lethal))
				{
					continue;
				}
				if (wingSize < .3 && entry.getKey().equalsIgnoreCase("wingvein")) //$NON-NLS-1$
				{
					continue;
				}
				if (filter(entry.getKey()).startsWith("patchsize")) //$NON-NLS-1$
				{
					continue;
				}
				if (filter(entry.getKey()).startsWith("overalleyecolor")) //$NON-NLS-1$
				{
					continue;
				}
				if (this.patchesCount != 0 & filter(entry.getKey()).startsWith("eyecolor")) //$NON-NLS-1$
				{
					ret.put(entry.getKey(), "patchy"); //$NON-NLS-1$
					continue;
				}

				if (removePatchColor && filter(entry.getKey()).startsWith("eyepatchcolor")) //$NON-NLS-1$
				{
					continue;
				}
				if (filter(entry.getKey()).startsWith("numberofpatches")) //$NON-NLS-1$
				{
					String val = entry.getValue();
					if ("wildtype".equals(val)) //$NON-NLS-1$
					{
						ret.put(entry.getKey(), "0"); //$NON-NLS-1$
						continue;
					}
				}
				if (filter(entry.getKey()).startsWith("sterile")) //$NON-NLS-1$
				{
					if (sterile)
					{
						ret.put(entry.getKey(), "sterile"); //$NON-NLS-1$
					}
					continue;
				}
				ret.put(entry.getKey(), entry.getValue());
			}
		}
		return ret;
	}

	public UIClass getUIClass()
	{
		// TODO Auto-generated method stub
		return UIClass.Fly;
	}
}
