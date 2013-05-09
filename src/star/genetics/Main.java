package star.genetics;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import star.genetics.genetic.impl.Utilities;
import star.genetics.v2.ui.MainFrame;
import star.genetics.v2.ui.QuitDialog;
import star.version.VersionChecker;
import utils.JNLPHelpers;
import utils.OS;
import utils.UIHelpers;

public class Main implements Runnable
{
	private final static String PROJECT = "StarGenetics1"; //$NON-NLS-1$
	static ImageIcon currentIcon;
	public static JFrame frame;
	public static String[] arguments;
	private File file;

	public Main(File file)
	{
		this.file = file;
	}

	public static void close(boolean isModelModified)
	{
		try
		{
			String text = isModelModified ? Messages.getString("Main.1") : Messages.getString("Main.2"); //$NON-NLS-1$ //$NON-NLS-2$

			QuitDialog d = new QuitDialog(frame, text);
			int result = d.get();

			if (result == JOptionPane.YES_OPTION)
			{
				frame.dispose();
				System.exit(0);
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			System.exit(0);
		}

	}

	private void init()
	{
		frame = (file == null) ? new MainFrame() : new MainFrame(file);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				close(Helper.isModelModified(e.getWindow()));
			}
		});
		VersionChecker.invokeLater(PROJECT, Version.getProject(), Version.getBuildDate(), frame);
		UIHelpers.setTooltipDelays(50, 16000, 50);
		// UIHelpers.registerScreenchangeHandler(frame);
	}

	public void run()
	{
		initUI();
		init();
	}

	private static void initUI()
	{
		System.setProperty("swing.aatext", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!OS.isMacOSX())
		{
			UIHelpers.tryNimbus();
		}
		try
		{
			if (UIManager.getLookAndFeelDefaults().getColor("background") == null) //$NON-NLS-1$
			{
				UIManager.getLookAndFeelDefaults().put("background", java.awt.Color.lightGray); //$NON-NLS-1$
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	static
	{
		try
		{
			Class<? extends Object> c = Class.forName("star.genetics.priv.MagicClass");
			Runnable r = (Runnable) c.newInstance();
			r.run();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		arguments = args;
		if (!VersionChecker.processVersionArguments(PROJECT, Version.getProject(), Version.getBuildDate(), args))
		{
			try
			{
				UIHelpers.addTracking("StarGenetics"); //$NON-NLS-1$
				JNLPHelpers.printArgs(args);
				File file = JNLPHelpers.parseFileFromArgs(args);
				Messages.updateBundle(Locale.getDefault());
				if (SwingUtilities.isEventDispatchThread())
				{
					(new Main(file)).run();
				}
				else
				{
					SwingUtilities.invokeAndWait(new Main(file));
				}
				Utilities.preload();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void main_restart()
	{
		main(arguments);
	}

}
