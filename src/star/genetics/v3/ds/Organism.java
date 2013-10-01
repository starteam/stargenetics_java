package star.genetics.v3.ds;

import org.json.JSONException;
import org.json.JSONObject;

import star.genetics.genetic.model.Creature;


public class Organism extends JSONObject
{
	public static String name = "name";
	public static String id = "id";

	public Organism(Creature c, Context context) throws JSONException
    {
		put(name, c.getName());
		put(id, c.getUUID());
    }
}
