package star.genetics.v2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;

import star.annotations.Preferences;
import star.annotations.SignalComponent;
import star.event.Adapter;
import star.event.UnhandledExceptionHandlerComponent;
import star.genetics.Messages;
import star.genetics.events.LoadModelRaiser;
import star.genetics.genetic.model.Model;
import star.genetics.genetic.model.ModelModifiedProvider;
import star.genetics.v2.ui.common.CommonMenuBar;
import star.version.VersionCheckerDecoration;
import utils.UIHelpers;

@SignalComponent(extend = JFrame.class)
@Preferences(application = "StarGenetics", loadResource = "resources/default.properties")
public class MainFrame extends MainFrame_generated implements VersionCheckerDecoration, ModelModifiedProvider
{
	private static final long serialVersionUID = 1L;

	private MainPanel mainPanel = new MainPanel(this);

	public MainFrame(final File file)
	{
		this();
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				super.windowOpened(e);
				mainPanel.open(file);
			}
		});
	}

	public MainFrame()
	{
		super();
		setVisible(true);
	}

	public MainFrame(final Model model)
	{
		this();
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				super.windowOpened(e);
				mainPanel.loadModel(new LoadModelRaiser()
				{

					@Override
					public void removeNotify()
					{
						// TODO Auto-generated method stub

					}

					@Override
					public Adapter getAdapter()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void addNotify()
					{
						// TODO Auto-generated method stub

					}

					@Override
					public String getModelName()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Model getModel()
					{
						return model;
					}
				});
			}
		});
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		UIHelpers.setIcon(Messages.getString("MainFrame.0"), this); //$NON-NLS-1$
		setJMenuBar(new CommonMenuBar());
		setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
		setTitle(getPreferences().get("title", "missing preferences")); //$NON-NLS-1$ //$NON-NLS-2$
		getAdapter().addComponent(new UnhandledExceptionHandlerComponent());
		pack();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(1024, 768);
	}

	@Override
	public void paint(Graphics gr)
	{
		super.paint(gr);
		String text = versionCheckerDecoration;
		JMenuBar menu = getJMenuBar();
		if (text != null && menu != null)
		{
			Rectangle menuRect = menu.getBounds();
			Graphics2D g = (Graphics2D) gr;
			g.setFont(g.getFont().deriveFont(Font.BOLD));
			FontMetrics fm = g.getFontMetrics();
			Rectangle2D r = g.getFontMetrics().getStringBounds(text, g);
			int top = menu.getLocationOnScreen().y - this.getLocationOnScreen().y;
			g.setColor(Color.red);
			int x0 = (int) (menuRect.x + menuRect.width - r.getWidth());
			int ww = (int) r.getWidth();
			g.fillRect(x0, top, ww, menuRect.height);
			g.setColor(Color.white);
			g.drawString(text, x0, top + fm.getAscent());
		}
	}

	String versionCheckerDecoration = null;

	@Override
	public void setVersionCheckerDecoration(String text, String longText)
	{
		this.versionCheckerDecoration = "  " + text + "  "; //$NON-NLS-1$ //$NON-NLS-2$
		if (mainPanel.homeScreen && longText != null)
		{
			JLabel label = new JLabel(longText);
			label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50), BorderFactory.createLineBorder(Color.red, 3)), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

			label.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			label.setForeground(Color.red);
			label.setBackground(Color.black);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			getContentPane().add(BorderLayout.SOUTH, label);
			UIHelpers.invalidate(this);
		}
		repaint();
	}

	@Override
	public void modelSaved()
	{
		mainPanel.modelSaved();

	}

	@Override
	public boolean isModelModified()
	{
		return mainPanel.isModelModified();
	}

	@Override
	public void saveModel()
	{
		mainPanel.saveModel();
	}

}
