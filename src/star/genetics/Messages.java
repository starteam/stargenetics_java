package star.genetics;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{
	private static final String BUNDLE_NAME = "star.genetics.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;

	private Messages()
	{
	}

	public static void updateBundle(Locale locale)
	{
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	public static String getString(String key)
	{
		try
		{
			String str = RESOURCE_BUNDLE.getString(key);
			str = str.replaceAll("\\\\n", "\n");
			return str;
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	public static String getString(String key, Locale locale)
	{
		try
		{
			ResourceBundle r = ResourceBundle.getBundle(BUNDLE_NAME, locale);
			String str = r.getString(key);
			str = str.replaceAll("\\\\n", "\n");
			return r.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
