package star.genetics.v2.ui.common;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import star.annotations.SignalComponent;
import star.genetics.Main;
import star.genetics.Messages;
import utils.Icons;
import utils.UIHelpers;

@SignalComponent(extend = JMenu.class, raises = {})
public class LanguageSelector extends LanguageSelector_generated
{
	private static final long serialVersionUID = 1L;

	private LanguageSelector self = this;

	TreeMap<String, String> map = new TreeMap<String, String>();

	public LanguageSelector()
	{
		super();
		map.put("ht", "Kreyól"); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("pt", "Português"); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("uk", "Translator Keys");
		map.put("en", new Locale("en").getDisplayLanguage(new Locale("en"))); //$NON-NLS-1$ //$NON-NLS-2$
		System.err.println("Lang map :" + map.keySet().toString());
		setIcon(getIcon(Locale.getDefault()));
		if (map.containsKey(Locale.getDefault().getLanguage()))
		{
			setText(map.get(Locale.getDefault().getLanguage()));
		}
		setToolTipText(Messages.getString("LanguageSelector.0"));
	}

	private Icon getIcon(Locale locale)
	{
		if (locale.getLanguage().equalsIgnoreCase("ht"))
		{
			return Icons.FLAG_HT.getIcon(16);
		}
		return Icons.FLAG_DEFAULT.getIcon(16);

	}

	@Override
	public void addNotify()
	{

		super.addNotify();
		removeAll();
		try
		{
			Set<String> keys = map.descendingMap().keySet();
			System.err.println("Lang map :" + keys.toString());
			Stack<JMenuItem> s = new Stack<JMenuItem>();
			for (String key : keys)
			{
				String value = map.get(key); //$NON-NLS-1$
				JMenuItem item = new JMenuItem(new SelectLanguage(value, key));
				item.setIcon(getIcon(new Locale(key)));
				s.push(item);
			}
			while (!s.isEmpty())
			{
				add(s.pop());
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	@Override
	public void removeNotify()
	{
		super.removeNotify();
		removeAll();
	};

	class SelectLanguage extends AbstractAction
	{
		private static final long serialVersionUID = 1L;
		private String name;
		private String code;

		public SelectLanguage(String name, String code)
		{
			super(name);
			this.name = name;
			this.code = code;
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			Locale new_locale = new Locale(code);
			int ret = JOptionPane.showConfirmDialog(UIHelpers.getFrame(self), MessageFormat.format(Messages.getString("LanguageSelector.5", new_locale), name), Messages.getString("LanguageSelector.6", new_locale), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
			if (ret == JOptionPane.OK_OPTION)
			{
				Frame f = (Frame) UIHelpers.getFrame(self);
				f.dispose();
				Locale.setDefault(new_locale);
				Messages.updateBundle(new_locale);
				UIHelpers.track("SwitchLanguage/" + code); //$NON-NLS-1$
				Main.main_restart();
			}
		}
	}
}
