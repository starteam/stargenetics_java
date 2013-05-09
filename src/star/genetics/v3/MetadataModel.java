package star.genetics.v3;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTabbedPane;

import star.event.Adapter;
import star.genetics.events.LoadModelRaiser;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.Model;
import star.genetics.v2.ui.MainPanel;
import star.version.VersionCheckerDecoration;

public class MetadataModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static JTabbedPane pane;
	public static Model defaultModel;
	private Model model;
	private Map<String, Creature> creatures_mapping = new TreeMap<String, Creature>();
	private Map<String, CrateModel> crates_mapping = new TreeMap<String, CrateModel>();
	private transient MainPanel frame;

	public Model getModel()
	{
		if (model == null)
		{
			return defaultModel;
		}
		return model;
	}

	public void setModel(final Model model)
	{
		if (model != this.model && frame != null)
		{
			if (pane != null)
			{
				pane.remove(frame);
			}
			frame = null;
		}
		this.model = model;
		if (model != null && pane != null)
		{
			// JFrame f2 = new MainFrame(model);
			// f2.pack();
			// f2.setVisible(true);
			frame = new MainPanel(new VersionCheckerDecoration()
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
			pane.addTab("StarGenetics", frame);
			frame.loadModel(new LoadModelRaiser()
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
					return "Model XYZ";
				}

				@Override
				public Model getModel()
				{
					// TODO Auto-generated method stub
					return model;
				}
			});
		}
	}

	public void registerCreature(Creature c)
	{
		creatures_mapping.put(c.getUUID(), c);
	}

	public Creature getCreatureByUUID(String uuid)
	{
		return creatures_mapping.get(uuid);
	}

	public void registerCrate(CrateModel m)
	{
		crates_mapping.put(m.getUUID(), m);
	}

	public CrateModel getCrateByUUID(String uuid)
	{
		System.out.println("getCrateByUUID");
		System.out.println(uuid);
		System.out.println(crates_mapping);
		System.out.println(crates_mapping.get(uuid));
		return crates_mapping.get(uuid);
	}

	public String getModelText()
	{
		try
		{
			java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream serializer = new java.io.ObjectOutputStream(bos);
			serializer.writeObject(model);
			serializer.close();
			bos.close();
			byte[] byteArray = bos.toByteArray();
			StringBuilder sb = new StringBuilder(":STARGENETICS:MODEL:");
			sb.append("START:");
			for (byte b : byteArray)
			{
				sb.append(Integer.toHexString(b));
			}
			sb.append(":END:");
			return sb.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public String getMetadataModelText()
	{
		try
		{
			java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream serializer = new java.io.ObjectOutputStream(bos);
			serializer.writeObject(this);
			serializer.close();
			bos.close();
			byte[] byteArray = bos.toByteArray();
			StringBuilder sb = new StringBuilder(":STARGENETICS:METAMODEL:");
			sb.append("START:");
			for (byte b : byteArray)
			{
				sb.append(Integer.toHexString(b));
			}
			sb.append(":END:");
			return sb.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public void setMetaModelText(String text)
	{
		throw new IllegalArgumentException("Not yet implemented");
	}

}
