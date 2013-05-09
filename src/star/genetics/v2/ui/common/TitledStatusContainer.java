package star.genetics.v2.ui.common;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import star.genetics.Messages;
import utils.UIHelpers;

public class TitledStatusContainer extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Component title;
	private Component body;
	private Component status;
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

	protected TitledStatusContainer()
	{
		isInitialized = false;
	}

	protected void init(Component title, Component body, Component status, Boolean visibile)
	{
		setLayout(null);
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		this.title = title;
		this.body = body;
		this.status = status;
		if (visibile != null)
		{
			this.close = new CloseButton(visibile.booleanValue());
			add(close);
		}
		add(title);
		add(body);
		add(status);
		invalidate();
		isInitialized = true;
	}

	@Override
	public void doLayout()
	{
		super.doLayout();
		Dimension containerDimension = getSize();
		Dimension titleDimension = title.getPreferredSize();
		Dimension statusDimension = status.getPreferredSize();
		Insets border = getBorder().getBorderInsets(this);
		title.setBounds(border.left, 0, Math.min(titleDimension.width, containerDimension.width - border.left - border.right), titleDimension.height);
		if (close != null && !close.getState())
		{
			status.setBounds(border.left, titleDimension.height, containerDimension.width - border.left - border.right, 0);
			body.setBounds(border.left, titleDimension.height, containerDimension.width - border.left - border.right, 0);
		}
		else
		{
			status.setBounds(border.left, containerDimension.height - statusDimension.height, Math.min(statusDimension.width, containerDimension.width - border.left - border.right), statusDimension.height);
			body.setBounds(border.left, titleDimension.height, containerDimension.width - border.left - border.right, containerDimension.height - titleDimension.height - statusDimension.height - border.bottom / 2);
		}
		if (close != null)
		{
			close.setBounds(containerDimension.width - border.left - border.right, titleDimension.height / 2 - 8 + border.top / 4, 16, 16);
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension ret;
		Dimension titleDimension = title.getPreferredSize();
		Dimension bodyDimension = body.getPreferredSize();
		Dimension statusDimension = status.getPreferredSize();
		Insets border = getBorder().getBorderInsets(this);
		Dimension frameSize = UIHelpers.getFrame(this).getSize();
		if (close != null && !close.getState())
		{
			ret = new Dimension(titleDimension.width + border.left + border.right, titleDimension.height);
		}
		else
		{
			ret = new Dimension(Math.max(Math.max(titleDimension.width, bodyDimension.width), statusDimension.width) + border.left + border.right, titleDimension.height + bodyDimension.height + statusDimension.height + border.bottom / 2);
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
			g2.fillRoundRect(bodyRect.x - border.left / 2, bodyRect.y, bodyRect.width + border.right, bodyRect.height, border.left / 2, border.top / 2);
		}
	}
}