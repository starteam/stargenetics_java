package star.genetics.v2.gel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JComponent;

import star.genetics.Messages;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Creature.Sex;
import star.genetics.genetic.model.Gel;
import star.genetics.genetic.model.GelPosition;

public class GelVisual extends JComponent
{
	private static final long serialVersionUID = 1L;
	private Gel gel;
	private TreeMap<Float, Integer> map = new TreeMap<Float, Integer>();
	private TreeMap<Float, Integer> allMap = new TreeMap<Float, Integer>();
	private ArrayList<Entry<Sex, TreeMap<Float, Integer>>> parentMap = new ArrayList<Map.Entry<Sex, TreeMap<Float, Integer>>>();

	public GelVisual(Gel g, Iterable<GelPosition> gp, ArrayList<Entry<Sex, Iterable<GelPosition>>> parentPositions)
	{
		this.gel = g;
		for (GelPosition gg : gp)
		{
			if (gg.getGel().equals(gel))
			{
				add(gg.getPosition(), map);
			}
		}
		for (GelPosition gg : g)
		{
			add(gg.getPosition(), allMap);
		}
		for (Entry<Sex, Iterable<GelPosition>> parentEntry : parentPositions)
		{
			final Sex sex = parentEntry.getKey();
			final TreeMap<Float, Integer> pMap = new TreeMap<Float, Integer>();
			Iterable<GelPosition> gpp = parentEntry.getValue();
			for (GelPosition gg : gpp)
			{
				if (gg.getGel().equals(gel))
				{
					add(gg.getPosition(), pMap);
				}
			}
			parentMap.add(new Entry<Creature.Sex, TreeMap<Float, Integer>>()
			{

				@Override
				public TreeMap<Float, Integer> setValue(TreeMap<Float, Integer> value)
				{
					return pMap;
				}

				@Override
				public TreeMap<Float, Integer> getValue()
				{
					return pMap;
				}

				@Override
				public Sex getKey()
				{
					return sex;
				}
			});
		}
	}

	private void add(Float[] pp, Map<Float, Integer> map)
	{
		for (float p : pp)
		{
			if (map.containsKey(p))
			{
				map.put(p, map.get(p) + 1);
			}
			else
			{
				map.put(p, 1);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();
		int workAreaX = 25;
		int workAreaY = 28;
		int lanes = 2 + parentMap.size();
		// draw Labels
		int label0X = workAreaX;
		int label0Y = workAreaY;
		int labelStep = (width - label0X) / lanes;

		int label1X = label0X + 1 * labelStep;
		int label1Y = label0Y;
		int label2X = label0X + 2 * labelStep;
		int label2Y = label0Y;

		int clrPadding = 2;
		int clrX = workAreaX + clrPadding;
		int clrY = workAreaY + clrPadding;
		g2.setStroke(new BasicStroke(.5f));
		Graphics2D g3 = (Graphics2D) g.create();
		float color = .98f;
		g3.setColor(new Color(color, color, color));
		g3.fillRect(clrX, clrY, width - clrX - clrPadding, height - clrY - clrPadding);
		color = 0f;
		g3.setStroke(new BasicStroke(.5f));
		g3.setColor(new Color(color, color, color));
		g3.drawRect(clrX, clrY, width - clrX - clrPadding, height - clrY - clrPadding);

		java.awt.Font originalFont = g2.getFont();
		java.awt.Font newFont = originalFont.deriveFont(originalFont.getSize2D() * .9f);
		g2.setFont(newFont);

		g2.drawString("kb", 0, label0Y); //$NON-NLS-1$
		g2.drawLine(0, label0Y + 2, 15, label0Y + 2);
		g2.drawString(Messages.getString("GelVisual.1"), label0X - 5, label0Y); //$NON-NLS-1$
		Rectangle2D r2 = g2.getFontMetrics().getStringBounds(Messages.getString("GelVisual.2"), g2); //$NON-NLS-1$
		g2.drawString(Messages.getString("GelVisual.3"), label0X + labelStep + (int) (labelStep - r2.getWidth() / 2), label0Y - 19); //$NON-NLS-1$
		g2.drawLine(label0X + labelStep, label0Y - 17, label0X + 3 * labelStep, label0Y - 17);
		g2.drawLine(label0X + labelStep, label0Y - 17, label0X + labelStep, label0Y - 14);
		g2.drawLine(label0X + 3 * labelStep, label0Y - 17, label0X + 3 * labelStep, label0Y - 14);
		for (int step = 1; step <= parentMap.size(); step++)
		{
			Sex sex = parentMap.get(step - 1).getKey();
			if (sex == null)
			{
				g2.drawString("P " + step, label0X + step * labelStep, label0Y); //$NON-NLS-1$
			}
			else
			{
				// g2.drawString(sex.toString().substring(0, 3), label0X + step * labelStep, label0Y);
				int w = 8;
				int h = 8;
				paintSex(g2, new Rectangle(label0X + step * labelStep + (labelStep - w) / 2, label0Y - h - h / 2, w, h), sex, Color.black);
			}
		}
		g2.drawString(Messages.getString("GelVisual.5"), label0X + (lanes - 1) * labelStep, label0Y); //$NON-NLS-1$

		for (float i : new float[] { .5f, 1, 2, 3, 4, 6, 10 })
		{
			drawMarker(g2, i, 0, workAreaY, workAreaX, height);
			drawRef(g2, i, label0X + 2, workAreaY, label1X - label0X - 2, height, 1);
		}
		for (int step = 1; step <= parentMap.size(); step++)
		{
			TreeMap<Float, Integer> pMap = parentMap.get(step - 1).getValue();
			for (Entry<Float, Integer> e : pMap.entrySet())
			{
				float pos = e.getKey();
				int cnt = e.getValue();
				drawRef(g2, pos, label0X + step * labelStep, workAreaY, labelStep, height, cnt);
			}
		}
		for (Entry<Float, Integer> e : map.entrySet())
		{
			float pos = e.getKey();
			int cnt = e.getValue();
			drawRef(g2, pos, label0X + (lanes - 1) * labelStep, workAreaY, labelStep, height, cnt);
		}
		g2.setFont(originalFont);
	}

	private void paintSex(Graphics2D g, Rectangle rect, Creature.Sex sex, Color c)
	{
		Ellipse2D.Float ellipse = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		g.setStroke(new BasicStroke(0.1f * rect.width));
		g.setColor(c);
		g.drawOval(rect.x, rect.y, rect.width, rect.height);
		// g.setColor(Color.black);
		g.setStroke(new BasicStroke(0.05f * rect.width));
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
				// Line2D.Float line4 = new Line2D.Float(center_x + rect.width * (float) (Math.sqrt(.5) / 2 + .01f), center_y + rect.height * (float)
				// Math.sqrt(.5) / 2, center_x + rect.width * .49f * f, center_y + rect.height * .49f * f);
				// Line2D.Float line5 = new Line2D.Float(center_x + rect.width * .49f * f, center_y + rect.height * .40f, center_x + rect.width * .40f, center_y
				// + rect.height * .49f * f);
				Line2D.Float line4 = new Line2D.Float(center_x, center_y + .5f * rect.height, center_x, center_y + rect.height);
				Line2D.Float line5 = new Line2D.Float(center_x - .25f * rect.width, center_y + .8f * rect.height, center_x + .25f * rect.width, center_y + .8f * rect.height);
				g.draw(line4);
				g.draw(line5);
				break;
			}
		}
	}

	private void drawRef(Graphics2D g, float x, int offsetX, int offsetY, int width, int height, int weight)
	{
		int WIDTH = 7;
		Graphics2D g2 = (Graphics2D) g.create();
		float y = 1 / (1 + x);
		float paddingL = 2;
		float paddingLL = 5;
		float paddingR = 6;
		float paddingRR = 9;

		float mmY = Math.max(height * y, WIDTH);
		float mY = mmY + offsetY;

		Line2D mark;

		if (weight == 1)
		{
			mark = new Line2D.Float(offsetX + paddingL, mY, offsetX + width - paddingR, mY);
			g2.setColor(new Color(0, 0, 0, .6f));
			g2.setStroke(new BasicStroke(WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		}
		else
		{
			mark = new Line2D.Float(offsetX + paddingLL, mY, offsetX + width - paddingRR, mY);
			g2.setColor(new Color(0, 0, 0, .6f));
			g2.setStroke(new BasicStroke(WIDTH * 2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		}
		g2.draw(mark);
	}

	private void drawMarker(Graphics2D g, float x, int offsetX, int offsetY, int width, int height)
	{
		float y = 1 / (1 + x);
		float mW = 3;
		float strW = 1;
		float mX = width - mW;
		float mY = height * y + offsetY;
		float textOffset = g.getFontMetrics().getHeight() * .3f;
		Line2D mark = new Line2D.Float(mX, mY, mX + mW, mY);

		g.draw(mark);
		String s = x < 1 ? String.valueOf(x) : String.valueOf((int) x);
		float wstr = (float) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, offsetX + width - wstr - mW - strW, mY + textOffset);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(150, 200);
	}

}
