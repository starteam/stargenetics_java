package star.genetics.v2.ui.common;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import star.genetics.Messages;
import star.genetics.v1.ui.punnett.PunnettSquareDialog;

public class PunnettSqaure extends Button
{
	private static final long serialVersionUID = 1L;

	private static class SquareIcon implements Icon
	{
		BufferedImage iconImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);

		public SquareIcon()
		{
			paintIconOnce(null, iconImage.getGraphics(), 0, 0);
		}

		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			g.drawImage(iconImage, x, y, null);
		}

		public void paintIconOnce(Component c, Graphics g, int x, int y)
		{
			g.setColor(java.awt.Color.black);
			for (int xx = 0; xx < 3; xx++)
			{
				for (int yy = 0; yy < 3; yy++)
				{
					g.drawRect(2 + x + xx * 4, 2 + y + yy * 4, 4, 4);
				}
			}
		}

		public int getIconHeight()
		{
			return 16;
		}

		public int getIconWidth()
		{
			return 16;
		}

	}

	@Override
	protected String getButtonTooltipText()
	{
		return getButtonText();
	}

	@Override
	protected Icon getButtonIcon()
	{
		return new SquareIcon();
	}

	@Override
	protected int getButtonMnemoic()
	{

		return KeyEvent.VK_S;
	}

	@Override
	protected String getButtonText()
	{

		return Messages.getString("PunnettSqaure.0"); //$NON-NLS-1$
	}

	@Override
	protected void onAction()
	{
		PunnettSquareDialog p = new PunnettSquareDialog(this);
		p.setVisible(true);
	}

}
