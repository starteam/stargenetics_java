package star.genetics.v2.ui.common;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import utils.UIHelpers;

public class TitledContainer extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Component title;
	private Component body;
	private CloseButton close;
	private boolean isInitialized = false;
	private float maxW = 1;
	private float maxH = 1;

	@Override
	public void addNotify()
	{
		super.addNotify();
		if (!isInitialized)
		{
			throw new RuntimeException("Need to initialize!"); //$NON-NLS-1$
		}

	}

	protected TitledContainer()
	{
		isInitialized = false;
	}

	protected void init(Component title, Component body, Boolean visibile)
	{
		setLayout(null);
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		this.title = title;
		this.body = body;
		if (visibile != null)
		{
			this.close = new CloseButton(visibile.booleanValue());
			add(close);
		}
		add(title);
		add(body);
		invalidate();
		if (title instanceof Container)
		{
			UIHelpers.fixOpaque((Container) title);
		}
		isInitialized = true;
	}

	public TitledContainer(Component title, Component body, boolean visibile)
	{
		init(title, body, visibile);
	}

	public TitledContainer(Component title, Component body, boolean visibile, float maxW, float maxH)
	{
		init(title, body, visibile);
	}

	@Override
	public void doLayout()
	{
		super.doLayout();
		Dimension containerDimension = getSize();
		Dimension titleDimension = title.getPreferredSize();
		Insets border = getBorder().getBorderInsets(this);
		title.setBounds(border.left, 0, Math.min(titleDimension.width, containerDimension.width - border.left - border.right), titleDimension.height);
		if (close != null && !close.getState())
		{
			body.setBounds(border.left, titleDimension.height, containerDimension.width - border.left - border.right, 0);
		}
		else
		{
			body.setBounds(border.left, titleDimension.height, containerDimension.width - border.left - border.right, containerDimension.height - titleDimension.height - border.bottom / 2);
		}
		if (close != null)
		{
			close.setBounds(containerDimension.width - border.left - border.right - 8, titleDimension.height / 2 - 8 + border.top / 4 - 4, 48, 24);
		}
	}

	public void setExpanded(boolean visible)
	{
		if (this.close != null)
		{
			this.close.setState(visible);
		}
	}

	public boolean isExpanded()
	{
		boolean ret = true;
		if (this.close != null)
		{
			ret = this.close.getState();
		}
		return ret;
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension ret;
		Dimension titleDimension = title.getPreferredSize();
		Dimension bodyDimension = body.getPreferredSize();
		Insets border = getBorder().getBorderInsets(this);
		Dimension frameSize = UIHelpers.getFrame(this).getSize();
		if (close != null && !close.getState())
		{
			ret = new Dimension(titleDimension.width + border.left + border.right, titleDimension.height);
		}
		else
		{
			ret = new Dimension(Math.max(titleDimension.width, bodyDimension.width) + border.left + border.right, titleDimension.height + bodyDimension.height + border.bottom / 2);
		}
		if (frameSize.height != 0 && frameSize.width != 0)
		{
			ret.width = (int) Math.min(ret.width, frameSize.width * maxW);
			ret.height = (int) Math.min(ret.height, frameSize.height * maxH);
		}
		return ret;
	}

	@Override
	protected void paintBorder(Graphics g)
	{
		super.paintBorder(g);
		Graphics2D g2 = (Graphics2D) g;
		Dimension dimension = getSize();
		Dimension titleDimension = title.getPreferredSize();
		Rectangle bodyRect = body.getBounds();
		Insets border = getBorder().getBorderInsets(this);
		int stroke = 2;
		g2.setColor(CommonUI.get().getTitleBackground());
		g2.setStroke(new BasicStroke(stroke));
		Paint p = new GradientPaint(0, 0, CommonUI.get().getTitleBackground(), dimension.width, titleDimension.height, CommonUI.get().getTitleBackground().brighter());
		g2.setPaint(p);
		g2.fillRoundRect(stroke, stroke, getWidth() - stroke * 2, getHeight() - stroke * 2, border.left, border.top);
		if (bodyRect.height > 0)
		{
			g2.setColor(CommonUI.get().getPanelBackground());
			// g2.setColor(body.getBackground());
			g2.fillRoundRect(border.left / 2, bodyRect.y, dimension.width - border.left / 2 - border.right / 2, dimension.height - bodyRect.y - border.bottom / 2, border.left / 2, border.top / 2);
		}
	}

}
