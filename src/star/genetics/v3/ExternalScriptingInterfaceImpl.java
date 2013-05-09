package star.genetics.v3;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import star.genetics.Version;
import star.genetics.genetic.impl.MatingException;
import star.genetics.genetic.model.CrateModel;
import star.genetics.genetic.model.CrateSet;
import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;
import star.genetics.genetic.model.Model;
import star.genetics.v3.exceptions.InvalidTokenException;
import star.genetics.v3.exceptions.UnauthenticatedException;
import star.genetics.visualizers.Visualizer;
import star.genetics.visualizers.VisualizerFactory;
import star.genetics.xls.Load;
import star.genetics.xls.ParseException;

public class ExternalScriptingInterfaceImpl
{
	static String COMMAND = "command";
	static String INFO = "info";
	static String REGISTER = "register";
	static String UNREGISTER = "unregister";
	static String LISTSTRAINS = "liststrains";
	static String ADDSTRAIN = "addstrain";
	static String REMOVESTRAIN = "removestrain";
	static String SAVESTATE = "savestate";
	static String LOADSTATE = "loadstate";
	static String LISTEXPERIMENTS = "listexperiments";
	static String NEWEXPERIMENT = "newexperiment";
	static String GETEXPERIMENT = "getexperiment";
	static String UPDATEEXPERIMENT = "updateexperiment";
	static String CREATEEXPERIMENT = "createexperiment";
	static String DESCRIBECREATURE = "describecreature";

	Map<String, MetadataModel> data = new TreeMap<String, MetadataModel>();

	private void setStatus(JSONObject ret, String status, String status_description)
	{
		try
		{
			ret.put("status", status);
			if (status_description != null)
			{
				ret.put("status_description", status_description);
			}
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}

	private void creaturesToArray(MetadataModel meta, JSONArray array, CreatureSet set) throws JSONException
	{
		VisualizerFactory f = meta.getModel().getVisualizerFactory();
		for (Creature c : set)
		{
			meta.registerCreature(c);
			// JSONObject strain = new JSONObject();

			JSONObject creature = new JSONObject();
			creature.put("id", c.getUUID());
			creature.put("name", c.getName());
			creature.put("sex", c.getSex());
			Visualizer v = f.newVisualizerInstance();
			v.setProperties(c.getProperties(), c.getSex());
			JSONObject properties = new JSONObject(v.getTooltipProperties());
			creature.put("properties", properties);
			// creature.put("image", visualizerToDataString(v));
			array.put(creature);
		}
	}

	private String visualizerToDataString(Visualizer v)
	{
		try
		{
			JComponent c = v.getJComponent();
			Dimension d = c.getPreferredSize();
			c.setSize(d);
			BufferedImage img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();
			c.printAll(g);
			g.dispose();
			img.flush();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(img, "png", stream);
			stream.flush();
			byte[] data = stream.toByteArray();
			StringBuilder sb = new StringBuilder();
			sb.append("data:image/png,");
			for (int i = 0; i < data.length; i++)
			{
				int dd = data[i];
				dd = dd & 0xff;
				sb.append('%');
				if (dd < 16)
				{
					sb.append(0);
					sb.append(Integer.toHexString(dd));
				}
				else
				{
					sb.append(Integer.toHexString(dd));
				}
			}
			return sb.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return "missing";
		}
	}

	public synchronized String invoke(String str)
	{
		JSONObject ret = new JSONObject();
		setStatus(ret, "invalid_request", null);
		try
		{
			JSONObject obj = new JSONObject(str);
			String command = obj.optString(COMMAND, INFO);
			// is un-authenticated call
			if (INFO.equalsIgnoreCase(command))
			{
				info(ret, obj);
			}
			else
			{
				authenticated(obj);
				if (REGISTER.equalsIgnoreCase(command))
				{
					register(ret, obj);
				}

				if (UNREGISTER.equalsIgnoreCase(command))
				{
					unregister(ret, obj);
				}

				MetadataModel meta = load_model(obj);
				if (LISTSTRAINS.equalsIgnoreCase(command))
				{
					list_strains(ret, obj, meta);
				}
				if (ADDSTRAIN.equalsIgnoreCase(command))
				{
					add_strain(ret, obj, meta);
				}
				if (REMOVESTRAIN.equalsIgnoreCase(command))
				{
					remove_strain(ret, obj, meta);
				}

				if (SAVESTATE.equalsIgnoreCase(command))
				{
					save_state(ret, obj, meta);
				}
				if (LOADSTATE.equalsIgnoreCase(command))
				{
					load_state(ret, obj, meta);
				}

				if (LISTEXPERIMENTS.equalsIgnoreCase(command))
				{
					list_experiments(ret, obj, meta);
				}
				if (NEWEXPERIMENT.equalsIgnoreCase(command))
				{
					new_experiment(ret, obj, meta);
				}
				if (GETEXPERIMENT.equalsIgnoreCase(command))
				{
					get_experiment(ret, obj, meta);
				}
				if (UPDATEEXPERIMENT.equalsIgnoreCase(command))
				{
					update_experiment(ret, obj, meta);
					get_experiment(ret, obj, meta);
				}
				if (CREATEEXPERIMENT.equalsIgnoreCase(command))
				{
					JSONObject ret_new = new JSONObject();
					new_experiment(ret_new, obj, meta);
					obj.getJSONObject("experiment").put("id", ret_new.getJSONObject("experiment").get("id"));
					update_experiment(ret, obj, meta);
					get_experiment(ret, obj, meta);

				}
				if (DESCRIBECREATURE.equalsIgnoreCase(command))
				{
					describe_creature(ret, obj, meta);
				}
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (UnauthenticatedException e)
		{
			setStatus(ret, "error", "unauthenticated - invalid authentication");
		}
		catch (InvalidTokenException e)
		{
			setStatus(ret, "error", "unauthenticated - invalid token");
		}
		return ret.toString();
	}

	private void describe_creature(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		JSONArray obj_creatures = obj.getJSONArray("creatures");
		JSONArray creatures = new JSONArray();
		VisualizerFactory f = meta.getModel().getVisualizerFactory();
		for (int i = 0; i < obj_creatures.length(); i++)
		{
			JSONObject obj_creature = obj_creatures.getJSONObject(i);
			String id = obj_creature.getString("id");
			Creature c = meta.getCreatureByUUID(id);
			JSONObject creature = new JSONObject();
			creature.put("id", c.getUUID());
			creature.put("name", c.getName());
			creature.put("sex", c.getSex());
			Visualizer v = f.newVisualizerInstance();
			v.setProperties(c.getProperties(), c.getSex());
			JSONObject properties = new JSONObject(v.getTooltipProperties());
			creature.put("properties", properties);
			creature.put("image", visualizerToDataString(v));
			creatures.put(creature);
		}
		ret.put("creatures", creatures);
		setStatus(ret, "ok", null);
	}

	private void update_experiment(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		JSONObject obj_exp = obj.getJSONObject("experiment");
		String id = obj_exp.getString("id");
		CrateModel experiment = meta.getCrateByUUID(id);
		Object visible = obj_exp.opt("visible");
		Object name = obj_exp.opt("name");
		JSONArray parents = obj_exp.optJSONArray("parents");
		Object mate = obj_exp.opt("mate");
		System.out.println("id: " + id);
		System.out.println("visible: " + visible + " " + (visible != null ? visible.getClass() : null));
		System.out.println("name: " + name + " " + (name != null ? name.getClass() : null));
		System.out.println("parents: " + parents + " " + (parents != null ? parents.getClass() : null));
		System.out.println("mate: " + mate + " " + (mate != null ? mate.getClass() : null));
		setStatus(ret, "ok", null);
		if (visible != null && visible.getClass() == Boolean.class)
		{
			experiment.setVisible((Boolean) visible);
		}
		if (name != null)
		{
			experiment.setName(String.valueOf(name));
		}
		if (parents != null)
		{
			if (experiment.getProgenies().size() != 0)
			{
				setStatus(ret, "error", "can not set parents on crate that is mated!");
			}
			else if (parents.length() != 2)
			{
				setStatus(ret, "error", "two parents required");
			}
			else
			{
				JSONObject obj_parent1 = parents.getJSONObject(0);
				String parent_id1 = obj_parent1.getString("id");
				Creature c1 = meta.getCreatureByUUID(parent_id1);
				JSONObject obj_parent2 = parents.getJSONObject(1);
				String parent_id2 = obj_parent2.getString("id");
				Creature c2 = meta.getCreatureByUUID(parent_id2);
				// this works for fly & yeast, but not for worms
				if (c1.getSex() != c2.getSex())
				{
					CreatureSet p = experiment.getParents();
					p.clear();
					p.add(c1);
					p.add(c2);
				}
				else
				{
					setStatus(ret, "error", "parents are not of oposite mating type");
				}
			}
		}
		if (mate != null && mate.getClass() == Integer.class)
		{
			if (experiment.getParents().size() != 2)
			{
				setStatus(ret, "error", "can not mate, set parents first!");
			}
			else
			{
				try
				{
					int matings_count = (Integer) mate;
					for (int i = 0; i < matings_count; i++)
					{
						CreatureSet new_progenies = meta.getModel().getMatingEngine().getProgenies(experiment.getName(), experiment.getParents(), experiment.getProgenies().size() + 1, meta.getModel().getProgeniesCount(), meta.getModel().getRules());
						CreatureSet progenies = experiment.getProgenies();
						for (Creature c : new_progenies)
						{
							progenies.add(c);
						}
					}
				}
				catch (MatingException e)
				{
					setStatus(ret, "error", "error mating: " + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private void get_experiment(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		JSONObject obj_exp = obj.getJSONObject("experiment");
		String id = obj_exp.getString("id");
		CrateModel experiment = meta.getCrateByUUID(id);
		JSONObject exp = new JSONObject();
		exp.put("visible", experiment.isVisible());
		exp.put("name", experiment.getName());
		exp.put("id", id);

		JSONArray parents = new JSONArray();
		creaturesToArray(meta, parents, experiment.getParents());
		exp.put("parents", parents);
		JSONArray progenies = new JSONArray();
		creaturesToArray(meta, progenies, experiment.getProgenies());
		exp.put("progenies", progenies);
		ret.put("experiment", exp);
		setStatus(ret, "ok", null);
	}

	private void list_experiments(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		JSONArray exps = new JSONArray();
		ret.put("experiments", exps);
		CrateSet cs = meta.getModel().getCrateSet();
		for (CrateModel c : cs)
		{
			JSONObject exp = new JSONObject();
			exp.put("id", c.getUUID());
			exps.put(exp);
			meta.registerCrate(c);
		}
		setStatus(ret, "ok", null);
	}

	private void new_experiment(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		CrateSet cs = meta.getModel().getCrateSet();
		JSONObject exp = new JSONObject();
		ret.put("experiment", exp);
		CrateModel c = cs.newCrateModel();
		exp.put("id", c.getUUID());
		meta.registerCrate(c);
		setStatus(ret, "ok", null);
	}

	private void load_state(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		String text = obj.getJSONObject("state").getString("metamodel");
		meta.setMetaModelText(text);
		setStatus(ret, "ok", null);
	}

	private void save_state(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		JSONObject state = new JSONObject();
		ret.put("state", state);
		state.put("model", meta.getModelText());
		state.put("metamodel", meta.getMetadataModelText());
		setStatus(ret, "ok", null);
	}

	private void list_strains(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		Model model = meta.getModel();

		JSONArray list = new JSONArray();
		creaturesToArray(meta, list, model.getCreatures());
		ret.put("strains", list);
		setStatus(ret, "ok", null);
	}

	private void remove_strain(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		Model model = meta.getModel();
		JSONArray strains = obj.getJSONArray("strains");
		CreatureSet creatures = model.getCreatures();
		for (int i = 0; i < strains.length(); i++)
		{
			JSONObject strain = strains.getJSONObject(i);
			String uuid = strain.getString("id");
			Creature c = meta.getCreatureByUUID(uuid);
			creatures.remove(c);
		}
		setStatus(ret, "ok", null);
	}

	private void add_strain(JSONObject ret, JSONObject obj, MetadataModel meta) throws JSONException
	{
		Model model = meta.getModel();
		JSONArray strains = obj.getJSONArray("strains");
		CreatureSet creatures = model.getCreatures();
		for (int i = 0; i < strains.length(); i++)
		{
			JSONObject strain = strains.getJSONObject(i);
			String uuid = strain.getString("id");
			Creature c = meta.getCreatureByUUID(uuid);
			creatures.add(c);
		}
		setStatus(ret, "ok", null);
	}

	private MetadataModel load_model(JSONObject obj) throws JSONException, InvalidTokenException
	{
		String token = obj.getString("token");
		MetadataModel model = data.get(token);
		if (model == null)
		{
			throw new InvalidTokenException();
		}
		return model;
	}

	private void unregister(JSONObject ret, JSONObject obj) throws JSONException
	{
		String token = obj.getString("token");
		if (data.containsKey(token))
		{
			data.get(token).setModel(null);
			data.remove(token);
			setStatus(ret, "ok", "removed");
		}
		else
		{
			setStatus(ret, "ok", "does not exist");
		}
	}

	private boolean authenticated(JSONObject obj) throws JSONException, UnauthenticatedException
	{
		JSONObject auth = obj.getJSONObject("authenticate");
		if (auth != null)
		{
			return true;
		}
		throw new UnauthenticatedException();
	}

	private void register(final JSONObject ret, final JSONObject obj) throws JSONException
	{
		register2(ret, obj);
	}

	private void register2(JSONObject ret, JSONObject obj) throws JSONException
	{
		String token = obj.getString("token");
		String url = obj.getString("url");

		try
		{
			Load load = new Load();
			MetadataModel metadataModel = new MetadataModel();
			// this should actually be done after model is instantiated; otherwise it's stupid - monkey patch for https testing
			data.put(token, metadataModel);
			// mokey-patch url to work for https
			Model model = load.Load((new URL(url)).openStream());
			metadataModel.setModel(model);
			setStatus(ret, "ok", null);
			return;
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DataFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setStatus(ret, "failed", null);
	}

	private String info(JSONObject ret, JSONObject obj) throws JSONException
	{
		setStatus(ret, "ok", null);
		ret.put("app", Version.getProject() + " Server");
		ret.put("version", Version.getBuildDate());
		return ret.toString();
	}

}
