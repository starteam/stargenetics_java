package star.genetics.v1.ui;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import star.annotations.SignalComponent;
import star.genetics.events.OpenModelRaiser;
import star.genetics.events.SaveModelRaiser;

@SignalComponent(raises = { SaveModelRaiser.class, OpenModelRaiser.class })
public class AutoSave extends AutoSave_generated implements Runnable
{
	java.io.File file;
	OutputStream fos;
	boolean exit;
	private InputStream fis;

	final static String prefix = "StarGenetics-autosave-"; //$NON-NLS-1$
	final static String suffix = ".sg1"; //$NON-NLS-1$

	static boolean hasPreviousTemporaryFiles()
	{
		try
		{
			java.io.File file = java.io.File.createTempFile(prefix, suffix);
			java.io.File dir = file.getParentFile();
			file.delete();
			int count = dir.listFiles(new FileFilter()
			{

				public boolean accept(File pathname)
				{
					String name = pathname.getName();
					return name.startsWith(prefix) && name.endsWith(suffix);
				}
			}).length;
			return count != 1;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	static void removePreviousTemporaryFiles()
	{
		try
		{
			java.io.File file = java.io.File.createTempFile(prefix, suffix);
			java.io.File dir = file.getParentFile();
			file.delete();
			java.io.File[] files = dir.listFiles(new FileFilter()
			{

				public boolean accept(File pathname)
				{
					String name = pathname.getName();
					return name.startsWith(prefix) && name.endsWith(suffix);
				}
			});
			for (java.io.File f : files)
			{
				f.delete();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		try
		{
			file = java.io.File.createTempFile("StarGenetics-autosave-", ".sg1"); //$NON-NLS-1$ //$NON-NLS-2$
			// file.deleteOnExit();
			exit = false;
			Thread t = new Thread(this, "Auto-save"); //$NON-NLS-1$
			t.setDaemon(true);
			t.start();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void removeNotify()
	{
		super.removeNotify();
		exit = true;
	}

	public void run()
	{
		while (!exit)
		{
			try
			{
				Thread.sleep(30000);
				fos = new java.io.FileOutputStream(file);
				raise_SaveModelEvent();
				fos.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public String getModelFileName()
	{
		return file.getName();
	}

	public OutputStream getSaveModelStream()
	{
		return fos;
	}

	public InputStream getOpenModelStream()
	{
		return fis;
	}

	public void loadPreviousTemporaryFiles()
	{
		try
		{
			java.io.File file = java.io.File.createTempFile(prefix, suffix);
			java.io.File dir = file.getParentFile();
			file.delete();
			java.io.File[] files = dir.listFiles(new FileFilter()
			{

				public boolean accept(File pathname)
				{
					String name = pathname.getName();
					return name.startsWith(prefix) && name.endsWith(suffix);
				}
			});
			if (files.length != 0)
			{
				fis = new java.io.FileInputStream(files[0]);
				raise_OpenModelEvent();
				fis.close();
				files[0].delete();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public URL getModelURL()
	{
		return null;
	}

}
