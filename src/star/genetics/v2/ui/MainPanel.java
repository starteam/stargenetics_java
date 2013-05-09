package star.genetics.v2.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import star.annotations.Handles;
import star.annotations.Preferences;
import star.annotations.Properties;
import star.annotations.Property;
import star.annotations.SignalComponent;
import star.genetics.CommandLineFileOpener;
import star.genetics.Messages;
import star.genetics.events.ErrorDialogRaiser;
import star.genetics.events.LoadModelRaiser;
import star.genetics.genetic.impl.InputOutput;
import star.genetics.genetic.model.Model;
import star.genetics.genetic.model.ModelModifiedProvider;
import star.genetics.v1.ui.common.ErrorDialogHandler;
import star.genetics.v2.ui.fly.FlyUI;
import star.genetics.v2.ui.yeast.YeastUI;
import star.genetics.visualizers.Visualizer;
import star.version.VersionCheckerDecoration;

@SignalComponent(extend = JPanel.class, raises = { ErrorDialogRaiser.class })
@Preferences(application = "StarGenetics")
@Properties(@Property(name = "errorMessage", type = Exception.class, getter = Property.PUBLIC))
public class MainPanel extends MainPanel_generated implements ModelModifiedProvider
{
	private static final long serialVersionUID = 1L;
	private byte[] savedSignature = new byte[0];
	private Model model;
	private CommandLineFileOpener commandLineFileOpener = new CommandLineFileOpener();
	public transient boolean homeScreen = true;
	private ModelSaver modelSaver = new ModelSaver(this);
	private transient VersionCheckerDecoration titleProvider;
	private transient InputOutput inputOutput = new InputOutput();

	public MainPanel(VersionCheckerDecoration d)
	{
		this.titleProvider = d;
	}

	public void open(File file)
	{
		commandLineFileOpener.openFile(file);
	}

	public void open(URL url)
	{
		commandLineFileOpener.openURL(url);
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		getAdapter().addComponent(new ErrorDialogHandler());
		getAdapter().addComponent(new star.genetics.xls.Load());
		getAdapter().addComponent(inputOutput);
		getAdapter().addComponent(commandLineFileOpener);
		getAdapter().addComponent(modelSaver);
		getContentPane().removeAll();
		getContentPane().setLayout(new BorderLayout());
		JLabel label = new JLabel(Messages.getString("MainPanel.0"), SwingConstants.CENTER); //$NON-NLS-1$
		getContentPane().add(label);
	}

	private Container getContentPane()
	{
		return this;
	}

	private void flyUI(final Model model)
	{
		Container contentPane = getContentPane();
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new FlyUI(model));
		homeScreen = false;
	}

	private void yeastUI(final Model model)
	{
		Container contentPane = getContentPane();
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new YeastUI(model));
		homeScreen = false;
	}

	@Override
	@Handles(raises = {})
	public void loadModel(final LoadModelRaiser r)
	{
		try
		{
			homeScreen = false;
			model = r.getModel();
			if (Visualizer.UIClass.Fly.equals(model.getVisualizerFactory().newVisualizerInstance().getUIClass()))
			{
				flyUI(model);
				synchronized (getTreeLock())
				{
					validateTree();
				}

				titleProvider.setTitle(getPreferences().get("title", Messages.getString("MainPanel.2")) + ((r.getModelName() != null && r.getModelName().length() != 0) ? " - " + r.getModelName() : "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			else if (Visualizer.UIClass.Yeast.equals(model.getVisualizerFactory().newVisualizerInstance().getUIClass()))
			{
				yeastUI(model);
				synchronized (getTreeLock())
				{
					validateTree();
				}

				titleProvider.setTitle(getPreferences().get("title", Messages.getString("MainPanel.6")) + ((r.getModelName() != null && r.getModelName().length() != 0) ? " - " + r.getModelName() : "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			else
			{
				getContentPane().removeAll();
				getContentPane().setLayout(new BorderLayout());
				getContentPane().add(BorderLayout.CENTER, new JLabel(MessageFormat.format(Messages.getString("MainPanel.9"), Arrays.toString(Visualizer.UIClass.values())))); //$NON-NLS-1$
			}
			modelSaved();
		}
		catch (RuntimeException exc)
		{
			exc.printStackTrace();
			setErrorMessage(new RuntimeException(Messages.getString("MainPanel.10"), exc)); //$NON-NLS-1$
			raise_ErrorDialogEvent();
		}
	}

	private byte[] getModelSignature()
	{
		byte[] sig = new byte[0];
		if (model != null)
		{
			try
			{
				ByteArrayOutputStream bos = new ByteArrayOutputStream(16384);
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(model);
				oos.close();
				bos.flush();
				sig = bos.toByteArray();
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
		return sig;
	}

	public boolean isModelModified()
	{
		return !Arrays.equals(savedSignature, getModelSignature());
	}

	public void modelSaved()
	{
		savedSignature = getModelSignature();
	}

	@Override
	public void saveModel()
	{
		modelSaver.saveModel();
	}

	public String getModel()
	{
		try
		{
			java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream serializer = new java.io.ObjectOutputStream(bos);
			serializer.writeObject(model);
			serializer.close();
			bos.close();
			byte[] byteArray = bos.toByteArray();
			StringBuilder sb = new StringBuilder(":STARGENETICS:"); //$NON-NLS-1$
			sb.append("START:"); //$NON-NLS-1$
			for (byte b : byteArray)
			{
				sb.append(Integer.toHexString(b));
			}
			sb.append(":END:"); //$NON-NLS-1$
			return sb.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public Model getModelAsModel()
	{
		return model;
	}

}
