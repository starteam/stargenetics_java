package star.genetics.v2.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.json.JSONException;
import org.json.JSONObject;

import star.genetics.Messages;
import star.genetics.v3.ExternalScriptingInterfaceImpl;
import star.genetics.v3.MetadataModel;
import star.version.VersionCheckerDecoration;

public class Applet extends JApplet
{
	MainPanel panel;
	JPanel north = new JPanel();

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	String file = "http://starapp.mit.edu/star/media/uploads/genetics/stargenetics_tutorial_1.sgz"; //$NON-NLS-1$

	class FinalGlassPanel extends JPanel implements AWTEventListener
	{
		/**
         * 
         */
		private static final long serialVersionUID = 1L;

		public FinalGlassPanel()
		{
			setOpaque(false);
			getToolkit().addAWTEventListener(this, AWTEvent.MOUSE_MOTION_EVENT_MASK);
		}

		@Override
		public boolean contains(int x, int y)
		{
			return false;
		}

		public void eventDispatched(AWTEvent event)
		{
			System.out.println(event);
		}
	}

	public void _start_problem()
	{
		final MainPanel p = new MainPanel(new VersionCheckerDecoration()
		{

			@Override
			public void setVersionCheckerDecoration(String text, String longText)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void setTitle(String text)
			{
				// TODO Auto-generated method stub

			}
		});
		panel = p;
		add(p);
		p.setSize(getSize());
		p.setMaximumSize(getSize());

		// setGlassPane(new FinalGlassPanel());
		synchronized (getTreeLock())
		{
			invalidate();
			synchronized (getTreeLock())
			{
				validateTree();
			}
		}
		JButton b = new JButton("open file in panel"); //$NON-NLS-1$
		north.add(b);
		north.invalidate();
		north.validate();
		b.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					p.open(new URL(file));
					MetadataModel.defaultModel = p.getModelAsModel();
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void _start_problem2()
	{
		final MainPanel p = new MainPanel(new VersionCheckerDecoration()
		{

			@Override
			public void setVersionCheckerDecoration(String text, String longText)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void setTitle(String text)
			{
				// TODO Auto-generated method stub

			}
		});
		panel = p;
		// p.setSize(getSize());
		// p.setMaximumSize(getSize());
		JFrame f = new JFrame("StarGenetics"); //$NON-NLS-1$
		f.getContentPane().add(p);
		f.pack();
		f.setVisible(true);

		JButton b = new JButton("open file in window"); //$NON-NLS-1$
		north.add(b);
		north.invalidate();
		north.validate();
		b.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					p.open(new URL(file));
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		});
		// invalidate();
		// validateTree();
	}

	public void init()
	{
		super.init();
		setLayout(new BorderLayout());
		JTabbedPane pane = new JTabbedPane();
		add(BorderLayout.CENTER, pane);
		MetadataModel.pane = pane;
		add(BorderLayout.NORTH, north);
		north.setLayout(new FlowLayout());
		System.out.println("init"); //$NON-NLS-1$
		final JButton open = new JButton("Open as panel"); //$NON-NLS-1$
		north.add(open);
		open.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.err.println("Open button pressed start"); //$NON-NLS-1$
				open.setText("Open " + new Date()); //$NON-NLS-1$
				_start_problem();
				System.err.println("Open button pressed finish"); //$NON-NLS-1$
			}
		});
		final JButton open2 = new JButton("Open as window"); //$NON-NLS-1$
		north.add(open2);
		open2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.err.println("Open button pressed start"); //$NON-NLS-1$
				open2.setText("Open " + new Date()); //$NON-NLS-1$
				_start_problem2();
				System.err.println("Open button pressed finish"); //$NON-NLS-1$
			}
		});
		JButton test = new JButton("ui tree"); //$NON-NLS-1$
		north.add(test);
		test.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// walk the tree!
				Component c = (Component) arg0.getSource();
				while (c != null)
				{
					System.out.println("Parent:" + c); //$NON-NLS-1$
					if (c instanceof Container)
					{
						Container q = (Container) c;
						System.out.println("Children:" + Arrays.toString(q.getComponents())); //$NON-NLS-1$
					}
					c = c.getParent();
				}
			}
		});
		postInit();
	}

	private void postInit()
	{
		String url = getParameter("url"); //$NON-NLS-1$
		String token = getParameter("token"); //$NON-NLS-1$
		if (token != null && url != null)
		{
			try
			{
				JSONObject cmd = new JSONObject();
				cmd.put("command", "register"); //$NON-NLS-1$ //$NON-NLS-2$
				cmd.put("authenticate", new JSONObject()); //$NON-NLS-1$
				cmd.put("token", token); //$NON-NLS-1$
				cmd.put("url", url); //$NON-NLS-1$
				executeCommand(cmd.toString());
				System.out.println("execute command: " + cmd); //$NON-NLS-1$
			}
			catch (JSONException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("no init param token=" + token + " url=" + url); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void start()
	{
		super.start();
		started = "started"; //$NON-NLS-1$
		System.out.println("start " + this.hashCode()); //$NON-NLS-1$
	}

	@Override
	public void stop()
	{
		super.stop();
		System.out.println("stop " + this.hashCode()); //$NON-NLS-1$
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		System.out.println("add Notify"); //$NON-NLS-1$
	}

	@Override
	public void removeNotify()
	{
		System.out.println("remove Notify"); //$NON-NLS-1$
		super.removeNotify();
	}

	public String state()
	{
		return panel.getModel();
	}

	String started = "not started"; //$NON-NLS-1$

	public String isStarted()
	{
		return started;
	}

	public String restart_problem()
	{
		_start_problem();
		return "OK"; //$NON-NLS-1$
	}

	ExternalScriptingInterfaceImpl ifc = new ExternalScriptingInterfaceImpl();

	public String executeCommand(String command)
	{
		try
		{
			return ifc.invoke(command);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return "{\"status\":\"failed\",\"message\":\"" + t.getMessage() + "\"}"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}
