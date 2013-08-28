package star.genetics.v3.ds;

import org.json.JSONException;
import org.json.JSONObject;

import star.genetics.genetic.model.CrateModel;

public class Experiment extends JSONObject
{
	public static String name = "name";
	public static String id = "id";
	public static String parents = "parents";

	public Experiment(CrateModel model, Context context ) throws JSONException
    {
		put(name, model.getName());
		put(id, model.getUUID());
		put(parents, new Parents(model.getParents(),context));
    }
}