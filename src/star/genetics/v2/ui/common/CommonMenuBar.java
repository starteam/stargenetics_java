package star.genetics.v2.ui.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import star.annotations.Handles;
import star.annotations.SignalComponent;
import star.genetics.Messages;
import star.genetics.events.LoadModelRaiser;
import star.genetics.events.OpenModelRaiser;
import star.genetics.genetic.model.Model;
import star.genetics.v2.ui.menu.About;
import star.genetics.v2.ui.menu.Contributions;
import star.genetics.v2.ui.menu.Export;
import star.genetics.v2.ui.menu.Feedback;
import star.genetics.v2.ui.menu.GettingStarted;
import star.genetics.v2.ui.menu.New;
import star.genetics.v2.ui.menu.Open;
import star.genetics.v2.ui.menu.PunnettSquareButton;
import star.genetics.v2.ui.menu.Quit;
import star.genetics.v2.ui.menu.RecentDocuments;
import star.genetics.v2.ui.menu.Save;
import star.genetics.v2.ui.menu.Screenshot;
import star.genetics.visualizers.Visualizer;

@SignalComponent(extend = JMenuBar.class)
public class CommonMenuBar extends CommonMenuBar_generated
{
	private static final long serialVersionUID = 1L;
	private Object lock = new Object();
	private RecentDocuments recentDocuments;
	private LanguageSelector languageSelector;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private Export export;
	private PunnettSquareButton punnettSquare;
	public static transient Map<String, Contributions> contributions = new HashMap<String, Contributions>();

	public CommonMenuBar()
	{
		super();
		add(getFileMenu());
		add(getToolsMenu());
		add(getHelpMenus());
		languageSelector = new LanguageSelector();
		add(languageSelector);
		add(Box.createHorizontalGlue());
	}

	protected JMenu getToolsMenu()
	{
		JMenu t = new JMenu();
		t.setText(Messages.getString("CommonMenuBar.0")); //$NON-NLS-1$
		t.setMnemonic('t');
		punnettSquare = new PunnettSquareButton();
		t.add(punnettSquare);
		Contributions c = contributions.get("toolsmenu");
		if (c != null)
		{
			c.add(t);
		}
		t.add(new Screenshot());
		punnettSquare.setEnabled(false);
		return t;
	}

	protected JMenu getFileMenu()
	{
		synchronized (lock)
		{
			if (fileMenu == null)
			{
				initFileMenu();
			}
			return fileMenu;
		}
	}

	protected JMenu getHelpMenus()
	{
		synchronized (lock)
		{
			if (helpMenu == null)
			{
				initHelpMenu();
			}
			return helpMenu;
		}
	}

	private void initFileMenu()
	{
		JMenu f = new JMenu(Messages.getString("CommonMenuBar.1")); //$NON-NLS-1$
		f.setMnemonic('f');
		f.add(new New());
		HashSet<String> lastFolder = new HashSet<String>();
		f.add(new Open(lastFolder));
		f.add(new Save(lastFolder));
		export = new Export();
		f.add(export);
		if (recentDocuments == null)
		{
			initRecentDocuments();
		}
		f.add(recentDocuments);
		f.add(new Quit());
		export.setEnabled(false);

		fileMenu = f;
	}

	private void initRecentDocuments()
	{
		recentDocuments = new RecentDocuments();
		getAdapter().addComponent(recentDocuments);
	}

	private void initHelpMenu()
	{
		JMenu h = new JMenu(Messages.getString("CommonMenuBar.2")); //$NON-NLS-1$
		h.setMnemonic('h');
		h.add(new GettingStarted());
		h.add(new About());
		h.add(new Feedback());
		helpMenu = h;
	}

	@Override
	@Handles(raises = {})
	void openFile(OpenModelRaiser r)
	{
		recentDocuments.openFile(r);
	}

	@Override
	@Handles(raises = {})
	void openModel(LoadModelRaiser r)
	{
		Model model = r.getModel();
		if (Visualizer.UIClass.Fly.equals(model.getVisualizerFactory().newVisualizerInstance().getUIClass()))
		{
			export.setEnabled(true);
			punnettSquare.setEnabled(true);
		}
		else
		{
			export.setEnabled(false);
			punnettSquare.setEnabled(false);
		}
	}
}
